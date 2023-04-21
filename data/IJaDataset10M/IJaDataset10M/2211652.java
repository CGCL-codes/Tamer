package net.woodstock.rockapi.struts2.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.TextField;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "maskTextField", tldTagClass = "net.woodstock.rockapi.struts2.views.jsp.ui.MaskTextFieldTag", description = "Render a HTML input field of type text")
public class MaskTextField extends TextField {

    public static final String TEMPLATE = "maskTextField";

    private String mask;

    private String numeric;

    public MaskTextField(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return MaskTextField.TEMPLATE;
    }

    @Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (this.mask != null) {
            this.addParameter("mask", this.findString(this.mask));
        }
        if (this.numeric != null) {
            this.addParameter("numeric", this.findString(this.numeric));
        }
    }

    @StrutsTagAttribute(description = "Mask for format", required = true)
    public void setMask(String mask) {
        this.mask = mask;
    }

    @StrutsTagAttribute(description = "Has only numbers", required = true, type = "Boolean")
    public void setNumeric(String numeric) {
        this.numeric = numeric;
    }
}
