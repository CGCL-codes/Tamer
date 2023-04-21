package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.Features;

public class FeaturesImpl extends BaseSchemaEntity implements Features {

    private static final long serialVersionUID = 2461660169443089969L;

    protected List<String> featureList;

    protected Long total;

    public List<String> getFeatureList() {
        if (featureList == null) {
            featureList = new ArrayList<String>();
        }
        return this.featureList;
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
            if (name.equals("feature")) {
                getFeatureList().add(XppUtils.getElementValueFromNode(parser));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "features");
        XppUtils.setAttributeValueToNode(element, "total", getTotal());
        for (String feature : getFeatureList()) {
            XppUtils.setElementValueToNode(element, "feature", feature);
        }
        serializer.endTag(null, "features");
    }
}
