package org.schwering.irc.manager.event;

/**
 * Adapter for connection listener.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class ConnectionAdapter implements ConnectionListener {

    public void channelJoined(UserParticipationEvent event) {
    }

    public void channelLeft(UserParticipationEvent event) {
    }

    public void connectionEstablished(ConnectionEvent event) {
    }

    public void connectionLost(ConnectionEvent event) {
    }

    public void errorReceived(ErrorEvent event) {
    }

    public void invitationReceived(InvitationEvent event) {
    }

    public void invitationDeliveryReceived(InvitationEvent event) {
    }

    public void motdReceived(MotdEvent event) {
    }

    public void infoReceived(InfoEvent event) {
    }

    public void linksReceived(LinksEvent event) {
    }

    public void statsReceived(StatsEvent event) {
    }

    public void pingReceived(PingEvent event) {
    }

    public void numericErrorReceived(NumericEvent event) {
    }

    public void numericReplyReceived(NumericEvent event) {
    }

    public void userModeReceived(UserModeEvent event) {
    }

    public void whoisReceived(WhoisEvent event) {
    }

    public void whowasReceived(WhowasEvent event) {
    }

    public void banlistReceived(BanlistEvent event) {
    }

    public void channelModeReceived(ChannelModeEvent event) {
    }

    public void messageReceived(MessageEvent event) {
    }

    public void namesReceived(NamesEvent event) {
    }

    public void whoReceived(WhoEvent event) {
    }

    public void nickChanged(NickEvent event) {
    }

    public void noticeReceived(MessageEvent event) {
    }

    public void topicReceived(TopicEvent event) {
    }

    public void listReceived(ListEvent event) {
    }

    public void userJoined(UserParticipationEvent event) {
    }

    public void userLeft(UserParticipationEvent event) {
    }
}
