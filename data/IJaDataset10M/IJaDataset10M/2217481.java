package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.NetworkStats;
import com.google.code.linkedinapi.schema.Property;

public class NetworkStatsImpl extends BaseSchemaEntity implements NetworkStats {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5888495492447011822L;

    protected List<Property> propertyList;

    protected Long total;

    public List<Property> getPropertyList() {
        if (propertyList == null) {
            propertyList = new ArrayList<Property>();
        }
        return this.propertyList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long value) {
        this.total = value;
    }

    @Override
    public void init(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, null);
        setTotal(XppUtils.getAttributeValueAsLongFromNode(parser, "total"));
        while (parser.nextTag() == XmlPullParser.START_TAG) {
            String name = parser.getName();
            if (name.equals("property")) {
                PropertyImpl personImpl = new PropertyImpl();
                personImpl.init(parser);
                getPropertyList().add(personImpl);
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "network-stats");
        XppUtils.setAttributeValueToNode(element, "total", getTotal());
        for (Property property : getPropertyList()) {
            ((PropertyImpl) property).toXml(serializer);
        }
        serializer.endTag(null, "network-stats");
    }
}
