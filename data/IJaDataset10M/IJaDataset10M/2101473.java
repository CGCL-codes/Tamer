package org.devyant.decorutils.tags.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.apache.cactus.JspTestCase;
import org.devyant.decorutils.decorators.DateDecorator;

/**
 * AbstractDecoratorTaglibTestCase is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision: 1.1 $ ($Author: ftavares $)
 * @since 19/Mar/2005 4:59:43
 */
public abstract class AbstractDecoratorTaglibTestCase extends JspTestCase {

    /**
     * The DATE_FORMAT <code>String</code>.
     */
    public static final String DATE_FORMAT = "dd MMMM yyyy HH mm";

    /**
     * The ATTRIBUTES <code>String</code>.
     */
    public static final String ATTRIBUTES = "format=" + DATE_FORMAT;

    /**
     * The TIME <code>long</code>.
     */
    protected static final long TIME = 15935445657L;

    /**
     * The TIME_2 <code>long</code>.
     */
    protected static final long TIME_2 = 886346534543553764L;

    /**
     * The BEAN_KEY <code>String</code>.
     */
    public static final String BEAN_KEY = "_bean";

    /**
     * The PROPERTY_KEY <code>String</code>.
     */
    public static final String PROPERTY_KEY = "collection";

    /**
     * The WRAPPER_PROPERTY <code>String</code>.
     */
    public static final String WRAPPER_PROPERTY = "string";

    /**
     * The DECORATOR_CLASS <code>String</code>.
     */
    public static final String DECORATOR_CLASS = "org.devyant.decorutils.decorators.DateDecorator";

    /**
     * The DECORATOR_CLASS <code>String</code>.
     */
    public static final String WRAPPER_CLASS = "org.devyant.decorutils.tags.decorator.TestWrapper";

    /**
     * The COLLECTION_SIZE <code>int</code>.
     */
    protected static final int COLLECTION_SIZE = 2;

    /**
     * The tag <code>DecorateTag</code>.
     */
    private transient DecorateTag tag;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() {
        tag = newTagInstance();
        tag.setPageContext(pageContext);
        tag.setDecorator(DECORATOR_CLASS);
        tag.setName(BEAN_KEY);
        tag.setProperty(PROPERTY_KEY);
        tag.setAttributes(ATTRIBUTES);
        tag.setIterate(true);
        final Date date = new Date(TIME);
        final Date date2 = new Date(TIME_2);
        final Collection dates = new ArrayList();
        dates.add(date);
        dates.add(date2);
        request.setAttribute(BEAN_KEY, new DummyContainer(dates));
    }

    /**
     * @param output The actual output from the test
     */
    protected static final void verifyOutput(final String output) {
        final Date date = new Date(TIME);
        final Date date2 = new Date(TIME_2);
        final DateFormat format = new SimpleDateFormat(DATE_FORMAT, DateDecorator.DEFAULT_LOCALE);
        final String expected = format.format(date) + format.format(date2);
        assertEquals("DecorateTag did not output correctly.", expected, output);
    }

    /**
     * @return Tag instance
     */
    protected abstract DecorateTag newTagInstance();

    /**
     * @return Returns the tag.
     */
    public final DecorateTag getTag() {
        return tag;
    }

    /**
     * DummyContainer is a <b>cool</b> class.
     * 
     * @author Filipe Tavares
     * @version $Revision: 1.1 $ ($Author: ftavares $)
     * @since 19/Mar/2005 5:37:30
     */
    public class DummyContainer {

        /**
         * The collection <code>Collection</code>.
         */
        private Collection collection;

        /**
         * Creates a new <code>DummyContainer</code> instance.
         * @param collection The collection
         */
        protected DummyContainer(final Collection collection) {
            this.collection = collection;
        }

        /**
         * @return Returns the collection.
         */
        public final Collection getCollection() {
            return collection;
        }

        /**
         * @param collection The collection <code>Collection</code> to set.
         */
        public final void setCollection(final Collection collection) {
            this.collection = collection;
        }
    }
}
