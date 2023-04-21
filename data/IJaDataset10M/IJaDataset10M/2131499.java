package edu.caltech.sbw;

import java.io.IOException;

/**
 * <p>
 * <h2>Message formats</h2>
 * 
 * There are four message types:
 * <ul>
 * <li> reply message (code 0)
 * <li> send message (code 1)
 * <li> call message (code 2)
 * <li> error message (code 3)
 * </ul>
 * <b>Call</b> and <b>send</b> messages have the following form:
 * 
 * <pre>
 * ,--------v------v------v-----v-----v------------v-----------v------.
 * | length | dest | type | mID | src | service id | method id | data |
 * `--------&circ;------&circ;------&circ;-----&circ;-----&circ;------------&circ;-----------&circ;------'
 * </pre>
 * 
 * where the meanings of the fields are as follows:
 * <dl>
 * <dt>length
 * <dd>(4 bytes) the length of the message, including the length field
 * <dt>dest
 * <dd>(4 bytes) an identifier indicating the destination module for the
 * message
 * <dt>type
 * <dd>(1 byte) the message type code (call, send, reply, error)
 * <dt>mID
 * <dd>(4 bytes) a unique identifier/sequence number for this message
 * <dt>src
 * <dd>(4 bytes) the identifier of the source module originating this message
 * <dt>service id
 * <dd>(4 bytes) an identifier indicating the desired service
 * <dt>method id
 * <dd>(4 bytes) an identifier indicating the desired method on the service
 * <dt>data
 * <dd>(arbitrary) the arguments to the method
 * </dl>
 * <p>
 * <b>Reply</b> messages have the following format:
 * 
 * <pre>
 * ,--------v------v------v-----v------.
 * | length | dest | type | mID | data |
 * `--------&circ;------&circ;------&circ;-----&circ;------'
 * </pre>
 * 
 * where the meanings of the fields are as follows:
 * <dl>
 * <dt>length
 * <dd>(4 bytes) the length of the message, including the length field
 * <dt>dest
 * <dd>(4 bytes) an identifier indicating the destination module for the
 * message
 * <dt>type
 * <dd>(1 byte) the message type code (call, send, reply, error)
 * <dt>mID
 * <dd>(4 bytes) a unique identifier/sequence number for this message
 * <dt>data
 * <dd>(arbitrary) the arguments to the method
 * </dl>
 * <p>
 * <b>Error</b> messages have the following format:
 * 
 * <pre>
 * ,--------v------v------v-----v-------v----------v--------------.
 * | length | dest | type | mID | error | user msg | detailed msg |
 * `--------&circ;------&circ;------&circ;-----&circ;-------&circ;----------&circ;--------------'
 * </pre>
 * 
 * where the meanings of the fields are as follows:
 * <dl>
 * <dt>length
 * <dd>(4 bytes) the length of the message, including the length field
 * <dt>dest
 * <dd>(4 bytes) an identifier indicating the destination module for the
 * message
 * <dt>type
 * <dd>(1 byte) the message type code (call, send, reply, error)
 * <dt>mID
 * <dd>(4 bytes) a unique identifier/sequence number for this message
 * <dt>error
 * <dd>(1 byte) a code indicating the type of exception
 * <dt>user msg
 * <dd>(arbitrary) a string giving a user-readable message of the error
 * <dt>detailed msg
 * <dd>(arbitrary) a string giving a more detailed error message
 * </dl>
 * <p>
 * 
 * @author Andrew Finney
 * @author $Author: fbergmann $
 * @version $Revision: 1.2 $
 */
class RPCInCall extends Thread {

    public RPCInCall(SBWRPC rpc, Receiver receiver) {
        this.rpc = rpc;
        this.receiver = receiver;
        SBWLog.trace("Spawning new message-handling thread for module " + rpc.getModuleId());
        setDaemon(true);
        setName("rpcincall-thread-" + threadNumber);
        threadNumber++;
        start();
    }

    /**
	 * When this is called, it assumes that the length, destination and type
	 * fields of the message have already been parsed out and consumed. (See the
	 * definition of the message structure above.)
	 */
    public synchronized void run() {
        while (operational) {
            try {
                if (!callPending && operational) wait();
                if (!operational) {
                    threadNumber--;
                    return;
                }
                SBWLog.trace("Parsing new message");
                callPending = false;
                int messageId = reader.unpackIntegerWithoutType();
                int srcModId = reader.unpackIntegerWithoutType();
                DataBlockWriter result = null;
                try {
                    int serviceId = reader.unpackIntegerWithoutType();
                    int methodId = reader.unpackIntegerWithoutType();
                    SBWLog.trace("msg id = " + messageId + ", module = " + srcModId + ", service = " + serviceId + ", method = " + methodId);
                    result = doReceive(srcModId, serviceId, methodId, reader);
                    if (transmitReply) rpc.transmit(srcModId, result.createReply(srcModId, messageId));
                } catch (IOException e) {
                    SBWLog.trace("Stopping incoming call thread");
                    pleaseStop();
                } catch (Throwable t) {
                    if (transmitReply) {
                        SBWException exception = SBWException.translateException(t);
                        SBWLog.trace("Transmitting the following exception:");
                        SBWLog.trace(t);
                        DynamicByteArray message = DataBlockWriter.createException(srcModId, messageId, exception.getCode(), exception.getMessage(), exception.getDetailedMessage());
                        try {
                            rpc.transmit(srcModId, message);
                        } catch (IOException e) {
                            SBWLog.trace("Stopping incoming call thread");
                            pleaseStop();
                        }
                    } else {
                        SBWLog.error("Unexpected throw while processing msg", t);
                        threadNumber--;
                        throw t;
                    }
                } finally {
                    if (result != null) result.release();
                }
            } catch (Throwable t) {
                SBWLog.exception("While extracting data from incoming call", t);
            }
            active = false;
        }
        threadNumber--;
    }

    public final DataBlockWriter doReceive(int srcModId, int serviceId, int methodId, DataBlockReader reader) throws SBWException {
        if (serviceId == SBWLowLevel.SYSTEM_SERVICE) switch(methodId) {
            case SBWLowLevel.ON_OTHER_MODULE_INSTANCE_SHUTDOWN_METHOD:
                rpc.onOtherModuleInstanceShutdown(reader, srcModId);
                return new DataBlockWriter();
            case SBWLowLevel.ON_OTHER_MODULE_INSTANCE_STARTUP_METHOD:
                rpc.onOtherModuleInstanceStartup(reader, srcModId);
                return new DataBlockWriter();
            case SBWLowLevel.ON_REGISTRATION_CHANGE_METHOD:
                rpc.onRegistrationChange(reader, srcModId);
                return new DataBlockWriter();
            case SBWLowLevel.SHUTDOWN_METHOD:
                rpc.onShutdown(reader, srcModId);
                return new DataBlockWriter();
            default:
                return receiver.receive(srcModId, serviceId, methodId, reader);
        } else return receiver.receive(srcModId, serviceId, methodId, reader);
    }

    public final boolean isActive() {
        return active;
    }

    /**
	 * assumes that the length, destination and type part of the message have
	 * already been consumed.
	 */
    public synchronized void execute(DataBlockReader reader, boolean transmitReply) {
        if (isActive()) {
            SBWLog.error("Attempted to execute incoming call in active call thread");
            return;
        }
        active = true;
        this.reader = reader;
        this.transmitReply = transmitReply;
        SBWLog.trace("Executing call, transmitReply = " + transmitReply);
        callPending = true;
        notifyAll();
    }

    public synchronized void pleaseStop() {
        operational = false;
        notifyAll();
    }

    DataBlockReader reader;

    boolean transmitReply;

    SBWRPC rpc;

    Receiver receiver;

    boolean active = false;

    boolean operational = true;

    boolean callPending = false;

    static int threadNumber = 1;

    static {
        Config.recordClassVersion(RPCInCall.class, "$Id: RPCInCall.java,v 1.2 2007/07/24 23:08:23 fbergmann Exp $");
    }
}
