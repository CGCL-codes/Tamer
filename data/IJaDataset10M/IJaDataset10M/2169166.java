package org.relayirc.chatengine;

import org.relayirc.util.*;
import org.relayirc.core.*;
import java.io.*;
import java.util.*;
import java.beans.*;

/**
 * An IRC channel class that includes methods for joining, parting,
 * kicking, banning, adding/removing channel listeners and property
 * change support.
 * @author David M. Johnson
 * @version $Revision: 1.12 $
 *
 * <p>The contents of this file are subject to the Mozilla Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/</p>
 * Original Code:     Relay IRC Chat Engine<br>
 * Initial Developer: David M. Johnson <br>
 * Contributor(s):    No contributors to this file <br>
 * Copyright (C) 1997-2000 by David M. Johnson <br>
 * All Rights Reserved.
 */
public class Channel implements IChatObject, Serializable {

    static final long serialVersionUID = -7306475367688154363L;

    private String _name = "";

    private String _desc = "";

    private String _topic = new String();

    private int _userCount = 0;

    private transient PropertyChangeSupport _propChangeSupport = null;

    private transient Server _server = null;

    private transient Vector _listeners = new Vector();

    private transient boolean _isConnected = false;

    private transient _ChannelMux _mux = new _ChannelMux();

    /** Construct channel with name alone. */
    public Channel(String name) {
        _name = name;
        _propChangeSupport = new PropertyChangeSupport(this);
    }

    /** Construct channel with server. */
    public Channel(String name, Server server) {
        this(name);
        _server = server;
    }

    /** Channel with name, topic, user count and a server. */
    public Channel(String name, String topic, int ucount, Server server) {
        this(name);
        _topic = topic;
        _userCount = ucount;
        _server = server;
    }

    public boolean equals(Object object) {
        if (object != null && object instanceof Channel) {
            if (((Channel) object).getName().equals(getName())) return true;
        }
        return false;
    }

    interface _ChannelEventNotifier {

        public void notify(ChannelListener listener);
    }

    private synchronized void notifyListeners(_ChannelEventNotifier notifier) {
        for (int i = 0; i < _listeners.size(); i++) {
            ChannelListener listener = (ChannelListener) _listeners.elementAt(i);
            notifier.notify(listener);
        }
    }

    /** Channel listener support. */
    public synchronized void addChannelListener(ChannelListener listener) {
        _listeners.addElement(listener);
    }

    /** Channel listener support. */
    public synchronized void removeChannelListener(ChannelListener listener) {
        _listeners.removeElement(listener);
    }

    /** Property change support. */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _propChangeSupport.addPropertyChangeListener(listener);
    }

    /** Property change support. */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _propChangeSupport.removePropertyChangeListener(listener);
    }

    /** True if channel is connected/joined. */
    public boolean isConnected() {
        return _isConnected;
    }

    /** Set connection status. */
    public void setConnected(boolean value) {
        _isConnected = value;
    }

    /** Get the channel's server, which may be null. */
    public Server getServer() {
        return _server;
    }

    /** Get channel's IRCConnectionListener, for internal use only. */
    IRCConnectionListener getChannelMux() {
        if (_mux == null) {
            _mux = new _ChannelMux();
        }
        return _mux;
    }

    /** Connect/join this channel, does nothing if channel has no server. */
    public void connect() {
        if (_server != null) {
            _server.sendJoin(this);
        }
    }

    /** Request channel disconnection by sending PART command to server. */
    public void disconnect() {
        if (_server != null) {
            _server.sendPart(this);
        }
    }

    /** Get name of channel (e.g. "#java" or "#raleigh" */
    public String getName() {
        return _name;
    }

    /**
    * Set the server to be used by this channel.
    */
    public void setServer(Server server) {
        _server = server;
    }

    /**
    * Set channel name, with property change support. Property
    * change event will include new and old values.
    */
    public void setName(String name) {
        String old = _name;
        _name = name;
        _propChangeSupport.firePropertyChange("Name", old, _name);
    }

    /** Get channe's topic. */
    public String getTopic() {
        return _topic;
    }

    public String getDescription() {
        return _desc;
    }

    public void setDescription(String desc) {
        _desc = desc;
    }

    /**
    * Set channel topic, with property change support. Property
    * change event will include new and old values.
    */
    public void setTopic(String topic) {
        String old = _topic;
        _topic = topic;
        _propChangeSupport.firePropertyChange("Topic", old, _topic);
    }

    /** Number of users currently on channel. */
    public int getUserCount() {
        return _userCount;
    }

    /**
    * Set channel user count, with property change support. Property change
    * event will include new and old values as java.lang.Integer objects.
    */
    public void setUserCount(int count) {
        int old = _userCount;
        _userCount = count;
        _propChangeSupport.firePropertyChange("UserCount", new Integer(old), new Integer(_userCount));
    }

    /** String representation is channel name. */
    public String toString() {
        return _name;
    }

    /** Request that this channel be activated, given-focus or
	 * brought-to-front. */
    public void activate() {
        final ChannelEvent event = new ChannelEvent(this);
        notifyListeners(new _ChannelEventNotifier() {

            public void notify(ChannelListener listener) {
                listener.onActivation(event);
            }
        });
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            in.defaultReadObject();
        } catch (NotActiveException e) {
            e.printStackTrace();
        }
        _propChangeSupport = new PropertyChangeSupport(this);
        _listeners = new Vector();
    }

    /** Send an action to this channel. */
    public void sendAction(String msg) {
        sendPrivMsg("\001ACTION " + msg + "\001");
    }

    /** Ban a user from this channel. */
    public void sendBan(String nick) {
        _server.sendCommand("MODE " + _name + " +b " + nick);
    }

    /** Take operator rights from a user. */
    public void sendDeop(String nick) {
        _server.sendCommand("MODE " + _name + " -o " + nick);
    }

    /** Join this channel, requires a server. Before you call
	 * this method, make sure you have set the server for this
	 * channel. */
    public synchronized void sendJoin() {
        if (!_isConnected) {
            _server.sendJoin(this);
        }
    }

    /** Kick a user from the channel. */
    public void sendKick(String nick) {
        _server.sendCommand("KICK " + _name + " " + nick);
    }

    /** Give operator rights to a user. */
    public void sendOp(String nick) {
        _server.sendCommand("MODE " + _name + " +o " + nick);
    }

    /** Part (leave) this channel. */
    public void sendPart() {
        _server.sendPart(_name);
    }

    /**
    * Send PRIVMSG to this channel.
    */
    public void sendPrivMsg(String str) {
        _server.sendCommand("PRIVMSG " + _name + " " + ":" + str);
    }

    private class _ChannelMux extends IRCConnectionAdapter {

        public void onAction(String user, String channel, String txt) {
            final ChannelEvent event = new ChannelEvent(Channel.this, txt);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onAction(event);
                }
            });
        }

        public void onBan(String banned, String chan, String banner) {
            final ChannelEvent event = new ChannelEvent(Channel.this, banner, "", banned, "", "");
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onBan(event);
                }
            });
            if (banned.equals(_server.getNick())) {
                _server.fireStatusEvent("\nYou were banned from " + _name + "\n");
                disconnect();
            }
        }

        /** Called when channel is parted. */
        public void onDisconnect() {
            Debug.println("Channel.onDisconnect(): listeners = " + _listeners.size());
            final ChannelEvent event = new ChannelEvent(Channel.this);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onDisconnect(event);
                }
            });
        }

        public void onJoin(String user, String nick, String chan, boolean create) {
            if (nick.equals(_server.getNick())) {
                _server.fireStatusEvent(nick + " joined channel " + _name + "\n");
                final ChannelEvent event2 = new ChannelEvent(Channel.this);
                notifyListeners(new _ChannelEventNotifier() {

                    public void notify(ChannelListener listener) {
                        listener.onConnect(event2);
                    }
                });
            } else {
                final ChannelEvent event = new ChannelEvent(Channel.this, nick, user, "");
                notifyListeners(new _ChannelEventNotifier() {

                    public void notify(ChannelListener listener) {
                        listener.onJoin(event);
                    }
                });
            }
        }

        public void onJoins(String users, String chans) {
            final ChannelEvent event = new ChannelEvent(Channel.this, users);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onJoins(event);
                }
            });
        }

        public void onKick(String kicked, String chan, String kicker, String txt) {
            final ChannelEvent event = new ChannelEvent(Channel.this, kicker, "", kicked, "", txt);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onKick(event);
                }
            });
            if (kicked.equals(_server.getNick())) {
                String reason = (String) event.getValue();
                _server.fireStatusEvent("\nYou were kicked from " + _name + "(" + reason + ")\n");
                final ChannelEvent event2 = new ChannelEvent(Channel.this);
                notifyListeners(new _ChannelEventNotifier() {

                    public void notify(ChannelListener listener) {
                        listener.onDisconnect(event2);
                    }
                });
            }
        }

        public void onTopic(String channel, String txt) {
            setTopic(txt);
        }

        public void onMessage(String txt) {
        }

        public void onPrivateMessage(String orgnick, String chan, String txt) {
            final ChannelEvent event = new ChannelEvent(Channel.this, orgnick, "", txt);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onMessage(event);
                }
            });
        }

        public void onNick(String user, String oldnick, String newnick) {
            final ChannelEvent event = new ChannelEvent(Channel.this, oldnick, user, newnick);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onNick(event);
                }
            });
        }

        public void onPart(String user, String nick, String chan) {
            final ChannelEvent event = new ChannelEvent(Channel.this, nick, user, "");
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onPart(event);
                }
            });
            if (nick.equals(_server.getNick())) {
                _server.fireStatusEvent(nick + " parted channel " + _name + ")\n");
                final ChannelEvent event2 = new ChannelEvent(Channel.this);
                notifyListeners(new _ChannelEventNotifier() {

                    public void notify(ChannelListener listener) {
                        listener.onDisconnect(event2);
                    }
                });
            }
        }

        public void onOp(String oper, String chan, String oped) {
            final ChannelEvent event = new ChannelEvent(Channel.this, oper, "", oped, "", "");
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onOp(event);
                }
            });
        }

        public void onQuit(String user, String nick, String txt) {
            final ChannelEvent event = new ChannelEvent(Channel.this, nick, "", txt);
            notifyListeners(new _ChannelEventNotifier() {

                public void notify(ChannelListener listener) {
                    listener.onQuit(event);
                }
            });
        }
    }
}
