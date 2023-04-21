package com.google.code.solrdimension.parsers.mappers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

/**
 * ObjectXMLMapper implementation
 * 
 * @author Michael Chan
 *
 */
@Service
public class JAXBXmlMapperImpl implements JAXBXmlMapper {

    private static Logger logger = LoggerFactory.getLogger(JAXBXmlMapperImpl.class);

    @Autowired
    @Qualifier("marshaller")
    private Unmarshaller unmarshaller;

    /**
     * Deserializes an object from the assigned file.
     *
     * @param filename
     *            - name of the file that should be deserialized
     * @return deserialized object
     * @throws IOException
     */
    public JAXBElement<?> readObjectFromXml(InputStream inputStream) throws IOException {
        FileInputStream fis = null;
        try {
            Object obj = unmarshaller.unmarshal(new StreamSource(inputStream));
            return (JAXBElement<?>) obj;
        } catch (IOException e) {
            logger.error("Xml-Deserialization failed due to an IOException.", e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return null;
    }
}
