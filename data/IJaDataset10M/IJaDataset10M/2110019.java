package br.ufmg.catustec.arangi.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;
import br.ufmg.catustec.arangi.commons.BasicException;
import br.ufmg.catustec.arangi.commons.NVLHelper;

public class FacesConfigConversationScan {

    private static final int MANAGED_BEAN_OTHER = 0;

    private static final int MANAGED_BEAN_NAME = 1;

    private static final int MANAGED_BEAN_SCOPE = 2;

    private String mbName;

    private String mbScope;

    private int currentValue = 0;

    private Map<String, String> conversationBeanNames;

    protected static Logger log = Logger.getLogger(FacesConfigConversationScan.class);

    public void scanConversationManagedBeans(Map<String, String> conversationBeanNames, InputStream is) throws BasicException {
        XMLStreamReader xmlReader = null;
        this.conversationBeanNames = conversationBeanNames;
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            xmlReader = xmlInputFactory.createXMLStreamReader(is);
        } catch (Exception e) {
            throw BasicException.errorHandling("Erro ao abrir file de dados xml de leitura", "msgErroAbrirArquivoLeituraDadosXML", e, log);
        }
        try {
            while (xmlReader.hasNext()) {
                xmlReader.next();
                if (xmlReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                    this.startElement(xmlReader);
                } else if (xmlReader.getEventType() == XMLStreamReader.END_ELEMENT) {
                    this.endElement(xmlReader);
                } else if (xmlReader.getEventType() == XMLStreamReader.CHARACTERS) {
                    this.characters(xmlReader.getText());
                }
            }
        } catch (Exception e) {
            throw BasicException.errorHandling("Error reading xml file", "errorMsgReadingXMLDataFile", e, log);
        }
    }

    private void endElement(XMLStreamReader xmlReader) {
        String qName = xmlReader.getLocalName().toLowerCase();
        if (qName.equals("managed-bean")) {
            if (mbScope != null && mbScope.trim().equals("#{conversation}")) {
                ;
                conversationBeanNames.put(mbName.trim(), mbName.trim());
            }
        }
    }

    private void startElement(XMLStreamReader xmlReader) throws Exception {
        String qName = xmlReader.getLocalName();
        if (qName.toLowerCase().equals("managed-bean-name")) {
            currentValue = MANAGED_BEAN_NAME;
        } else if (qName.toLowerCase().equals("managed-bean-scope")) {
            currentValue = MANAGED_BEAN_SCOPE;
        } else {
            currentValue = MANAGED_BEAN_OTHER;
        }
    }

    private void characters(String text) throws Exception {
        switch(currentValue) {
            case MANAGED_BEAN_SCOPE:
                mbScope = text;
                currentValue = MANAGED_BEAN_OTHER;
                break;
            case MANAGED_BEAN_NAME:
                mbName = text;
                currentValue = MANAGED_BEAN_OTHER;
                break;
            default:
                ;
        }
    }
}
