package net.sourceforge.pebble.mock;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * A mock FilterChain implementation.
 *
 * @author    Simon Brown
 */
public class MockFilterChain implements FilterChain {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
    }
}
