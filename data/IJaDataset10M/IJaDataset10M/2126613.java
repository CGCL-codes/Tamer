package de.ifgi.simcat.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.SAXOutputter;
import org.jdom.output.XMLOutputter;
import de.ifgi.simcat.exceptions.*;
import de.ifgi.simcat.reasoner.KnowledgeBase;
import de.ifgi.simcat.reasoner.Tuple;
import de.ifgi.simcat.server.results.Result;

/**
 * Controller object that manages the SimCat server operations and delegates requests to their corresponding operation handlers.
 * @author Boris Baeumer
 * @author Ilija Panov
 * @version 1.0
 */
public class ServerController {

    private static Logger logger = Logger.getLogger(ServerController.class);

    private RequestParser reqParser;

    private ResponseBuilder respBuilder;

    private SimCatReasoner simcatReasoner;

    /**
	 * Constructs the server controller by using the new knowledge base's reasoner object.
	 * @param reasoner - The reasoner object based upon a new knowledge base.
	 */
    public ServerController(SimCatReasoner reasoner) {
        simcatReasoner = reasoner;
        reqParser = new RequestParser();
        respBuilder = new ResponseBuilder();
    }

    /**
	 * Parses the request's elements and starts operations corresponding to different request tags.
	 * @param request - The request input stream.
	 * @param response - The response output stream.
	 * @throws SimCatException - A specific parsing exception with its DIG error code.
	 */
    public void process(InputStream request, OutputStream response) throws SimCatException {
        TellHandler tellHandler = new TellHandler();
        AskHandler askHandler = new AskHandler();
        org.w3c.dom.Document outputDocument;
        OutputStreamWriter outWriter = new OutputStreamWriter(response);
        try {
            Document inputDocument = reqParser.createXMLDocument(request);
            Element cmdNode = inputDocument.getRootElement();
            String cmdName = cmdNode.getName();
            if (ServerConstants.GET_IDENTIFIER.equals(cmdName)) {
                try {
                    outputDocument = respBuilder.buildIdentifierResponse();
                } catch (RuntimeException e) {
                    throw new SimCatException(0);
                }
            } else if (ServerConstants.NEWKB.equals(cmdName)) {
                try {
                    outputDocument = respBuilder.buildNewKBResponse(simcatReasoner.createKB());
                } catch (RuntimeException e) {
                    throw new SimCatException(4);
                }
            } else if (ServerConstants.RELEASEKB.equals(cmdName)) {
                try {
                    String uri = cmdNode.getAttributeValue("uri");
                    simcatReasoner.releaseKB(uri);
                    outputDocument = respBuilder.buildReleaseKBResponse();
                } catch (RuntimeException e) {
                    throw new SimCatException(7);
                }
            } else if (ServerConstants.TELLS.equals(cmdName)) {
                try {
                    String uuidOfKBToFill = cmdNode.getAttributeValue("uri");
                    KnowledgeBase kb = simcatReasoner.getUuidKBByUri(uuidOfKBToFill);
                    if (kb == null) throw new SimCatException(6);
                    tellHandler.fillKnowledgeBase(cmdNode, kb);
                    outputDocument = respBuilder.buildTellsResponse();
                } catch (SimCatException e) {
                    throw e;
                } catch (RuntimeException e) {
                    throw new SimCatException(9);
                }
            } else if (ServerConstants.ASKS.equals(cmdName)) {
                try {
                    String uuidOfKBToFill = cmdNode.getAttributeValue("uri");
                    KnowledgeBase kb = simcatReasoner.getUuidKBByUri(uuidOfKBToFill);
                    if (kb == null) throw new SimCatException(6);
                    Result result = askHandler.performReasoning(cmdNode, kb);
                    outputDocument = respBuilder.buildAsksResponse(result);
                } catch (SimCatException e) {
                    throw e;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw new SimCatException(12);
                }
            } else {
                throw new SimCatException(1);
            }
            OutputFormat format = new OutputFormat(outputDocument);
            format.setLineWidth(0);
            format.setPreserveSpace(false);
            format.setIndenting(true);
            format.setOmitXMLDeclaration(false);
            XMLSerializer serial = new XMLSerializer(outWriter, format);
            serial.asDOMSerializer();
            serial.serialize(outputDocument.getDocumentElement());
        } catch (JDOMException errorCode) {
            throw new SimCatException(0);
        } catch (IOException errorCode) {
            throw new SimCatException(0);
        } finally {
            tellHandler = null;
            askHandler = null;
        }
    }
}
