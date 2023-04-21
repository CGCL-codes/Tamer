package com.tonicsystems.jarjar;

import junit.framework.*;

public class WildcardTest extends TestCase {

    public void testWildcards() {
        wildcard("net/sf/cglib/**", "foo/@1", "net/sf/cglib/proxy/Mixin$Generator", "foo/proxy/Mixin$Generator");
        wildcard("net/sf/cglib/**", "foo/@1", "net/sf/cglib/Bar", "foo/Bar");
        wildcard("net/sf/cglib/**", "foo/@1", "net/sf/cglib/Bar/Baz", "foo/Bar/Baz");
        wildcard("net/sf/cglib/**", "foo/@1", "net/sf/cglib/", "foo/");
        wildcard("net/sf/cglib/**", "foo/@1", "net/sf/cglib/!", null);
        wildcard("net/sf/cglib/*", "foo/@1", "net/sf/cglib/Bar", "foo/Bar");
        wildcard("net/sf/cglib/*/*", "foo/@2/@1", "net/sf/cglib/Bar/Baz", "foo/Baz/Bar");
    }

    private void wildcard(String pattern, String result, String value, String expect) {
        Wildcard wc = new Wildcard(pattern, result);
        assertEquals(expect, wc.replace(value));
    }

    public WildcardTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(WildcardTest.class);
    }
}
