package org.jasen.plugins;

import java.util.Arrays;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.jasen.core.PointTestResult;
import org.jasen.core.StandardParserData;
import org.jasen.core.engine.Jasen;
import org.jasen.error.JasenException;
import org.jasen.interfaces.JasenMessage;
import org.jasen.interfaces.JasenPlugin;
import org.jasen.interfaces.JasenTestResult;
import org.jasen.interfaces.ParserData;
import org.jasen.interfaces.ReceivedHeaderParser;

/**
 * <P>
 * 	Looks for unknown tcp port references in url tag attributes discovered by the SpamHTMLParser.
 * </P>
 * @author Jason Polites
 */
public class TagSourcePortScanner implements JasenPlugin {

    private float min = 0.5f;

    private float max = 0.9f;

    private int threshold = 1;

    private String[] knownPorts;

    /**
     *
     */
    public TagSourcePortScanner() {
        super();
    }

    public void init(Properties properties) throws JasenException {
        if (properties != null) {
            String strMin = properties.getProperty("min");
            String strMax = properties.getProperty("max");
            String strThresh = properties.getProperty("threshold");
            String strPorts = properties.getProperty("ports");
            if (strMin != null) {
                min = Float.parseFloat(strMin);
            }
            if (strMax != null) {
                max = Float.parseFloat(strMax);
            }
            if (strThresh != null) {
                threshold = Integer.parseInt(strThresh);
            }
            if (strPorts != null) {
                knownPorts = strPorts.split(",");
                Arrays.sort(knownPorts);
            }
        }
    }

    public void destroy() throws JasenException {
    }

    public JasenTestResult test(Jasen engine, MimeMessage rawMessage, JasenMessage parsedMessage, ParserData data, ReceivedHeaderParser parser) throws JasenException {
        PointTestResult result = new PointTestResult();
        result.setMin(min);
        result.setMax(max);
        result.setThreshold(threshold);
        int count = 0;
        if (data instanceof StandardParserData) {
            StandardParserData spData = (StandardParserData) data;
            if (spData.getSrcPortCount() > 0 && spData.getPorts() != null) {
                String port = null;
                for (int i = 0; i < spData.getPorts().size(); i++) {
                    port = (String) spData.getPorts().get(i);
                    if (Arrays.binarySearch(knownPorts, port) <= -1) {
                        count++;
                    }
                }
            }
        }
        result.setPoints(count);
        return result;
    }
}
