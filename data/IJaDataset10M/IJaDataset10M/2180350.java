package org.gvsig.remoteClient.sld.sld1_0_0;

import java.io.IOException;
import org.gvsig.remoteClient.gml.schemas.XMLSchemaParser;
import org.gvsig.remoteClient.sld.SLDExtent;
import org.gvsig.remoteClient.sld.SLDTags;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Implements the Extent element of an SLD implementation specification (version 
 * 1.0.0).<p>
 * When used in a UserLayer (<code>SLDUserDefinedLayer</code>), it defines what 
 * features are to be included in the layer and when used in a NamedLayer (<code>SLDNamedLayer</code>)
 * , it filters the features that are part of the named layer.
 *  
 * @see http://portal.opengeospatial.org/files/?artifact_id=1188 
 * @author pepe vidal salvador - jose.vidal.salvador@iver.es
 */
public class SLDExtent1_0_0 extends SLDExtent {

    public void parse(XMLSchemaParser parser, int cuTag, String expressionType) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(XMLSchemaParser.START_TAG, null, SLDTags.EXTENT);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case XMLSchemaParser.START_TAG:
                    if (parser.getName().compareTo(SLDTags.NAME) == 0) {
                        setName(parser.nextText());
                    } else if (parser.getName().compareTo(SLDTags.VALUE) == 0) {
                        setValue(parser.nextText());
                    }
                    break;
                case XMLSchemaParser.END_TAG:
                    if (parser.getName().compareTo(SLDTags.EXTENT) == 0) end = true;
                    break;
                case XMLSchemaParser.TEXT:
                    break;
            }
            if (!end) currentTag = parser.next();
        }
        parser.require(XMLSchemaParser.END_TAG, null, SLDTags.EXTENT);
    }

    public String toXML() {
        throw new Error("Not yet implemented");
    }
}
