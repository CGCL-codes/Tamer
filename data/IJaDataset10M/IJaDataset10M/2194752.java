package net.sf.stax;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * For-child dispatch.  Use this class to handle multiple child
 * elements of the same type.  The list of values, returned by the
 * <code>endTree(StAXContext)</code> method, is available to a
 * parent handler via its <code>endElement(String, String, String,
 * String, Object, StAXContext)</code> method.
 *
 * <p>For example, for the following (sub-)tree:
 * <pre>
 * &gt;foo&lt;
 *   &gt;int&lt;0&gt;/int&lt;
 *   &gt;int&lt;1&gt;/int&lt;
 *   &gt;int&lt;2&gt;/int&lt;
 *   &gt;int&lt;3&gt;/int&lt;
 *   &gt;int&lt;4&gt;/int&lt;
 * &gt;/foo&lt;
 * </pre>
 * </p>
 * <p>Use
 * <pre>
 * class FooHandler
 *     extends StAXContentHandlerBase
 * {
 *     private List integerValues;
 *     private DispatchForChild dispatch = new DispatchForChild(new IntElementHandler());
 *
 *     public void startElement(..., StAXDelegationContext dctx)
 *     {
 *         dctx.delegate(dispatch);
 *     }
 *
 *     public void endElement(..., Object result, ...)
 *     {
 *         this.integerValues = (List) result;
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author  Thomas Down
 * @author  Matthew Pocock
 * @author  Michael Heuer
 * @version $Revision: 1.3 $ $Date: 2006/01/02 20:37:34 $
 */
public final class DispatchForChild extends StAXContentHandlerBase {

    /** Wrapped handler. */
    private final StAXContentHandler handler;

    /** Stack of frames. */
    private final Stack frames;

    /**
     * Create a new for-child dispatch wrapping the specified handler.
     *
     * @param handler wrapped handler
     */
    public DispatchForChild(final StAXContentHandler handler) {
        this.handler = handler;
        frames = new Stack();
    }

    /** @see StAXContentHandlerBase */
    public void startTree(final StAXContext ctx) {
        frames.push(new Frame());
    }

    /** @see StAXContentHandlerBase */
    public void startElement(final String nsURI, final String localName, final String qName, final Attributes attrs, final StAXDelegationContext dctx) throws SAXException {
        Frame f = (Frame) frames.peek();
        if (f.getDepth() == 1) {
            dctx.delegate(handler);
        } else {
            if (f.getDepth() > 1) {
                throw new SAXException("Too deep");
            }
        }
        f.setDepth(f.getDepth() + 1);
    }

    /** @see StAXContentHandlerBase */
    public void endElement(final String nsURI, final String localName, final String qName, final Object result, final StAXContext ctx) {
        Frame f = (Frame) frames.peek();
        f.setDepth(f.getDepth() - 1);
        if (f.getDepth() == 1) {
            f.getResults().add(result);
        }
    }

    /** @see StAXContentHandlerBase */
    public Object endTree(final StAXContext ctx) {
        Frame f = (Frame) frames.pop();
        return f.getResults();
    }

    /**
     * Memento frame class.
     */
    private static final class Frame {

        /** List of results. */
        private List results;

        /** Depth. */
        private int depth;

        /**
         * Create a new empty frame.
         */
        public Frame() {
            results = new ArrayList();
            depth = 0;
        }

        /**
         * Return a modifiable list of results for this frame.
         *
         * @return a modifiable list of results for this frame
         */
        public List getResults() {
            return results;
        }

        /**
         * Return the depth for this frame.
         *
         * @return the depth for this frame
         */
        public int getDepth() {
            return depth;
        }

        /**
         * Set the depth for this frame to <code>depth</code>.
         *
         * @param depth depth
         */
        public void setDepth(final int depth) {
            this.depth = depth;
        }
    }
}
