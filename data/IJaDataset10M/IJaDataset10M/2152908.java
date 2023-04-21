package net.sf.istcontract.wsimport.handler;

import javax.activation.DataHandler;
import javax.xml.ws.handler.MessageContext;
import net.sf.istcontract.wsimport.api.message.Attachment;
import net.sf.istcontract.wsimport.api.message.AttachmentSet;
import net.sf.istcontract.wsimport.api.message.Packet;
import net.sf.istcontract.wsimport.util.ReadOnlyPropertyException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author WS Development Team
 */
class MessageContextImpl implements MessageContext {

    private Map<String, Object> fallbackMap = null;

    private Set<String> handlerScopeProps;

    Packet packet;

    void fallback() {
        if (fallbackMap == null) {
            fallbackMap = new HashMap<String, Object>();
            fallbackMap.putAll(packet.createMapView());
            fallbackMap.putAll(packet.invocationProperties);
        }
    }

    /** Creates a new instance of MessageContextImpl */
    public MessageContextImpl(Packet packet) {
        this.packet = packet;
        handlerScopeProps = packet.getHandlerScopePropertyNames(false);
    }

    protected void updatePacket() {
        throw new UnsupportedOperationException("wrong call");
    }

    public void setScope(String name, Scope scope) {
        if (!containsKey(name)) throw new IllegalArgumentException("Property " + name + " does not exist.");
        if (scope == Scope.APPLICATION) {
            handlerScopeProps.remove(name);
        } else {
            handlerScopeProps.add(name);
        }
    }

    public Scope getScope(String name) {
        if (!containsKey(name)) throw new IllegalArgumentException("Property " + name + " does not exist.");
        if (handlerScopeProps.contains(name)) {
            return Scope.HANDLER;
        } else {
            return Scope.APPLICATION;
        }
    }

    public int size() {
        fallback();
        return fallbackMap.size();
    }

    public boolean isEmpty() {
        fallback();
        return fallbackMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        if (fallbackMap == null) {
            if (packet.supports(key)) return true;
            return packet.invocationProperties.containsKey(key);
        } else {
            fallback();
            return fallbackMap.containsKey(key);
        }
    }

    public boolean containsValue(Object value) {
        fallback();
        return fallbackMap.containsValue(value);
    }

    public Object put(String key, Object value) {
        if (fallbackMap == null) {
            if (packet.supports(key)) {
                return packet.put(key, value);
            }
            if (!packet.invocationProperties.containsKey(key)) {
                handlerScopeProps.add(key);
            }
            return packet.invocationProperties.put(key, value);
        } else {
            fallback();
            if (!fallbackMap.containsKey(key)) {
                handlerScopeProps.add(key);
            }
            return fallbackMap.put(key, value);
        }
    }

    public Object get(Object key) {
        if (key == null) return null;
        Object value;
        if (fallbackMap == null) {
            if (packet.supports(key)) {
                value = packet.get(key);
            } else {
                value = packet.invocationProperties.get(key);
            }
        } else {
            fallback();
            value = fallbackMap.get(key);
        }
        if (key.equals(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS) || key.equals(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)) {
            Map<String, DataHandler> atts = (Map<String, DataHandler>) value;
            if (atts == null) atts = new HashMap<String, DataHandler>();
            AttachmentSet attSet = packet.getMessage().getAttachments();
            for (Attachment att : attSet) {
                atts.put(att.getContentId(), att.asDataHandler());
            }
            return atts;
        }
        return value;
    }

    public void putAll(Map<? extends String, ? extends Object> t) {
        fallback();
        for (String key : t.keySet()) {
            if (!fallbackMap.containsKey(key)) {
                handlerScopeProps.add(key);
            }
        }
        fallbackMap.putAll(t);
    }

    public void clear() {
        fallback();
        fallbackMap.clear();
    }

    public Object remove(Object key) {
        fallback();
        handlerScopeProps.remove(key);
        return fallbackMap.remove(key);
    }

    public Set<String> keySet() {
        fallback();
        return fallbackMap.keySet();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        fallback();
        return fallbackMap.entrySet();
    }

    public Collection<Object> values() {
        fallback();
        return fallbackMap.values();
    }

    /**
     * Fill a {@link Packet} with values of this {@link MessageContext}.
     */
    void fill(Packet packet) {
        if (fallbackMap != null) {
            for (Entry<String, Object> entry : fallbackMap.entrySet()) {
                String key = entry.getKey();
                if (packet.supports(key)) {
                    try {
                        packet.put(key, entry.getValue());
                    } catch (ReadOnlyPropertyException e) {
                    }
                } else {
                    packet.invocationProperties.put(key, entry.getValue());
                }
            }
            packet.createMapView().keySet().retainAll(fallbackMap.keySet());
            packet.invocationProperties.keySet().retainAll(fallbackMap.keySet());
        }
    }
}
