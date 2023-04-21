package org.apache.catalina.tribes.group;

import org.apache.catalina.tribes.ChannelException;
import org.apache.catalina.tribes.ChannelInterceptor;
import org.apache.catalina.tribes.ChannelMessage;
import org.apache.catalina.tribes.Member;

/**
 * Abstract class for the interceptor base class.
 * @author Filip Hanik
 * @version $Revision: 467222 $, $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public abstract class ChannelInterceptorBase implements ChannelInterceptor {

    protected static org.apache.juli.logging.Log log = org.apache.juli.logging.LogFactory.getLog(ChannelInterceptorBase.class);

    private ChannelInterceptor next;

    private ChannelInterceptor previous;

    protected int optionFlag = 0;

    public ChannelInterceptorBase() {
    }

    public boolean okToProcess(int messageFlags) {
        if (this.optionFlag == 0) return true;
        return ((optionFlag & messageFlags) == optionFlag);
    }

    public final void setNext(ChannelInterceptor next) {
        this.next = next;
    }

    public final ChannelInterceptor getNext() {
        return next;
    }

    public final void setPrevious(ChannelInterceptor previous) {
        this.previous = previous;
    }

    public void setOptionFlag(int optionFlag) {
        this.optionFlag = optionFlag;
    }

    public final ChannelInterceptor getPrevious() {
        return previous;
    }

    public int getOptionFlag() {
        return optionFlag;
    }

    public void sendMessage(Member[] destination, ChannelMessage msg, InterceptorPayload payload) throws ChannelException {
        if (getNext() != null) getNext().sendMessage(destination, msg, payload);
    }

    public void messageReceived(ChannelMessage msg) {
        if (getPrevious() != null) getPrevious().messageReceived(msg);
    }

    public boolean accept(ChannelMessage msg) {
        return true;
    }

    public void memberAdded(Member member) {
        if (getPrevious() != null) getPrevious().memberAdded(member);
    }

    public void memberDisappeared(Member member) {
        if (getPrevious() != null) getPrevious().memberDisappeared(member);
    }

    public void heartbeat() {
        if (getNext() != null) getNext().heartbeat();
    }

    /**
     * has members
     */
    public boolean hasMembers() {
        if (getNext() != null) return getNext().hasMembers(); else return false;
    }

    /**
     * Get all current cluster members
     * @return all members or empty array
     */
    public Member[] getMembers() {
        if (getNext() != null) return getNext().getMembers(); else return null;
    }

    /**
     *
     * @param mbr Member
     * @return Member
     */
    public Member getMember(Member mbr) {
        if (getNext() != null) return getNext().getMember(mbr); else return null;
    }

    /**
     * Return the member that represents this node.
     *
     * @return Member
     */
    public Member getLocalMember(boolean incAlive) {
        if (getNext() != null) return getNext().getLocalMember(incAlive); else return null;
    }

    /**
     * Starts up the channel. This can be called multiple times for individual services to start
     * The svc parameter can be the logical or value of any constants
     * @param svc int value of <BR>
     * DEFAULT - will start all services <BR>
     * MBR_RX_SEQ - starts the membership receiver <BR>
     * MBR_TX_SEQ - starts the membership broadcaster <BR>
     * SND_TX_SEQ - starts the replication transmitter<BR>
     * SND_RX_SEQ - starts the replication receiver<BR>
     * @throws ChannelException if a startup error occurs or the service is already started.
     */
    public void start(int svc) throws ChannelException {
        if (getNext() != null) getNext().start(svc);
    }

    /**
     * Shuts down the channel. This can be called multiple times for individual services to shutdown
     * The svc parameter can be the logical or value of any constants
     * @param svc int value of <BR>
     * DEFAULT - will shutdown all services <BR>
     * MBR_RX_SEQ - stops the membership receiver <BR>
     * MBR_TX_SEQ - stops the membership broadcaster <BR>
     * SND_TX_SEQ - stops the replication transmitter<BR>
     * SND_RX_SEQ - stops the replication receiver<BR>
     * @throws ChannelException if a startup error occurs or the service is already started.
     */
    public void stop(int svc) throws ChannelException {
        if (getNext() != null) getNext().stop(svc);
    }

    public void fireInterceptorEvent(InterceptorEvent event) {
    }
}
