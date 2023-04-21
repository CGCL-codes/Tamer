package com.luxoft.fitpro.htmleditor.core.templates;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TemplateBuilderFactoryTest {

    private MockFixture mockFixture;

    @Before
    public void setup() {
        mockFixture = new MockFixture();
    }

    @Test
    public void testGetColumnFixtureTemplateBuilder() throws Exception {
        mockFixture.setTypes(MockFixture.Type.COLUMN_FIXTURE);
        MockTemplateBuilderRegistry builderRegistry = new MockTemplateBuilderRegistry();
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.COLUMN_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.ColumnFixtureTemplateBuilder"));
        TemplateBuilderFactory templateFactory = new TemplateBuilderFactory(builderRegistry);
        Assert.assertTrue(templateFactory.getTemplateBuilder(mockFixture) instanceof ColumnFixtureTemplateBuilder);
    }

    @Test
    public void testGetActionFixtureTemplateBuilder() throws Exception {
        mockFixture.setTypes(MockFixture.Type.ACTION_FIXTURE);
        MockTemplateBuilderRegistry builderRegistry = new MockTemplateBuilderRegistry();
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.ACTION_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.ActionFixtureTemplateBuilder"));
        TemplateBuilderFactory templateFactory = new TemplateBuilderFactory(builderRegistry);
        Assert.assertTrue(templateFactory.getTemplateBuilder(mockFixture) instanceof ActionFixtureTemplateBuilder);
    }

    @Test
    public void testGetRowFixtureTemplateBuilder() throws Exception {
        mockFixture.setTypes(MockFixture.Type.ROW_FIXTURE, MockFixture.Type.COLUMN_FIXTURE);
        MockTemplateBuilderRegistry builderRegistry = new MockTemplateBuilderRegistry();
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.ROW_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.RowFixtureTemplateBuilder"));
        TemplateBuilderFactory templateFactory = new TemplateBuilderFactory(builderRegistry);
        Assert.assertTrue(templateFactory.getTemplateBuilder(mockFixture) instanceof RowFixtureTemplateBuilder);
    }

    @Test(expected = TemplateBuilderException.class)
    public void testGetFixtureTemplateBuilderForInvalidFixture() throws Exception {
        mockFixture.setTypes(MockFixture.Type.SETUP_FIXTURE, MockFixture.Type.SETUP_FIXTURE);
        MockTemplateBuilderRegistry builderRegistry = new MockTemplateBuilderRegistry();
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.ROW_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.RowFixtureTemplateBuilder"));
        TemplateBuilderFactory templateFactory = new TemplateBuilderFactory(builderRegistry);
        templateFactory.getTemplateBuilder(mockFixture);
    }

    @Test
    public void testGetMatchingBuilderExtensionElement() throws Exception {
        mockFixture.setTypes(MockFixture.Type.ROW_FIXTURE);
        MockTemplateBuilderRegistry builderRegistry = new MockTemplateBuilderRegistry();
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.COLUMN_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.ColumnFixtureTemplateBuilder"));
        builderRegistry.addElement(new MockTemplateBuilderExtensionElement(MockFixture.Type.ROW_FIXTURE.toString(), "com.luxoft.fitpro.htmleditor.core.templates.RowFixtureTemplateBuilder"));
        TemplateBuilderFactory templateFactory = new TemplateBuilderFactory(builderRegistry);
        Assert.assertTrue(templateFactory.getTemplateBuilder(mockFixture) instanceof RowFixtureTemplateBuilder);
    }
}
