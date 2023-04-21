package org.scopemvc.view.swing;

import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * The render for cell elements used by STable. <br>
 * The values are converted to text by using a StringConvertor.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.10 $ $Date: 2003/01/17 16:33:41 $
 * @created 05 September 2002
 */
public class SDefaultTableCellRenderer extends DefaultTableCellRenderer {

    private static final Log LOG = LogFactory.getLog(SDefaultTableCellRenderer.class);

    private StringConvertor convertor;

    /**
     * Constructor for the SDefaultTableCellRenderer object. <br>
     * The StringConvertor used to convert the column values to strings is the
     * once defined by default in StringConvertors.
     *
     * @param inClass The class of the values in the column.
     * @see org.scopemvc.util.convertor.StringConvertors
     */
    public SDefaultTableCellRenderer(Class inClass) {
        if (inClass == null) {
            throw new IllegalArgumentException("Can't create a renderer for null class");
        }
        convertor = StringConvertors.forClass(inClass);
        if (convertor == null) {
            throw new IllegalArgumentException("Can't create a renderer for: " + inClass + ", because not StringConvertor was found");
        }
    }

    /**
     * Constructor for the SDefaultTableCellRenderer object
     *
     * @param inConvertor The StringConvertor to use to convert the column
     *      values to strings
     */
    public SDefaultTableCellRenderer(StringConvertor inConvertor) {
        convertor = inConvertor;
    }

    /**
     * Returns the StringConvertor used to convert the values in the column.
     *
     * @return a StringConvertor
     */
    public final StringConvertor getStringConvertor() {
        return convertor;
    }

    /**
     * Return a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        return new ToStringBuilder(this).append("convertor", convertor).toString();
    }

    /**
     * Overriden method, which uses Scope's String converters to convert value
     * to text.
     *
     * @param inValue the value for this cell
     * @see javax.swing.table.DefaultTableCellRenderer#setValue
     */
    protected void setValue(Object inValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setValue: " + inValue);
        }
        if (convertor == null) {
            super.setValue(inValue);
        } else {
            setText(convertor.valueAsString(inValue));
        }
    }
}
