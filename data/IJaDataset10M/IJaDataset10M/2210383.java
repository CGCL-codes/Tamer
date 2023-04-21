package bufferings.ktr.s3unit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockRequestDispatcher;
import org.slim3.tester.MockServletConfig;
import org.slim3.tester.MockServletContext;
import org.slim3.util.BooleanUtil;
import org.slim3.util.DateUtil;
import org.slim3.util.DoubleUtil;
import org.slim3.util.FloatUtil;
import org.slim3.util.IntegerUtil;
import org.slim3.util.LocaleLocator;
import org.slim3.util.LongUtil;
import org.slim3.util.NumberUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.ShortUtil;
import org.slim3.util.StringUtil;
import org.slim3.util.TimeZoneLocator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A helper for servlet tests.
 * 
 * @author bufferings[at]gmail.com
 * 
 */
public class KtrServletTester extends KtrDatastoreTester {

    /**
   * Previous {@link ServletContext}.
   */
    protected ServletContext prevServletContext;

    /**
   * Previous {@link ServletConfig}.
   */
    protected ServletConfig prevConfig;

    /**
   * Previous {@link HttpServletRequest}.
   */
    protected HttpServletRequest prevRequest;

    /**
   * Previous {@link HttpServletResponse}.
   */
    protected HttpServletResponse prevResponse;

    /**
   * Previous {@link Locale}.
   */
    protected Locale prevLocale;

    /**
   * Previous {@link TimeZone}.
   */
    protected TimeZone prevTimeZone;

    /**
   * The mock for {@link ServletContext}.
   */
    public MockServletContext servletContext = new MockServletContext();

    /**
   * The mock for {@link ServletConfig}.
   */
    public MockServletConfig config = new MockServletConfig(servletContext);

    /**
   * The mock for {@link HttpServletRequest}.
   */
    public MockHttpServletRequest request = new MockHttpServletRequest(servletContext);

    /**
   * The mock for {@link HttpServletResponse}.
   */
    public MockHttpServletResponse response = new MockHttpServletResponse();

    /**
   * Locale
   */
    public Locale locale = Locale.US;

    /**
   * TimeZone
   */
    public TimeZone timeZone = TimeZone.getTimeZone("UTC");

    /**
   * Sets up before the Test method.
   */
    public void setUp() throws Exception {
        super.setUp();
        prevServletContext = ServletContextLocator.get();
        prevRequest = RequestLocator.get();
        prevResponse = ResponseLocator.get();
        prevLocale = LocaleLocator.get();
        prevTimeZone = TimeZoneLocator.get();
        ServletContextLocator.set(servletContext);
        RequestLocator.set(request);
        ResponseLocator.set(response);
        LocaleLocator.set(locale);
        TimeZoneLocator.set(timeZone);
    }

    /**
   * Tears down after the Test method.
   */
    public void tearDown() throws Exception {
        servletContext = null;
        config = null;
        request = null;
        response = null;
        locale = null;
        timeZone = null;
        ServletContextLocator.set(prevServletContext);
        RequestLocator.set(prevRequest);
        ResponseLocator.set(prevResponse);
        LocaleLocator.set(prevLocale);
        TimeZoneLocator.set(prevTimeZone);
        super.tearDown();
    }

    /**
   * Returns the request parameter.
   * 
   * @param name
   *          the parameter name
   * @return the parameter value
   */
    public String param(String name) {
        return request.getParameter(name);
    }

    /**
   * Sets the request parameter.
   * 
   * @param name
   *          the parameter name
   * @param value
   *          the parameter value
   */
    public void param(String name, Object value) {
        request.setParameter(name, StringUtil.toString(value));
    }

    /**
   * Returns the request parameter.
   * 
   * @param name
   *          the parameter name
   * @return the parameter value
   */
    public String[] paramValues(String name) {
        return request.getParameterValues(name);
    }

    /**
   * Returns the request attribute value as short.
   * 
   * @param name
   *          the attribute name
   * @return the short attribute value
   */
    public Short asShort(String name) {
        return ShortUtil.toShort(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as short.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link DecimalFormat}
   * @return the short attribute value
   */
    public Short asShort(String name, String pattern) {
        return ShortUtil.toShort(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
   * Returns the request attribute value as integer.
   * 
   * @param name
   *          the attribute name
   * @return the integer attribute value
   */
    public Integer asInteger(String name) {
        return IntegerUtil.toInteger(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as integer.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link DecimalFormat}
   * @return the integer attribute value
   */
    public Integer asInteger(String name, String pattern) {
        return IntegerUtil.toInteger(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
   * Returns the request attribute value as long.
   * 
   * @param name
   *          the attribute name
   * @return the long attribute value
   */
    public Long asLong(String name) {
        return LongUtil.toLong(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as long.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link DecimalFormat}
   * @return the long attribute value
   */
    public Long asLong(String name, String pattern) {
        return LongUtil.toLong(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
   * Returns the request attribute value as float.
   * 
   * @param name
   *          the attribute name
   * @return the float attribute value
   */
    public Float asFloat(String name) {
        return FloatUtil.toFloat(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as float.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link DecimalFormat}
   * @return the float attribute value
   */
    public Float asFloat(String name, String pattern) {
        return FloatUtil.toFloat(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
   * Returns the request attribute value as double.
   * 
   * @param name
   *          the attribute name
   * @return the double attribute value
   */
    public Double asDouble(String name) {
        return DoubleUtil.toDouble(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as double.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link DecimalFormat}
   * @return the double attribute value
   */
    public Double asDouble(String name, String pattern) {
        return DoubleUtil.toDouble(NumberUtil.toNumber(asString(name), pattern));
    }

    /**
   * Returns the request attribute value as string.
   * 
   * @param name
   *          the attribute name
   * @return the string attribute value
   */
    public String asString(String name) {
        return StringUtil.toString(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as boolean.
   * 
   * @param name
   *          the attribute name
   * @return the boolean attribute value
   */
    public Boolean asBoolean(String name) {
        return BooleanUtil.toBoolean(request.getAttribute(name));
    }

    /**
   * Returns the request attribute value as date.
   * 
   * @param name
   *          the attribute name
   * @param pattern
   *          the pattern for {@link SimpleDateFormat}
   * @return the date attribute value
   */
    public Date asDate(String name, String pattern) {
        return DateUtil.toDate(asString(name), pattern);
    }

    /**
   * Returns the request attribute value as {@link Key}.
   * 
   * @param name
   *          the attribute name
   * @return the request attribute value as {@link Key}
   */
    public Key asKey(String name) {
        Object key = request.getAttribute(name);
        if (key == null) {
            return null;
        }
        if (key instanceof Key) {
            return (Key) key;
        }
        return KeyFactory.stringToKey(key.toString());
    }

    /**
   * Sets the request parameter.
   * 
   * @param name
   *          the parameter name
   * @param value
   *          the parameter value
   */
    public void paramValues(String name, String[] value) {
        request.setParameter(name, value);
    }

    /**
   * Returns the request attribute.
   * 
   * @param <T>
   *          the return type
   * @param name
   *          the attribute name
   * @return the request attribute
   */
    @SuppressWarnings("unchecked")
    public <T> T requestScope(String name) {
        return (T) request.getAttribute(name);
    }

    /**
   * Sets the request attribute.
   * 
   * @param name
   *          the attribute name
   * @param value
   *          the attribute value
   */
    public void requestScope(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
   * Returns the session attribute.
   * 
   * @param <T>
   *          the return type
   * @param name
   *          the attribute name
   * @return the attribute value
   */
    @SuppressWarnings("unchecked")
    public <T> T sessionScope(String name) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name);
    }

    /**
   * Sets the session attribute.
   * 
   * @param name
   *          the attribute name
   * @param value
   *          the attribute value
   */
    public void sessionScope(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
   * Returns the servlet context attribute.
   * 
   * @param <T>
   *          the return type
   * @param name
   *          the attribute name
   * @return the attribute value
   */
    @SuppressWarnings("unchecked")
    public <T> T applicationScope(String name) {
        return (T) servletContext.getAttribute(name);
    }

    /**
   * Sets the servlet context attribute.
   * 
   * @param name
   *          the attribute name
   * @param value
   *          the attribute value
   */
    public void applicationScope(String name, Object value) {
        servletContext.setAttribute(name, value);
    }

    /**
   * Determines if the test result is "redirect".
   * 
   * @return whether the test result is "redirect"
   */
    public boolean isRedirect() {
        return response.getRedirectPath() != null;
    }

    /**
   * Returns the destination path.
   * 
   * @return the destination path
   */
    public String getDestinationPath() {
        MockRequestDispatcher dispatcher = servletContext.getLatestRequestDispatcher();
        if (dispatcher != null) {
            return dispatcher.getPath();
        }
        if (response.getRedirectPath() != null) {
            return response.getRedirectPath();
        }
        return null;
    }
}
