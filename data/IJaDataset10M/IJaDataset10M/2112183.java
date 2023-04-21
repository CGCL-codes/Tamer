package wayic.http.extract;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;
import wayic.utils.SearchUtils;

/**
 * @author Ashesh Nishant
 * Created, Mar 1, 2009 2:06:14 AM
 */
public class Harvester {

    private static final Logger LOGGER = LogManager.getLogger(Harvester.class);

    private static final Harvester THIS = new Harvester();

    private static String userHome = System.getProperty("user.home");

    private Hashtable<String, ScraperConfiguration> configCache;

    @SuppressWarnings("unchecked")
    private Harvester() {
        Properties hostConfigMap = new Properties();
        configCache = new Hashtable<String, ScraperConfiguration>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("hostConfig.properties");
            hostConfigMap.load(is);
            Enumeration hosts = hostConfigMap.propertyNames();
            LOGGER.info("Configuring Web Extraction rules for ...");
            while (hosts.hasMoreElements()) {
                String hostName = (String) hosts.nextElement();
                String configFileName = hostConfigMap.getProperty(hostName);
                InputStream configInput = getClass().getClassLoader().getResourceAsStream(configFileName);
                if (configInput != null) {
                    ScraperConfiguration config = new ScraperConfiguration(new InputSource(configInput));
                    LOGGER.info(hostName);
                    configCache.put(hostName.toLowerCase(), config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Harvester could not get constructed : " + e.getLocalizedMessage());
        }
    }

    public static Harvester instance() {
        return THIS;
    }

    public List<Variable> execute(URL url, String[] variables) throws HostNotConfiguredEx {
        if (url == null) {
            return null;
        }
        String host = SearchUtils.friendlyName(url.getHost()).toLowerCase();
        ArrayList<Variable> extractedVariables = new ArrayList<Variable>();
        ScraperConfiguration config = configCache.get(host);
        if (config == null) {
            throw new HostNotConfiguredEx("Host not configured in hostConfig.properties : " + host);
        }
        Scraper scraper = new Scraper(config, userHome);
        scraper.getLogger().setLevel(Level.ERROR);
        scraper.setDebug(false);
        scraper.addVariableToContext("url", url.toExternalForm());
        scraper.execute();
        ScraperContext context = scraper.getContext();
        for (String var : variables) {
            Variable variable = context.getVar(var);
            if (variable != null) {
                extractedVariables.add(variable);
            }
        }
        return extractedVariables;
    }

    public String extract(URL url) throws HostNotConfiguredEx {
        List<Variable> extracts = Harvester.instance().execute(url, new String[] { "extract" });
        if (extracts == null || extracts.size() == 0) {
            return null;
        }
        return extracts.get(0).toString();
    }

    public static void main(String args[]) throws HostNotConfiguredEx, MalformedURLException {
        URL url = new URL("http://www.elyrics.net/read/m/madonna-lyrics/frozen-lyrics.html");
        List<Variable> extracts = Harvester.instance().execute(url, new String[] { "extract" });
        LOGGER.debug(extracts.get(0).toString());
        url = new URL("http://www.metrolyrics.com/heaven-tonight-lyrics-yngwie-malmsteen.html");
        extracts = Harvester.instance().execute(url, new String[] { "extract" });
        LOGGER.debug(extracts.get(0).toString());
        url = new URL("http://www.azlyrics.com/lyrics/dido/whiteflag.html");
        extracts = Harvester.instance().execute(url, new String[] { "extract" });
        LOGGER.debug(extracts.get(0).toString());
    }
}
