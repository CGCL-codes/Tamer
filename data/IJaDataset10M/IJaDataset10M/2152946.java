package edu.iu.iv.toolkits.vwtk.datamodel.user.impl;

import java.util.HashMap;
import java.util.Map;
import edu.iu.iv.toolkits.vwtk.datamodel.user.IUserAction;
import edu.iu.iv.toolkits.vwtk.datamodel.user.IUserActionKey;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Arrival;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.ChatMessage;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Click;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Departure;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.TrailSegment;

public class UserActionKeys {

    public static final IUserActionKey<Click> CLICK = new AbstractUserActionKey<Click>("CLICK");

    public static final IUserActionKey<ChatMessage> CHAT = new AbstractUserActionKey<ChatMessage>("CHAT");

    public static final IUserActionKey<TrailSegment> MOVE = new AbstractUserActionKey<TrailSegment>("MOVE");

    public static final IUserActionKey<Arrival> ARRIVE = new AbstractUserActionKey<Arrival>("ARRIVE");

    public static final IUserActionKey<Departure> DEPART = new AbstractUserActionKey<Departure>("DEPART");

    private static final Map<String, IUserActionKey<?>> strKeyMap = new HashMap<String, IUserActionKey<?>>();

    static {
        strKeyMap.put(CLICK.toString(), CLICK);
        strKeyMap.put(CHAT.toString(), CHAT);
        strKeyMap.put(CLICK.toString(), CLICK);
        strKeyMap.put(MOVE.toString(), MOVE);
        strKeyMap.put(ARRIVE.toString(), ARRIVE);
        strKeyMap.put(DEPART.toString(), DEPART);
    }

    public static IUserActionKey getUserActionKey(String name) {
        return strKeyMap.get(name);
    }

    @SuppressWarnings("all")
    public static IUserActionKey<? extends IUserAction>[] values() {
        return new IUserActionKey[] { MOVE, CHAT, CLICK, ARRIVE, DEPART };
    }
}

class AbstractUserActionKey<E> implements IUserActionKey<E> {

    private String name;

    public AbstractUserActionKey(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(IUserActionKey k) {
        return (this.name.toString().compareTo(k.toString()) == 0);
    }
}
