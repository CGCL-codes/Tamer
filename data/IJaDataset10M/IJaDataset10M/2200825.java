package com.webhiker.dreambox.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.webhiker.dreambox.api.Utils;

/**
 * The Class StreamInfo lists detailed information about the currently showing stream.
 */
public class ServiceData {

    private static final String CHANNEL = "channel";

    private String serviceName, serviceReference;

    private List<Audio> audio;

    /**
	 * Instantiates a new stream info.
	 * 
	 * @param is the is
	 * 
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public ServiceData(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        is.close();
        setServiceName(Utils.getValue(Utils.getElement(doc, "service", 0), "name"));
        setServiceReference(Utils.getValue(Utils.getElement(doc, "service", 0), "reference"));
        audio = new ArrayList<Audio>();
        for (int i = 0; i < Utils.getSize(doc, CHANNEL); i++) {
            audio.add(new Audio(Utils.getElement(doc, CHANNEL, i), i));
        }
    }

    public ServiceData() {
        setServiceName("");
        setServiceReference("");
        setAudio(new ArrayList<Audio>());
    }

    /**
	 * Gets the service name.
	 * 
	 * @return the service name
	 */
    public String getServiceName() {
        return serviceName;
    }

    /**
	 * Sets the service name.
	 * 
	 * @param serviceName the new service name
	 */
    private void setServiceName(String serviceName) {
        if (serviceName == null) {
            this.serviceName = "";
        } else {
            this.serviceName = serviceName;
        }
    }

    /**
	 * Gets the service reference.
	 * 
	 * @return the service reference
	 */
    public String getServiceReference() {
        return serviceReference;
    }

    /**
	 * Sets the service reference.
	 * 
	 * @param serviceReference the new service reference
	 */
    private void setServiceReference(String serviceReference) {
        if (serviceReference == null) {
            this.serviceReference = "";
        } else {
            this.serviceReference = serviceReference;
        }
    }

    public List<Audio> getAudio() {
        return audio;
    }

    private void setAudio(List<Audio> audio) {
        this.audio = audio;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceData) {
            if (!getServiceName().equals(((ServiceData) obj).getServiceName())) return false;
            if (!getServiceReference().equals(((ServiceData) obj).getServiceReference())) return false;
            List<Audio> a1 = getAudio();
            List<?> a2 = ((ServiceData) obj).getAudio();
            for (Iterator<Audio> i = a1.iterator(); i.hasNext(); ) {
                if (!a2.contains(i.next())) return false;
            }
            return true;
        }
        return false;
    }
}
