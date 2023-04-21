package task1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

public class LoadXMLTemplate {

    private static final Logger logger = Logger.getLogger(LoadXMLTemplate.class);

    private String inputFile;

    private File file;

    private JAXBContext jaxbContext;

    private Unmarshaller jaxbUnmarshaller;

    private Object obj;

    public LoadXMLTemplate(String inputFile) {
        this.inputFile = inputFile;
        logger.info("LoadXMLTemplate(String) constructor");
    }

    public XMLTemplate getXMLTemplate() {
        try {
            logger.info("LoadXMLTemplate.getXMLTemplate() method");
            file = new File(inputFile);
            jaxbContext = JAXBContext.newInstance(XMLTemplate.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            obj = jaxbUnmarshaller.unmarshal(new FileInputStream(file));
        } catch (JAXBException e) {
            logger.error("LoadXMLTemplate.getXMLTemplate() error : JAXB exception has occurred", e);
        } catch (FileNotFoundException e) {
            logger.error("LoadXMLTemplate.getXMLTemplate() error : File does not exist or for some reason is inaccessible", e);
        }
        return (XMLTemplate) obj;
    }
}
