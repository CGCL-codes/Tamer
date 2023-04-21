package net.sf.csutils.groovy.xml;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class XmlTemplateEngineTest {

    @Test
    public void testBasicTemplate() throws Exception {
        final String template = "<abc xmlns='asjhas'>" + "  <n:def xmlns:n='sdlkj'>Okay</n:def>" + "</abc>";
        final StringWriter sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template).make().writeTo(sw);
        Assert.assertEquals(template, sw.toString().replace('\"', '\''));
    }

    @Test
    public void testInterpolation() throws Exception {
        final String template = "<abc xmlns='as${xy}as'>" + "  <n:def xmlns:n='sdlkj'>${okay}</n:def>" + "</abc>";
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("okay", "Okay");
        values.put("xy", "3");
        final StringWriter sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template).make(values).writeTo(sw);
        final String expect = template.replace("${xy}", "3").replace("${okay}", "Okay");
        Assert.assertEquals(expect, sw.toString().replace('\"', '\''));
    }

    @Test
    public void testScriptlet() throws Exception {
        final String template = "<abc xmlns='as${xy}as' xmlns:gsp='http://namespaces.csutils.sf.net/groovy/xml/XmlTemplateEngine'>" + "  <gsp:scriptlet>if(b) {</gsp:scriptlet><n:def xmlns:n='sdlkj'>${okay}</n:def><gsp:scriptlet>}</gsp:scriptlet>" + "</abc>";
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("okay", "Okay");
        values.put("xy", "3");
        values.put("b", Boolean.TRUE);
        StringWriter sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template).make(values).writeTo(sw);
        Assert.assertEquals("<abc xmlns='as3as'>  <n:def xmlns:n='sdlkj'>Okay</n:def></abc>", sw.toString().replace('\"', '\''));
        values.put("b", Boolean.FALSE);
        sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template).make(values).writeTo(sw);
        Assert.assertEquals("<abc xmlns='as3as'>  </abc>", sw.toString().replace('\"', '\''));
    }

    @Test
    public void testElement() throws Exception {
        final String template1 = "<abc xmlns='as${xy}as' xmlns:gsp='http://namespaces.csutils.sf.net/groovy/xml/XmlTemplateEngine'>" + "  <gsp:element xmlns:n='sdlkj' gsp:name='n:foo'><n:def>${okay}</n:def></gsp:element>" + "</abc>";
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("okay", "Okay");
        values.put("xy", "3");
        StringWriter sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template1).make(values).writeTo(sw);
        Assert.assertEquals("<abc xmlns='as3as'>  <n:foo xmlns:n='sdlkj'><n:def>Okay</n:def></n:foo></abc>", sw.toString().replace('\"', '\''));
        final String template2 = "<abc xmlns='as${xy}as' xmlns:gsp='http://namespaces.csutils.sf.net/groovy/xml/XmlTemplateEngine'>" + "  <gsp:element xmlns:n='sdlkj' gsp:name='${b}'><n:def>${okay}</n:def></gsp:element>" + "</abc>";
        values.put("b", "n:foo");
        sw = new StringWriter();
        new XmlTemplateEngine().createTemplate(template2).make(values).writeTo(sw);
        Assert.assertEquals("<abc xmlns='as3as'>  <n:foo xmlns:n='sdlkj'><n:def>Okay</n:def></n:foo></abc>", sw.toString().replace('\"', '\''));
    }
}
