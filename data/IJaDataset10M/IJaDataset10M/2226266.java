package net.sf.xmlunit.transform;

import javax.xml.transform.OutputKeys;
import net.sf.xmlunit.TestResources;
import net.sf.xmlunit.builder.Input;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.hamcrest.core.IsNot.*;
import static org.junit.Assert.*;

public class TransformationTest {

    private Transformation t;

    @Before
    public void createTransformation() {
        t = new Transformation(Input.fromFile(TestResources.DOG_FILE).build());
        t.setStylesheet(Input.fromFile(TestResources.ANIMAL_XSL).build());
    }

    @Test
    public void transformAnimalToString() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><dog/>", t.transformToString());
    }

    @Test
    public void transformAnimalToDocument() {
        Document doc = t.transformToDocument();
        assertEquals("dog", doc.getDocumentElement().getTagName());
    }

    @Test
    public void transformAnimalToHtml() {
        t.addOutputProperty(OutputKeys.METHOD, "html");
        assertThat(t.transformToString(), not("<?xml version=\"1.0\" encoding=\"UTF-8\"?><dog/>"));
    }
}
