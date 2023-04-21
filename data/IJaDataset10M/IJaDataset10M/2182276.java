package com.abiquo.framework.xml.transaction;

import java.io.OutputStream;
import java.util.concurrent.Callable;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.evt.XMLEvent2;
import com.abiquo.framework.comm.CommunicationTime;
import com.abiquo.framework.messages.IGridMessage;
import com.abiquo.framework.xml.AbsXMLMessage;
import com.abiquo.framework.xml.EventConstants;
import com.abiquo.framework.xml.Stax2Factories;

/**
 * This thread is used to write the generated XML message to socket, when the event message is write it returns, but if
 * attached streaming is also available it is read into specified input Stream. Used on ActiveTransaction
 * PassiveTransaction. For PassiveTransaction it close communication at its end.
 */
public class XMLMessageWriter implements Callable<Boolean> {

    /** Log object. */
    private static final Logger logger = Logger.getLogger(XMLMessageWriter.class);

    /** Way of the parser to create writers for output XML. */
    protected static final XMLOutputFactory2 outFact = Stax2Factories.getStreamWriterFactory();

    /** Writer from StreamTransaction. */
    private XMLStreamWriter2 writer;

    private AbsXMLMessage messageXML;

    private boolean isAddDocumentTags;

    /** Records the communication time */
    private CommunicationTime time;

    /**
	 * TODO redoc
	 * 
	 * 
	 * The Class constructor.
	 * 
	 * @param tx
	 *            the transaction to write some of its messages (if active the request, if passive the response)
	 * 
	 * @param writer2
	 *            the Writer object used to render XML to socket output stream
	 * @param sock
	 *            the socket object
	 * @param configurationTx
	 *            to tune communication
	 * @param t
	 *            the time communication created on Active or Passive Communication
	 * @throws XMLStreamException  when the XMLStreamWriter can not be created.
	 */
    public XMLMessageWriter(IGridMessage message, XMLStreamWriter2 w, boolean isDocumentHeadder, CommunicationTime t) throws XMLStreamException {
        time = t;
        isAddDocumentTags = isDocumentHeadder;
        messageXML = message.toXML();
        writer = w;
    }

    /**
	 * Perform the write operation. Return after the event message is write. For DataTransfers starts another thread
	 * WriteXMLFromInputStream to push data from transaction input stream.
	 */
    public Boolean call() throws XMLStreamException {
        boolean isRequireStream = false;
        isRequireStream = messageXML.getEventType() == EventConstants.ET_TRANSFER_RESPONSE;
        time.setTimeStartWrite();
        writeStartEnvelope(writer);
        logger.debug("write message " + messageXML.getClass().getName());
        messageXML.writeUsing(writer);
        writer.flush();
        if (!isRequireStream) {
            finishWrite();
        } else {
            logger.debug("Required WriteXMLFromInputStream");
        }
        return Boolean.TRUE;
    }

    /**
	 * Ends the XML communication transaction write step.
	 * For PassiveCommunication also close the socket.
	 */
    private void finishWrite() throws XMLStreamException {
        writeEndEnvelope(writer);
        time.setTimeEndWrite();
        logger.debug("Closing writer");
        writer.close();
    }

    /**
	 * Adds the GridMessage start header.
	 * 
	 * @param writer
	 *            the XML writer where put the start header
	 * 
	 * @throws XMLStreamException
	 *             some problem occurs during event creation/write
	 */
    protected void writeStartEnvelope(XMLStreamWriter2 writer) throws XMLStreamException {
        XMLEvent2 sE;
        if (isAddDocumentTags) {
            XMLEvent2 documentStart = (XMLEvent2) EventConstants.evntFact.createStartDocument();
            documentStart.writeUsing(writer);
            writer.flush();
        }
        sE = (XMLEvent2) EventConstants.evntFact.createStartElement(EventConstants.QN_GRID_MESSAGE, null, null);
        sE.writeUsing(writer);
        writer.flush();
    }

    /**
	 * Adds the GridMessage XML end element to finish the transaction.
	 * 
	 * @param writer
	 *            the XML writer where put the end header
	 * 
	 * @throws XMLStreamException
	 *             Some problem occured during event creation/write
	 */
    protected void writeEndEnvelope(XMLStreamWriter2 writer) throws XMLStreamException {
        XMLEvent2 eE;
        eE = (XMLEvent2) EventConstants.evntFact.createEndElement(EventConstants.QN_GRID_MESSAGE, null);
        eE.writeUsing(writer);
        writer.flush();
        if (isAddDocumentTags) {
            XMLEvent2 documentEnd = (XMLEvent2) EventConstants.evntFact.createEndDocument();
            documentEnd.writeUsing(writer);
            writer.flush();
        }
    }
}
