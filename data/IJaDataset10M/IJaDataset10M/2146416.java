package net.sf.compressionfilter;

import junit.framework.TestCase;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Maxim Butov
 * @version $Id: ParametrizedFilterTest.java,v 1.2 2007/07/30 16:43:57 maxim_butov Exp $
 */
public class ParametrizedFilterTest extends TestCase {

    private static class FilterConfigMockObject implements FilterConfig {

        private Map map;

        public FilterConfigMockObject(Map map) {
            this.map = map;
        }

        public String getFilterName() {
            throw new UnsupportedOperationException();
        }

        public ServletContext getServletContext() {
            throw new UnsupportedOperationException();
        }

        public String getInitParameter(String name) {
            return (String) map.get(name);
        }

        public Enumeration getInitParameterNames() {
            return new Vector(map.keySet()).elements();
        }
    }

    private static class ParametrizedFilterMockObject extends ParametrizedFilter {

        private String string;

        private boolean primitiveBoolean;

        private int primitiveInt;

        private Integer wrappedInt;

        private Double wrappedDouble;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean primitiveBoolean) {
            this.primitiveBoolean = primitiveBoolean;
        }

        public int getPrimitiveInt() {
            return primitiveInt;
        }

        public void setPrimitiveInt(int primitiveInt) {
            this.primitiveInt = primitiveInt;
        }

        public Integer getWrappedInt() {
            return wrappedInt;
        }

        public void setWrappedInt(Integer wrappedInt) {
            this.wrappedInt = wrappedInt;
        }

        public Double getWrappedDouble() {
            return wrappedDouble;
        }

        public void setWrappedDouble(Double wrappedDouble) {
            this.wrappedDouble = wrappedDouble;
        }

        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            throw new UnsupportedOperationException();
        }
    }

    public void testParametrizedFilter() throws Exception {
        ParametrizedFilterMockObject filter = new ParametrizedFilterMockObject();
        filter.init(new FilterConfigMockObject(new HashMap() {

            {
                put("string", "Test");
                put("primitiveBoolean", "true");
                put("primitiveInt", "-1");
                put("wrappedInt", "1");
                put("wrappedDouble", "123.456");
            }
        }));
        assertEquals(filter.getString(), "Test");
        assertEquals(filter.isPrimitiveBoolean(), true);
        assertEquals(filter.getPrimitiveInt(), -1);
        assertEquals(filter.getWrappedInt(), new Integer(1));
        assertEquals(filter.getWrappedDouble(), new Double(123.456));
    }
}
