package de.psychomatic.messagesystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import de.psychomatic.applicationtools.collection.WeakHashSet;

/**
 * Copyright (C) 2004 Oliver Kykal
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Implementation des MessageSenders
 * @author Kykal
 */
public class Messenger implements MessageSenderIf {

    /**
     * Liste der bekannten Listener
     */
    private final Set<MessageListenerIf> _receivers;

    /**
     * Erstellt einen neuen Messenger
     */
    public Messenger() {
        this(true);
    }

    /**
     * Erstellt einen neuen Messenger
     */
    public Messenger(final boolean weak) {
        if (weak) {
            _receivers = new WeakHashSet<MessageListenerIf>();
        } else {
            _receivers = new HashSet<MessageListenerIf>();
        }
    }

    @Override
    public void addMessageListener(final MessageListenerIf listener) {
        _receivers.add(listener);
    }

    @Override
    public synchronized Iterator<MessageEventIf> fireMessageEvent(final MessageEventIf e) {
        final List<MessageEventIf> result = new ArrayList<MessageEventIf>();
        for (final MessageListenerIf listener : new HashSet<MessageListenerIf>(_receivers)) {
            result.add(listener.receive(e));
        }
        return result.iterator();
    }

    @Override
    public void removeMessageListener(final MessageListenerIf listener) {
        _receivers.remove(listener);
    }
}
