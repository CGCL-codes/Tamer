package viewer.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class ConfigReader extends DefaultHandler {

    static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    /**
	 * Read a configuration from the given fileame.
	 * 
	 * @param filename
	 *            the filename to read the xml from.
	 * @return the configuration read.
	 * @throws SAXException
	 *             on parsing failure
	 * @throws ParserConfigurationException
	 *             on failure.
	 * @throws IOException
	 *             on failure.
	 * @throws FileNotFoundException
	 *             on failure.
	 */
    public static Configuration read(String filename) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
        ConfigHandler configHandler = new ConfigHandler();
        sax.parse(new FileInputStream(filename), configHandler);
        for (TileConfig config : configHandler.configuration.getTileConfigs()) {
            logger.info("tiles: " + config.toString());
        }
        for (TileConfig config : configHandler.configuration.getTileConfigsOverlay()) {
            logger.info("overlay: " + config.toString());
        }
        return configHandler.configuration;
    }

    /**
	 * Test
	 * 
	 * @param args
	 *            none
	 * @throws Exception
	 *             on failure
	 */
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        read("res/searcher/config1.xml");
    }
}

class ConfigHandler extends DefaultHandler {

    static final Logger logger = LoggerFactory.getLogger(ConfigHandler.class);

    Configuration configuration = new Configuration();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("option")) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            parseOption(name, value);
        } else if (qName.equals("tiles")) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            if (type.equals("urldisk")) {
                String url = attributes.getValue("url");
                String path = attributes.getValue("path");
                String userAgent = attributes.getValue("userAgent");
                TileConfigUrlDisk c = new TileConfigUrlDisk(configuration.getTileConfigs().size() + 1, name, url, path);
                if (userAgent != null) {
                    c.setUserAgent(userAgent);
                }
                configuration.getTileConfigs().add(c);
            } else if (type.equals("mapsforge")) {
                String data = attributes.getValue("data");
                String style = attributes.getValue("style");
                configuration.getTileConfigs().add(new TileConfigMapsforge(configuration.getTileConfigs().size() + 1, name, data, style));
            }
        } else if (qName.equals("overlay")) {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            if (type.equals("urldisk")) {
                String url = attributes.getValue("url");
                String path = attributes.getValue("path");
                String userAgent = attributes.getValue("userAgent");
                TileConfigUrlDisk c = new TileConfigUrlDisk(configuration.getTileConfigsOverlay().size() + 1, name, url, path);
                if (userAgent != null) {
                    c.setUserAgent(userAgent);
                }
                configuration.getTileConfigsOverlay().add(c);
            } else if (type.equals("disk")) {
                String path = attributes.getValue("path");
                configuration.getTileConfigsOverlay().add(new TileConfigUrlDisk(configuration.getTileConfigsOverlay().size() + 1, name, null, path));
            }
        }
    }

    private void parseOption(String name, String value) {
        if (name.equals("grid")) {
            configuration.setShowGrid(parseBoolean(value));
        } else if (name.equals("tilenumbers")) {
            configuration.setShowTileNumbers(parseBoolean(value));
        } else if (name.equals("crosshair")) {
            configuration.setShowCrosshair(parseBoolean(value));
        } else if (name.equals("overlay")) {
            configuration.setShowOverlay(parseBoolean(value));
        } else if (name.equals("online")) {
            configuration.setOnline(parseBoolean(value));
        } else if (name.equals("tilenumbers")) {
            configuration.setShowGrid(parseBoolean(value));
        } else if (name.equals("database")) {
            configuration.setFileDb(value);
        } else if (name.equals("width")) {
            configuration.setWidth(parseInteger(value));
        } else if (name.equals("height")) {
            configuration.setHeight(parseInteger(value));
        } else if (name.equals("zoom")) {
            configuration.setZoom(parseInteger(value));
        } else if (name.equals("lon")) {
            configuration.setLon(parseDouble(value));
        } else if (name.equals("lat")) {
            configuration.setLat(parseDouble(value));
        } else {
            logger.debug(String.format("unhandled option: %s:%s", name, value));
        }
    }

    private boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
    }

    private int parseInteger(String value) {
        return Integer.parseInt(value);
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value);
    }
}
