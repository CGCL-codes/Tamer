package net.sf.ldaptemplate.support.filter;

import net.sf.ldaptemplate.support.filter.EqualsFilter;
import net.sf.ldaptemplate.support.filter.OrFilter;
import junit.framework.TestCase;

/**
 * @author Adam Skogman
 */
public class OrFilterTest extends TestCase {

    /**
     * Constructor for OrFilterTest.
     * 
     * @param name
     */
    public OrFilterTest(String name) {
        super(name);
    }

    public void testZero() {
        OrFilter of = new OrFilter();
        assertEquals("", of.encode());
    }

    public void testOne() {
        OrFilter of = new OrFilter().or(new EqualsFilter("a", "b"));
        assertEquals("(a=b)", of.encode());
    }

    public void testTwo() {
        OrFilter of = new OrFilter().or(new EqualsFilter("a", "b")).or(new EqualsFilter("c", "d"));
        assertEquals("(|(a=b)(c=d))", of.encode());
    }

    public void testThree() {
        OrFilter of = new OrFilter().or(new EqualsFilter("a", "b")).or(new EqualsFilter("c", "d")).or(new EqualsFilter("e", "f"));
        assertEquals("(|(a=b)(c=d)(e=f))", of.encode());
    }
}
