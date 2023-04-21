package net.sf.babble;

import java.util.EventListener;
import javax.swing.event.EventListenerList;
import net.sf.babble.events.*;

/**
 * This class handles the events fired by the <code>net.sf.babble</code> package.
 *
 * Implementations of the event interfaces in the <CODE>net.sf.babble.events</CODE> package
 * can be added or removed to the <code>EventManager</code>. When an event
 * implementation is added, it will receive all events for that interface until
 * the implementation is destroyed or removed.
 *
 * See the <code>net.sf.babble.events</code> package for details on each event and its
 * parameters.
 * @see net.sf.babble.events
 * @see java.util.EventListener
 * @version $Id: EventManager.java 243 2004-10-12 10:54:00Z speakmon $
 * @author Ben Speakmon
 */
public final class EventManager {

    private EventListenerList listenerList;

    private static final EventManager INSTANCE = new EventManager();

    /** Creates a new instance of EventManager */
    private EventManager() {
        listenerList = new EventListenerList();
    }

    /**
     * Returns the shared instance of <code>EventManager</code>.
     * @return the shared <code>EventManager</code> instance
     */
    public static final EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a string representation of all currently registered event listeners.
     * This is done simply by calling <code>toString()</code> on each. String
     * representations of classes outside the Babble library are not guaranteed to be
     * nonzero or non-null.
     * @return a <code>String</code> representation of all registered event listeners
     */
    public String printListeners() {
        return listenerList.toString();
    }

    /**
     * Returns the total number of registered listeners.
     * @return the total number of registered listeners
     */
    public int getListenerCount() {
        return listenerList.getListenerCount();
    }

    /**
     * Returns the number of registered listeners that are instances of the specified
     * class.
     * @param c the <code>Class</code> instance to use for counting the listeners
     * @return the number of registered listeners of this class
     */
    public int getListenerCount(Class<? extends EventListener> c) {
        return listenerList.getListenerCount(c);
    }

    public Object[] getListenerList() {
        return listenerList.getListenerList();
    }

    public EventListener[] getListeners(Class<? extends EventListener> c) {
        return listenerList.getListeners(c);
    }

    private <T extends EventListener> void add(Class<T> c, T l) {
        listenerList.add(c, l);
    }

    private <T extends EventListener> void remove(Class<T> c, T l) {
        listenerList.remove(c, l);
    }

    public void addActionEventHandler(ActionEventHandler aeh) {
        add(ActionEventHandler.class, aeh);
    }

    public void removeActionEventHandler(ActionEventHandler aeh) {
        remove(ActionEventHandler.class, aeh);
    }

    void fireActionEventHandlers(ActionEvent event) {
        EventListener[] handlers = getListeners(ActionEventHandler.class);
        for (EventListener handler : handlers) {
            ((ActionEventHandler) handler).onAction(event);
        }
    }

    public void addAdminEventHandler(AdminEventHandler aeh) {
        add(AdminEventHandler.class, aeh);
    }

    public void removeAdminEventHandler(AdminEventHandler aeh) {
        remove(AdminEventHandler.class, aeh);
    }

    void fireAdminEventHandlers(AdminEvent event) {
        EventListener[] handlers = getListeners(AdminEventHandler.class);
        for (EventListener handler : handlers) {
            ((AdminEventHandler) handler).onAdmin(event);
        }
    }

    public void addAwayEventHandler(AwayEventHandler aeh) {
        add(AwayEventHandler.class, aeh);
    }

    public void removeAwayEventHandler(AwayEventHandler aeh) {
        remove(AwayEventHandler.class, aeh);
    }

    void fireAwayEventHandlers(AwayEvent event) {
        EventListener[] handlers = getListeners(AwayEventHandler.class);
        for (EventListener handler : handlers) {
            ((AwayEventHandler) handler).onAway(event);
        }
    }

    public void addChannelListEventHandler(ChannelListEventHandler cleh) {
        add(ChannelListEventHandler.class, cleh);
    }

    public void removeChannelListEventHandler(ChannelListEventHandler cleh) {
        remove(ChannelListEventHandler.class, cleh);
    }

    void fireChannelListEventHandlers(ChannelListEvent event) {
        EventListener[] handlers = getListeners(ChannelListEventHandler.class);
        for (EventListener handler : handlers) {
            ((ChannelListEventHandler) handler).onChannelList(event);
        }
    }

    public void addChannelModeChangeEventHandler(ChannelModeChangeEventHandler cmceh) {
        add(ChannelModeChangeEventHandler.class, cmceh);
    }

    public void removeChannelModeChangeEventHandler(ChannelModeChangeEventHandler cmceh) {
        remove(ChannelModeChangeEventHandler.class, cmceh);
    }

    void fireChannelModeChangeEventHandlers(ChannelModeChangeEvent event) {
        EventListener[] handlers = getListeners(ChannelModeChangeEventHandler.class);
        for (EventListener handler : handlers) {
            ((ChannelModeChangeEventHandler) handler).onChannelModeChange(event);
        }
    }

    public void addChannelModeRequestEventHandler(ChannelModeRequestEventHandler cmreh) {
        add(ChannelModeRequestEventHandler.class, cmreh);
    }

    public void removeChannelModeRequestEventHandler(ChannelModeRequestEventHandler cmreh) {
        remove(ChannelModeRequestEventHandler.class, cmreh);
    }

    void fireChannelModeRequestEventHandlers(ChannelModeRequestEvent event) {
        EventListener[] handlers = getListeners(ChannelModeRequestEventHandler.class);
        for (EventListener handler : handlers) {
            ((ChannelModeRequestEventHandler) handler).onChannelModeRequest(event);
        }
    }

    public void addDisconnectedEventHandler(DisconnectedEventHandler deh) {
        add(DisconnectedEventHandler.class, deh);
    }

    public void removeDisconnectedEventHandler(DisconnectedEventHandler deh) {
        remove(DisconnectedEventHandler.class, deh);
    }

    void fireDisconnectedEventHandlers(DisconnectedEvent event) {
        EventListener[] handlers = getListeners(DisconnectedEventHandler.class);
        for (EventListener handler : handlers) {
            ((DisconnectedEventHandler) handler).onDisconnected(event);
        }
    }

    public void addDisconnectingEventHandler(DisconnectingEventHandler deh) {
        add(DisconnectingEventHandler.class, deh);
    }

    public void removeDisconnectingEventHandler(DisconnectingEventHandler deh) {
        remove(DisconnectingEventHandler.class, deh);
    }

    void fireDisconnectingEventHandlers(DisconnectingEvent event) {
        EventListener[] handlers = getListeners(DisconnectingEventHandler.class);
        for (EventListener handler : handlers) {
            ((DisconnectingEventHandler) handler).onDisconnecting(event);
        }
    }

    public void addErrorMessageEventHandler(ErrorMessageEventHandler emeh) {
        add(ErrorMessageEventHandler.class, emeh);
    }

    public void removeErrorMessageEventHandler(ErrorMessageEventHandler emeh) {
        remove(ErrorMessageEventHandler.class, emeh);
    }

    void fireErrorMessageEventHandlers(ErrorMessageEvent event) {
        EventListener[] handlers = getListeners(ErrorMessageEventHandler.class);
        for (EventListener handler : handlers) {
            ((ErrorMessageEventHandler) handler).onError(event);
        }
    }

    public void addInfoEventHandler(InfoEventHandler ieh) {
        add(InfoEventHandler.class, ieh);
    }

    public void removeInfoEventHandler(InfoEventHandler ieh) {
        remove(InfoEventHandler.class, ieh);
    }

    void fireInfoEventHandlers(InfoEvent event) {
        EventListener[] handlers = getListeners(InfoEventHandler.class);
        for (EventListener handler : handlers) {
            ((InfoEventHandler) handler).onInfo(event);
        }
    }

    public void addInviteEventHandler(InviteEventHandler ieh) {
        add(InviteEventHandler.class, ieh);
    }

    public void removeInviteEventHandler(InviteEventHandler ieh) {
        remove(InviteEventHandler.class, ieh);
    }

    void fireInviteEventHandlers(InviteEvent event) {
        EventListener[] handlers = getListeners(InviteEventHandler.class);
        for (EventListener handler : handlers) {
            ((InviteEventHandler) handler).onInvite(event);
        }
    }

    public void addInviteSentEventHandler(InviteSentEventHandler iseh) {
        add(InviteSentEventHandler.class, iseh);
    }

    public void removeInviteSentEventHandler(InviteSentEventHandler iseh) {
        remove(InviteSentEventHandler.class, iseh);
    }

    void fireInviteSentEventHandlers(InviteSentEvent event) {
        EventListener[] handlers = getListeners(InviteSentEventHandler.class);
        for (EventListener handler : handlers) {
            ((InviteSentEventHandler) handler).onInviteSent(event);
        }
    }

    public void addIsonEventHandler(IsonEventHandler ieh) {
        add(IsonEventHandler.class, ieh);
    }

    public void removeIsonEventHandler(IsonEventHandler ieh) {
        remove(IsonEventHandler.class, ieh);
    }

    void fireIsonEventHandlers(IsonEvent event) {
        EventListener[] handlers = getListeners(IsonEventHandler.class);
        for (EventListener handler : handlers) {
            ((IsonEventHandler) handler).onIson(event);
        }
    }

    public void addJoinEventHandler(JoinEventHandler jeh) {
        add(JoinEventHandler.class, jeh);
    }

    public void removeJoinEventHandler(JoinEventHandler jeh) {
        remove(JoinEventHandler.class, jeh);
    }

    void fireJoinEventHandlers(JoinEvent event) {
        EventListener[] handlers = getListeners(JoinEventHandler.class);
        for (EventListener handler : handlers) {
            ((JoinEventHandler) handler).onJoin(event);
        }
    }

    public void addKickEventHandler(KickEventHandler keh) {
        add(KickEventHandler.class, keh);
    }

    public void removeKickEventHandler(KickEventHandler keh) {
        remove(KickEventHandler.class, keh);
    }

    void fireKickEventHandlers(KickEvent event) {
        EventListener[] handlers = getListeners(KickEventHandler.class);
        for (EventListener handler : handlers) {
            ((KickEventHandler) handler).onKick(event);
        }
    }

    public void addLinksEventHandler(LinksEventHandler leh) {
        add(LinksEventHandler.class, leh);
    }

    public void removeLinksEventHandler(LinksEventHandler leh) {
        remove(LinksEventHandler.class, leh);
    }

    void fireLinksEventHandlers(LinksEvent event) {
        EventListener[] handlers = getListeners(LinksEventHandler.class);
        for (EventListener handler : handlers) {
            ((LinksEventHandler) handler).onLinks(event);
        }
    }

    public void addListEventHandler(ListEventHandler leh) {
        add(ListEventHandler.class, leh);
    }

    public void removeListEventHandler(ListEventHandler leh) {
        remove(ListEventHandler.class, leh);
    }

    void fireListEventHandlers(ListEvent event) {
        EventListener[] handlers = getListeners(ListEventHandler.class);
        for (EventListener handler : handlers) {
            ((ListEventHandler) handler).onList(event);
        }
    }

    public void addLusersEventHandler(LusersEventHandler leh) {
        add(LusersEventHandler.class, leh);
    }

    public void removeLusersEventHandler(LusersEventHandler leh) {
        remove(LusersEventHandler.class, leh);
    }

    void fireLusersEventHandlers(LusersEvent event) {
        EventListener[] handlers = getListeners(LusersEventHandler.class);
        for (EventListener handler : handlers) {
            ((LusersEventHandler) handler).onLusers(event);
        }
    }

    public void addMotdEventHandler(MotdEventHandler meh) {
        add(MotdEventHandler.class, meh);
    }

    public void removeMotdEventHandler(MotdEventHandler meh) {
        remove(MotdEventHandler.class, meh);
    }

    void fireMotdEventHandlers(MotdEvent event) {
        EventListener[] handlers = getListeners(MotdEventHandler.class);
        for (EventListener handler : handlers) {
            ((MotdEventHandler) handler).onMotd(event);
        }
    }

    public void addNamesEventHandler(NamesEventHandler neh) {
        add(NamesEventHandler.class, neh);
    }

    public void removeNamesEventHandler(NamesEventHandler neh) {
        remove(NamesEventHandler.class, neh);
    }

    void fireNamesEventHandlers(NamesEvent event) {
        EventListener[] handlers = getListeners(NamesEventHandler.class);
        for (EventListener handler : handlers) {
            ((NamesEventHandler) handler).onNames(event);
        }
    }

    public void addNickErrorEventHandler(NickErrorEventHandler neeh) {
        add(NickErrorEventHandler.class, neeh);
    }

    public void removeNickErrorEventHandler(NickErrorEventHandler neeh) {
        remove(NickErrorEventHandler.class, neeh);
    }

    void fireNickErrorEventHandlers(NickErrorEvent event) {
        EventListener[] handlers = getListeners(NickErrorEventHandler.class);
        for (EventListener handler : handlers) {
            ((NickErrorEventHandler) handler).onNickError(event);
        }
    }

    public void addNickEventHandler(NickEventHandler neh) {
        add(NickEventHandler.class, neh);
    }

    public void removeNickEventHandler(NickEventHandler neh) {
        remove(NickEventHandler.class, neh);
    }

    void fireNickEventHandlers(NickEvent event) {
        EventListener[] handlers = getListeners(NickEventHandler.class);
        for (EventListener handler : handlers) {
            ((NickEventHandler) handler).onNick(event);
        }
    }

    public void addPartEventHandler(PartEventHandler peh) {
        add(PartEventHandler.class, peh);
    }

    public void removePartEventHandler(PartEventHandler peh) {
        remove(PartEventHandler.class, peh);
    }

    void firePartEventHandlers(PartEvent event) {
        EventListener[] handlers = getListeners(PartEventHandler.class);
        for (EventListener handler : handlers) {
            ((PartEventHandler) handler).onPart(event);
        }
    }

    public void addPingEventHandler(PingEventHandler peh) {
        add(PingEventHandler.class, peh);
    }

    public void removePingEventHandler(PingEventHandler peh) {
        remove(PingEventHandler.class, peh);
    }

    void firePingEventHandlers(PingEvent event) {
        EventListener[] handlers = getListeners(PingEventHandler.class);
        for (EventListener handler : handlers) {
            ((PingEventHandler) handler).onPing(event);
        }
    }

    public void addPrivateActionEventHandler(PrivateActionEventHandler paeh) {
        add(PrivateActionEventHandler.class, paeh);
    }

    public void removePrivateActionEventHandler(PrivateActionEventHandler paeh) {
        remove(PrivateActionEventHandler.class, paeh);
    }

    void firePrivateActionEventHandlers(PrivateActionEvent event) {
        EventListener[] handlers = getListeners(PrivateActionEventHandler.class);
        for (EventListener handler : handlers) {
            ((PrivateActionEventHandler) handler).onPrivateAction(event);
        }
    }

    public void addPrivateMessageEventHandler(PrivateMessageEventHandler paeh) {
        add(PrivateMessageEventHandler.class, paeh);
    }

    public void removePrivateMessageEventHandler(PrivateMessageEventHandler paeh) {
        remove(PrivateMessageEventHandler.class, paeh);
    }

    void firePrivateMessageEventHandlers(PrivateMessageEvent event) {
        EventListener[] handlers = getListeners(PrivateMessageEventHandler.class);
        for (EventListener handler : handlers) {
            ((PrivateMessageEventHandler) handler).onPrivate(event);
        }
    }

    public void addPrivateNoticeEventHandler(PrivateNoticeEventHandler pneh) {
        add(PrivateNoticeEventHandler.class, pneh);
    }

    public void removePrivateNoticeEventHandler(PrivateNoticeEventHandler pneh) {
        remove(PrivateNoticeEventHandler.class, pneh);
    }

    void firePrivateNoticeEventHandlers(PrivateNoticeEvent event) {
        EventListener[] handlers = getListeners(PrivateNoticeEventHandler.class);
        for (EventListener handler : handlers) {
            ((PrivateNoticeEventHandler) handler).onPrivateNotice(event);
        }
    }

    public void addPublicMessageEventHandler(PublicMessageEventHandler pmeh) {
        add(PublicMessageEventHandler.class, pmeh);
    }

    public void removePublicMessageEventHandler(PublicMessageEventHandler pmeh) {
        remove(PublicMessageEventHandler.class, pmeh);
    }

    void firePublicMessageEventHandlers(PublicMessageEvent event) {
        EventListener[] handlers = getListeners(PublicMessageEventHandler.class);
        for (EventListener handler : handlers) {
            ((PublicMessageEventHandler) handler).onPublic(event);
        }
    }

    public void addPublicNoticeEventHandler(PublicNoticeEventHandler pneh) {
        add(PublicNoticeEventHandler.class, pneh);
    }

    public void removePublicNoticeEventHandler(PublicNoticeEventHandler pneh) {
        remove(PublicNoticeEventHandler.class, pneh);
    }

    void firePublicNoticeEventHandlers(PublicNoticeEvent event) {
        EventListener[] handlers = getListeners(PublicNoticeEventHandler.class);
        for (EventListener handler : handlers) {
            ((PublicNoticeEventHandler) handler).onPublicNotice(event);
        }
    }

    public void addQuitEventHandler(QuitEventHandler qeh) {
        add(QuitEventHandler.class, qeh);
    }

    public void removeQuitEventHandler(QuitEventHandler qeh) {
        remove(QuitEventHandler.class, qeh);
    }

    void fireQuitEventHandlers(QuitEvent event) {
        EventListener[] handlers = getListeners(QuitEventHandler.class);
        for (EventListener handler : handlers) {
            ((QuitEventHandler) handler).onQuit(event);
        }
    }

    public void addRawMessageReceivedEventHandler(RawMessageReceivedEventHandler rmreh) {
        add(RawMessageReceivedEventHandler.class, rmreh);
    }

    public void removeRawMessageReceivedEventHandler(RawMessageReceivedEventHandler rmreh) {
        remove(RawMessageReceivedEventHandler.class, rmreh);
    }

    void fireRawMessageReceivedEventHandlers(RawMessageReceivedEvent event) {
        EventListener[] handlers = getListeners(RawMessageReceivedEventHandler.class);
        for (EventListener handler : handlers) {
            ((RawMessageReceivedEventHandler) handler).onRawMessageReceived(event);
        }
    }

    public void addRawMessageSentEventHandler(RawMessageSentEventHandler rmseh) {
        add(RawMessageSentEventHandler.class, rmseh);
    }

    public void removeRawMessageSentEventHandler(RawMessageSentEventHandler rmseh) {
        remove(RawMessageSentEventHandler.class, rmseh);
    }

    void fireRawMessageSentEventHandlers(RawMessageSentEvent event) {
        EventListener[] handlers = getListeners(RawMessageSentEventHandler.class);
        for (EventListener handler : handlers) {
            ((RawMessageSentEventHandler) handler).onRawMessageSent(event);
        }
    }

    public void addRegisteredEventHandler(RegisteredEventHandler reh) {
        add(RegisteredEventHandler.class, reh);
    }

    public void removeRegisteredEventHandler(RegisteredEventHandler reh) {
        remove(RegisteredEventHandler.class, reh);
    }

    void fireRegisteredEventHandlers(RegisteredEvent event) {
        EventListener[] handlers = getListeners(RegisteredEventHandler.class);
        for (EventListener handler : handlers) {
            ((RegisteredEventHandler) handler).onRegistered(event);
        }
    }

    public void addReplyEventHandler(ReplyEventHandler reh) {
        add(ReplyEventHandler.class, reh);
    }

    public void removeReplyEventHandler(ReplyEventHandler reh) {
        remove(ReplyEventHandler.class, reh);
    }

    void fireReplyEventHandlers(ReplyEvent event) {
        EventListener[] handlers = getListeners(ReplyEventHandler.class);
        for (EventListener handler : handlers) {
            ((ReplyEventHandler) handler).onReply(event);
        }
    }

    public void addStatsEventHandler(StatsEventHandler seh) {
        add(StatsEventHandler.class, seh);
    }

    public void removeStatsEventHandler(StatsEventHandler seh) {
        remove(StatsEventHandler.class, seh);
    }

    void fireStatsEventHandlers(StatsEvent event) {
        EventListener[] handlers = getListeners(StatsEventHandler.class);
        for (EventListener handler : handlers) {
            ((StatsEventHandler) handler).onStats(event);
        }
    }

    public void addTimeEventHandler(TimeEventHandler teh) {
        add(TimeEventHandler.class, teh);
    }

    public void removeTimeEventHandler(TimeEventHandler teh) {
        remove(TimeEventHandler.class, teh);
    }

    void fireTimeEventHandlers(TimeEvent event) {
        EventListener[] handlers = getListeners(TimeEventHandler.class);
        for (EventListener handler : handlers) {
            ((TimeEventHandler) handler).onTime(event);
        }
    }

    public void addTopicEventHandler(TopicEventHandler teh) {
        add(TopicEventHandler.class, teh);
    }

    public void removeTopicEventHandler(TopicEventHandler teh) {
        remove(TopicEventHandler.class, teh);
    }

    void fireTopicEventHandlers(TopicEvent event) {
        EventListener[] handlers = getListeners(TopicEventHandler.class);
        for (EventListener handler : handlers) {
            ((TopicEventHandler) handler).onTopicChanged(event);
        }
    }

    public void addTopicRequestEventHandler(TopicRequestEventHandler treh) {
        add(TopicRequestEventHandler.class, treh);
    }

    public void removeTopicRequestEventHandler(TopicRequestEventHandler treh) {
        remove(TopicRequestEventHandler.class, treh);
    }

    void fireTopicRequestEventHandlers(TopicRequestEvent event) {
        EventListener[] handlers = getListeners(TopicRequestEventHandler.class);
        for (EventListener handler : handlers) {
            ((TopicRequestEventHandler) handler).onTopicRequest(event);
        }
    }

    public void addUserModeChangeEventHandler(UserModeChangeEventHandler umceh) {
        add(UserModeChangeEventHandler.class, umceh);
    }

    public void removeUserModeChangeEventHandler(UserModeChangeEventHandler umceh) {
        remove(UserModeChangeEventHandler.class, umceh);
    }

    void fireUserModeChangeEventHandlers(UserModeChangeEvent event) {
        EventListener[] handlers = getListeners(UserModeChangeEventHandler.class);
        for (EventListener handler : handlers) {
            ((UserModeChangeEventHandler) handler).onUserModeChange(event);
        }
    }

    public void addUserModeRequestEventHandler(UserModeRequestEventHandler umreh) {
        add(UserModeRequestEventHandler.class, umreh);
    }

    public void removeUserModeRequestEventHandler(UserModeRequestEventHandler umreh) {
        remove(UserModeRequestEventHandler.class, umreh);
    }

    void fireUserModeRequestEventHandlers(UserModeRequestEvent event) {
        EventListener[] handlers = getListeners(UserModeRequestEventHandler.class);
        for (EventListener handler : handlers) {
            ((UserModeRequestEventHandler) handler).onUserModeRequest(event);
        }
    }

    public void addVersionEventHandler(VersionEventHandler veh) {
        add(VersionEventHandler.class, veh);
    }

    public void removeVersionEventHandler(VersionEventHandler veh) {
        remove(VersionEventHandler.class, veh);
    }

    void fireVersionEventHandlers(VersionEvent event) {
        EventListener[] handlers = getListeners(VersionEventHandler.class);
        for (EventListener handler : handlers) {
            ((VersionEventHandler) handler).onVersion(event);
        }
    }

    public void addWhoEventHandler(WhoEventHandler weh) {
        add(WhoEventHandler.class, weh);
    }

    public void removeWhoEventHandler(WhoEventHandler weh) {
        remove(WhoEventHandler.class, weh);
    }

    void fireWhoEventHandlers(WhoEvent event) {
        EventListener[] handlers = getListeners(WhoEventHandler.class);
        for (EventListener handler : handlers) {
            ((WhoEventHandler) handler).onWho(event);
        }
    }

    public void addWhoisEventHandler(WhoisEventHandler weh) {
        add(WhoisEventHandler.class, weh);
    }

    public void removeWhoisEventHandler(WhoisEventHandler weh) {
        remove(WhoisEventHandler.class, weh);
    }

    void fireWhoisEventHandlers(WhoisEvent event) {
        EventListener[] handlers = getListeners(WhoisEventHandler.class);
        for (EventListener handler : handlers) {
            ((WhoisEventHandler) handler).onWhois(event);
        }
    }

    public void addWhowasEventHandler(WhowasEventHandler weh) {
        add(WhowasEventHandler.class, weh);
    }

    public void removeWhowasEventHandler(WhowasEventHandler weh) {
        remove(WhowasEventHandler.class, weh);
    }

    void fireWhowasEventHandlers(WhowasEvent event) {
        EventListener[] handlers = getListeners(WhowasEventHandler.class);
        for (EventListener handler : handlers) {
            ((WhowasEventHandler) handler).onWhowas(event);
        }
    }
}
