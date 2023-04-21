package be.lassi.lanbox;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import be.lassi.context.ShowContext;
import be.lassi.lanbox.commands.Command;
import be.lassi.lanbox.commands.CommandListener;
import be.lassi.lanbox.commands.CommandProcessor;
import be.lassi.lanbox.commands.Common16BitMode;
import be.lassi.lanbox.commands.CommonGetLayers;
import be.lassi.lanbox.commands.channel.ChannelSetData;
import be.lassi.lanbox.domain.ChannelChange;
import be.lassi.lanbox.domain.ChannelChanges;
import be.lassi.util.Util;
import be.lassi.util.Wait;

/**
 * Coordinates the communication to the lanbox.  Spawns a 'forever' loop
 * in which lanbox commands are sent to the lanbox and the response from
 * the lanbox is interpreted.
 * <p>
 * Commands can be sent as a result of:
 * <ul>
 *   <li>Commands read from the commandQueue.</li>
 *   <li>A 'change dmx levels' command is created when there are channel
 *   changes waiting in the channelChangesQueue.</li>
 *   <li>At regular intervals during the loop the DMX input and output levels
 * are read from the lanbox.</li>
 * </ul>
 *
 */
public class LanboxEngine implements ChannelChangeProcessor {

    /**
     * Destination for log messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(LanboxEngine.class);

    private boolean run = true;

    private static final int TIMEOUT = 60000;

    /**
     *
     */
    private final Connection connection;

    /**
     *
     */
    private CommandProcessor processor;

    /**
     *
     */
    private final ChannelChangeQueue[] channelChangeQueues = new ChannelChangeQueue[Lanbox.MAX_LAYERS];

    /**
     * Queue with lanbox commands that have to be send to the lanbox.
     */
    private final CommandQueue commandQueue = new CommandQueue();

    /**
     * Queue that is used for lanbox command logging purposes; all commands that have
     * been executed on the lanbox are put on this queue.
     */
    private final CommandLogQueue commandLogQueue = new CommandLogQueue();

    /**
     * Time after which dmx values should be re-read from the lanbox.
     */
    private final long readTimeout = 250;

    /**
     * Constructs a new engine.
     *
     * @param context the show context
     */
    public LanboxEngine(final ShowContext context) {
        for (int i = 0; i < channelChangeQueues.length; i++) {
            channelChangeQueues[i] = new ChannelChangeQueue();
        }
        ConnectionPreferences preferences = context.getPreferences().getConnectionPreferences();
        connection = new Connection(context.getConnectionStatus(), preferences);
        if (preferences.isEnabled()) {
            connection.openAndWait();
        }
        processor = new ConnectionCommandProcessor(connection);
        execute(new Common16BitMode(true));
        spawnLoop();
    }

    /**
     * Puts a <code>Command</code> on the command queue for execution. If the
     * execution thread is in a wait state, it will be notified that it has some
     * more processing to do.
     *
     * @param command the command to be executed.
     */
    public void execute(final Command command) {
        commandQueue.put(command);
        synchronized (this) {
            notify();
        }
    }

    public void execute(final List<Command> commands) {
        for (Command command : commands) {
            commandQueue.put(command);
        }
        synchronized (this) {
            notify();
        }
    }

    public void executeAndWait(final Command command) {
        final Wait wait = new Wait();
        command.add(new WaitCommandListener(wait));
        execute(command);
        wait.here();
    }

    /**
     * Adds channel changes to the channel change queue.
     *
     * @param changes the channel changes to be added
     */
    public void change(final int layerId, final List<ChannelChange> changes) {
        channelChangeQueues[layerId].add(changes);
        synchronized (this) {
            notify();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void change(final int layerId, final ChannelChange change) {
        channelChangeQueues[layerId].add(change);
        synchronized (this) {
            notify();
        }
    }

    /**
     * Start a new thread for the communications with the lanbox.
     * <p>
     * The priority is set higher than the process from which this thread is
     * started. This is done to ensure the fastest possible response from the
     * lanbox. Performing the communications with the lanbox is more important
     * than performing user interface updates.
     * <p>
     * Note that the thread is marked as a daemon thread. This means that the
     * Java Virtual Machine can exit without the application having to
     * explicitely stop this thread.
     */
    private void spawnLoop() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                loop();
            }
        };
        thread.setDaemon(true);
        thread.setName("LassiLanboxDaemon");
        thread.setPriority(Thread.currentThread().getPriority() + 1);
        thread.start();
    }

    /**
     *
     * Strategy:
     * first execute all commands on the command queue
     * then communicate any pending channel changes
     * then pick up channel values for dmx out and dmx in
     *
     * if nothing more to do, then sleep until next poll of dmx values
     * is needed (depends on the time of the latest user interface update)
     *
     * the sleep is interrupted when new commands are available on
     * the <code>commandQueue</code> or when new channel change requests
     * are arrive on the <code>channelChanges</code> queue
     *
     */
    private void loop() {
        long timeLastCommand = Util.now();
        for (; run; ) {
            try {
                boolean changes = true;
                while (changes) {
                    changes = performCommands();
                    changes |= performChannelChanges();
                    if (changes) {
                        timeLastCommand = Util.now();
                    }
                }
                if (Util.now() > timeLastCommand + TIMEOUT) {
                    processor.process(new CommonGetLayers());
                    timeLastCommand = Util.now();
                }
                sleep();
            } catch (Throwable e) {
                LOGGER.error("Error in LanboxEngine loop", e);
            }
        }
        connection.close();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("End loop (connection closed)");
        }
    }

    private boolean performCommands() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Perform commands (" + commandQueue.size() + " commands in queue)");
        }
        int commandCount = 0;
        while (commandQueue.size() > 0) {
            Command command = commandQueue.get();
            processor.process(command);
            commandLogQueue.put(command);
            commandCount++;
        }
        return commandCount > 0;
    }

    private boolean performChannelChanges() {
        int changeCount = 0;
        for (int i = 0; i < channelChangeQueues.length; i++) {
            if (channelChangeQueues[i].size() > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Perform channel changes (" + channelChangeQueues[i].size() + " changes in queue)");
                }
                ChannelChanges changes = channelChangeQueues[i].get();
                changes.eliminateDoubles();
                ChannelChanges[] cc = changes.split(ChannelSetData.MAX_CHANGES);
                for (ChannelChanges c : cc) {
                    Command command = new ChannelSetData(i, c);
                    processor.process(command);
                    log(command);
                    changeCount++;
                }
            }
        }
        return changeCount > 0;
    }

    private void sleep() {
        long sleepTime = readTimeout;
        if (sleepTime > 0) {
            try {
                synchronized (this) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Sleep " + sleepTime + "ms");
                    }
                    wait(sleepTime);
                }
            } catch (InterruptedException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("New command on queue before timeout period expired");
                }
            }
        }
    }

    public void close() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Closing");
        }
        connection.close();
        run = false;
        synchronized (this) {
            notify();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Closed");
        }
    }

    /**
     * Gets the queue on which the lanbox commands are written after
     * they have been executed on the lanbox.
     *
     * @return the command log queue
     */
    public CommandLogQueue getCommandLogQueue() {
        return commandLogQueue;
    }

    private void log(final Command command) {
        commandLogQueue.put(command);
        if (LOGGER.isInfoEnabled()) {
            StringBuilder b = new StringBuilder();
            command.appendCommand(b);
            command.appendResponse(b);
            b.append('\n');
            String string = b.toString();
            LOGGER.info(string);
        }
    }

    public int getWaitingCommandsCount() {
        return commandQueue.size();
    }

    public void setCommandProcessor(final CommandProcessor processor) {
        this.processor = processor;
    }

    private class WaitCommandListener implements CommandListener {

        final Wait wait;

        private WaitCommandListener(final Wait wait) {
            this.wait = wait;
        }

        public void commandPerformed(final Command command) {
            wait.stop();
        }
    }
}
