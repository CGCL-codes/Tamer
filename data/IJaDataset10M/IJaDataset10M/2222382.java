package net.jxta.pipe;

import java.util.EventListener;

/**
 *  The listener interface for receiving {@link net.jxta.pipe.PipeMsgEvent}
 *  events.
 *
 *  The following example illustrates how to implement a {@link net.jxta.pipe.PipeMsgListener}:
 *<pre><tt>
 * PipeMsgListener myListener = new PipeMsgListener() {
 *
 *   public void pipeMsgEvent(PipeMsgEvent event) {
 *     Message msg=null;
 *     try {
 *       msg = event.getMessage();
 *     } catch (Exception e) {
 *       e.printStackTrace();
 *       return;
 *     }
 *   }
 * }
 *
 * InputPipe pipeIn = pipe.createInputPipe(pipeAdv, myListener);
 * *</tt></pre>
 */
public interface PipeMsgListener extends EventListener {

    /**
     * Called for each pipe message event that occurs.
     *
     * @param  event  The event being received.
     */
    void pipeMsgEvent(PipeMsgEvent event);
}
