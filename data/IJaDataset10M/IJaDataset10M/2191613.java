package org.springframework.web.servlet.view.json.writer.jsonlib;

import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.view.json.JsonWriterConfiguratorTemplateRegistry;
import org.springframework.web.servlet.view.json.converter.model.Bean;
import org.springframework.web.servlet.view.json.mock.writer.MockBindingResult;
import org.springframework.web.servlet.view.json.mock.writer.sojo.MockSimpleSojoJsonWriterConfiguratorTemplate;
import org.springjson.test.TestBase;

public class SimpleJsonJsonlibStringWriterTest extends TestBase {

    protected static final String COMMAND_BEAN_NAME = "command";

    public static final String JSON_STRING_WRITER_BEAN = "jsonlibJsonWriter";

    public static final String JSON_STRING_WRITER_WITH_JSONLIB_CONFIG_SUPORT_BEAN = "jsonlibJsonWriterJsonConfigSupport";

    protected Date date1 = new GregorianCalendar(2000, Calendar.MARCH, 17).getTime();

    protected Date date2 = new GregorianCalendar(2002, Calendar.SEPTEMBER, 20).getTime();

    protected JsonlibJsonStringWriter jsonStringWriter;

    @After
    public void setUp() throws Exception {
        jsonStringWriter = null;
    }

    @SuppressWarnings("unchecked")
    public Map getModelMap() {
        Map model = new HashMap();
        model.put("date1", date1);
        model.put("date2", date2);
        model.put("number", new Long(1));
        model.put(COMMAND_BEAN_NAME, new Bean("beanstring", new Long(2), date1, date2));
        return model;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertSimpleModel() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":2,\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, null, registry, expected, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertAllSimpleModel() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":2,\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, null, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertSimpleModelWithEmptyBindingResult() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getEmptyBindingResult();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertAllSimpleModelWithEmptyBindingResult() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getEmptyBindingResult();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":\"1\",\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertSimpleModelWithTypedPropertyEditor() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getTypedBindingResult();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"20-09-2002\",\"number\":\"2\",\"date1\":\"17-03-2000\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertAllSimpleModelWithTypedPropertyEditor() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getTypedBindingResult();
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"20-09-2002\",\"number\":\"2\",\"date1\":\"17-03-2000\",\"string\":\"beanstring\"},\"date2\":\"20-09-2002\",\"number\":\"1\",\"date1\":\"17-03-2000\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertSimpleModelWithPathedPropertyEditor() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getBeanPathBindingResult("date1");
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"17-03-2000\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertAllSimpleModelWithPathedPropertyEditor() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getBeanPathBindingResult("date1");
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"17-03-2000\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":\"1\",\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertSimpleModelWithPathedPropertyEditorOutBean() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getBeanPathBindingResult("(date1)");
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":1,\"date1\":\"" + date1 + "\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertAllSimpleModelWithPathedPropertyEditorOutBean() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getBeanPathBindingResult("(date1)");
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"" + date2 + "\",\"number\":\"2\",\"date1\":\"" + date1 + "\",\"string\":\"beanstring\"},\"date2\":\"" + date2 + "\",\"number\":\"1\",\"date1\":\"17-03-2000\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertWithCustomEditorHirachy() {
        jsonStringWriter = (JsonlibJsonStringWriter) beanFactory.getBean(JSON_STRING_WRITER_BEAN);
        Map model = getModelMap();
        MockBindingResult bindingResult = getBeanPathAndTypedBindingResult("(date1)", "date1");
        JsonWriterConfiguratorTemplateRegistry registry = getEmptyConfiguratorTemplateRegistry();
        String expected = "{\"command\":{\"date2\":\"2002\",\"number\":\"2\",\"date1\":\"03\",\"string\":\"beanstring\"},\"date2\":\"2002\",\"number\":\"1\",\"date1\":\"17\"}";
        testJsonStringWriter(model, bindingResult, registry, expected, true);
    }

    @SuppressWarnings("unchecked")
    private void testJsonStringWriter(Map model, MockBindingResult bindingResult, JsonWriterConfiguratorTemplateRegistry registry, String expected, boolean convertAllMapValues) {
        jsonStringWriter.setConvertAllMapValues(convertAllMapValues);
        StringWriter writer = new StringWriter();
        try {
            jsonStringWriter.convertAndWrite(model, registry, writer, bindingResult);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEqualsJson(expected, writer.toString());
    }

    public MockBindingResult getEmptyBindingResult() {
        return new MockBindingResult(COMMAND_BEAN_NAME);
    }

    public MockBindingResult getTypedBindingResult() {
        MockBindingResult binder = new MockBindingResult(COMMAND_BEAN_NAME);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.getPropertyEditorRegistry().registerCustomEditor(Date.class, editor);
        return binder;
    }

    public MockBindingResult getBeanPathBindingResult(String nestedPath) {
        MockBindingResult binder = new MockBindingResult(COMMAND_BEAN_NAME);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.getPropertyEditorRegistry().registerCustomEditor(Date.class, nestedPath, editor);
        return binder;
    }

    public MockBindingResult getBeanPathAndTypedBindingResult(String nestedPath1, String nestedPath2) {
        MockBindingResult binder = new MockBindingResult(COMMAND_BEAN_NAME);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd");
        CustomDateEditor editor1 = new CustomDateEditor(dateFormat1, true);
        binder.getPropertyEditorRegistry().registerCustomEditor(Date.class, nestedPath1, editor1);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM");
        CustomDateEditor editor2 = new CustomDateEditor(dateFormat2, true);
        binder.getPropertyEditorRegistry().registerCustomEditor(Date.class, nestedPath2, editor2);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
        CustomDateEditor editor3 = new CustomDateEditor(dateFormat3, true);
        binder.getPropertyEditorRegistry().registerCustomEditor(Date.class, editor3);
        return binder;
    }

    public JsonWriterConfiguratorTemplateRegistry getEmptyConfiguratorTemplateRegistry() {
        return JsonWriterConfiguratorTemplateRegistry.load(new MockHttpServletRequest());
    }

    public JsonWriterConfiguratorTemplateRegistry getConfiguratorTemplateRegistry() {
        JsonWriterConfiguratorTemplateRegistry registry = JsonWriterConfiguratorTemplateRegistry.load(new MockHttpServletRequest());
        registry.registerConfiguratorTemplate(new MockSimpleSojoJsonWriterConfiguratorTemplate());
        return registry;
    }
}
