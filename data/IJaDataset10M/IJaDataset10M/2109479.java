package org.springframework.web.servlet.view.json;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.converter.model.Bean;
import org.springframework.web.servlet.view.json.mock.MockControllerInterfaceImpl;
import org.springjson.test.TestBase;

public class ControllerInterfaceImplTest extends TestBase {

    public static final String JSON_VIEW_BEAN_NAME = "jsonView";

    public static final String CONTROLLER_INTERFACE_IMPL_BEAN_NAME = "controllerInterfaceImpl";

    protected Date date1 = new GregorianCalendar(2000, Calendar.MARCH, 17).getTime();

    protected Date date2 = new GregorianCalendar(2002, Calendar.SEPTEMBER, 20).getTime();

    private MockHttpServletResponse response;

    private MockHttpServletRequest request;

    private Map model;

    private JsonView view;

    private MockControllerInterfaceImpl controller;

    @Before
    public void setUp() throws Exception {
        view = (JsonView) beanFactory.getBean(JSON_VIEW_BEAN_NAME);
        model = getModel();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        controller = (MockControllerInterfaceImpl) beanFactory.getBean(CONTROLLER_INTERFACE_IMPL_BEAN_NAME);
    }

    @After
    public void tearDown() throws Exception {
        view = null;
        model = null;
        response = null;
        request = null;
        controller = null;
    }

    @Test
    public void testJsonViewWithControllerInterfaceImpl() throws Exception {
        controller.setModel(model);
        ModelAndView mv = controller.handleRequest(request, response);
        view.renderMergedOutputModel(mv.getModelMap(), request, response);
        String expected = "{\"num\":1,\"bean\":{\"date2\":\"Fri Sep 20 00:00:00 GMT 2002\",\"number\":2,\"date1\":\"Fri Mar 17 00:00:00 GMT 2000\",\"string\":\"beanstring\"},\"arr\":[1,2,3],\"string\":\"test\",\"map\":{\"num\":1,\"string\":\"test\",\"date1\":\"Fri Mar 17 00:00:00 GMT 2000\"},\"date1\":1}";
        assertEquals(expected, response.getContentAsString());
        JSONObject.fromObject(response.getContentAsString());
    }

    private Map getModel() {
        Map modelMap = new HashMap();
        modelMap.put("string", "test");
        modelMap.put("num", new Long(1));
        modelMap.put("date1", date1);
        List list = new ArrayList();
        list.add("test");
        list.add(new Long(1));
        modelMap.put("date1", new Long(1));
        Map map = new HashMap();
        map.put("string", "test");
        map.put("num", new Long(1));
        map.put("date1", date1);
        modelMap.put("map", map);
        int[] arr = new int[] { 1, 2, 3 };
        modelMap.put("arr", arr);
        Bean bean = new Bean("beanstring", new Long(2), date1, date2);
        modelMap.put("bean", bean);
        return modelMap;
    }
}
