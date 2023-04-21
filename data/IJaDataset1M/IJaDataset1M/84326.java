package dw2;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Owner
 */
public class XMLFileIO extends FileIO {

    @Override
    public final boolean read() {
        try {
            LinkedList<User> listUsers;
            listUsers = getUserList();
            File file = this.getFile();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("User");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node fstNode = nodeLst.item(s);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fstElmnt = (Element) fstNode;
                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("Username");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    String username = ((Node) fstNm.item(0)).getNodeValue();
                    NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("Score");
                    Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
                    NodeList lstNm = lstNmElmnt.getChildNodes();
                    int score = Integer.parseInt(((Node) lstNm.item(0)).getNodeValue());
                    listUsers.add(new User(username, score));
                }
            }
        } catch (SAXException ex) {
            Logger.getLogger(XMLFileIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(XMLFileIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            releaseList();
        }
        return true;
    }

    @Override
    public final boolean write(String[] username, int[] score) {
        try {
            LinkedList<User> listusers = new LinkedList<User>();
            for (int i = 0; i < username.length; i++) {
                listusers.add(new User(username[i], score[i]));
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("highscores");
            document.appendChild(rootElement);
            Iterator iterator;
            for (iterator = listusers.listIterator(); iterator.hasNext(); ) {
                User user = (User) iterator.next();
                Element em = document.createElement("User");
                Element nameElm = document.createElement("Username");
                Element scoreElm = document.createElement("Score");
                scoreElm.appendChild(document.createTextNode(Integer.toString(user.getScore())));
                nameElm.appendChild(document.createTextNode(user.getUsername()));
                rootElement.appendChild(em);
                em.appendChild(nameElm);
                em.appendChild(scoreElm);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(this.getFile());
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLFileIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLFileIO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            releaseList();
        }
        return true;
    }

    public XMLFileIO(File file) {
        this.setFile(file);
    }

    public XMLFileIO() {
    }
}
