package org.scribble.monitor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class represents a message being 'sent'.
 */
public class SendNode extends MonitorNode implements java.io.Externalizable {

    /**
	 * This method processes the 'sent' message.
	 * 
	 * @param mc The monitor context
	 * @param context The conversation context
	 * @param mesg The message
	 * @return Whether the 'sent' message has been handled
	 */
    @Override
    public boolean sent(MonitorContext mc, ConversationContext context, Message mesg) {
        boolean ret = canProcessSentMessage(mesg);
        if (ret && m_types != null) {
            for (int i = 0; ret && i < mesg.getConversationIdentity().length; i++) {
                for (int j = 0; ret && j < m_types.length; j++) {
                    if (mesg.getConversationIdentity()[i].getType().equals(m_types[j])) {
                        ret = context.assessIdentity(mesg.getConversationIdentity()[i]);
                    }
                }
            }
        }
        if (ret == false) {
            context.register(this);
        } else if (getParent() != null) {
            getParent().processedNodeWithSentMessage(mc, context, this, null);
        }
        return (ret);
    }

    /**
	 * This method processes the 'received' message.
	 * 
	 * @param mc The monitor context
	 * @param context The conversation context
	 * @param mesg The message
	 * @return Whether the 'received' message has been handled
	 */
    @Override
    public boolean received(MonitorContext mc, ConversationContext context, Message mesg) {
        if (mesg == null) {
            context.register(this);
        }
        return (false);
    }

    /**
	 * This method defines the identity types that are of
	 * interest to this 'send' node.
	 * 
	 * @param types The conversation identity types
	 */
    public void setIdentityTypes(ConversationIdentityType[] types) {
        m_types = types;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if (m_types == null) {
            out.writeInt(0);
        } else {
            out.writeInt(m_types.length);
            for (int i = 0; i < m_types.length; i++) {
                out.writeObject(m_types[i]);
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int num = in.readInt();
        if (num > 0) {
            m_types = new ConversationIdentityType[num];
            for (int i = 0; i < num; i++) {
                m_types[i] = (ConversationIdentityType) in.readObject();
            }
        }
    }

    private ConversationIdentityType[] m_types = null;
}
