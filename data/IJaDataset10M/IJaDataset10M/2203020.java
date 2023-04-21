package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.Publisher;

public class PublisherImpl extends BaseSchemaEntity implements Publisher {

    private static final long serialVersionUID = 2461660169443089969L;

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public void init(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, null);
        while (parser.nextTag() == XmlPullParser.START_TAG) {
            String name = parser.getName();
            if (name.equals("name")) {
                setName(XppUtils.getElementValueFromNode(parser));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "publisher");
        XppUtils.setElementValueToNode(element, "name", getName());
        element.endTag(null, "publisher");
        ;
    }
}
