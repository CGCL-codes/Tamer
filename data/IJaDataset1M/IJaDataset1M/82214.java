package net.sf.mxlosgi.chatstate;

import net.sf.mxlosgi.xmpp.PacketExtension;

/**
 * 
 * @author noah
 *
 */
public class ChatStateExtension implements PacketExtension {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3342578975019908870L;

    public static final String NAMESPACE = "http://jabber.org/protocol/chatstates";

    private ChatState elementName;

    public ChatStateExtension(ChatState elementName) {
        if (elementName == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        }
        this.elementName = elementName;
    }

    @Override
    public String getElementName() {
        return elementName.name();
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<" + getElementName() + " " + "xmlns=\"" + getNamespace() + "\"/>");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ChatStateExtension extension = (ChatStateExtension) super.clone();
        extension.elementName = this.elementName;
        return extension;
    }

    public enum ChatState {

        active, composing, paused, inactive, gone
    }
}
