package examples;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * Example1: the simplest tag
 * Collect attributes and call into some actions
 *
 * <foo att1="..." att2="...." att3="...." />
 */
public class FooTag extends ExampleTagBase {

    private static final long serialVersionUID = 1L;

    private String atts[] = new String[3];

    int i = 0;

    private final void setAtt(int index, String value) {
        atts[index] = value;
    }

    public void setAtt1(String value) {
        setAtt(0, value);
    }

    public void setAtt2(String value) {
        setAtt(1, value);
    }

    public void setAtt3(String value) {
        setAtt(2, value);
    }

    /**
     * Process start tag
     *
     * @return EVAL_BODY_INCLUDE
     */
    @Override
    public int doStartTag() throws JspException {
        i = 0;
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void doInitBody() throws JspException {
        pageContext.setAttribute("member", atts[i]);
        i++;
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            if (i == 3) {
                bodyOut.writeOut(bodyOut.getEnclosingWriter());
                return SKIP_BODY;
            }
            pageContext.setAttribute("member", atts[i]);
            i++;
            return EVAL_BODY_BUFFERED;
        } catch (IOException ex) {
            throw new JspTagException(ex.toString());
        }
    }
}
