package org.dbe.composer.wfengine.bpel;

import java.util.Map;
import javax.xml.namespace.QName;
import org.dbe.composer.wfengine.bpel.message.ISdlMessageData;
import org.dbe.composer.wfengine.bpel.message.SdlMessageData;

/**
 * The factory for creating message data implementations which contain the data
 * for variables in a bpel process.
 */
public class SdlMessageDataFactory {

    /** The singleton instance of the Win32 Service factory */
    private static SdlMessageDataFactory mFactory = new SdlMessageDataFactory();

    /**
    * Private constructor to force singleton.
    */
    private SdlMessageDataFactory() {
    }

    /**
    * Returns the singleton instance of the Win32 Service factory.
    */
    public static SdlMessageDataFactory instance() {
        return mFactory;
    }

    /**
    * Creates a message to be used during BPEL process execution.
    * @param aMsgName The qualified name of the message we are creating
    */
    public ISdlMessageData createMessageData(QName aMsgName) {
        return new SdlMessageData(aMsgName);
    }

    /**
    * Create a message with the given QName and message data.
    * @param aMsgName
    * @param aMessageData
    */
    public ISdlMessageData createMessageData(QName aMsgName, Map aMessageData) {
        return new SdlMessageData(aMsgName, aMessageData);
    }
}
