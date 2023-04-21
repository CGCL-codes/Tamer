package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.Country;

public class CountryImpl extends BaseSchemaEntity implements Country {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3791971546760035359L;

    protected String code;

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

    @Override
    public void init(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, null);
        while (parser.nextTag() == XmlPullParser.START_TAG) {
            String name = parser.getName();
            if (name.equals("code")) {
                setCode(XppUtils.getElementValueFromNode(parser));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "country");
        XppUtils.setElementValueToNode(element, "code", getCode());
        serializer.endTag(null, "country");
    }
}
