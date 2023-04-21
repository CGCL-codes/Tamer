package src.com.tadp.grupo4.blackjack.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ResourceBundle;
import org.apache.commons.configuration.XMLConfiguration;
import src.com.tadp.grupo4.blackjack.utils.exceptions.NotFoundConfigFile;

/**
 * Es una clase abstracta utilizada para parsear cualquier clase de archivo xml.
 * Esta clase va a buscar un archivo tipo properties para encontrar el address del archivo xml a leer.
 * Por default el archivo tipo properties es "application.properties" 
 * @author pasutm
 *
 */
public abstract class XmlBuilder {

    protected static final String DEFAULT_PATH = "application.properties";

    protected String path;

    protected XMLConfiguration conf;

    /**
	 * Levanta como un objeto el archivo xml, aqui obriene el archivo desde el properties 
	 * @param property key de para obtener el xml desde el archivo tipo properties
	 * @throws NotFoundConfigFile
	 */
    protected void init(String property) throws NotFoundConfigFile {
        PropertyReader prop = PropertyReader.getInstance();
        prop.setPath(path);
        String realPath = prop.getProperty(property);
        conf = getXMLDocument(realPath);
    }

    public void setPath(String path) throws NotFoundConfigFile {
        this.path = path;
    }

    private static XMLConfiguration getXMLDocument(String path) throws NotFoundConfigFile {
        XMLConfiguration conf = null;
        File file = new File(path);
        System.out.println("XmlBuider: " + path);
        try {
            InputStream in = new FileInputStream(file);
            Reader r = new InputStreamReader(in);
            conf = new XMLConfiguration();
            conf.setValidating(false);
            conf.load(r);
        } catch (Exception e) {
            throw new NotFoundConfigFile();
        }
        return conf;
    }
}
