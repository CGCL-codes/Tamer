package integration;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.xisemele.api.Editor;
import net.sf.xisemele.api.Element;
import net.sf.xisemele.api.Formatter;
import net.sf.xisemele.impl.XisemeleFactory;
import org.junit.Test;

/**
 * Casos de teste de integração da funcionalidade de edição de XML da API Xisemele.
 * 
 * @author Carlos Eduardo Coral.
 */
public class EditorTest {

    /**
    * Testa a edição de nome de elementos.
    */
    @Test
    public void testRename() {
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a/>" + "<b>" + "<c/>" + "</b>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rootElement>" + "<nodeA/>" + "<nodeB>" + "<nodeC/>" + "</nodeB>" + "</rootElement>";
        Editor editor = XisemeleFactory.newXisemele().createEditor(original).defineAsCurrent("root").rename("rootElement").defineAsCurrent("rootElement/a").rename("nodeA").defineAsCurrent("rootElement/b").rename("nodeB").defineAsCurrent("rootElement/nodeB/c").rename("nodeC");
        assertEquals(expected, editor.result().toXML());
    }

    /**
    * Testa a remoção de elementos e atributos de determinado documento XML.
    */
    @Test
    public void testRemove() {
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a/>" + "<b attr1=\"value1\" attr2=\"value2\">" + "<c/>" + "<d>value1</d>" + "<d>value2</d>" + "<d>value3</d>" + "</b>" + "<e attr3=\"value3\" attr4=\"value4\">" + "<f/>" + "<g/>" + "</e>" + "<h>" + "<i/>" + "<j/>" + "</h>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<b attr2=\"value2\">" + "<c/>" + "</b>" + "<e>" + "<f/>" + "</e>" + "<h/>" + "</root>";
        Editor editor = XisemeleFactory.newXisemele().createEditor(original).defineAsCurrent("root/a").remove().defineAsCurrent("root/b").removeChildren("d").removeAttribute("attr1").defineAsCurrent("root/e").removeChild("g").removeAttributes().defineAsCurrent("root/h").removeChildren();
        assertEquals(expected, editor.result().toXML());
    }

    /**
    * Testa a edição de valor de elementos de determinado documento XML.
    */
    @Test
    public void testValue() throws Exception {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2009");
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a>old value</a>" + "<b>12/10/1979</b>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a>new value</a>" + "<b>06/06/2009</b>" + "</root>";
        Editor editor = XisemeleFactory.newXisemele().createEditor(original).defineAsCurrent("root/a").value("new value").defineAsCurrent("root/b").value(date, "dd/MM/yyyy");
        assertEquals(expected, editor.result().toXML());
    }

    /**
    * Testa a edição de atributos em determinado documento XML.
    */
    @Test
    public void testAttribute() throws Exception {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2009");
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a attr1=\"old value\"/>" + "<b/>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root attrRoot=\"value\">" + "<a attr1=\"new value\"/>" + "<b date=\"06/06/2009\"/>" + "</root>";
        Editor editor = XisemeleFactory.newXisemele().createEditor(original).attribute("attrRoot", "value").defineAsCurrent("root/a").attribute("attr1", "new value").defineAsCurrent("root/b").attribute("date", date, "dd/MM/yyyy");
        assertEquals(expected, editor.result().toXML());
    }

    /**
    * Testa a edição de elementos de determinado documento XML.
    */
    @Test
    public void testElements() {
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a/>" + "<b/>" + "</root>";
        String other = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<j/>" + "<k/>" + "<l>" + "<m/>" + "<n>" + "<o/>" + "</n>" + "</l>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a>" + "<c>" + "<d/>" + "<e/>" + "</c>" + "</a>" + "<b/>" + "<f>" + "<l>" + "<m/>" + "<n>" + "<o/>" + "</n>" + "</l>" + "</f>" + "</root>";
        Element element = XisemeleFactory.newXisemele().createReader(other).find("root/l");
        Editor editor = XisemeleFactory.newXisemele().createEditor(original).within().element("f").within().element(element).endWithin().endWithin().defineAsCurrent("root/a").within().element("c").within().element("d").element("e").endWithin().endWithin();
        assertEquals(expected, editor.result().toXML());
    }

    /**
    * Testa o método {@link Editor#containsElement(String)}.
    */
    @Test
    public void testContainsElement() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a>" + "<c/>" + "</a>" + "<b/>" + "</root>";
        Editor editor = XisemeleFactory.newXisemele().createEditor(xml);
        assertTrue(editor.containsElement("root"));
        assertTrue(editor.containsElement("root/a"));
        assertTrue(editor.containsElement("root/a/c"));
        assertTrue(editor.containsElement("root/b"));
        assertFalse(editor.containsElement("root/c"));
    }

    /**
    * Testa a formatação de valores para tipos específicos.
    */
    @Test
    @SuppressWarnings("serial")
    public void testFormatter() {
        Formatter<Long> longFormatter = new Formatter<Long>() {

            public String format(Long value) {
                return "formattedLong[" + value.toString() + "]";
            }

            public Long parse(String text) {
                return new Long(text.substring(17, text.length() - 1));
            }

            public Class<Long> type() {
                return Long.class;
            }
        };
        Formatter<Boolean> booleanFormatter = new Formatter<Boolean>() {

            public String format(Boolean value) {
                return "formattedBoolean[" + value.toString() + "]";
            }

            public Boolean parse(String text) {
                return new Boolean(text.substring(17, text.length() - 1));
            }

            public Class<Boolean> type() {
                return Boolean.class;
            }
        };
        String original = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a/>" + "<b/>" + "<c/>" + "<d/>" + "</root>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + "<a>formattedLong[123]</a>" + "<b>formattedBoolean[true]</b>" + "<c long=\"formattedLong[456]\"/>" + "<d boolean=\"formattedBoolean[true]\"/>" + "</root>";
        Editor editor = XisemeleFactory.newXisemele().setFormatter(longFormatter).setFormatter(booleanFormatter).createEditor(original).defineAsCurrent("root/a").value(new Long("123")).defineAsCurrent("root/b").value(Boolean.TRUE).defineAsCurrent("root/c").attribute("long", new Long("456")).defineAsCurrent("root/d").attribute("boolean", Boolean.TRUE);
        assertEquals(expected, editor.result().toXML());
    }
}
