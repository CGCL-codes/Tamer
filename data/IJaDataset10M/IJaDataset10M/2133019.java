package org.powerfolder.workflow.model.trigger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.powerfolder.PFRuntimeException;
import org.powerfolder.utils.xml.XMLHelper;
import org.powerfolder.workflow.model.trigger.Trigger;
import org.powerfolder.workflow.model.attributes.AttributeSet;

class TriggerBean implements Trigger, Serializable {

    private String textData;

    protected TriggerBean(String tData) {
        try {
            XMLHelper.loadDocument(tData);
            this.textData = tData;
        } catch (ParserConfigurationException pce) {
            throw new PFRuntimeException(pce);
        } catch (SAXException se) {
            throw new PFRuntimeException(se);
        } catch (IOException ie) {
            throw new PFRuntimeException(ie);
        }
    }

    public String getDataAsString() {
        return this.textData;
    }
}
