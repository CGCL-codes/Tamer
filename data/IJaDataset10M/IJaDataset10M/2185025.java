package org.xmlcml.cml.element;

import java.io.InputStream;
import junit.framework.Assert;
import nu.xom.NodeFactory;
import org.junit.Test;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLNodeFactory;
import org.xmlcml.euclid.Util;

/** test CMLBuilder routines.
 * 
 * @author pm286
 *
 */
public class CMLBuilderTest extends AbstractTest implements CMLConstants {

    /** Test method for 'org.xmlcml.cml.element.CMLBuilder.CMLBuilder(boolean, NodeFactory)'
     */
    @Test
    public void testCMLBuilderBooleanNodeFactory() {
        String s = S_EMPTY + "<cml " + CML_XMLNS + ">" + "<inchi/>" + "</cml>";
        try {
            new CMLBuilder().parseString(s);
            Assert.fail("should throw: Unknown CML element: inchi");
        } catch (Exception e) {
            Assert.assertEquals("OldNodeFactory validation", "Unknown CML Element : inchi", e.getMessage());
        }
        builder = new CMLBuilder(new NodeFactory());
        try {
            builder.parseString(s);
        } catch (Exception e) {
            neverThrow(e);
        }
        boolean validate = true;
        builder = new CMLBuilder(validate, new NodeFactory());
        try {
            builder.parseString(s);
            Assert.fail("should throw: Unknown CML element: inchi");
        } catch (Exception e) {
            Assert.assertEquals("OldNodeFactory validation", "Document root element \"cml\", must match DOCTYPE root \"null\".", e.getMessage());
        }
        validate = true;
        builder = new CMLBuilder(validate, CMLNodeFactory.nodeFactory);
        try {
            builder.parseString(s);
            Assert.fail("should throw: Unknown CML element: inchi");
        } catch (Exception e) {
            Assert.assertEquals("OldNodeFactory validation", "Unknown CML Element : inchi", e.getMessage());
        }
    }

    /** Test method for 'org.xmlcml.cml.element.CMLBuilder.parseString(String)'
     */
    @Test
    public void testParseString() {
        String s = S_EMPTY + "<cml " + CML_XMLNS + ">" + "</cml>";
        CMLBuilder builder = new CMLBuilder();
        try {
            builder.parseString(s);
        } catch (Exception e) {
            neverThrow(e);
        }
        s = S_EMPTY + "<cml " + CML_XMLNS + ">" + "<inchi/>" + "</cml>";
        builder = new CMLBuilder();
        try {
            builder.parseString(s);
        } catch (Exception e) {
            Assert.assertEquals("OldNodeFactory validation", "Unknown CML Element : inchi", e.getMessage());
        }
        s = "<!DOCTYPE cml [" + "<!ELEMENT cml ANY>" + "<!ATTLIST cml xmlns CDATA #REQUIRED>" + "]>" + "<cml " + CML_XMLNS + ">" + "<inchi/>" + "</cml>";
        boolean validate = true;
        builder = new CMLBuilder(validate, new NodeFactory());
        try {
            builder.parseString(s);
            Assert.fail("Should throw: Element type \"inchi\" must be declared.");
        } catch (Exception e) {
            Assert.assertEquals("missing element in doctype", "Element type \"inchi\" must be declared.", e.getMessage());
        }
    }

    /** Test method for 'org.xmlcml.cml.element.CMLBuilder.build(File)'
     */
    @Test
    public void testBuildFile() {
        CMLBuilder builder = new CMLBuilder();
        InputStream in = null;
        try {
            in = Util.getInputStreamFromResource(COMPLEX_RESOURCE + U_S + "castep2.xml");
            builder.build(in);
            in.close();
        } catch (Exception e) {
            neverThrow(e);
        }
        boolean validate = true;
        builder = new CMLBuilder(validate);
        try {
            in = Util.getInputStreamFromResource(COMPLEX_RESOURCE + U_S + "castep2.xml");
            builder.build(in);
            in.close();
        } catch (Exception e) {
            Assert.assertEquals("OldNodeFactory validation", "Document root element \"cml\", must match DOCTYPE root \"null\".", e.getMessage());
        }
    }
}
