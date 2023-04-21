package org.openexi.proc.io;

import org.openexi.proc.common.QName;
import junit.framework.Assert;
import junit.framework.TestCase;

public class QNameTest extends TestCase {

    public QNameTest(String name) {
        super(name);
    }

    /**
   * QName test
   */
    public void testQName() throws Exception {
        QName fooA = new QName("foo:A", "urn:foo");
        Assert.assertEquals("urn:foo", fooA.namespaceName);
        Assert.assertEquals("A", fooA.localName);
        Assert.assertEquals("foo", fooA.prefix);
        QName fooB = new QName("B", "urn:foo");
        Assert.assertEquals("urn:foo", fooB.namespaceName);
        Assert.assertEquals("B", fooB.localName);
        Assert.assertEquals("", fooB.prefix);
        QName bareC = new QName("C", (String) null);
        Assert.assertEquals("", bareC.namespaceName);
        Assert.assertEquals("C", bareC.localName);
        Assert.assertEquals("", bareC.prefix);
        QName fooD = new QName("foo:D", (String) null);
        Assert.assertNull(fooD.namespaceName);
        Assert.assertEquals("D", fooD.localName);
        Assert.assertEquals("foo", fooD.prefix);
        QName fooE = new QName(":E", "urn:foo");
        Assert.assertEquals("urn:foo", fooE.namespaceName);
        Assert.assertEquals("E", fooE.localName);
        Assert.assertEquals("", fooE.prefix);
        QName foo = new QName("foo:", "urn:foo");
        Assert.assertEquals("urn:foo", foo.namespaceName);
        Assert.assertEquals("", foo.localName);
        Assert.assertEquals("foo", foo.prefix);
        QName empty = new QName("", "urn:foo");
        Assert.assertEquals("urn:foo", empty.namespaceName);
        Assert.assertEquals("", empty.localName);
        Assert.assertEquals("", empty.prefix);
    }

    /**
   * QName equality test
   */
    public void testQNameEquality() throws Exception {
        QName fooA = new QName("foo:A", "urn:foo");
        QName _fooA = new QName("_foo:A", "urn:foo");
        Assert.assertTrue(fooA.equals(_fooA));
        QName fooB = new QName("foo:B", "urn:foo");
        Assert.assertFalse(fooB.equals(fooA));
        QName gooA = new QName("goo:A", "urn:goo");
        Assert.assertFalse(gooA.equals(fooA));
        QName disguisedGooA = new QName("foo:A", "urn:goo");
        Assert.assertFalse(disguisedGooA.equals(fooA));
        QName A = new QName("A", "");
        QName _A = new QName("A", "");
        Assert.assertTrue(A.equals(_A));
        Assert.assertFalse(A.equals(fooA));
        QName B = new QName("B", "");
        Assert.assertFalse(B.equals(A));
        QName unboundFooA = new QName("foo:A", (String) null);
        QName _unboundFooA = new QName("foo:A", (String) null);
        Assert.assertFalse(unboundFooA.equals(fooA));
        Assert.assertFalse(unboundFooA.equals(_unboundFooA));
    }
}
