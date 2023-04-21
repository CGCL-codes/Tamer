package com.jcorporate.expresso.services.taglib;

import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import org.apache.log4j.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * SelectOptions tag goes inside a &lt;select&gt; tag.  It outputs all the
 * &lt;option&gt; values as specified by an Input's ValidValues, and provides
 * the the default selected value based upon the 'defaultValue' of the Input.
 * <p>Set parameter 'value' to a JSTL expression that evaluates to an Input</p>
 *
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class SelectOptions extends TagSupport {

    public SelectOptions() {
    }

    private static final Logger log = Logger.getLogger(SelectOptions.class);

    /**
     * JSTL EL expression
     */
    private String value;

    /**
     * Get the JSTL expression for value.
     *
     * @return java.lang.String
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value JSTL expression
     *
     * @param value the JSTL expression value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Does the actual grunt work.
     *
     * @return EVAL_PAGE
     * @throws javax.servlet.jsp.JspException
     */
    public int doEndTag() throws javax.servlet.jsp.JspException {
        ELTagSupport support = ELTagSupport.getInstance();
        Input result = (Input) support.evaluate("value", this.getValue(), Input.class, this, this.pageContext);
        if (result == null) {
            throw new JspException("Unable to find Input with expression: " + this.getValue());
        }
        String writeValue;
        FastStringBuffer fsb = FastStringBuffer.getInstance();
        List defaultValues = result.getDefaultValueList();
        try {
            Vector vec = result.getValidValues();
            if (vec != null) {
                int size = vec.size();
                for (int i = 0; i < size; i++) {
                    ValidValue vv = (ValidValue) vec.get(i);
                    boolean selected = false;
                    String value = vv.getValue();
                    for (Iterator iterator = defaultValues.iterator(); iterator.hasNext(); ) {
                        String def = (String) iterator.next();
                        if (value.equals(def)) {
                            selected = true;
                            break;
                        }
                    }
                    fsb.append("<option value=\"");
                    fsb.append(vv.getValue());
                    fsb.append("\"");
                    if (selected) {
                        fsb.append(" selected ");
                    }
                    fsb.append(">");
                    fsb.append(vv.getDescription());
                    fsb.append("</option>\n");
                }
            } else {
                System.err.println("Cannot find valid values for input: " + this.getValue());
            }
        } finally {
            writeValue = fsb.toString();
            fsb.release();
        }
        try {
            pageContext.getOut().write(writeValue);
        } catch (IOException ex1) {
            log.error("I/O exception writing output value", ex1);
        }
        return EVAL_PAGE;
    }
}
