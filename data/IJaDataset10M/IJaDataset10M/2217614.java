package com.google.code.linkedinapi.schema.xpp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import com.google.code.linkedinapi.schema.EmailDomains;

public class EmailDomainsImpl extends BaseSchemaEntity implements EmailDomains {

    private static final long serialVersionUID = 2461660169443089969L;

    protected List<String> emailDomainList;

    protected Long total;

    public List<String> getEmailDomainList() {
        if (emailDomainList == null) {
            emailDomainList = new ArrayList<String>();
        }
        return this.emailDomainList;
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
            if (name.equals("email-domain")) {
                getEmailDomainList().add(XppUtils.getElementValueFromNode(parser));
            } else {
                LOG.warning("Found tag that we don't recognize: " + name);
                XppUtils.skipSubTree(parser);
            }
        }
    }

    @Override
    public void toXml(XmlSerializer serializer) throws IOException {
        XmlSerializer element = serializer.startTag(null, "email-domains");
        XppUtils.setAttributeValueToNode(element, "total", getTotal());
        for (String node : getEmailDomainList()) {
            XppUtils.setElementValueToNode(element, "email-domain", node);
        }
        serializer.endTag(null, "email-domains");
    }
}
