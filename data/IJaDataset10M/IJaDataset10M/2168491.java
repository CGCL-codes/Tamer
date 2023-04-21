package AbilitiesComponents;

import Negotiation.DBconn.DBmanager;
import Negotiation.DroolsTest;
import Negotiation.txt.MakeRule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOnly;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.servicemix.MessageExchangeListener;
import org.apache.servicemix.components.util.ComponentSupport;
import org.apache.servicemix.jbi.jaxp.SourceTransformer;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.edu.metu.srdc.utils.xml.XMLParser;

public class Negotiation extends ComponentSupport implements MessageExchangeListener {

    String Dir = "";

    String fileID, MsgName, MsgID, MsgCorrelationID, ProcessID, State, User, Password, AccessMethod, AsynchResponse, Receivers, Reconciliation, Negotiation, UBLtype;

    public Negotiation() {
        setService(new QName("urn:logicblaze:soa:Negotiation", "NegotiationService"));
        setEndpoint("Negotiation");
    }

    public String getRepository_path() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("abilities_bus");
        String p_repository_path = resourceBundle.getString("repository_path");
        return p_repository_path;
    }

    public boolean fileExist(String path) {
        boolean exists = (new File(path)).exists();
        return exists;
    }

    public void deleteFile(String pathFile) {
        try {
            File f = new File(pathFile);
            System.out.println("pathFile alessandra=" + pathFile);
            boolean success = f.delete();
        } catch (Exception e) {
        }
    }

    public Document doNegotiation(String file2Neg, String user, String UBLtype, String fileID) throws Exception {
        String xmlInstance = "";
        String nomeDB = "negotiation_db";
        String userDB = "root";
        String passwdDB = "";
        DroolsTest dt = new DroolsTest();
        String ruleListName = this.getRepository_path() + File.separator + "NegotiationRepository" + File.separator + user + "_" + UBLtype + ".drl";
        Document tmp = null;
        DBmanager DBm = new DBmanager();
        DBm.connect(nomeDB, userDB, passwdDB);
        ArrayList auxList = DBm.getRuleByUserNameAndDocType(user, UBLtype);
        DBm.disConnect();
        ArrayList rulesList = (ArrayList) auxList.get(0);
        ArrayList rulesNameList = (ArrayList) auxList.get(1);
        MakeRule mr = new MakeRule();
        int ruleListLenght = rulesList.size();
        int i = 0;
        String ruleTemp;
        String ruleNameTemp;
        String mrule = "package Negotiation.com.sample;\n" + "import org.w3c.dom.Document;\n";
        boolean existingrule = false;
        while (i < ruleListLenght) {
            existingrule = true;
            ruleTemp = (String) rulesList.get(i);
            ruleNameTemp = (String) rulesNameList.get(i);
            mrule = mrule + mr.createRule(ruleTemp, ruleNameTemp);
            i++;
        }
        if (!existingrule) {
            mrule = "";
        } else {
            System.out.println("ALESSANDRA--------------> writeRule");
            mr.writeRule(mrule, ruleListName);
        }
        if (fileExist(ruleListName)) {
            tmp = dt.goDrools(ruleListName, file2Neg);
            OutputFormat format = new OutputFormat(tmp);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File(Dir + File.separator + "UBLdoc_Negotiated_" + fileID + ".xml")), format);
            serializer.serialize(tmp);
            this.deleteFile(ruleListName);
        } else {
            DOMParser parser = new DOMParser();
            parser.parse(file2Neg);
            Document tmpDoc = parser.getDocument();
            OutputFormat format = new OutputFormat(tmpDoc);
            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File(Dir + File.separator + "UBLdoc_Negotiated_" + fileID + ".xml")), format);
            serializer.serialize(tmpDoc);
        }
        return tmp;
    }

    public String createOutMessage(Document doc) {
        MsgName = doc.getElementsByTagName("MsgName").item(0).getFirstChild().getNodeValue();
        MsgID = doc.getElementsByTagName("MsgID").item(0).getFirstChild().getNodeValue();
        MsgCorrelationID = doc.getElementsByTagName("MsgCorrelationID").item(0).getFirstChild().getNodeValue();
        ProcessID = doc.getElementsByTagName("ProcessID").item(0).getFirstChild().getNodeValue();
        State = doc.getElementsByTagName("State").item(0).getFirstChild().getNodeValue();
        User = doc.getElementsByTagName("User").item(0).getFirstChild().getNodeValue();
        Password = doc.getElementsByTagName("Password").item(0).getFirstChild().getNodeValue();
        AccessMethod = doc.getElementsByTagName("AccessMethod").item(0).getFirstChild().getNodeValue();
        AsynchResponse = doc.getElementsByTagName("AsynchResponse").item(0).getFirstChild().getNodeValue();
        Receivers = doc.getElementsByTagName("Receivers").item(0).getFirstChild().getNodeValue();
        Reconciliation = doc.getElementsByTagName("Reconciliation").item(0).getFirstChild().getNodeValue();
        Negotiation = doc.getElementsByTagName("Negotiation").item(0).getFirstChild().getNodeValue();
        UBLtype = doc.getElementsByTagName("ESBBody").item(0).getFirstChild().getNextSibling().getNodeName();
        if (UBLtype.contains(":")) {
            UBLtype = UBLtype.split(":")[1];
        }
        String out = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ESBMessage><ESBHeader><MsgInfo><MsgName>" + MsgName + "</MsgName><MsgID>" + MsgID + "</MsgID><MsgCorrelationID>" + MsgCorrelationID + "</MsgCorrelationID><ProcessID>" + ProcessID + "</ProcessID><State>" + State + "</State></MsgInfo><SenderInfo><User>" + User + "</User><Password>" + Password + "</Password><AccessMethod>" + AccessMethod + "</AccessMethod><AsynchResponse>" + AsynchResponse + "</AsynchResponse></SenderInfo><ReceiverInfo><Receivers>" + Receivers + "</Receivers></ReceiverInfo><FlagInfo><Reconciliation>" + Reconciliation + "</Reconciliation><Negotiation>" + Negotiation + "</Negotiation><component_producer>negotiation</component_producer></FlagInfo><fileID>" + fileID + "</fileID></ESBHeader><ESBBody></ESBBody></ESBMessage>";
        return out;
    }

    public Node FindNode(Node nodo, String tag) throws Exception {
        Node nodeResp;
        if (nodo.getNodeType() == 1) {
            String name;
            name = nodo.getNodeName();
            if (name.compareTo(tag) == 0) {
                nodeResp = nodo;
                return nodeResp;
            }
        }
        NodeList nl = nodo.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            nodeResp = FindNode(nl.item(i), tag);
            if (nodeResp != null) {
                return nodeResp;
            }
        }
        return null;
    }

    public Document XmlUnifier(Document doc1, String tag1, Document doc2, String tag2) throws Exception {
        try {
            Node node1 = FindNode(doc1.getFirstChild(), tag1);
            Node node2 = FindNode(doc2.getFirstChild(), tag2);
            Node tmp = doc1.importNode(node2, true);
            node1.appendChild(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc1;
    }

    public void onMessageExchange(MessageExchange messageExchange) throws MessagingException {
        InOut inOut = (InOut) messageExchange;
        if (inOut.getStatus() == ExchangeStatus.DONE) {
            return;
        } else if (inOut.getStatus() == ExchangeStatus.ERROR) {
            return;
        } else if (inOut.getStatus() == ExchangeStatus.ACTIVE) {
            Document doc = null;
            String out = "", tmp = "", tmp2 = "";
            try {
                System.out.println("\n---------- NEGOTIATION COMPONENT ----------");
                doc = (Document) new SourceTransformer().toDOMNode(inOut.getInMessage());
                fileID = doc.getElementsByTagName("fileID").item(0).getFirstChild().getNodeValue();
                Dir = this.getRepository_path() + File.separator + "DocumentRepository" + File.separator + fileID + "_directory";
                String user = doc.getElementsByTagName("User").item(0).getFirstChild().getNodeValue();
                String receiver = doc.getElementsByTagName("Receivers").item(0).getFirstChild().getNodeValue();
                String UBLtype = doc.getElementsByTagName("ESBBody").item(0).getFirstChild().getNextSibling().getNodeName();
                if (UBLtype.contains(":")) {
                    UBLtype = UBLtype.split(":")[1];
                }
                String file2Neg = Dir + File.separator + "UBLdocReconciliated_" + fileID + ".xml";
                this.doNegotiation(file2Neg, user, UBLtype, fileID);
                file2Neg = Dir + File.separator + "UBLdoc_Negotiated_" + fileID + ".xml";
                this.doNegotiation(file2Neg, receiver, UBLtype, fileID);
                DOMParser parser = new DOMParser();
                parser.parse(Dir + File.separator + "UBLdoc_Negotiated_" + fileID + ".xml");
                Document tmpDoc = parser.getDocument();
                String xmlInstance = XMLParser.convertToString(tmpDoc);
                out = this.createOutMessage(doc);
                NormalizedMessage domForHead = inOut.createMessage();
                domForHead.setContent(new StringSource(out));
                Document docHead = (Document) new SourceTransformer().toDOMNode(domForHead);
                NormalizedMessage domForOut = inOut.createMessage();
                domForOut.setContent(new StringSource(xmlInstance));
                Document docOut = (Document) new SourceTransformer().toDOMNode(domForOut);
                Document docNeg = XmlUnifier(docHead, "ESBBody", docOut, docOut.getFirstChild().getNodeName());
                out = XMLParser.convertToString(docNeg);
                NormalizedMessage answer = inOut.createMessage();
                answer.setContent(new StringSource(out));
                answer(inOut, answer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
