package org.xmlcml.cml.element.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import nu.xom.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.element.CMLBuilder;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.element.CMLUnit;
import org.xmlcml.cml.element.CMLUnitList;
import org.xmlcml.cml.element.NamespaceToUnitListMap;
import org.xmlcml.cml.element.UnitAttribute;
import org.xmlcml.cml.element.UnitTypeAttribute;

/**
 * attribute supporting unitType.
 *
 * @author pmr
 *
 */
public class UnitTypeAttributeTest extends AbstractTest {

    CMLUnitList unitList = null;

    String unitTypeS = "<c:cml " + "  type='unit'" + "xmlns:c='" + CML_NS + "' " + "xmlns:units='" + UNIT_NS + "' " + "xmlns:unitsComp='http://www.xml-cml.org/units/comp'>" + "<c:scalar id='s1' dictRef='cmlDict:angle' units='" + U_DEGREE + "'>123</c:scalar>" + "<c:scalar id='s2' dictRef='foo:bar' units='units:foo'>456</c:scalar>" + "<c:scalar id='s3' dictRef='cmlComp:ionPot' units='units:volt'>123</c:scalar>" + "</c:cml>";

    String unitTypeNS = "<unitList " + "  type='unit'" + "  xmlns='" + CML_NS + "' " + "  xmlns:unitType='" + UNITTYPES_NS + "'>" + "  <unit id='mm' name='millimetre' unitType='unitType:length'>" + "    <description>omitted</description>" + "  </unit>" + "  <unit id='g' name='gram' unitType='unitType:mass'>" + "    <description>omitted</description>" + "  </unit>" + "</unitList>";

    /**
	 * setup.
	 *
	 * @throws Exception
	 */
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * get prefix and namespace. use namespace declaration in instance file.
	 *
	 */
    @Test
    public void testGetPrefixAndNamespaceURI() {
        unitList = (CMLUnitList) parseValidString(unitTypeNS);
        Assert.assertNotNull("unitList ", unitList);
        Assert.assertEquals("type ", "unit", unitList.getType());
        List<CMLElement> children = unitList.getChildCMLElements();
        CMLUnit unit0 = (CMLUnit) children.get(0);
        UnitTypeAttribute unitType0 = (UnitTypeAttribute) unit0.getUnitTypeAttribute();
        String prefix0 = unitType0.getPrefix();
        Assert.assertEquals("prefix ", "unitType", prefix0);
        String namespace0 = unitType0.getNamespaceURIString();
        Assert.assertEquals("namespaceURI ", UNITTYPES_NS, namespace0);
        String unitTypeId0 = unitType0.getIdRef();
        Assert.assertNotNull("namespace ", unitTypeId0);
        Assert.assertEquals("unitType ", "length", unitTypeId0);
        CMLUnit unit1 = (CMLUnit) children.get(1);
        UnitTypeAttribute unitType1 = (UnitTypeAttribute) unit1.getUnitTypeAttribute();
        String prefix1 = unitType1.getPrefix();
        Assert.assertEquals("prefix ", "unitType", prefix1);
        String namespace1 = unitType1.getNamespaceURIString();
        Assert.assertNotNull("namespace not null", namespace1);
    }

    /**
	 * find dictionaries relevant to document or element.
	 * @throws IOException
	 */
    @Test
    public void testGetDictionaries() throws IOException {
        try {
            unitList = (CMLUnitList) new CMLBuilder().build(new StringReader(unitTypeNS)).getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("should not throw " + e);
        }
        Assert.assertNotNull("unitList ", unitList);
        Assert.assertEquals("type ", "unit", unitList.getType());
        List<CMLElement> children = unitList.getChildCMLElements();
        CMLUnit unit0 = (CMLUnit) children.get(0);
        UnitTypeAttribute unitType0 = (UnitTypeAttribute) unit0.getUnitTypeAttribute();
        String prefix0 = unitType0.getPrefix();
        Assert.assertEquals("prefix ", "unitType", prefix0);
        String namespace0 = unitType0.getNamespaceURIString();
        Assert.assertEquals("namespaceURI ", UNITTYPES_NS, namespace0);
        String unitTypeId0 = unitType0.getIdRef();
        Assert.assertNotNull("namespace ", unitTypeId0);
        Assert.assertEquals("unitType ", "length", unitTypeId0);
        NamespaceToUnitListMap unitListMap = null;
        unitListMap = new NamespaceToUnitListMap(CMLUtil.getResource(UNIT_RESOURCE + U_S + CATALOG_XML));
        Assert.assertEquals("unitList count", NUNIT_DICT, unitListMap.size());
    }

    /**
	 * large test example.
	 *
	 * @param filename
	 * @param ndict
     * @exception Exception
	 */
    public void largeExample(String filename, int ndict) throws Exception {
        int NERR = 20;
        NamespaceToUnitListMap unitsUnitListMap = null;
        unitsUnitListMap = new NamespaceToUnitListMap(CMLUtil.getResource(UNIT_RESOURCE + U_S + CATALOG_XML));
        CMLCml cml = null;
        InputStream in = CMLUtil.getInputStreamFromResource(COMPLEX_RESOURCE + filename + ".xml");
        cml = (CMLCml) new CMLBuilder().build(in).getRootElement();
        in.close();
        int namespaceCount = cml.getNamespaceDeclarationCount();
        Assert.assertEquals("namespaces ", ndict, namespaceCount);
        List<CMLElement> scalars = cml.getElements(".//cml:scalar");
        int count = 0;
        for (CMLElement scalar : scalars) {
            UnitAttribute unitsAttribute = (UnitAttribute) ((CMLScalar) scalar).getUnitsAttribute();
            if (unitsAttribute == null) {
                unitsAttribute = (UnitAttribute) ((Element) scalar.getParent()).getAttribute("units");
            }
            if (unitsAttribute == null) {
                Assert.fail("Missing dictRef");
            } else {
                CMLUnit unit = unitsUnitListMap.getUnit(unitsAttribute);
                if (unit == null && count++ < NERR) {
                    Assert.fail("Missing units");
                } else {
                }
            }
        }
    }
}
