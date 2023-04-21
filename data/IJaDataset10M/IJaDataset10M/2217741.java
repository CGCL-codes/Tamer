package com.internetcds.jdbc.tds;

/**   
 *   This class provides support for canceling queries. 
 *  <p>
 *   Basically all threads can be divided into two groups, workers and
 *   cancelers.  The canceler can cancel at anytime, even when there is no
 *   outstanding query to cancel.  A worker can be in one of 4 states-
 *  <p>
 *     1) Not doing anything DB related.<br>
 *     2) currently sending a request to the database. (Note-  any time
 *        a request is sent to the DB the DB will send a response.  This
 *        means a thread in state 2 must go to state 3.)<br>
 *     3) waiting for a response from DB<br>
 *     4) reading the response from DB<br>
 *  <p>
 *   I can easily make it so that only one thread at a time can be in state
 *   2, 3, or 4.
 *  <p>
 *   The way that a cancel works in TDS is you send a cancel packet to
 *   server.  The server will then stop whatever it might be doing and
 *   reply with END_OF_DATA packet with the cancel flag set.  (It sends
 *   this packet even if it wasn't doing anything.)  I will call this
 *   packet a CANCEL_ACK packet
 *  <p>
 *   All that I really need is to do is make sure that I try to read as
 *   many CANCEL_ACKs as I request and the I make sure that some thread is
 *   out there ready to read any CANCEL_ACKs that i request.
 *  <p>
 *   Clearly if all my worker threads are in state 1 then the cancel
 *   request could be just a nop.
 *  <p>
 *   If I have some worker thread in state 2, 3, or 4 I think I will be fine
 *   if I just make sure that the thread reads until the CANCEL_ACK packet.
 *  <p>
 *   I think I will just have a control object that has one boolean,
 *   readInProgress and two integers, cancelsRequested and
 *   cancelsProcessed.
 *  <p>
 *  <p>
 *   The doCancel() method will-
 *     a) lock the object
 *     b) if there is no read in progress it will unlock and return.
 *     c) otherwise it will send the CANCEL packet,
 *     d) increment the cancelsRequested
 *     e) unlock object and wait until notified that the 
 *        cancel was ack'd
 *  <p>
 *   Whenever the worker thread wants to read a response from the DB it
 *   must-
 *     a) lock the control object,<b>
 *     b) set the queryOutstanding flag<b>
 *     c) unlock the control object<b>
 *     d) call the Tds.processSubPacket() method.<b>
 *     e) lock the control object<b>
 *     f) If the packet was a cancel ack it will increment
 *        cancelsProcessed <b>
 *     g) notify any threads that are waiting for cancel acknowledgment<b>
 *     h) unlock the control object.<b>
 * 
 * @version  $Id: CancelController.java,v 1.2 2001-08-31 12:47:20 curthagenlocher Exp $
 @ @author Craig Spannring
 */
public class CancelController {

    public static final String cvsVersion = "$Id: CancelController.java,v 1.2 2001-08-31 12:47:20 curthagenlocher Exp $";

    boolean awaitingData = false;

    int cancelsRequested = 0;

    int cancelsProcessed = 0;

    public synchronized void setQueryInProgressFlag() {
        awaitingData = true;
    }

    private synchronized void clearQueryInProgressFlag() {
        awaitingData = false;
    }

    public synchronized void finishQuery(boolean wasCanceled, boolean moreResults) {
        if (!moreResults) {
            clearQueryInProgressFlag();
        }
        if (wasCanceled) {
            handleCancelAck();
        }
    }

    public synchronized void doCancel(TdsComm comm) throws java.io.IOException {
        if (awaitingData) {
            comm.startPacket(TdsComm.CANCEL);
            comm.sendPacket();
            cancelsRequested++;
            while (cancelsRequested > cancelsProcessed) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        } else {
        }
    }

    private synchronized void handleCancelAck() {
        cancelsProcessed++;
        notify();
    }
}
