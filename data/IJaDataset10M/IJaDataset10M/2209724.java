package org.apache.shindig.gadgets.spec;

import static org.junit.Assert.assertEquals;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.xml.XmlUtil;
import org.junit.Test;

public class LocaleSpecTest {

    private static final Uri SPEC_URL = Uri.parse("http://example.org/foo.xml");

    @Test
    public void normalLocale() throws Exception {
        String xml = "<Locale" + " lang=\"en\"" + " country=\"US\"" + " language_direction=\"rtl\"" + " messages=\"http://example.org/msgs.xml\"/>";
        LocaleSpec locale = new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("rtl", locale.getLanguageDirection());
        assertEquals("http://example.org/msgs.xml", locale.getMessages().toString());
    }

    @Test
    public void relativeLocale() throws Exception {
        String xml = "<Locale messages=\"/test/msgs.xml\"/>";
        LocaleSpec locale = new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
        assertEquals("http://example.org/test/msgs.xml", locale.getMessages().toString());
    }

    @Test
    public void defaultLanguageAndCountry() throws Exception {
        String xml = "<Locale/>";
        LocaleSpec locale = new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
        assertEquals("all", locale.getLanguage());
        assertEquals("ALL", locale.getCountry());
    }

    @Test(expected = SpecParserException.class)
    public void invalidLanguageDirection() throws Exception {
        String xml = "<Locale language_direction=\"invalid\"/>";
        new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
    }

    @Test(expected = SpecParserException.class)
    public void invalidMessagesUrl() throws Exception {
        String xml = "<Locale messages=\"fobad@$%!fdf\"/>";
        new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
    }

    @Test
    public void nestedMessages() throws Exception {
        String msgName = "message name";
        String msgValue = "message value";
        String xml = "<Locale>" + "<msg name=\"" + msgName + "\">" + msgValue + "</msg>" + "</Locale>";
        LocaleSpec locale = new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
        assertEquals(msgValue, locale.getMessageBundle().getMessages().get(msgName));
    }

    @Test
    public void toStringIsSane() throws Exception {
        String xml = "<Locale lang='en' country='US' language_direction='rtl'" + " messages='foo'>" + "  <msg name='hello'>World</msg>" + "  <msg name='foo'>Bar</msg>" + "</Locale>";
        LocaleSpec loc = new LocaleSpec(XmlUtil.parse(xml), SPEC_URL);
        LocaleSpec loc2 = new LocaleSpec(XmlUtil.parse(loc.toString()), SPEC_URL);
        assertEquals(loc.getLanguage(), loc2.getLanguage());
        assertEquals(loc.getCountry(), loc2.getCountry());
        assertEquals(loc.getLanguageDirection(), loc2.getLanguageDirection());
        assertEquals(loc.getMessages(), loc2.getMessages());
        assertEquals(loc.getMessageBundle().getMessages(), loc2.getMessageBundle().getMessages());
    }
}
