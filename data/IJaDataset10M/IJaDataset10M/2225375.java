package experiment.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import android.content.Context;

/**
 * 
 * @author kuehnel.christine
 * 
 * reads and parses xml file stored in \assets
 * file must be named VPx.xml with x being the number of the VP
 */
public class ExpConfigParsing {

    private static final Logger logger = LoggerFactory.getLogger();

    private ExpParsedConfigDataSet myExpParsedConfigDataSet;

    public String vp;

    public String configString;

    InputStreamReader isr;

    public ExpConfigParsing(Context context, String vp, ExpParsedConfigDataSet myExpParsedConfigDataSet) {
        this.vp = vp;
        this.myExpParsedConfigDataSet = myExpParsedConfigDataSet;
        this.myExpParsedConfigDataSet.setVPNumber(vp);
        readAndParseConfigFile(context);
    }

    public void readAndParseConfigFile(Context context) {
        InputStream fIn;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            String filename = "config" + vp + ".xml";
            logger.info("* " + filename);
            fIn = context.getAssets().open(filename);
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            ExpConfigHandler myExpHandler = new ExpConfigHandler(myExpParsedConfigDataSet);
            xr.setContentHandler(myExpHandler);
            xr.parse(new InputSource(fIn));
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
            logger.error(e);
        } catch (ParserConfigurationException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }
}
