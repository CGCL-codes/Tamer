package br.ita.autowidget.defaultgeneration.templates.html;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import br.ita.autowidget.annotations.ComponentName;
import br.ita.autowidget.defaultgeneration.templates.html.HtmlEmailAddressTemplate;
import br.ita.autowidget.defaultgeneration.templates.html.HtmlTextAreaTemplate;
import br.ita.autowidget.metadata.ComponentMetadata;
import br.ita.autowidget.metadata.ComponentMetadataReader;

public class HtmlTextAreaTemplateTest {

    private HtmlTextAreaTemplate template;

    private ComponentMetadataReader metadataReader = ComponentMetadataReader.getInstance();

    @Before
    public void setup() {
        template = new HtmlTextAreaTemplate();
    }

    @Test
    public void display() {
        ComponentMetadata beanMetadata = metadataReader.getMetadataContainer("This is an example with many characters that will ensure that the " + "text area field is going to suport it anyhow");
        String displayHtml = template.display(beanMetadata);
        assertEquals("Display String Generation Error!", "This is an example with many characters that will ensure that the " + "text area field is going to suport it anyhow", displayHtml);
    }

    @Test
    public void displayNull() {
        ComponentMetadata beanMetadata = metadataReader.getMetadataContainer(new TestBean());
        String displayHtml = template.display(beanMetadata.getProperties().get(0));
        assertEquals("Display String Generation Error!", "&nbsp", displayHtml);
    }

    @Test
    public void editExistingString() {
        ComponentMetadata beanMetadata = metadataReader.getMetadataContainer("This is an example with many characters that will ensure that the " + "text area field is going to suport it anyhow");
        String editHtml = template.edit(beanMetadata);
        assertEquals("Edit String Generation Error!", "<input class=\"text-box multi-line\" type=\"textarea\" name=\"String\" " + "value=\"This is an example with many characters that will ensure that the " + "text area field is going to suport it anyhow\"/>", editHtml);
    }

    @Test
    public void editEmptyString() {
        ComponentMetadata beanMetadata = metadataReader.getMetadataContainer(new TestBean());
        String editHtml = template.edit(beanMetadata.getProperties().get(0));
        assertEquals("Edit String Generation Error!", "<input class=\"text-box multi-line\" type=\"textarea\" name=\"TestBean_stringAttribute\"/>", editHtml);
    }

    private class TestBean {

        @ComponentName("EmailAddress")
        private String stringAttribute;
    }
}
