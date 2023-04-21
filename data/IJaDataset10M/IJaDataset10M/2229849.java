package net.woodstock.rockapi.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import net.woodstock.rockapi.struts2.components.NumericTextField;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.TextFieldTag;
import com.opensymphony.xwork2.util.ValueStack;

public class NumericTextFieldTag extends TextFieldTag {

    private static final long serialVersionUID = -5337032586186335109L;

    private String decimalChar;

    private String groupChar;

    @Override
    public int doEndTag() throws JspException {
        int i = super.doEndTag();
        this.pageContext.getRequest().setAttribute(JSConstants.NUMERIC_MASK, Boolean.TRUE);
        return i;
    }

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new NumericTextField(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        NumericTextField textField = ((NumericTextField) this.component);
        textField.setDecimalChar(this.decimalChar);
        textField.setGroupChar(this.groupChar);
    }

    public void setDecimalChar(String decimalChar) {
        this.decimalChar = decimalChar;
    }

    public void setGroupChar(String groupChar) {
        this.groupChar = groupChar;
    }
}
