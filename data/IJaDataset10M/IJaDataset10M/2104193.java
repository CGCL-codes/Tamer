package org.gvsig.remoteClient.sld;

import java.io.IOException;
import org.gvsig.remoteClient.gml.schemas.XMLSchemaParser;
import org.xmlpull.v1.XmlPullParserException;
import com.iver.cit.gvsig.fmap.drivers.legend.LegendDriverException;

/**
 * Interface that has to be implemented by all the SLDClases.
 * 
 *
 * @see http://portal.opengeospatial.org/files/?artifact_id=1188 
 * @author Pepe Vidal Salvador - jose.vidal.salvador@iver.es
 *
 */
public interface ISLDFeatures {

    /**
	 * Parses the contents of the SLD document to extract the 
	 * information about a specific SLD Feature
	 * 
	 * @param parser
	 * @param cuTag
	 * @param expressionType
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws LegendDriverException
	 */
    public void parse(XMLSchemaParser parser, int cuTag, String expressionType) throws XmlPullParserException, IOException, LegendDriverException;

    /**
	 * Translate the object into an XML String according with
	 * the Styled Layer Specification
	 * 
	 * @return
	 */
    public String toXML();
}
