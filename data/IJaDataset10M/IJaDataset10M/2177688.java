package org.apache.harmony.jpda.tests.framework.jdwp;

import java.util.List;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.harmony.jpda.tests.framework.LogWriter;
import org.apache.harmony.jpda.tests.framework.TestOptions;
import org.apache.harmony.jpda.tests.framework.jdwp.exceptions.TimeoutException;

/**
 * This class provides asynchronous sending JDWP commands and receiving JDWP
 * events through established JDWP connection and supports timeout for these
 * operations.
 */
public class PacketDispatcher extends Thread {

    /**
     * Variables below are intended only to help with tests failures
     * investigation. They turn on/off some kinds of trace during
     * tests execution which can clear up details of test failure.
     * 
     * commandsNumberForTrace and begCommandIdForTrace define trace
     * of sent JDWP commands and received replies for these commands:
     * - begCommandIdForTrace defines starting command ID for trace
     *   (the first command has ID=1, the second - ID=2 and so on).
     *   if <= 0 then the same as = 1.
     * - commandsNumberForTrace defines number of command for trace.
     *   if <= 0 then commands' trace is off.
     * 
     * - eventRequestIDForTrace defines trace of received events
     *   according to request ID value:
     *   if < 0 then this trace is off;
     *   if = 0 then trace is for all received events;
     *   if > 0 then trace is for received events, which are triggered
     *          by this specified request ID value;
     * 
     * - eventKindForTrace defines trace of received events
     *   according to this specified kind of event.
     *   if = 0 then this trace is off;
     *   See JDWPConstants.EventKind class for values of
     *   event kinds.
     */
    int begCommandIdForTrace = 1;

    int commandsNumberForTrace = 0;

    int eventRequestIDForTrace = -1;

    byte eventKindForTrace = 0;

    /**
     * Internal class to synchronize jdwp events. When an event is received it
     * is stored in eventQueue. If there are any thread that waits for event it
     * is notified.
     */
    private class EventsSynchronyzer {

        /**
         * List of received events.
         */
        private List<EventPacket> eventQueue;

        /**
         * A default constructor.
         */
        EventsSynchronyzer() {
            eventQueue = new ArrayList<EventPacket>();
        }

        /**
         * Notifies thread that the new event has been received.
         * 
         * @param eventPacket
         *            instance of EventPacket
         * @throws InterruptedException
         */
        public void notifyThread(EventPacket eventPacket) throws InterruptedException {
            synchronized (this) {
                eventQueue.add(eventPacket);
                this.notify();
            }
        }

        /**
         * Waits for new event during timeout.
         * 
         * @param timeout
         *            wait timeout
         * @return EventPacket
         * @throws InterruptedException
         * @throws IOException
         * @throws TimeoutException
         *             if no event was received
         */
        public EventPacket waitForNextEvent(long timeout) throws InterruptedException, IOException {
            synchronized (this) {
                synchronized (eventQueue) {
                    if (!eventQueue.isEmpty()) {
                        return (EventPacket) eventQueue.remove(0);
                    }
                    if (connectionException != null) throw connectionException;
                }
                this.wait(timeout);
                synchronized (eventQueue) {
                    if (!eventQueue.isEmpty()) {
                        EventPacket event = (EventPacket) eventQueue.remove(0);
                        return event;
                    }
                    if (connectionException != null) {
                        throw connectionException;
                    }
                }
            }
            throw new TimeoutException(false);
        }

        /**
         * This method is called when connection is closed. It notifies all the
         * waiting threads.
         */
        public void terminate() {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    /**
     * Internal class to synchronize jdwp commands. It sends command packets
     * through connection and returns replies.
     */
    class CommandsSynchronyzer {

        private int commandId;

        private Hashtable<Integer, CommandPacket> commands;

        private Hashtable<Integer, ReplyPacket> replies;

        /**
         * A default constructor.
         */
        CommandsSynchronyzer() {
            commands = new Hashtable<Integer, CommandPacket>();
            replies = new Hashtable<Integer, ReplyPacket>();
            commandId = 1;
        }

        /**
         * Gets the next new id for a command.
         * 
         * @return int
         */
        private synchronized int getNextId() {
            return commandId++;
        }

        /**
         * Notifies thread that reply packet was received.
         * 
         * @param replyPacket
         *            instance of ReplyPacket
         * @throws IOException
         * @throws InterruptedException
         */
        public void notifyThread(ReplyPacket replyPacket) throws IOException, InterruptedException {
            synchronized (commands) {
                Integer Id = new Integer(replyPacket.getId());
                CommandPacket command = (CommandPacket) commands.remove(Id);
                if (command == null) {
                    throw new IOException("Reply id is corresponded to no command. Id = " + Id);
                }
                synchronized (command) {
                    synchronized (replies) {
                        replies.put(new Integer(replyPacket.getId()), replyPacket);
                    }
                    command.notifyAll();
                }
            }
        }

        /**
         * Sends command and waits for the reply during timeout.
         * 
         * @param command
         *            instance of CommandPacket
         * @param timeout
         *            reply wait timeout
         * @return
         * @throws TimeoutException
         *             if no reply was received
         */
        public ReplyPacket waitForReply(CommandPacket command, long timeout) throws InterruptedException, IOException {
            synchronized (command) {
                if (connectionException != null) throw connectionException;
                Integer Id = new Integer(getNextId());
                command.setId(Id.intValue());
                synchronized (commands) {
                    commands.put(Id, command);
                    if (commandsNumberForTrace > 0) {
                        int begCommandId = begCommandIdForTrace > 1 ? begCommandIdForTrace : 1;
                        if (Id.intValue() >= begCommandId) {
                            if ((Id.intValue() - begCommandId) < commandsNumberForTrace) {
                                logWriter.println(">>>>>>>>>> PacketDispatcher: PERFORM command: ID = " + Id.intValue() + "; CommandSet = " + command.getCommandSet() + "; Command = " + command.getCommand() + "...");
                            }
                        }
                    }
                    connection.writePacket(command.toBytesArray());
                }
                if (connectionException != null) throw connectionException;
                command.wait(timeout);
                ReplyPacket currentReply = null;
                synchronized (replies) {
                    currentReply = (ReplyPacket) replies.remove(Id);
                }
                if (currentReply != null) {
                    return currentReply;
                }
                if (connectionException != null) throw connectionException;
            }
            throw new TimeoutException(false);
        }

        /**
         * Sends command without waiting for the reply and returns id of the
         * sent command.
         * 
         * @param command
         *            instance of CommandPacket
         * @return command id
         * @throws IOException
         */
        public int sendCommand(CommandPacket command) throws IOException {
            if (connectionException != null) throw connectionException;
            Integer Id = new Integer(getNextId());
            command.setId(Id.intValue());
            synchronized (commands) {
                commands.put(Id, command);
                if (commandsNumberForTrace > 0) {
                    int begCommandId = begCommandIdForTrace > 1 ? begCommandIdForTrace : 1;
                    if (Id.intValue() >= begCommandId) {
                        if ((Id.intValue() - begCommandId) < commandsNumberForTrace) {
                            logWriter.println(">>>>>>>>>> PacketDispatcher: PERFORM command: ID = " + Id.intValue() + "; CommandSet = " + command.getCommandSet() + "; Command = " + command.getCommand() + "...");
                        }
                    }
                }
                connection.writePacket(command.toBytesArray());
            }
            if (connectionException != null) {
                throw connectionException;
            }
            return Id.intValue();
        }

        /**
         * Receives the reply during timeout for command with specified command
         * ID.
         * 
         * @param commandId
         *            id of previously sent commend
         * @param timeout
         *            receive timeout
         * @return received ReplyPacket
         * @throws TimeoutException
         *             if no reply was received
         */
        public ReplyPacket receiveReply(int commandId, long timeout) throws InterruptedException, IOException {
            if (connectionException != null) throw connectionException;
            ReplyPacket currentReply = null;
            long endTimeMlsecForWait = System.currentTimeMillis() + timeout;
            synchronized (replies) {
                while (true) {
                    currentReply = (ReplyPacket) replies.remove(new Integer(commandId));
                    if (currentReply != null) {
                        return currentReply;
                    }
                    if (connectionException != null) {
                        throw connectionException;
                    }
                    if (System.currentTimeMillis() >= endTimeMlsecForWait) {
                        break;
                    }
                    replies.wait(100);
                }
            }
            throw new TimeoutException(false);
        }

        /**
         * This method is called when connection is closed. It notifies all the
         * waiting threads.
         * 
         */
        public void terminate() {
            synchronized (commands) {
                for (Enumeration en = commands.keys(); en.hasMoreElements(); ) {
                    CommandPacket command = (CommandPacket) commands.get(en.nextElement());
                    synchronized (command) {
                        command.notifyAll();
                    }
                }
            }
        }
    }

    /** Transport which is used to sent and receive packets. */
    private TransportWrapper connection;

    /** Current test run configuration. */
    TestOptions config;

    private CommandsSynchronyzer commandsSynchronyzer;

    private EventsSynchronyzer eventsSynchronyzer;

    private LogWriter logWriter;

    private IOException connectionException;

    /**
     * Creates new PacketDispatcher instance.
     * 
     * @param connection
     *            open connection for reading and writing packets
     * @param config
     *            test run options
     * @param logWriter
     *            LogWriter object
     */
    public PacketDispatcher(TransportWrapper connection, TestOptions config, LogWriter logWriter) {
        this.connection = connection;
        this.config = config;
        this.logWriter = logWriter;
        commandsSynchronyzer = new CommandsSynchronyzer();
        eventsSynchronyzer = new EventsSynchronyzer();
        setDaemon(true);
        start();
    }

    /**
     * Reads packets from connection and dispatches them between waiting
     * threads.
     */
    public void run() {
        connectionException = null;
        try {
            while (!isInterrupted()) {
                byte[] packet = connection.readPacket();
                if (packet == null || packet.length == 0) break;
                if (packet.length < Packet.FLAGS_INDEX) {
                    logWriter.println(">>>>>>>>>> PacketDispatcher WARNING: WRONG received packet size = " + packet.length);
                } else {
                    int flag = packet[Packet.FLAGS_INDEX] & 0xFF;
                    if (flag != 0) {
                        if (flag != Packet.REPLY_PACKET_FLAG) {
                            logWriter.println(">>>>>>>>>> PacketDispatcher WARNING: WRONG received packet flags = " + Integer.toHexString(flag));
                        }
                    }
                }
                if (Packet.isReply(packet)) {
                    ReplyPacket replyPacket = new ReplyPacket(packet);
                    int packetLength = replyPacket.getLength();
                    if (packetLength < Packet.HEADER_SIZE) {
                        logWriter.println(">>>>>>>>>> PacketDispatcher WARNING: WRONG received packet length = " + packetLength);
                    }
                    if (commandsNumberForTrace > 0) {
                        int replyID = replyPacket.getId();
                        int begCommandId = begCommandIdForTrace > 1 ? begCommandIdForTrace : 1;
                        if (replyID >= begCommandId) {
                            if ((replyID - begCommandId) < commandsNumberForTrace) {
                                logWriter.println(">>>>>>>>>> PacketDispatcher: Received REPLY ID = " + replyID);
                            }
                        }
                    }
                    commandsSynchronyzer.notifyThread(replyPacket);
                } else {
                    EventPacket eventPacket = new EventPacket(packet);
                    ParsedEvent[] parsedEvents = ParsedEvent.parseEventPacket(eventPacket);
                    if ((eventRequestIDForTrace >= 0) || (eventKindForTrace > 0)) {
                        for (int i = 0; i < parsedEvents.length; i++) {
                            boolean trace = false;
                            int eventRequestID = parsedEvents[i].getRequestID();
                            if (eventRequestIDForTrace == 0) {
                                trace = true;
                            } else {
                                if (eventRequestID == eventRequestIDForTrace) {
                                    trace = true;
                                }
                            }
                            byte eventKind = parsedEvents[i].getEventKind();
                            if (eventKind == eventKindForTrace) {
                                trace = true;
                            }
                            if (trace) {
                                logWriter.println(">>>>>>>>>> PacketDispatcher: Received_EVENT[" + i + "]: eventRequestID= " + eventRequestID + "; eventKind =  " + eventKind + "(" + JDWPConstants.EventKind.getName(eventKind) + ")");
                            }
                        }
                    }
                    eventsSynchronyzer.notifyThread(eventPacket);
                }
            }
            connectionException = new TimeoutException(true);
        } catch (IOException e) {
            connectionException = e;
            e.printStackTrace();
        } catch (InterruptedException e) {
            connectionException = new InterruptedIOException(e.getMessage());
            connectionException.initCause(e);
            e.printStackTrace();
        }
        eventsSynchronyzer.terminate();
        commandsSynchronyzer.terminate();
    }

    /**
     * Receives event from event queue if there are any events or waits during
     * timeout for any event occurrence. This method should not be used
     * simultaneously from different threads. If there were no reply during the
     * timeout, TimeoutException is thrown.
     * 
     * @param timeout
     *            timeout in milliseconds
     * @return received event packet
     * @throws IOException
     *             is any connection error occurred
     * @throws InterruptedException
     *             if reading packet was interrupted
     * @throws TimeoutException
     *             if timeout exceeded
     */
    public EventPacket receiveEvent(long timeout) throws IOException, InterruptedException, TimeoutException {
        return eventsSynchronyzer.waitForNextEvent(timeout);
    }

    /**
     * Sends JDWP command packet and waits for reply packet during default
     * timeout. If there were no reply packet during the timeout,
     * TimeoutException is thrown.
     * 
     * @return received reply packet
     * @throws InterruptedException
     *             if reading packet was interrupted
     * @throws IOException
     *             if any connection error occurred
     * @throws TimeoutException
     *             if timeout exceeded
     */
    public ReplyPacket performCommand(CommandPacket command) throws InterruptedException, IOException, TimeoutException {
        return performCommand(command, config.getTimeout());
    }

    /**
     * Sends JDWP command packet and waits for reply packet during certain
     * timeout. If there were no reply packet during the timeout,
     * TimeoutException is thrown.
     * 
     * @param command
     *            command packet to send
     * @param timeout
     *            timeout in milliseconds
     * @return received reply packet
     * @throws InterruptedException
     *             if packet reading was interrupted
     * @throws IOException
     *             if any connection error occurred
     * @throws TimeoutException
     *             if timeout exceeded
     */
    public ReplyPacket performCommand(CommandPacket command, long timeout) throws InterruptedException, IOException, TimeoutException {
        return commandsSynchronyzer.waitForReply(command, timeout);
    }

    /**
     * Sends CommandPacket to debuggee VM without waiting for the reply. This
     * method is intended for special cases when there is need to divide
     * command's performing into two actions: command's sending and receiving
     * reply (e.g. for asynchronous JDWP commands' testing). After this method
     * the 'receiveReply()' method must be used latter for receiving reply for
     * sent command. It is NOT recommended to use this method for usual cases -
     * 'performCommand()' method must be used.
     * 
     * @param command
     *            Command packet to be sent
     * @return command ID of sent command
     * @throws IOException
     *             if any connection error occurred
     */
    public int sendCommand(CommandPacket command) throws IOException {
        return commandsSynchronyzer.sendCommand(command);
    }

    /**
     * Waits for reply for command which was sent before by 'sendCommand()'
     * method. Specified timeout is used as time limit for waiting. This method
     * (jointly with 'sendCommand()') is intended for special cases when there
     * is need to divide command's performing into two actions: command's
     * sending and receiving reply (e.g. for asynchronous JDWP commands'
     * testing). It is NOT recommended to use 'sendCommand()- receiveReply()'
     * pair for usual cases - 'performCommand()' method must be used.
     * 
     * @param commandId
     *            Command ID of sent before command, reply from which is
     *            expected to be received
     * @param timeout
     *            Specified timeout in milliseconds to wait for reply
     * @return received ReplyPacket
     * @throws IOException
     *             if any connection error occurred
     * @throws InterruptedException
     *             if reply packet's waiting was interrupted
     * @throws TimeoutException
     *             if timeout exceeded
     */
    public ReplyPacket receiveReply(int commandId, long timeout) throws InterruptedException, IOException, TimeoutException {
        return commandsSynchronyzer.receiveReply(commandId, timeout);
    }
}
