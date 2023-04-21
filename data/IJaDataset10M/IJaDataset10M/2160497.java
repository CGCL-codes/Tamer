package ca.uhn.hunit.msg;

import ca.uhn.hunit.iface.TestMessage;
import ca.uhn.hunit.util.AbstractModelClass;
import ca.uhn.hunit.xsd.MessageDefinition;
import java.beans.PropertyVetoException;

public abstract class AbstractMessage<T> extends AbstractModelClass {

    public static final String SOURCE_MESSAGE_PROPERTY = "SOURCE_MESSAGE_PROPERTY";

    protected String myId;

    public AbstractMessage(MessageDefinition theConfig) {
        myId = theConfig.getId();
    }

    public AbstractMessage(String theId) {
        myId = theId;
    }

    /**
     * Constructor
     */
    public AbstractMessage() {
        super();
    }

    /**
     * Subclasses should make use of this method to export AbstractInterface properties into
     * the return value for {@link #exportConfigToXml()}
     */
    protected MessageDefinition exportConfig(MessageDefinition theConfig) {
        return theConfig;
    }

    /**
     * Declare a concrete type for subclass implementations of this method
     */
    @Override
    public abstract MessageDefinition exportConfigToXml();

    @Deprecated
    public String getId() {
        return myId;
    }

    public abstract Class<? extends T> getMessageClass();

    public abstract String getSourceMessage();

    public abstract TestMessage<T> getTestMessage();

    /**
     * On successful completion, should fire {@link #SOURCE_MESSAGE_PROPERTY}
     */
    public abstract void setSourceMessage(String theSourceMessage) throws PropertyVetoException;
}
