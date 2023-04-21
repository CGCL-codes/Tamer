package org.matsim.signalsystems;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.io.MatsimJaxbXmlParser;
import org.matsim.jaxb.signalsystemsconfig11.XMLSignalSystemConfig;
import org.xml.sax.SAXException;

/**
 * Reader for the signalSystemConfigurations_v1.1.xsd file format
 * @author dgrether
 */
public class SignalSystemConfigurationsReader11 extends MatsimJaxbXmlParser {

    private static final Logger log = Logger.getLogger(SignalSystemConfigurationsReader11.class);

    public SignalSystemConfigurationsReader11(String schemaLocation) {
        super(schemaLocation);
    }

    public XMLSignalSystemConfig readSignalSystemConfig11File(String filename) throws SAXException, ParserConfigurationException, IOException, JAXBException {
        XMLSignalSystemConfig xmlLssConfig;
        JAXBContext jc;
        jc = JAXBContext.newInstance(org.matsim.jaxb.signalsystemsconfig11.ObjectFactory.class);
        Unmarshaller u = jc.createUnmarshaller();
        super.validateFile(filename, u);
        InputStream stream = null;
        try {
            stream = IOUtils.getInputStream(filename);
            xmlLssConfig = (XMLSignalSystemConfig) u.unmarshal(stream);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                log.warn("Could not close stream.", e);
            }
        }
        return xmlLssConfig;
    }

    @Override
    public void readFile(String filename) throws JAXBException, SAXException, ParserConfigurationException, IOException {
        throw new UnsupportedOperationException("Use readSignalSystemConfig11File() method");
    }
}
