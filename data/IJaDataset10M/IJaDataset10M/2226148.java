package uk.org.windswept.feedreader.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.log4j.Logger;
import uk.org.windswept.feedreader.Configuration;
import uk.org.windswept.feedreader.ConfigurationException;
import uk.org.windswept.feedreader.Constants;
import uk.org.windswept.feedreader.FeedInfoList;
import uk.org.windswept.util.SimpleConsoleErrorAppender;

/**
 * @version    : $Revision: 73 $
 * @author     : $Author: satkinson $
 * Last Change : $Date$
 * URL         : $HeadURL$
 * ID          : $Id$
 */
public class PropertiesToFeedInfoListXml {

    private static final Logger LOGGER = Logger.getLogger(PropertiesToFeedInfoListXml.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new PropertiesToFeedInfoListXml().run(args);
    }

    private void run(String[] args) {
        Logger.getRootLogger().addAppender(new SimpleConsoleErrorAppender());
        String propertiesFilename = Constants.PROPERTIES_FILE;
        String xmlFilename = Constants.FEEDS_XML_FILE;
        if (args.length > 0) propertiesFilename = args[0];
        if (args.length > 1) xmlFilename = args[1];
        LOGGER.warn("Properties file = " + propertiesFilename);
        LOGGER.warn("XML file = " + xmlFilename);
        File file = new File(xmlFilename);
        if (file.exists()) {
            LOGGER.error("Cannot create " + xmlFilename + " as it already exists");
            return;
        }
        try {
            LOGGER.warn("Loading feed list from properties");
            Configuration config = Configuration.getInstance();
            config.loadProperties(propertiesFilename);
            FeedInfoList feedInfoList = config.loadFeedListFromProperties();
            Writer writer = new FileWriter(file);
            LOGGER.warn("Writing feed list to XML");
            config.writeFeedListToXml(feedInfoList, writer);
        } catch (ConfigurationException e) {
            LOGGER.error("Problem with configurations", e);
        } catch (IOException e) {
            LOGGER.error("Problem writing file " + file, e);
        }
    }
}
