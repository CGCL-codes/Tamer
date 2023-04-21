package com.kenstevens.stratdom.site.httpunit;

import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang.NotImplementedException;
import com.kenstevens.stratdom.site.SiteForm;
import com.kenstevens.stratdom.site.SiteFormFactory;
import com.kenstevens.stratdom.site.SiteResponse;
import com.kenstevens.stratdom.site.parser.ParseException;

public class SiteFormFactoryHttpUnit implements SiteFormFactory {

    @Override
    public SiteForm parseForm(SiteResponse response, String attributeName, String attributeValue) throws ParseException {
        SiteResponseHttpUnit siteResponse = (SiteResponseHttpUnit) response;
        if ("name".equals(attributeName)) {
            return siteResponse.getFormWithName(attributeValue);
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    public SiteForm parseSendMailForm(SiteResponse response, String attributeName, String attributeValue) throws XPathExpressionException {
        SiteResponseHttpUnit siteResponse = (SiteResponseHttpUnit) response;
        if ("name".equals(attributeName)) {
            return siteResponse.getSendmailFormWithName(attributeValue);
        } else {
            throw new NotImplementedException();
        }
    }
}
