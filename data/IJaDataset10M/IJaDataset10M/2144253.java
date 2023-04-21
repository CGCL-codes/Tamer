package webservicesapi.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webservicesapi.command.Command;
import webservicesapi.command.CommandErrorException;
import webservicesapi.command.InvalidCommandException;
import webservicesapi.output.Output;
import webservicesapi.output.OutputQueue;
import webservicesapi.output.OutputQueueListener;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Used to monitor the output of a specific command.
 * If the output of the commands output changes over an interval
 * the listeners are notified.
 *
 * @author Ben Leov
 */
public class CommandChangeNotifier implements OutputQueueListener {

    private static Logger logger = LoggerFactory.getLogger(CommandChangeNotifier.class);

    private long interval;

    private String parameter;

    private Command command;

    private OutputQueue queue;

    private Output last;

    private Set<CommandChangeListener> listeners;

    private Timer timer;

    /**
     * @param interval The amount of time (milliseconds) between checks.
     */
    public CommandChangeNotifier(int interval) {
        this.interval = interval;
        listeners = new HashSet<CommandChangeListener>();
        this.queue = new OutputQueue();
        queue.addListener(this);
        schedule();
    }

    private synchronized void schedule() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new CommandTask(), interval);
        }
    }

    public void setWatchInterval(long interval) {
        this.interval = interval;
    }

    /**
     * Starts watching the specified command.
     *
     * @param command The command to execute
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void addListener(CommandChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CommandChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onOutputReceived(Output received) {
        if (last == null || !last.equals(received)) {
            notifyListeners(received);
        }
        last = received;
    }

    private void notifyListeners(Output output) {
        for (CommandChangeListener curr : listeners) {
            curr.onCommandChanged(output);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        for (CommandChangeListener curr : listeners) {
            curr.onStop();
        }
    }

    private class CommandTask extends TimerTask {

        @Override
        public synchronized void run() {
            if (command != null) {
                try {
                    command.processCommand(command.getCommandName(), parameter, queue);
                } catch (InvalidCommandException e) {
                    logger.error("Invalid command. Removing command notifier: " + command.getCommandName(), e);
                    stop();
                } catch (CommandErrorException e) {
                    logger.error("Error occurred performing command", e);
                }
            }
            timer = null;
            schedule();
        }
    }
}
