package koppa.widget.comboBox;

import java.io.IOException;
import java.util.Map;
import junit.framework.TestCase;
import koppa.internal.test.WidgetTestUtil;
import koppa.widget.RenderException;
import koppa.widget.WidgetModule;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ComboBoxBuilderTest extends TestCase {

    private Map<String, String> contextMap;

    protected void setUp() throws Exception {
        super.setUp();
        contextMap = WidgetTestUtil.initContextMapWithGroovy("koppa/widget/comboBox/comboBox.groovy");
    }

    public void testBuild() throws DocumentException, IOException {
        Document document = WidgetTestUtil.parseXulFileToDocument("koppa/widget/comboBox/comboBox.opal.xml");
        Element rootElement = document.getRootElement();
        Element comboBoxElement = (Element) rootElement.elements().get(0);
        Injector injector = Guice.createInjector(new WidgetModule());
        ComboBoxBuilder builder = injector.getInstance(ComboBoxBuilder.class);
        ComboBox comboBox = (ComboBox) builder.build(comboBoxElement);
        assertNotNull(comboBox);
        assertEquals("comboBox1", comboBox.getMetaData().getId());
    }

    public void testRender() throws RenderException {
        Injector injector = Guice.createInjector(new WidgetModule());
        ComboBox comboBox = injector.getInstance(ComboBox.class);
        assertNotNull(comboBox);
        ComboBoxMetaData metaData = comboBox.getMetaData();
        metaData.setId("comboBox1");
        metaData.getAttribute().setCssClass("comboBoxClass");
        metaData.getBindingExp().setValue("student.gender");
        metaData.getBindingExp().setOptions("genders");
        metaData.getBindingExp().setLabelKey("label");
        metaData.getBindingExp().setValueKey("value");
        String expectedCode = contextMap.get("expectedCode").replaceAll("\\s*", "");
        String actualCode = comboBox.render().replaceAll("\\s*", "");
        assertEquals(expectedCode, actualCode);
    }
}
