package com.google.gwt.uibinder.elementparsers;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;
import com.google.gwt.uibinder.rebind.messages.MessageWriter;
import com.google.gwt.uibinder.rebind.messages.MessagesWriter;
import com.google.gwt.uibinder.rebind.messages.PlaceholderInterpreter;

/**
 * The interpreter of choice for calls to {@link XMLElement#consumeInnerText}.
 * It handles &lt;m:msg/&gt; elements, replacing them with references to Messages
 * interface calls.
 * <P>
 * Calls to {@link XMLElement#consumeInnerHtml} should instead use
 * {@link HtmlInterpreter}
 */
public class TextInterpreter implements XMLElement.Interpreter<String> {

    private final UiBinderWriter writer;

    public TextInterpreter(UiBinderWriter writer) {
        this.writer = writer;
    }

    public String interpretElement(XMLElement elem) throws UnableToCompleteException {
        MessagesWriter messages = writer.getMessages();
        if (messages.isMessage(elem)) {
            String messageInvocation = consumeAsTextMessage(elem, messages);
            return writer.tokenForStringExpression(elem, messageInvocation);
        }
        return new UiTextInterpreter(writer).interpretElement(elem);
    }

    private String consumeAsTextMessage(XMLElement elem, MessagesWriter messages) throws UnableToCompleteException {
        if (!elem.hasChildNodes()) {
            writer.die(elem, "Empty message");
        }
        MessageWriter message = messages.newMessage(elem);
        PlaceholderInterpreter interpreter = new TextPlaceholderInterpreter(writer, message);
        message.setDefaultMessage(elem.consumeInnerText(interpreter));
        return messages.declareMessage(message);
    }
}
