package com.meterware.servletunit;

import java.util.Vector;
import java.util.Enumeration;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 * A base class for servlet unit tests.
 **/
public abstract class ServletUnitTest extends TestCase {

    public ServletUnitTest(String name) {
        super(name);
    }

    protected Object[] toArray(Enumeration e) {
        ArrayList result = new ArrayList();
        while (e.hasMoreElements()) result.add(e.nextElement());
        return result.toArray();
    }

    protected void assertMatchingSet(String comment, Object[] expected, Object[] found) {
        Vector expectedItems = new Vector();
        Vector foundItems = new Vector();
        for (int i = 0; i < expected.length; i++) expectedItems.addElement(expected[i]);
        for (int i = 0; i < found.length; i++) foundItems.addElement(found[i]);
        for (int i = 0; i < expected.length; i++) {
            if (!foundItems.contains(expected[i])) {
                fail(comment + ": expected " + asText(expected) + " but found " + asText(found));
            } else {
                foundItems.removeElement(expected[i]);
            }
        }
        for (int i = 0; i < found.length; i++) {
            if (!expectedItems.contains(found[i])) {
                fail(comment + ": expected " + asText(expected) + " but found " + asText(found));
            } else {
                expectedItems.removeElement(found[i]);
            }
        }
        if (!foundItems.isEmpty()) fail(comment + ": expected " + asText(expected) + " but found " + asText(found));
    }

    protected String asText(Object[] args) {
        StringBuffer sb = new StringBuffer("{");
        for (int i = 0; i < args.length; i++) {
            if (i != 0) sb.append(",");
            sb.append('"').append(args[i]).append('"');
        }
        sb.append("}");
        return sb.toString();
    }
}
