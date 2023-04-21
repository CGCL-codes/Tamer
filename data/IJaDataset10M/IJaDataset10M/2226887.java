package br.ita.autowidget.defaultgeneration.templates.html;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import br.ita.autowidget.defaultgeneration.templates.html.HtmlDoubleTemplate;
import br.ita.autowidget.metadata.ComponentMetadata;
import br.ita.autowidget.metadata.ComponentMetadataReader;

public class HtmlDoubleTemplateTest {

    private HtmlDoubleTemplate template;

    private ComponentMetadataReader metadataReader = ComponentMetadataReader.getInstance();

    @Before
    public void setup() {
        template = new HtmlDoubleTemplate();
    }

    @Test
    public void display() {
        ComponentMetadata doubleMetadata = metadataReader.getMetadataContainer(new Double(10.3d));
        String displayHtml = template.display(doubleMetadata);
        assertEquals("Display Double Generation Error!", "10,30", displayHtml);
        ComponentMetadata floatMetadata = metadataReader.getMetadataContainer(new Float(10.3f));
        displayHtml = template.display(floatMetadata);
        assertEquals("Display Double Generation Error!", "10,30", displayHtml);
    }

    @Test
    public void displayNull() {
        ComponentMetadata testMetadata = metadataReader.getMetadataContainer(new TestBean());
        String displayHtml = template.display(testMetadata.getProperties().get(0));
        assertEquals("Edit Double Generation Error!", "&nbsp", displayHtml);
        displayHtml = template.display(testMetadata.getProperties().get(1));
        assertEquals("Edit Double Generation Error!", "&nbsp", displayHtml);
    }

    @Test
    public void editExistingDate() {
        ComponentMetadata doubleMetadata = metadataReader.getMetadataContainer(new Double(10.3d));
        String editHtml = template.edit(doubleMetadata);
        assertEquals("Edit Double Generation Error!", "<input class=\"text-box number\" type=\"text\" name=\"Double\" value=\"10.3\"/>", editHtml);
        ComponentMetadata floatMetadata = metadataReader.getMetadataContainer(new Float(10.3f));
        editHtml = template.edit(floatMetadata);
        assertEquals("Display Double Generation Error!", "<input class=\"text-box number\" type=\"text\" name=\"Float\" value=\"10.3\"/>", editHtml);
    }

    @Test
    public void editEmptyDate() {
        ComponentMetadata testMetadata = metadataReader.getMetadataContainer(new TestBean());
        String editHtml = template.edit(testMetadata.getProperties().get(0));
        assertEquals("Edit Double Generation Error!", "<input class=\"text-box number\" type=\"text\" name=\"TestBean_doubleNumber\"/>", editHtml);
        editHtml = template.edit(testMetadata.getProperties().get(1));
        assertEquals("Edit Double Generation Error!", "<input class=\"text-box number\" type=\"text\" name=\"TestBean_floatNumber\"/>", editHtml);
    }

    private class TestBean {

        Double doubleNumber;

        Float floatNumber;
    }
}
