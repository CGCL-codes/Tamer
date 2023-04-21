package de.alexanderwilden.jatobo.modules.xmlauthentication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import de.alexanderwilden.jatoli.JatoliUtils;

class AuthDataContainer {

    private class AuthDataParser extends DefaultHandler {

        private Map<String, Map<String, List<String>>> rights = new HashMap<String, Map<String, List<String>>>();

        private String currUser = null;

        private String currModule = null;

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
            String eName = ("".equals(localName)) ? qName : localName;
            if (eName.equals("user")) {
                currUser = JatoliUtils.normalize(attrs.getValue("name"));
                if (!rights.containsKey(currUser)) {
                    rights.put(currUser, new HashMap<String, List<String>>());
                }
            } else if (eName.equals("module")) {
                if (currUser != null) {
                    currModule = attrs.getValue("name");
                    if (!rights.get(currUser).containsKey(currModule)) {
                        rights.get(currUser).put(currModule, new LinkedList<String>());
                    }
                }
            } else if (eName.equals("right")) {
                if (currUser != null && currModule != null) {
                    String right = attrs.getValue("name");
                    if (!rights.get(currUser).get(currModule).contains(right)) {
                        rights.get(currUser).get(currModule).add(right);
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String eName = ("".equals(localName)) ? qName : localName;
            if (eName.equals("user")) {
                currUser = null;
            } else if (eName.equals("module")) {
                currModule = null;
            }
        }

        public Map<String, Map<String, List<String>>> getRights() {
            return rights;
        }
    }

    private Map<String, Map<String, List<String>>> rights;

    private File authData;

    AuthDataContainer(String authDataFile) {
        authData = new File(authDataFile);
        try {
            readData();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isAuthorized(String buddyName, String module, String action) {
        Map<String, List<String>> userRights = rights.get(buddyName);
        if (userRights != null) {
            List<String> moduleRights = userRights.get(module);
            if (moduleRights != null) {
                return moduleRights.contains(action);
            }
        }
        return false;
    }

    void addRight(String buddyName, String module, String action) {
        Map<String, List<String>> userRights = rights.get(buddyName);
        if (userRights == null) {
            userRights = new HashMap<String, List<String>>();
            rights.put(buddyName, userRights);
        }
        List<String> moduleRights = userRights.get(module);
        if (moduleRights == null) {
            moduleRights = new LinkedList<String>();
            userRights.put(module, moduleRights);
        }
        if (!moduleRights.contains(action)) {
            moduleRights.add(action);
        }
    }

    private void readData() throws ParserConfigurationException, SAXException, IOException {
        AuthDataParser handler = new AuthDataParser();
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(authData, handler);
        rights = handler.getRights();
    }

    void writeData() throws IOException {
        String dataString = rightsToXml(true);
        FileWriter fw = new FileWriter(authData);
        fw.write(dataString);
        fw.close();
    }

    private String rightsToXml(boolean nice) {
        StringBuilder res = new StringBuilder("<?xml version=\"1.0\"?>");
        if (nice) res.append("\n");
        for (String userName : rights.keySet()) {
            res.append("<user name=\"").append(userName).append("\">");
            Map<String, List<String>> userRights = rights.get(userName);
            for (String moduleName : userRights.keySet()) {
                if (nice) res.append("\n\t");
                res.append("<module name=\"").append(moduleName).append("\">");
                List<String> moduleRights = userRights.get(moduleName);
                for (String rightName : moduleRights) {
                    if (nice) res.append("\n\t\t");
                    res.append("<right name=\"").append(rightName).append("\" />");
                }
                if (nice) res.append("\n\t");
                res.append("</module>");
            }
            if (nice) res.append("\n");
            res.append("</user>");
            if (nice) res.append("\n");
        }
        return res.toString();
    }
}
