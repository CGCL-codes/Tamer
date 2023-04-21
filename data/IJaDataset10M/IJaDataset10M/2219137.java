package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.Visibility;
import com.google.code.linkedinapi.schema.VisibilityType;

public class VisibilityImpl extends BaseSchemaEntity implements Visibility {

    private static final long serialVersionUID = 2461660169443089969L;

    protected VisibilityType code;

    public VisibilityType getCode() {
        return code;
    }

    public void setCode(VisibilityType value) {
        this.code = value;
    }

    @Override
    public void init(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.nextTag() == XmlPullParser.START_TAG) {
            String name = parser.getName();
            if (name.equals("code")) {
                setCode(VisibilityType.fromValue(XppUtils.getElementValueFromNode(parser)));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "visibility");
        if (getCode() != null) {
            XppUtils.setElementValueToNode(element, "code", getCode().value());
        }
        serializer.endTag(null, "visibility");
    }
}
