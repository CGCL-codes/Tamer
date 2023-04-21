package com.nhncorp.usf.macro.method;

import java.util.*;
import javax.servlet.http.Cookie;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.nhncorp.lucy.common.config.ConfigurationLoader;
import com.nhncorp.lucy.common.config.model.ApplicationInfo;
import com.nhncorp.lucy.common.util.LocaleUtil;
import com.nhncorp.lucy.web.util.FreeMarkerUtil;
import com.nhncorp.usf.core.config.ConfigurationBuilder;
import com.nhncorp.usf.core.util.StringUtil;
import com.nhncorp.usf.macro.method.component.*;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import junit.framework.Assert;

/**
 * @author Web Platform Development Team
 */
public class TemplateMethodTest {

    static Configuration configuration;

    static MockHttpServletRequest request;

    static MockHttpServletResponse response;

    static Map<Object, Object> dataMap;

    @BeforeClass
    public static void initialize() throws Exception {
        ApplicationInfo.setFileEncoding("utf-8");
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(FreeMarkerUtil.class, "/META-INF/template");
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        configuration.setTemplateUpdateDelay(0);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        configuration.setDefaultEncoding("utf-8");
        if (!FreeMarkerUtil.setSettingsToConfiguration(configuration) && StringUtil.isNotEmpty(ApplicationInfo.getFileEncoding())) {
            configuration.setDefaultEncoding(ApplicationInfo.getFileEncoding());
        }
    }

    @Before
    public void initUserLocale() throws Exception {
        LocaleUtil.setUserLocale(null);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        dataMap = new HashMap<Object, Object>();
        dataMap.put("request", request);
        dataMap.put("response", response);
    }

    @After
    public void cleanTest() {
        request = null;
        response = null;
        dataMap = null;
    }

    @Test
    public void messageMethod() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        messageInfo.put("message", new MessageMethod());
        LocaleUtil.setUserLocale(Locale.US);
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_test.ftl", messageInfo).toString();
        }
        Assert.assertEquals("defaultMessageIsKimsk", templateString.trim());
    }

    @Test
    public void messageMethodErrorTest() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        messageInfo.put("message", new MessageMethod());
        LocaleUtil.setUserLocale(Locale.US);
        try {
            FreeMarkerUtil.processTemplate(configuration, "macro_message_error.ftl", messageInfo);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Arguments size must be lager than 1.", e.getMessage());
        }
    }

    @Test
    public void messageMethodEmptyTest() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        messageInfo.put("message", new MessageMethod());
        LocaleUtil.setUserLocale(Locale.US);
        try {
            FreeMarkerUtil.processTemplate(configuration, "macro_message_empty.ftl", messageInfo);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Key value must not be empty.", e.getMessage());
        }
    }

    @Test
    public void messageMethodValidationTest() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        messageInfo.put("message", new MessageMethod());
        LocaleUtil.setUserLocale(Locale.US);
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_message_validation.ftl", messageInfo).toString();
        }
        Assert.assertEquals("unknown", templateString.trim());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void compositeBOMethod() throws Exception {
        Map dataModel = new HashMap();
        Map scheduleData1 = new HashMap();
        Map scheduleData2 = new HashMap();
        Map scheduleData3 = new HashMap();
        Map scheduleData4 = new HashMap();
        Map scheduleData5 = new HashMap();
        Map calendarData1 = new HashMap();
        Map calendarData2 = new HashMap();
        Map calendarData3 = new HashMap();
        scheduleData1.put("scheduleID", "seqno1");
        scheduleData1.put("contents", "birthday");
        scheduleData1.put("important", true);
        scheduleData1.put("notifiable", false);
        scheduleData1.put("repetitive", true);
        scheduleData1.put("calendarID", "calendarNo3");
        scheduleData1.put("scheduleType", "schedule1");
        scheduleData2.put("scheduleID", "seqno2");
        scheduleData2.put("contents", "vacation");
        scheduleData2.put("important", true);
        scheduleData2.put("notifiable", false);
        scheduleData2.put("repetitive", false);
        scheduleData2.put("calendarID", "calendarNo2");
        scheduleData2.put("scheduleType", "schedule2");
        scheduleData3.put("scheduleID", "seqno3");
        scheduleData3.put("contents", "birthday");
        scheduleData3.put("important", true);
        scheduleData3.put("notifiable", false);
        scheduleData3.put("repetitive", true);
        scheduleData3.put("calendarID", "calendarNo1");
        scheduleData3.put("scheduleType", "schedule3");
        scheduleData4.put("scheduleID", "seqno4");
        scheduleData4.put("contents", "vacation");
        scheduleData4.put("important", true);
        scheduleData4.put("notifiable", false);
        scheduleData4.put("repetitive", false);
        scheduleData4.put("calendarID", "calendarNo2");
        scheduleData4.put("scheduleType", "schedule4");
        scheduleData5.put("scheduleID", "seqno5");
        scheduleData5.put("contents", "birthday");
        scheduleData5.put("important", true);
        scheduleData5.put("notifiable", false);
        scheduleData5.put("repetitive", true);
        scheduleData5.put("calendarID", "calendarNo3");
        scheduleData5.put("scheduleType", "schedule5");
        calendarData1.put("calendarName", "calendarApril");
        calendarData1.put("calendarColor", "blue");
        calendarData1.put("calendarDesc", "desc1");
        calendarData1.put("calendarOwner", "choco");
        calendarData1.put("sharingCalendar", true);
        calendarData1.put("notifiable", false);
        calendarData1.put("calendarID", "calendarNo1");
        calendarData2.put("calendarName", "calendarJune");
        calendarData2.put("calendarColor", "red");
        calendarData2.put("calendarDesc", "desc2");
        calendarData2.put("calendarOwner", "nhn");
        calendarData2.put("sharingCalendar", true);
        calendarData2.put("notifiable", false);
        calendarData2.put("calendarID", "calendarNo2");
        calendarData3.put("calendarName", "calendarJuly");
        calendarData3.put("calendarColor", "white");
        calendarData3.put("calendarDesc", "desc3");
        calendarData3.put("calendarOwner", "choco");
        calendarData3.put("sharingCalendar", true);
        calendarData3.put("notifiable", false);
        calendarData3.put("calendarID", "calendarNo3");
        List scheduleList = new ArrayList();
        List calendarList = new ArrayList();
        scheduleList.add(scheduleData1);
        scheduleList.add(scheduleData2);
        scheduleList.add(scheduleData3);
        scheduleList.add(scheduleData4);
        scheduleList.add(scheduleData5);
        calendarList.add(calendarData1);
        calendarList.add(calendarData2);
        calendarList.add(calendarData3);
        dataModel.put("scheduleData", scheduleList);
        dataModel.put("calendarData", calendarList);
        dataModel.put("compositeBO", new CompositeBOMethod());
        LocaleUtil.setUserLocale(Locale.US);
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_compositeBO.ftl", dataModel).toString();
        }
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n" + "<html>\r\n" + "<head>\r\n" + "\t<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\r\n" + "\t<title>Composite BO Test</title>\r\n" + "</head>\r\n" + "<body>\r\n\r\n" + "<ul>\r\n" + "  <li>schedule ID : seqno1\r\n" + "  <li>calendar ID : calendarNo3\r\n" + "  <li>calendar name : calendarJuly\r\n" + "  <li>schedule ID : seqno2\r\n" + "  <li>calendar ID : calendarNo2\r\n" + "  <li>calendar name : calendarJune\r\n" + "  <li>schedule ID : seqno3\r\n" + "  <li>calendar ID : calendarNo1\r\n" + "  <li>calendar name : calendarApril\r\n" + "  <li>schedule ID : seqno4\r\n" + "  <li>calendar ID : calendarNo2\r\n" + "  <li>calendar name : calendarJune\r\n" + "  <li>schedule ID : seqno5\r\n" + "  <li>calendar ID : calendarNo3\r\n" + "  <li>calendar name : calendarJuly\r\n" + "</ul>\r\n" + "</body>\r\n" + "</html>";
        Assert.assertEquals(expected, templateString.trim());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void compositeBOMethodNullTest() throws Exception {
        Map dataModel = new HashMap();
        Map scheduleData1 = new HashMap();
        Map scheduleData2 = new HashMap();
        Map scheduleData3 = new HashMap();
        Map scheduleData4 = new HashMap();
        Map scheduleData5 = new HashMap();
        Map calendarData1 = new HashMap();
        Map calendarData2 = new HashMap();
        Map calendarData3 = new HashMap();
        scheduleData1.put("scheduleID", "seqno1");
        scheduleData1.put("contents", "birthday");
        scheduleData1.put("important", true);
        scheduleData1.put("notifiable", false);
        scheduleData1.put("repetitive", true);
        scheduleData1.put("calendarID", "calendarNo3");
        scheduleData1.put("scheduleType", "schedule1");
        scheduleData2.put("scheduleID", "seqno2");
        scheduleData2.put("contents", "vacation");
        scheduleData2.put("important", true);
        scheduleData2.put("notifiable", false);
        scheduleData2.put("repetitive", false);
        scheduleData2.put("calendarID", "calendarNo2");
        scheduleData2.put("scheduleType", "schedule2");
        scheduleData3.put("scheduleID", "seqno3");
        scheduleData3.put("contents", "birthday");
        scheduleData3.put("important", true);
        scheduleData3.put("notifiable", false);
        scheduleData3.put("repetitive", true);
        scheduleData3.put("calendarID", "calendarNo1");
        scheduleData3.put("scheduleType", "schedule3");
        scheduleData4.put("scheduleID", "seqno4");
        scheduleData4.put("contents", "vacation");
        scheduleData4.put("important", true);
        scheduleData4.put("notifiable", false);
        scheduleData4.put("repetitive", false);
        scheduleData4.put("calendarID", "calendarNo2");
        scheduleData4.put("scheduleType", "schedule4");
        scheduleData5.put("scheduleID", "seqno5");
        scheduleData5.put("contents", "birthday");
        scheduleData5.put("important", true);
        scheduleData5.put("notifiable", false);
        scheduleData5.put("repetitive", true);
        scheduleData5.put("calendarID", "calendarNo3");
        scheduleData5.put("scheduleType", "schedule5");
        calendarData1.put("calendarName", "calendarApril");
        calendarData1.put("calendarColor", "blue");
        calendarData1.put("calendarDesc", "desc1");
        calendarData1.put("calendarOwner", "choco");
        calendarData1.put("sharingCalendar", true);
        calendarData1.put("notifiable", false);
        calendarData1.put("calendarID", "calendarNo1");
        calendarData2.put("calendarName", "calendarJune");
        calendarData2.put("calendarColor", "red");
        calendarData2.put("calendarDesc", "desc2");
        calendarData2.put("calendarOwner", "nhn");
        calendarData2.put("sharingCalendar", true);
        calendarData2.put("notifiable", false);
        calendarData2.put("calendarID", "calendarNo2");
        calendarData3.put("calendarName", "calendarJuly");
        calendarData3.put("calendarColor", "white");
        calendarData3.put("calendarDesc", "desc3");
        calendarData3.put("calendarOwner", "choco");
        calendarData3.put("sharingCalendar", true);
        calendarData3.put("notifiable", false);
        calendarData3.put("calendarID", "calendarNo3");
        List scheduleList = new ArrayList();
        List calendarList = new ArrayList();
        scheduleList.add(scheduleData1);
        scheduleList.add(scheduleData2);
        scheduleList.add(scheduleData3);
        scheduleList.add(scheduleData4);
        scheduleList.add(scheduleData5);
        calendarList.add(calendarData1);
        calendarList.add(calendarData2);
        calendarList.add(calendarData3);
        dataModel.put("scheduleData", scheduleList);
        dataModel.put("calendarData", calendarList);
        dataModel.put("compositeBO", new CompositeBOMethod());
        LocaleUtil.setUserLocale(Locale.US);
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_compositeBO_null.ftl", dataModel).toString();
        }
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n" + "<html>\r\n" + "<head>\r\n" + "\t<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />\r\n" + "\t<title>Composite BO Test</title>\r\n" + "</head>\r\n" + "<body>\r\n\r\n" + "<ul>\r\n" + "  <li>schedule ID : seqno1\r\n" + "  <li>calendar ID : calendarNo3\r\n" + "  <li>calendar name : calendarJuly\r\n" + "  <li>schedule ID : seqno2\r\n" + "  <li>calendar ID : calendarNo2\r\n" + "  <li>calendar name : calendarJune\r\n" + "  <li>schedule ID : seqno3\r\n" + "  <li>calendar ID : calendarNo1\r\n" + "  <li>calendar name : calendarApril\r\n" + "  <li>schedule ID : seqno4\r\n" + "  <li>calendar ID : calendarNo2\r\n" + "  <li>calendar name : calendarJune\r\n" + "  <li>schedule ID : seqno5\r\n" + "  <li>calendar ID : calendarNo3\r\n" + "  <li>calendar name : calendarJuly\r\n" + "</ul>\r\n" + "</body>\r\n" + "</html>";
        Assert.assertEquals(expected, templateString.trim());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void compositeBOMethodErrorTest() throws Exception {
        Map dataModel = new HashMap();
        dataModel.put("compositeBO", new CompositeBOMethod());
        LocaleUtil.setUserLocale(Locale.US);
        for (int i = 0; i < 1; i++) {
            try {
                FreeMarkerUtil.processTemplate(configuration, "macro_compositeBO_error.ftl", dataModel);
            } catch (IllegalArgumentException e) {
                Assert.assertTrue(true);
            }
        }
    }

    @Test
    public void replaceAllThrowRuntimeException() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        LocaleUtil.setUserLocale(Locale.US);
        messageInfo.put("replaceAll", new ReplaceAllMethod());
        StringBuffer templateString = null;
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_replaceAll_failed.ftl", messageInfo);
        }
        Assert.assertNull(templateString);
    }

    @Test
    public void replaceAllThroughMacro() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        LocaleUtil.setUserLocale(Locale.US);
        messageInfo.put("replaceAll", new ReplaceAllMethod());
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_replaceAll.ftl", messageInfo).toString();
        }
        Assert.assertEquals("개발 센터", templateString.trim());
    }

    @Test
    public void replaceAllThroughMethod() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        LocaleUtil.setUserLocale(Locale.US);
        messageInfo.put("replaceAll", new ReplaceAllMethod());
        long startTime = System.currentTimeMillis();
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "direct_replaceAll.ftl", messageInfo).toString();
        }
        long endTime = System.currentTimeMillis();
        Assert.assertEquals("개발 센터", templateString.trim());
        System.out.println("Test through direct method call elasped Time through direct method call: " + (endTime - startTime) + "ms");
    }

    @Test
    public void checkHangulThroughMethod() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        LocaleUtil.setUserLocale(Locale.US);
        messageInfo.put("checkHangul", new CheckHangulMethod());
        long startTime = System.currentTimeMillis();
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "direct_checkHangul.ftl", messageInfo).toString();
        }
        long endTime = System.currentTimeMillis();
        Assert.assertEquals("true\r\nfalse", templateString.trim());
        System.out.println("Test through direct method call elasped Time through direct method call: " + (endTime - startTime) + "ms");
    }

    @Test
    public void checkHangulThroughMethodErrorTest() throws Exception {
        Map<String, Object> messageInfo = new HashMap<String, Object>();
        LocaleUtil.setUserLocale(Locale.US);
        messageInfo.put("checkHangul", new CheckHangulMethod());
        try {
            FreeMarkerUtil.processTemplate(configuration, "direct_checkHangul_error.ftl", messageInfo);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void setCookieMethod() throws Exception {
        List<Object> params = new ArrayList<Object>();
        params.add(BeansWrapper.getDefaultInstance().wrap(response));
        params.add("testCookie");
        params.add("baby");
        SetCookieMethod method = new SetCookieMethod();
        method.exec(params);
        Cookie cookie = response.getCookie("testCookie");
        Assert.assertEquals("baby", cookie.getValue());
    }

    @Test
    public void setCookieMethodErrorTest() throws Exception {
        List<Object> params = new ArrayList<Object>();
        params.add(BeansWrapper.getDefaultInstance().wrap(response));
        params.add("testCookie");
        SetCookieMethod method = new SetCookieMethod();
        try {
            method.exec(params);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        params = new ArrayList<Object>();
        params.add(null);
        params.add("testCookie");
        params.add("baby");
        method = new SetCookieMethod();
        try {
            method.exec(params);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getCookieMethod() throws Exception {
        Cookie cookie = new Cookie("huhuCookie", "Okabary");
        request.setCookies(new Cookie[] { cookie });
        List<Object> params = new ArrayList<Object>();
        params.add(BeansWrapper.getDefaultInstance().wrap(request));
        params.add("huhuCookie");
        GetCookieMethod method = new GetCookieMethod();
        Cookie returnCookie = (Cookie) method.exec(params);
        Assert.assertEquals("Okabary", returnCookie.getValue());
    }

    @Test
    public void getCookieMethodErrorTest() throws Exception {
        Cookie cookie = new Cookie("huhuCookie", "Okabary");
        request.setCookies(new Cookie[] { cookie });
        List<Object> params = new ArrayList<Object>();
        params.add(BeansWrapper.getDefaultInstance().wrap(request));
        GetCookieMethod method = new GetCookieMethod();
        try {
            method.exec(params);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
        params = new ArrayList<Object>();
        params.add(null);
        params.add("huhuCookie");
        method = new GetCookieMethod();
        try {
            method.exec(params);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getCookieMethodNullTest() throws Exception {
        List<Object> params = new ArrayList<Object>();
        params.add(BeansWrapper.getDefaultInstance().wrap(request));
        params.add("huhuCookie");
        GetCookieMethod method = new GetCookieMethod();
        Cookie returnCookie = (Cookie) method.exec(params);
        Assert.assertNull(returnCookie);
    }

    @Test
    public void datahandlerTest() throws Exception {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        configurationLoader.addPreConfigurationAware(new ConfigurationBuilder());
        configurationLoader.initialize("configuration.xml");
        Map<String, Object> datahandlerInfo = new HashMap<String, Object>();
        datahandlerInfo.put("dataHandler", new DataHandlerMethod());
        String templateString = "";
        for (int i = 0; i < 1; i++) {
            templateString = FreeMarkerUtil.processTemplate(configuration, "macro_datahandler.ftl", datahandlerInfo).toString();
        }
        Assert.assertEquals("false", templateString.trim());
        configurationLoader.destroy();
    }

    @Test
    public void datahandlerEmptyTest() throws Exception {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        configurationLoader.addPreConfigurationAware(new ConfigurationBuilder());
        configurationLoader.initialize("configuration.xml");
        Map<String, Object> datahandlerInfo = new HashMap<String, Object>();
        datahandlerInfo.put("dataHandler", new DataHandlerMethod());
        try {
            FreeMarkerUtil.processTemplate(configuration, "macro_datahandler_empty.ftl", datahandlerInfo);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Path must not be empty.", e.getMessage());
        }
        configurationLoader.destroy();
    }
}
