package net.israfil.markup.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import net.israfil.markup.model.Component;
import net.israfil.markup.model.Markup;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class JAXBLoadTest {

    public void testLoadingMarkupFile() throws Throwable {
        Markup model = new JAXBHelperImpl().parse(Thread.currentThread().getContextClassLoader().getResource("testMarkup.markup.xml"), "net.israfil.markup.model");
        Assert.assertNotNull(model);
        Assert.assertNotNull(model.getView());
        Assert.assertEquals(1, model.getView().getComponent().size());
        Component component = model.getView().getComponent().get(0);
        Assert.assertEquals("java.awt.GridBagLayout", component.getLayout().getType());
        Assert.assertNotNull(component.getComponent().get(0));
        Assert.assertEquals(2, component.getComponent().size());
        Assert.assertNotNull("javax.swing.JTextField", component.getComponent().get(0).getType());
        Assert.assertNotNull("javax.swing.JTextField", component.getComponent().get(1).getType());
    }
}
