package edu.upmc.opi.caBIG.caTIES.client.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import org.apache.axis.types.URI.MalformedURIException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import edu.upmc.opi.caBIG.caTIES.services.dispatcher.client.DispatcherClient;

public class MMTxSearchServiceClient {

    public static final String SERVICE_URL = "https://localhost:8443/wsrf/services/public/Dispatcher";

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String searchString = "prostatic adenocarcinoma";
        try {
            DispatcherClient client = new DispatcherClient(SERVICE_URL);
            String connectResponse = connectToService(client);
            System.out.println(connectResponse);
            String uuid = extractUUID(connectResponse);
            String queryResponse = executeQuery(client, uuid, searchString);
            printResults(queryResponse);
            disconnectFromService(client, uuid);
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String connectToService(DispatcherClient client) throws RemoteException {
        String connect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<CaTIESCommand " + "action=\"new\" " + "className=\"edu.upmc.opi.caBIG.caTIES.server.dispatcher.mmtx.EvsMMTxCommandProcessor\"/>";
        String connectResponse = client.processCommand(connect);
        return connectResponse;
    }

    private static String extractUUID(String connectResponse) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document responseDocument = saxBuilder.build(new ByteArrayInputStream(connectResponse.getBytes()));
        String uuid = responseDocument.getRootElement().getAttributeValue("uuid");
        return uuid;
    }

    private static String executeQuery(DispatcherClient client, String uuid, String searchString) throws RemoteException {
        String query = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " + "<CaTIESCommand action=\"process\" " + "className=\"edu.upmc.opi.caBIG.caTIES.server.dispatcher.mmtx.EvsMMTxCommandProcessor\" " + "uuid=\"" + uuid + "\">" + "<Request>" + "<PhraseRequest Limit=\"10\">" + "<Phrase>" + searchString + "</Phrase>" + "</PhraseRequest>" + "</Request>" + "</CaTIESCommand>";
        return client.processCommand(query);
    }

    private static void printResults(String queryResponse) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document doc = saxBuilder.build(new ByteArrayInputStream(queryResponse.getBytes()));
            XPath xpath = XPath.newInstance("//Annotation[@AnnotationType='Concept']");
            List results = xpath.selectNodes(doc);
            for (Iterator iterator = results.iterator(); iterator.hasNext(); ) {
                Element conceptElement = (Element) iterator.next();
                String name = getAttributeAsString(conceptElement, "cn");
                String cui = getAttributeAsString(conceptElement, "cui");
                String tui = getAttributeAsString(conceptElement, "tui");
                String sty = getAttributeAsString(conceptElement, "sty");
                int rank = getAttributeAsInteger(conceptElement, "crank");
                System.out.println("Name:" + name + "; CUI:" + cui + "; Rank:" + rank);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void disconnectFromService(DispatcherClient client, String uuid) throws RemoteException {
        String disconnect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<CaTIESCommand " + "action=\"dispose\" " + "className=\"edu.upmc.opi.caBIG.caTIES.server.dispatcher.mmtx.EvsMMTx\" uuid=\"" + uuid + "\" />";
        client.processCommand(disconnect);
    }

    private static String getAttributeAsString(Element element, String attributeName) {
        return (element.getAttribute(attributeName) != null) ? element.getAttribute(attributeName).getValue() : "";
    }

    private static int getAttributeAsInteger(Element element, String attributeName) {
        int result = -1;
        try {
            result = element.getAttribute(attributeName).getIntValue();
        } catch (Exception x) {
            result = -1;
        } finally {
            return result;
        }
    }
}
