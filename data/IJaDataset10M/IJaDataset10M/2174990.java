package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.ExperienceLevel;
import com.google.code.linkedinapi.schema.ExperienceLevelCode;

public class ExperienceLevelImpl extends BaseSchemaEntity implements ExperienceLevel {

    private static final long serialVersionUID = 2461660169443089969L;

    protected ExperienceLevelCode code;

    protected String name;

    public ExperienceLevelCode getCode() {
        return code;
    }

    public void setCode(ExperienceLevelCode value) {
        this.code = value;
    }

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
            if (name.equals("code")) {
                setCode(ExperienceLevelCode.fromValue(XppUtils.getElementValueFromNode(parser)));
            } else if (name.equals("name")) {
                setName(XppUtils.getElementValueFromNode(parser));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "experience-level");
        if (getCode() != null) {
            XppUtils.setElementValueToNode(element, "code", getCode().value());
        }
        XppUtils.setElementValueToNode(element, "name", getName());
        serializer.endTag(null, "experience-level");
    }
}
