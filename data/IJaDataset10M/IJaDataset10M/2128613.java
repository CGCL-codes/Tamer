package test.javax.management;

import java.util.Hashtable;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import test.MX4JTestCase;

/**
 * @version $Revision: 1.15 $
 */
public class ObjectNameTest extends MX4JTestCase {

    public ObjectNameTest(String s) {
        super(s);
    }

    public void testInvalidDomain() throws Exception {
        try {
            new ObjectName("missingColon");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("newLinePresent" + '\n' + ":k=v");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testValidNonPatternDomain() throws Exception {
        ObjectName name = new ObjectName(":k=v");
        if (name.isDomainPattern()) fail("Not a domain pattern");
        name = new ObjectName("domain:k=v");
        if (name.isDomainPattern()) fail("Not a domain pattern");
    }

    public void testValidPatternDomain() throws Exception {
        ObjectName name = new ObjectName("*:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("?:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("*domain:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("?domain:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("dom*ain:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("dom?ain:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("domain*:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
        name = new ObjectName("domain?:k=v");
        if (!name.isDomainPattern()) fail("Domain is a pattern");
    }

    public void testInvalidProperties() throws Exception {
        try {
            new ObjectName("noProps:");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("noPropsWithBlank: ");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("noPropsWithGarbage: abc ");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("noKey:=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:trailingSlash=Invalid,");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:key,invalid=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:key:invalid=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:key*invalid=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:key?invalid=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:?=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:*=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("keyWithInvalidChar:,=value");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("duplicateKey:key=value,key=value1");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testValidPatternProperties() throws Exception {
        ObjectName name = new ObjectName("domain:*");
        if (!name.isPropertyPattern()) fail("Properties are pattern");
        name = new ObjectName("domain:k=v,*");
        if (!name.isPropertyPattern()) fail("Properties are pattern");
        name = new ObjectName("domain:*,k=v");
        if (!name.isPropertyPattern()) fail("Properties are pattern");
        name = new ObjectName("domain:k=v,*,k1=v1");
        if (!name.isPropertyPattern()) fail("Properties are pattern");
    }

    public void testValidNonPatternProperties() throws Exception {
        ObjectName name = new ObjectName("domain:k=v");
        if (name.isPropertyPattern()) fail("Properties are not pattern");
        name = new ObjectName("domain:k=v, k1=v1");
        if (name.isPropertyPattern()) fail("Properties are not pattern");
        name = new ObjectName("domain:k=\"\\*\"");
        if (name.isPropertyPattern()) fail("Properties are not pattern");
        name = new ObjectName("domain:k=\",\\*\"");
        if (name.isPropertyPattern()) fail("Properties are not pattern");
    }

    public void testInvalidValue() throws Exception {
        try {
            new ObjectName("domain:key=newLinePresent" + '\n');
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\"quotedNewLinePresent" + '\n' + "\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\" ");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\"unterminatedQuote");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\"\\\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain:key=\"unterminatedQuote\\\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testEmptyValue() throws Exception {
        try {
            new ObjectName("domain:key=");
            fail("Expecting a MalformedObjectNameException");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testInvalidUnquotedValue() throws Exception {
        try {
            new ObjectName("invalidValueChar:k=,");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidValueChar:k=v=");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidValueChar:k=v:");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidValueChar:k=v\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidValueChar:k=v*");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidValueChar:k=v?");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testInvalidQuotedValue() throws Exception {
        try {
            new ObjectName("invalidQuotedValueChar:k=\"v?\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidQuotedValueChar:k=\"v*\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidQuotedValueChar:evenNumberOfBackslashes=\"v" + '\\' + '\\' + "*\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("garbage:afterQuoted=\"value\"garbage");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidEscapedChar:k=\"\\x\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidEscapedChar:k=\"\\\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidEscapedChar:k=\"\\\\\\\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("invalidEscapedChar:k=\"value\\\"");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
        try {
            new ObjectName("domain", "x", "\"unterminated");
            fail("Wrong ObjectName");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testValidQuotedObjectName() throws Exception {
        ObjectName name = new ObjectName("domain:key=\"\"");
        name = new ObjectName("domain:key=\"\\\\\"");
        name = new ObjectName("domain:key=\":\"");
        name = new ObjectName("domain:key=\",\"");
        name = new ObjectName("domain:key=\"=\"");
        name = new ObjectName("domain:key=\"\\\"\"");
        name = new ObjectName("domain:key=\"\\*\"");
        name = new ObjectName("domain:key=\"\\?\"");
        name = new ObjectName("domain:key1=\"v1,v2\",key2=value2");
        if (name.getKeyPropertyList().size() != 2) fail("Too many properties");
        name = new ObjectName("domain:key1=\"k1=v1,k2=v2\", key2= value2");
        if (name.getKeyPropertyList().size() != 2) fail("Too many properties");
        name = new ObjectName("domain:key1=\"v1,\\*,v2\",*,key2=value2");
        if (!name.isPropertyPattern()) fail("ObjectName is property pattern");
        if (name.getKeyPropertyList().size() != 2) fail("Too many properties");
    }

    public void testValidObjectNameWithSpaces() throws Exception {
        String key = " key ";
        String value = " value ";
        ObjectName name = new ObjectName("domain:" + key + "=" + value);
        String val = name.getKeyProperty(key.trim());
        if (val != null) fail("Key is not present");
        val = name.getKeyProperty(key);
        if (!value.equals(val)) fail("Wrong value");
    }

    public void testValidObjectNames() throws Exception {
        ObjectName name = new ObjectName("domain:property1=value1,property2=value2");
        if (name.getKeyPropertyList().size() != 2) fail("Wrong properties number");
        name = new ObjectName("*:*");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (!name.isDomainPattern()) fail("ObjectName is a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 0) fail("Wrong properties number");
        if (name.getKeyPropertyListString().length() != 0) fail("Wrong properties string");
        if (name.getCanonicalKeyPropertyListString().length() != 0) fail("Wrong properties string");
        name = new ObjectName("");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (!name.isDomainPattern()) fail("ObjectName is a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 0) fail("Wrong properties number");
        if (name.getKeyPropertyListString().length() != 0) fail("Wrong properties string");
        if (name.getCanonicalKeyPropertyListString().length() != 0) fail("Wrong properties string");
        name = new ObjectName(":*");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (name.isDomainPattern()) fail("ObjectName is not a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 0) fail("Wrong properties number");
        if (name.getKeyPropertyListString().length() != 0) fail("Wrong properties string");
        if (name.getCanonicalKeyPropertyListString().length() != 0) fail("Wrong properties string");
        name = new ObjectName(":*,property=value");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 1) fail("Wrong properties number");
        if (!"property=value".equals(name.getKeyPropertyListString())) fail("Wrong properties string");
        name = new ObjectName(":property=value,*");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 1) fail("Wrong properties number");
        if (!"property=value".equals(name.getKeyPropertyListString())) fail("Wrong properties string");
        name = new ObjectName(":property2=value2,*,property1=value1");
        if (!name.isPattern()) fail("ObjectName is a pattern");
        if (!name.isPropertyPattern()) fail("ObjectName is a pattern");
        if (name.getKeyPropertyList().size() != 2) fail("Wrong properties number");
        if (!"property2=value2,property1=value1".equals(name.getKeyPropertyListString())) fail("Wrong properties string");
        if (!"property1=value1,property2=value2".equals(name.getCanonicalKeyPropertyListString())) fail("Wrong properties string");
        name = new ObjectName("*uu*:*");
        if (!name.isDomainPattern()) fail("ObjectName is a domain pattern");
        name = new ObjectName("*domain:property=value,*");
        if (!name.isDomainPattern()) fail("ObjectName is a domain pattern");
        name = new ObjectName("??Domain:*");
        if (!name.isDomainPattern()) fail("ObjectName is a domain pattern");
        name = new ObjectName("JMImplementation:type=MBeanServerDelegate");
        if (name.isPattern()) fail("ObjectName is not a pattern");
        name = new ObjectName("domain", "key", "value");
        if (name.isPattern()) fail("ObjectName is not a pattern");
        if (name.isPropertyPattern()) fail("ObjectName is not a pattern");
        if (name.getKeyPropertyList().size() != 1) fail("Wrong properties number");
    }

    public void testProperties() throws Exception {
        String properties = "b=1,a=2,d=0,c=3,aa=4";
        String canonicals = "a=2,aa=4,b=1,c=3,d=0";
        ObjectName name = new ObjectName(":" + properties);
        assertEquals(properties, name.getKeyPropertyListString());
        assertEquals(canonicals, name.getCanonicalKeyPropertyListString());
        properties = "b=1,a=\"\\*2\",d=0,c=3,aa=4";
        canonicals = "a=\"\\*2\",aa=4,b=1,c=3,d=0";
        name = new ObjectName(":" + properties);
        assertEquals(properties, name.getKeyPropertyListString());
        assertEquals(canonicals, name.getCanonicalKeyPropertyListString());
        name = new ObjectName(":b=1,a=\"\\*2\",d=0,*,c=3,aa=4");
        assertEquals(properties, name.getKeyPropertyListString());
        assertEquals(canonicals, name.getCanonicalKeyPropertyListString());
    }

    public void testCanonicalName() throws Exception {
        String origin = "domain:a=1,b=1,c=1,*";
        ObjectName name = new ObjectName(origin);
        String canonical = name.getCanonicalName();
        assertEquals(canonical, origin);
    }

    public void testNullConstructorParameters() throws Exception {
        try {
            new ObjectName(null);
            fail("Expecting a NullPointerException on null 'name'");
        } catch (NullPointerException x) {
        }
        try {
            new ObjectName("domain", null);
            fail("Expecting a NullPointerException on null 'table'");
        } catch (NullPointerException x) {
        }
        try {
            new ObjectName(null, new Hashtable());
            fail("Expecting a NullPointerException on null 'domain'");
        } catch (NullPointerException x) {
        }
        try {
            new ObjectName(null, "key", "value");
            fail("Expecting a NullPointerException on null 'domain'");
        } catch (NullPointerException x) {
        }
        try {
            new ObjectName("domain", null, "value");
            fail("Expecting a NullPointerException on null 'key'");
        } catch (NullPointerException x) {
        }
        try {
            new ObjectName("domain", "key", null);
            fail("Expecting a NullPointerException on null 'value'");
        } catch (NullPointerException x) {
        }
    }

    public void testApply() throws Exception {
        ObjectName notpatone = new ObjectName("names:id=notpatone");
        ObjectName notpattwo = new ObjectName("names:id=notpattwo");
        ObjectName patone = new ObjectName("names:*");
        ObjectName pattwo = new ObjectName("names/patterns:id=pattwo,*");
        assertTrue("Expecting true on notpatone.apply(notpatone)", notpatone.apply(notpatone));
        assertTrue("Expecting true on patone.apply(notpatone)", patone.apply(notpatone));
        assertFalse("Expecting false on notpattwo.apply(notpatone)", notpattwo.apply(notpatone));
        assertFalse("Expecting false on notpat.apply(patone)", notpatone.apply(patone));
        assertFalse("Expecting false on patone.apply(pattwo)", patone.apply(pattwo));
        assertFalse("Expecting false on patone.apply(patone)", patone.apply(patone));
    }

    public void testEmptyHashtable() throws Exception {
        try {
            Hashtable ht = new Hashtable();
            new ObjectName("afinedomain", ht);
            fail("Expecting MalformedObjectNameException");
        } catch (MalformedObjectNameException x) {
        }
    }

    public void testNonStringProperties() throws Exception {
        try {
            Hashtable ht = new Hashtable();
            ht.put("key", new Integer(42));
            new ObjectName("afinedomain", ht);
            fail("Expecting MalformedObjectNameException");
        } catch (MalformedObjectNameException x) {
        }
    }
}
