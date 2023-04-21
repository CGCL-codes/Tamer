package org.chess.quasimodo.engine.uci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.EngineOutputListener;
import org.chess.quasimodo.event.PlayerReadyAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class EnginePlugin {

    private Log logger = LogFactory.getLog(EnginePlugin.class);

    private final BufferedReader input;

    private final BufferedWriter output;

    private final SimpleAsyncTaskExecutor executor;

    private CommandAndListen commandAndListen;

    /**
    * Constructor.
    * @param process The process that's wraps the engine's execution.
    * @param threadPriority The priority of the asynchronious executor.
    */
    protected EnginePlugin(Process process, int threadPriority) {
        this.input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        (executor = new SimpleAsyncTaskExecutor()).setConcurrencyLimit(1);
        executor.setThreadPriority(threadPriority);
    }

    /**
	 * Send a command to the engine to be executed and listen for the feedback.
	 * @param command The command to be sent
	 * @param outputListener Listen to command output.
	 */
    public void submitCommandAndListen(final String command, final EngineOutputListener outputListener, final PlayerReadyAware playerReadyAware) {
        initCommandAndListen(command, outputListener, playerReadyAware);
        executor.submit(commandAndListen);
    }

    private void initCommandAndListen(String command, EngineOutputListener outputListener, PlayerReadyAware commandAware) {
        initCommandAndListen(command, null, outputListener, commandAware);
    }

    private void initCommandAndListen(String command, String stopCondition, EngineOutputListener outputListener, PlayerReadyAware playerReadyAware) {
        Assert.isTrue(StringUtils.hasLength(command), "Command cannot be empty");
        Assert.notNull(outputListener, "Listener cannot be null");
        if (commandAndListen != null && commandAndListen.isRunning()) {
            throw new IllegalStateException("Still listen to the previous command");
        }
        commandAndListen = new CommandAndListen(command, stopCondition, outputListener, playerReadyAware);
    }

    /**
	 * Does the same as {@link #submitCommandAndListenWaitResult(String, String, EngineOutputListener)} does,
	 * with no stop command.
	 */
    public void submitCommandAndListenWaitResult(String command, EngineOutputListener outputListener, PlayerReadyAware playerReadyAware) throws EngineException {
        submitCommandAndListenWaitResult(command, null, outputListener, playerReadyAware);
    }

    /**
	 * Send a command to the engine to be executed, listen the feedback and wait for the result. 
	 * @param command The command to be sent.
	 * @param stopCondition The stop condition (i.e. when a line starting with 
	 * <code>stopCondition</code> is received, the listening stops).
	 * @param outputListener The listener to read engine's output.
	 * @param playerReadyAware
	 * @throws EngineException
	 */
    public void submitCommandAndListenWaitResult(String command, String stopCondition, EngineOutputListener outputListener, PlayerReadyAware playerReadyAware) throws EngineException {
        initCommandAndListen(command, stopCondition, outputListener, playerReadyAware);
        Future<Object> result = executor.submit(commandAndListen);
        try {
            result.get();
        } catch (Exception e) {
            logger.error(e, e);
            throw new EngineException(e);
        }
    }

    /**
	 * Send a command to the engine to be executed, wait for the result and ignore the feedback. 
	 * @param command The command to be sent.
	 * @throws EngineException 
	 * @throws IOException
	 */
    public void submitCommandWaitResult(final String command) throws EngineException {
        Callable<Object> asyncCall = new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                sendCommand(command);
                return null;
            }
        };
        Future<Object> result = executor.submit(asyncCall);
        try {
            result.get();
        } catch (Exception e) {
            logger.error(e, e);
            throw new EngineException(e);
        }
    }

    /**
	 * Send a command to the engine to be immediately executed and ignore the feedback. 
	 * @param command The command to be sent.
	 * @throws EngineException 
	 */
    public void sendCommandNow(final String command) throws EngineException {
        try {
            sendCommand(command);
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }

    public void requestStopRunningCommandAndListen() {
        if (commandAndListen != null) {
            commandAndListen.stopRunning();
        }
    }

    public boolean isCommandAndListenRunning() {
        if (commandAndListen != null) {
            return commandAndListen.isRunning();
        }
        return false;
    }

    private void sendCommand(String command) throws IOException {
        output.write(command + "\n");
        output.flush();
        logger.info("Command sent: " + command);
    }

    private class CommandAndListen implements Callable<Object> {

        String command;

        String stopCondition;

        EngineOutputListener outputListener;

        PlayerReadyAware playerReadyAware;

        boolean running;

        /**
		 * 
		 * @param command
		 * @param stopCondition
		 * @param outputListener
		 * @param commandAware
		 */
        CommandAndListen(String command, String stopCondition, EngineOutputListener outputListener, PlayerReadyAware commandAware) {
            this.command = command;
            this.stopCondition = stopCondition;
            this.outputListener = outputListener;
            this.playerReadyAware = commandAware;
        }

        @Override
        public Object call() throws Exception {
            try {
                logger.debug("Start running and listening [" + command + "]");
                if (playerReadyAware != null) {
                    playerReadyAware.onPlayerReady();
                }
                running = true;
                sendCommand(command);
                logger.info("Now start reading response");
                String line;
                while (running && (line = input.readLine()) != null) {
                    outputListener.onReceiveLine(line);
                    if (stopCondition != null && line.startsWith(stopCondition)) {
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e, e);
                outputListener.onReceiveLine("ERROR - Cannot listen to engine's output");
                throw e;
            } finally {
                logger.debug("End running and listening [" + command + "]");
                running = false;
            }
            return null;
        }

        boolean isRunning() {
            return running;
        }

        void stopRunning() {
            this.running = false;
        }
    }
}
