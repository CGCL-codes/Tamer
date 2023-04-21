package it.unina.seclab.jafimon;

import it.unina.seclab.jafimon.exceptions.CannotWriteFileException;
import it.unina.seclab.jafimon.exceptions.GenericConfigurationParsingException;
import it.unina.seclab.jafimon.exceptions.GenericConfigurationTrasformException;
import it.unina.seclab.jafimon.exceptions.InvalidConfigurationFileException;
import it.unina.seclab.jafimon.interfaces.IConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

/**
 * Questa classe incapsula il comportamento della configurazione usata
 * dal framework
 * 
 * @author 	Mauro Iorio
 *
 */
public class Configuration implements IConfiguration {

    private Logger logger = Logger.getRootLogger();

    /**
	 * Il nome della <code>Configuration</code>
	 */
    private String cName;

    /**
	 * Contiene il documento XML che definisce la <code>Configuration</code>
	 */
    private Document xmlDocument;

    /**
	 * 
	 * @param name il nome della configurazione (ad es.: predefined/Tomcat5/tomcat5.xml)
	 */
    public Configuration(String name) {
        cName = name;
    }

    public String getName() {
        return cName;
    }

    public void load(String basePath) throws FileNotFoundException, InvalidConfigurationFileException, GenericConfigurationParsingException {
        File f = new File(basePath + File.separator + cName);
        try {
            logger.debug(basePath + File.separator + cName);
            logger.debug(f.getCanonicalPath());
        } catch (IOException e) {
        }
        if ((!f.isFile()) || (!f.canRead())) throw new FileNotFoundException(basePath + File.separator + cName);
        xmlDocument = ConfigurationParser.parse(f);
        if (xmlDocument == null) {
            throw new FileNotFoundException("Exception during XML parsing. See log for details");
        }
    }

    public void save(String basePath) throws CannotWriteFileException, GenericConfigurationTrasformException {
        String fullName = basePath + File.separator + cName;
        File f = new File(fullName);
        if (f.exists()) throw new CannotWriteFileException(cName, "File already exists");
        if (!new File(fullName.substring(0, fullName.lastIndexOf(File.separator))).mkdirs()) throw new CannotWriteFileException(cName, "Cannot create intermediate directories");
        try {
            if (!f.createNewFile() || !f.canWrite()) throw new CannotWriteFileException(cName, "Unable to write to file (probably read-only)");
        } catch (IOException e) {
            throw new CannotWriteFileException(cName, e.getCause() + " - " + e.getLocalizedMessage());
        }
        ConfigurationParser.save(xmlDocument, fullName);
    }

    /**
	 * Restituisce il nome del sistema cui si riferisce questa configurazione
	 * @return il nome del sistema
	 */
    public String getSystemName() {
        return xmlDocument.getDocumentElement().getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
    }

    /**
	 * Crea una istanza di <code>Configuration</code> a partire dalla rappresentazione
	 * testuale del suo codice XML
	 * 
	 * @param source la stringa contenente il codice XML
	 * @param name il nome della configurazione
	 * @return l'istanza della <code>Configuration</code>
	 * @throws InvalidConfigurationFileException la rappresentazione del codice XML
	 * 			non definisce un file di configurazione valido
	 * @throws GenericConfigurationParsingException errore nel parsing del codice XML
	 */
    public static Configuration configurationFromString(String source, String name) throws InvalidConfigurationFileException, GenericConfigurationParsingException {
        Configuration cfg = new Configuration(name);
        cfg.xmlDocument = ConfigurationParser.parse(source);
        return cfg;
    }
}
