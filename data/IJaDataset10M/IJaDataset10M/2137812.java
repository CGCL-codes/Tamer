package pedro.mda.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import pedro.system.PedroException;
import pedro.system.PedroResources;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class FileLauncher {

    private Hashtable commandFromExtension;

    private static FileLauncher fileLauncher = null;

    public FileLauncher() {
        commandFromExtension = new Hashtable();
    }

    public int getDescribedExtensionsCount() {
        return commandFromExtension.size();
    }

    /**
     * @param fileExtension example would be .jpg, .gif, .html
     * @return String the command line invocation for the program
     *         e.g: netscape
     */
    public String getLaunchCommand(String fileExtension) {
        return (String) commandFromExtension.get(fileExtension.toUpperCase());
    }

    /**
     * reads the file extensions file
     *
     * @param inputStream the file ./pedro/config/FileExtensionsToLaunch.xml
     */
    public void parseExtensionLauncherRegistry(InputStream inputStream) throws ParserConfigurationException, PedroException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        Element launchProgramNode = null;
        Node currentChild = document.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentChild;
                if (element.getTagName().equals("launch_programs") == true) {
                    launchProgramNode = element;
                    break;
                }
            }
            currentChild = currentChild.getNextSibling();
        }
        if (launchProgramNode == null) {
            String errorMessage = PedroResources.getMessage("config.fileLauncherNoLaunchTag");
            throw new PedroException(errorMessage);
        }
        currentChild = launchProgramNode.getFirstChild();
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                parseExtensionEntry(currentElement);
            }
            currentChild = currentChild.getNextSibling();
        }
    }

    /**
     * parses an association between a file extension and a program
     * to launch it with
     *
     * @param record the parser element representing the XML fragment for
     *               a single association
     */
    private void parseExtensionEntry(Element record) throws PedroException {
        String recordName = record.getTagName();
        Node currentChild = record.getFirstChild();
        String currentExtension = null;
        while (currentChild != null) {
            if (currentChild.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentChild;
                String attributeName = currentElement.getTagName();
                Node fieldChild = currentElement.getFirstChild();
                if (fieldChild.getNodeType() == Node.TEXT_NODE) {
                    Text text = (Text) fieldChild;
                    String data = text.getData();
                    data = data.trim();
                    if (data.equals(PedroResources.EMPTY_STRING) == false) {
                        if (attributeName.equals("extension") == true) {
                            currentExtension = data.toUpperCase();
                        } else if (attributeName.equals("launch") == true) {
                            if (currentExtension == null) {
                                String errorMessage = PedroResources.getMessage("config.fileLauncherNoExtensionValue");
                                throw new PedroException(errorMessage.toString());
                            } else if (commandFromExtension.contains(currentExtension) == true) {
                                String errorMessage = PedroResources.getMessage("config.fileLauncherMultipleLaunchPrograms", currentExtension);
                                throw new PedroException(errorMessage);
                            } else {
                                commandFromExtension.put(currentExtension, data);
                            }
                        }
                    }
                }
            }
            currentChild = currentChild.getNextSibling();
        }
    }
}
