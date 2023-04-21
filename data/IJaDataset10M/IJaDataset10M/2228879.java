package net.jxta.pipe;

import java.io.IOException;
import java.util.Set;
import net.jxta.id.ID;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.service.Service;

/**
 *  Pipes are the core mechanism for exchanging messages between JXTA
 *  applications or services.
 *
 *  <p/>Pipes are uniquely identified by a
 *  {@link net.jxta.protocol.PipeAdvertisement} which is associated with each
 *  pipe.
 *
 *  <p/>Several types of Pipe can currently be used:
 *
 *  <ul>
 *      <li><tt>JxtaUnicast</tt> - unicast, unreliable and unsecured pipe
 *      <li><tt>JxtaUnicastSecure</tt> - unicast and secure pipe
 *      <li><tt>JxtaPropagate</tt> - propagated, unreliable and unsecured pipe
 *  </ul>
 *
 *  <p/>The type of a Pipe is defined when creating its
 *  {@link net.jxta.protocol.PipeAdvertisement}.
 *
 *  @see net.jxta.protocol.PipeAdvertisement
 *  @see net.jxta.pipe.InputPipe
 *  @see net.jxta.pipe.OutputPipe
 *  @see net.jxta.endpoint.Message
 *  @see <a href="https://jxta-spec.dev.java.net/nonav/JXTAProtocols.html#overview-pipes" target='_blank'>JXTA Protocols Specification : Pipes</a>
 *  @see <a href="https://jxta-spec.dev.java.net/nonav/JXTAProtocols.html#proto-pbp" target='_blank'>JXTA Protocols Specification : Pipe Binding Protocol</a>
 */
public interface PipeService extends Service {

    /**
     * Unicast, unreliable and unsecured type of Pipe
     */
    public static final String UnicastType = "JxtaUnicast";

    /**
     * Propagated, unsecured and unreliable type of Pipe
     */
    public static final String PropagateType = "JxtaPropagate";

    /**
     * End-to-end secured unicast pipe of Pipe
     */
    public static final String UnicastSecureType = "JxtaUnicastSecure";

    /**
     * Create an InputPipe from a pipe Advertisement
     *
     * @param adv The advertisement of the Pipe.
     * @return The InputPipe created.
     * @throws IOException  error creating input pipe
     */
    public InputPipe createInputPipe(PipeAdvertisement adv) throws IOException;

    /**
     * Create an InputPipe from a pipe Advertisement
     *
     * @param adv is the advertisement of the Pipe.
     * @param listener PipeMsgListener to receive msgs.
     * @return InputPipe The InputPipe created.
     * @throws IOException Error creating input pipe
     */
    public InputPipe createInputPipe(PipeAdvertisement adv, PipeMsgListener listener) throws IOException;

    /**
     * Attempt to create an OutputPipe using the specified Pipe Advertisement.
     * The pipe will be be resolved within the provided timeout.
     *
     * @param pipeAdv The advertisement of the pipe being resolved.
     * @param timeout Time duration in milliseconds to wait for a successful
     * pipe resolution. <tt>0</tt> will wait indefinitely.
     * @return OutputPipe the successfully resolved OutputPipe.
     * @throws IOException  If the pipe cannot be created or failed to resolve
     * within the specified time.
     */
    public OutputPipe createOutputPipe(PipeAdvertisement pipeAdv, long timeout) throws IOException;

    /**
     * Attempt to create an OutputPipe using the specified Pipe Advertisement.
     * The pipe will be be resolved to one of the peers in the set of peer ids
     * provided within the provided timeout.
     *
     * @param pipeAdv The advertisement of the pipe being resolved.
     * @param resolvablePeers The peers on which the pipe may be resolved.
     * <strong>If the Set is empty then the pipe may be resolved to any 
     * destination peer.</strong>
     * @param timeout Time duration in milliseconds to wait for a successful
     * pipe resolution. <tt>0</tt> will wait indefinitely.
     * @return The successfully resolved OutputPipe.
     * @throws IOException If the pipe cannot be created or failed to resolve
     * within the specified time.
     */
    public OutputPipe createOutputPipe(PipeAdvertisement pipeAdv, Set<? extends ID> resolvablePeers, long timeout) throws IOException;

    /**
     * Attempt to create an OutputPipe using the specified Pipe Advertisement.
     * The pipe may be resolved to any destination peer. When the pipe is
     * resolved the listener will be called.
     *
     * @param pipeAdv The advertisement of the pipe being resolved.
     * @param listener The listener to be called when the pipe is resolved.
     * @throws IOException If the pipe cannot be created.
     */
    public void createOutputPipe(PipeAdvertisement pipeAdv, OutputPipeListener listener) throws IOException;

    /**
     * Attempt to create an OutputPipe using the specified Pipe Advertisement.
     * When the pipe is resolved to one of the peers in the set of peer ids
     * provided the listener will be called.
     *
     * @param pipeAdv The advertisement of the pipe being resolved.
     * @param resolvablePeers The set of peers on which the pipe may be resolved.
     * <strong>If the Set is empty then the pipe may be resolved to any 
     * destination peer.</strong>
     * @param listener the listener to be called when the pipe is resolved.
     * @throws IOException  If the pipe cannot be created.
     */
    public void createOutputPipe(PipeAdvertisement pipeAdv, Set<? extends ID> resolvablePeers, OutputPipeListener listener) throws IOException;

    /**
     *  Remove an OutputPipeListener previously registered with
     *  <code>createOuputputPipe</code>.
     *
     * @deprecated This method is being replaced by {@link #removeOutputPipeListener(ID,OutputPipeListener)}.
     *
     * @param pipeID The pipe who's listener is to be removed.
     * @param listener The listener to remove.
     * @return The listener which was removed or null if the key did not have a mapping.
     */
    @Deprecated
    public OutputPipeListener removeOutputPipeListener(String pipeID, OutputPipeListener listener);

    /**
     *  Remove an OutputPipeListener previously registered with
     *  <code>createOuputputPipe</code>.
     *
     * @param pipeID The pipe who's listener is to be removed.
     * @param listener The listener to remove.
     * @return The listener which was removed or null if the key did not have a mapping.
     */
    public OutputPipeListener removeOutputPipeListener(ID pipeID, OutputPipeListener listener);
}
