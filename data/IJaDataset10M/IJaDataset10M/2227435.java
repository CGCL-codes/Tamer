package com.sitescape.team.module.definition.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static com.sitescape.team.util.Maybe.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.sitescape.team.domain.Definition;
import com.sitescape.team.domain.Workspace;
import com.sitescape.team.module.definition.DefinitionModule;
import com.sitescape.team.support.AbstractTestBase;
import com.sitescape.team.util.CollectionUtil.Func1;

public class DefinitionModuleImplTest extends AbstractTestBase {

    private final String name = RandomStringUtils.randomAlphabetic(16);

    private final String schemaLoc = "xsi:schemaLocation=\"http://www.icecore.org/definition-0.1\n" + "definition_builder_config-0.1.xsd\"\n";

    @Autowired
    private DefinitionModule definitions;

    @Test
    public void addDefinitionNoName() throws Exception {
        defaultRequestContext();
        Definition d = definitions.addDefinition(IOUtils.toInputStream(getXml()), true);
        assertNotNull(d);
        assertEquals(name, d.getName());
        assertEquals(null, d.getBinderId());
    }

    @Test
    public void addDefinitionWithBinderNoName() throws Exception {
        Workspace b = defaultRequestContext().getZone();
        Definition d = definitions.addDefinition(IOUtils.toInputStream(getXml()), b, true);
        assertNotNull(d);
        assertEquals(name, d.getName());
        assertEquals(b.getId(), d.getBinderId());
    }

    @Test
    public void getDefinitionByNameNoBinder() throws Exception {
        definitions.addDefinition(IOUtils.toInputStream(getXml()), true);
        Definition d = definitions.getDefinitionByName(name);
        assertNotNull(d);
        assertEquals(name, d.getName());
    }

    @Test
    public void getDefinitionAsXmlHasSchemaLocation() throws Exception {
        definitions.addDefinition(IOUtils.toInputStream(getXmlNoSchema()), true);
        Definition d = definitions.getDefinitionByName(name);
        QName schemaLocAttr = new QName("schemaLocation", new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        assertFalse(maybe(d.getDefinition().getRootElement().attributeValue(schemaLocAttr)).and(new Func1<String, Boolean>() {

            public Boolean apply(String x) {
                return x.contains("http://www.icecore.org/definition");
            }
        }, false));
        assertTrue(maybe(definitions.getDefinitionAsXml(d).getRootElement().attributeValue(schemaLocAttr)).and(new Func1<String, Boolean>() {

            public Boolean apply(String x) {
                return x.matches("http://www.icecore.org/definition(-[0-9\\.]*)?\\s+http://www.icecore.org/definition(-[0-9\\.]*)?");
            }
        }, false));
    }

    @Test
    public void getDefinitionAsXmlDoNotDupeSchemaLoc() throws Exception {
        definitions.addDefinition(IOUtils.toInputStream(getXml()), true);
        Definition d = definitions.getDefinitionByName(name);
        QName schemaLocAttr = new QName("schemaLocation", new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        assertTrue(maybe(definitions.getDefinitionAsXml(d).getRootElement().attributeValue(schemaLocAttr)).and(new Func1<String, Boolean>() {

            public Boolean apply(String x) {
                return x.matches("http://www.icecore.org/definition(-[0-9\\.]*)?\\s+http://www.icecore.org/definition(-[0-9\\.]*)?");
            }
        }, false));
    }

    private String getXml() {
        return xmlWithSchemaLocation(schemaLoc);
    }

    private String getXmlNoSchema() {
        return xmlWithSchemaLocation("");
    }

    private String xmlWithSchemaLocation(String schema) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<definition xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + schema + "name=\"" + name + "\"\n" + "caption=\"" + name + "\"\n" + "type=\"1\"\n" + "definitionType=\"1\">\n" + "<item />\n" + "</definition>";
    }
}
