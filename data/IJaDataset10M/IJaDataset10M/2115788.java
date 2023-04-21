package com.informa.hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import com.informa.utils.XmlUtils;

public class XmlMatcher extends TypeSafeMatcher<String> {

    private String actualXml;

    private XmlMatcher(String actualXml) {
        this.actualXml = actualXml;
    }

    @Override
    public boolean matchesSafely(String expectedXml) {
        return XmlUtils.toCanonicalForm(actualXml).equals(XmlUtils.toCanonicalForm(expectedXml));
    }

    public void describeTo(Description description) {
        description.appendText(actualXml);
    }

    public static XmlMatcher matchesXml(String actualXml) {
        return new XmlMatcher(actualXml);
    }
}
