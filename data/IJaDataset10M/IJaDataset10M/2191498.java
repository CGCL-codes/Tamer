package org.openfast.simple;

import org.lasalletech.entity.simple.SimpleEObject;
import org.openfast.FastObject;
import org.openfast.Message;
import org.openfast.template.Composite;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Type;

public class SimpleMessage extends SimpleEObject<MessageTemplate, Message, Type, Field, MessageTemplate, Message, Composite<FastObject>, FastObject> implements Message {

    protected SimpleMessage(MessageTemplate template) {
        super(template);
    }

    public MessageTemplate getTemplate() {
        return getEntity();
    }
}
