package ca.uhn.hunit.event.expect;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.validation.impl.ValidationContextImpl;
import ca.uhn.hunit.ex.ConfigurationException;
import ca.uhn.hunit.ex.IncorrectMessageReceivedException;
import ca.uhn.hunit.ex.TestFailureException;
import ca.uhn.hunit.ex.UnexpectedTestFailureException;
import ca.uhn.hunit.iface.TestMessage;
import ca.uhn.hunit.l10n.Strings;
import ca.uhn.hunit.msg.AbstractMessage;
import ca.uhn.hunit.msg.Hl7V2MessageImpl;
import ca.uhn.hunit.run.IExecutionContext;
import ca.uhn.hunit.test.TestImpl;
import ca.uhn.hunit.xsd.Event;
import ca.uhn.hunit.xsd.HL7V2ExpectAbstract;
import java.beans.PropertyVetoException;
import java.util.LinkedHashMap;

public abstract class AbstractHl7V2ExpectMessage extends AbstractExpectMessage<Hl7V2MessageImpl> {

    @Deprecated
    public static final String REPLY_MESSAGE_ID_PROPERTY = "AHEM_REPLY_MESSAGE_ID_PROPERTY";

    private Hl7V2MessageImpl myReplyMessage;

    private GenericParser myParser;

    public AbstractHl7V2ExpectMessage(TestImpl theTest, HL7V2ExpectAbstract theConfig) throws ConfigurationException {
        super(theTest, theConfig);
        if (theConfig.getReplyMsg() != null) {
            myReplyMessage = new Hl7V2MessageImpl(theConfig.getReplyMsg());
        } else {
            try {
                setReplyMessageId(theConfig.getReplyMessage());
            } catch (PropertyVetoException ex) {
                throw new ConfigurationException("Setting reply message ID for expect event failed with message: " + ex.getMessage());
            }
        }
        myParser = new GenericParser();
        myParser.setPipeParserAsPrimary();
        myParser.setValidationContext(new ValidationContextImpl());
    }

    protected Event exportConfig(HL7V2ExpectAbstract theConfig) {
        super.exportConfig(theConfig);
        if (myReplyMessage != null) {
            theConfig.setReplyMsg(myReplyMessage.exportConfigToXml());
        }
        return theConfig;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Hl7V2MessageImpl getReplyMessage() {
        if (myReplyMessage != null) {
            return myReplyMessage;
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void receiveMessage(IExecutionContext theCtx, TestMessage<?> theMessage) throws TestFailureException {
        TestMessage<Message> testMessage = (TestMessage<Message>) theMessage;
        if (testMessage.getParsedMessage() == null && testMessage.getRawMessage() == null) {
            throw new UnexpectedTestFailureException("Message has neither parsed nor raw message");
        }
        if (testMessage.getParsedMessage() == null) {
            try {
                testMessage.setParsedMessage(myParser.parse(testMessage.getRawMessage()));
            } catch (HL7Exception e) {
                throw new IncorrectMessageReceivedException(getTest(), theMessage, "Unable to parse response message. Error: " + e.getMessage());
            }
        }
        if (testMessage.getRawMessage() == null) {
            try {
                testMessage.setRawMessage(myParser.encode(testMessage.getParsedMessage()));
            } catch (HL7Exception e) {
                throw new IncorrectMessageReceivedException(getTest(), theMessage, "Unable to parse response message. Error: " + e.getMessage());
            }
        }
        validateMessage(testMessage);
    }

    public abstract void validateMessage(TestMessage<Message> theMessage) throws TestFailureException;

    /**
     * Sets the ID for the message, if any, that will be used as a reply
     */
    @Deprecated
    public void setReplyMessageId(String theMessageId) throws PropertyVetoException {
        String oldValue = (myReplyMessage != null) ? myReplyMessage.getId() : null;
        fireVetoableChange(REPLY_MESSAGE_ID_PROPERTY, oldValue, theMessageId);
        try {
            if (theMessageId == null) {
                myReplyMessage = null;
            } else {
                myReplyMessage = (Hl7V2MessageImpl) getBattery().getMessage(theMessageId);
            }
        } catch (ConfigurationException ex) {
            throw new PropertyVetoException("Unknown message ID - " + theMessageId, null);
        }
        firePropertyChange(REPLY_MESSAGE_ID_PROPERTY, oldValue, theMessageId);
    }

    /**
     * Sets the reply message to be used by this event
     */
    public void setReplyMessage(Hl7V2MessageImpl theReplyMessage) {
        Hl7V2MessageImpl oldValue = myReplyMessage;
        myReplyMessage = theReplyMessage;
        firePropertyChange(INTERFACE_MESSAGES_PROPERTY, oldValue, myReplyMessage);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public LinkedHashMap<String, AbstractMessage<?>> getAllMessages() {
        LinkedHashMap<String, AbstractMessage<?>> retVal = new LinkedHashMap<String, AbstractMessage<?>>();
        if (getReplyMessage() != null) {
            retVal.put(Strings.getMessage("eventeditor.reply_message"), getReplyMessage());
        }
        return retVal;
    }
}
