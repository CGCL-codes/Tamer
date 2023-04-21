package org.bhf.view.jsp.util;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * Tags utilities.
 * @exclude
 */
public class TagUtils {

    private static final String CONTAINER_STACK_ATTR = "$containers$";

    private static long time = System.currentTimeMillis();

    /**
     * Push a parent tag onto the stack so that it can be found by its child tags, even across
     * includes as the stack is kept in request scope.
     * @param container Must not be <code>null</code>.
     * @param pageContext Current tag's page context.
     */
    public static void pushTag(final Tag container, final PageContext pageContext) {
        assert container != null;
        List<Tag> stack = getTagStack(pageContext);
        if (stack == null) {
            stack = new ArrayList<Tag>(10);
            pageContext.getRequest().setAttribute(CONTAINER_STACK_ATTR, stack);
        }
        stack.add(container);
    }

    /**
     * Pop a parent tag from the tag stack.
     * @param pageContext Current tag's page context.
     * @return The popped container.
     */
    public static Tag popTag(final PageContext pageContext) {
        final List<Tag> stack = getTagStack(pageContext);
        if (stack == null) throw new IllegalStateException("No container stack in request scope");
        if (stack.size() == 0) throw new IllegalStateException("Container stack is empty");
        if (stack.size() == 1) pageContext.getRequest().removeAttribute(CONTAINER_STACK_ATTR);
        return stack.remove(stack.size() - 1);
    }

    /**
     * Peek at the parent tag stack.
     * @param pageContext Current tag's page context.
     * @return The popped container.
     */
    public static Tag peekTag(final PageContext pageContext) {
        final List<Tag> stack = getTagStack(pageContext);
        if (stack == null) throw new IllegalStateException("No container stack in request scope");
        if (stack.size() == 0) throw new IllegalStateException("Container stack is empty");
        return stack.get(stack.size() - 1);
    }

    /**
     * Search the tag stack for the closest tag matching the given class. Somewhat similar
     * to <code>findAncestorWithClass(...)</code> but works across includes and is specific to
     * parent tags.
     * @param pageContext current tag's page context.
     * @param clazz The <code>Class of the tag.  Must not be <code>null</code>
     * @return The container or <code>null</code> os none.
     */
    public static <T extends Tag> T findTag(final PageContext pageContext, final Class<T> clazz) {
        final List<Tag> stack = getTagStack(pageContext);
        if (stack != null) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                final Tag container = stack.get(i);
                if (clazz.isAssignableFrom(container.getClass())) {
                    return clazz.cast(container);
                }
            }
        }
        return null;
    }

    /**
     * Search the tag stack for the tags matching the given class. The matching tags are returned in order of closest
     * to farthest.
     * @param pageContext current tag's page context.
     * @param clazz The <code>Class of the tag.  Must not be <code>null</code>
     * @return May be empty, but not <code>null</code>.
     */
    public static <T extends Tag> List<T> findTags(final PageContext pageContext, final Class<T> clazz) {
        final List<Tag> stack = getTagStack(pageContext);
        final List<T> found = new ArrayList<T>(8);
        if (stack != null) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                final Tag container = stack.get(i);
                if (clazz.isAssignableFrom(container.getClass())) {
                    found.add(clazz.cast(container));
                }
            }
        }
        return found;
    }

    /**
     * Returns the time in milliseconds since epoch that this <code>Class</code> was loaded.
     * This time is intended to be used by <code>Tag</code>s that fetch additional resources
     * on a page (i.e. images, scripts, and stylesheets).  A browser will cache the resource
     * based on timestamp if it is appended to the resource URL.  Effectively, this will occur
     * whenever the application is loaded.
     *
     * @return the time in milliseconds since epoch that this <code>Class</code> was loaded.
     */
    public static long getTime() {
        return time;
    }

    /**
     * Creates a valid URL with scheme (protocol), host, port, and uri.
     *
     * @param scheme The HTTP scheme (http or https).
     * @param server The server.
     * @param port The port.
     * @param uri The URI.
     *
     * @return A valid URL with scheme (protocol), host, port, and uri.
     */
    public static StringBuffer createServerUriStringBuffer(final String scheme, final String server, int port, final String uri) {
        final StringBuffer url = new StringBuffer();
        if (port < 0) {
            port = 80;
        }
        url.append(scheme);
        url.append("://");
        url.append(server);
        if (("http".equals(scheme) && (port != 80)) || ("https".equals(scheme) && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(uri);
        return url;
    }

    @SuppressWarnings("unchecked")
    private static List<Tag> getTagStack(final PageContext pageContext) {
        return (List) pageContext.getRequest().getAttribute(CONTAINER_STACK_ATTR);
    }
}
