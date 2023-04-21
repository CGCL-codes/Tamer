package ca.cbc.sportwire.dochandler;

import org.jdom.Document;
import javax.xml.transform.Transformer;
import ca.cbc.sportwire.WireFeederProperties;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Category;

/**
 * <p><code>OddsToSML</code> is used by the TSN Play-by-play to
 * into Sportsml
 *
 * <p>Created: Sun Apr  3 10:18:57 EDT 2005</p>
 *
 * @author <a href="mailto:garym@xmlteam.com">Gary Lawrence Murphy</a>
 * @version $Id: OddsToSML.java,v 1.1 2006-09-08 03:06:22 garym Exp $
 */
public class OddsToSML extends XSLFilter {

    /**
	 * Set up a reporting category in Log4J
	 */
    static Category cat = Category.getInstance(OddsToSML.class.getName());

    private DocHandler _instance = null;

    public DocHandler getInstance(ExtendedProperties config) {
        if (_instance == null) {
            _instance = (DocHandler) new OddsToSML(config);
        }
        return _instance;
    }

    /**
	 * <code>OddsToSML</code> constructor stores the config so
	 * we can get at connection details later
	 *
	 * @param config an <code>ExtendedProperties</code> value
	 */
    public OddsToSML(ExtendedProperties config) {
        super(config);
        setXslfile(config.getString(ODDS_XSL_FILENAME_PROPERTY, DEFAULT_ODDS_XSL_FILENAME));
        cat.info(getXslfile());
    }

    protected String setDocParameter(Transformer xslt, Document dtd, String doctag) {
        cat.debug(doctag + " using Odds XSL " + getXslfile());
        return null;
    }
}
