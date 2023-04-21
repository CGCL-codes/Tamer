package net.sourceforge.strueaf.components;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.util.ValueStack;

public class DateField extends org.apache.struts2.components.TextField implements StrueafFieldComponent {

    private HashMap<String, String> m_strueafAttributes;

    public DateField(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
        m_strueafAttributes = new HashMap<String, String>();
    }

    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        this.setTemplate("date");
        addStrueafParameters();
    }

    public void addStrueafParameters() {
        addParameter("layoutCol1", getProperty("layoutCol1"));
        addParameter("layoutCol2", getProperty("layoutCol2"));
        addParameter("layoutCol3", getProperty("layoutCol3"));
        addParameter("layoutCol4", getProperty("layoutCol4"));
        addParameter("layoutCol5", getProperty("layoutCol5"));
        addParameter("layoutCol6", getProperty("layoutCol6"));
        addParameter("layoutCol7", getProperty("layoutCol7"));
        addParameter("layoutCol8", getProperty("layoutCol8"));
        addParameter("prefix", getProperty("prefix"));
        addParameter("errorposition", getProperty("errorposition"));
        addParameter("requiredindicator", getProperty("requiredindicator"));
        addParameter("helpplacement", getProperty("helpplacement"));
        addParameter("helptextkey", getProperty("helptextkey"));
        addParameter("labelposition", getProperty("labelposition"));
    }

    public void setStrueafAttributes(HashMap<String, String> p_strueafAttributes) {
        m_strueafAttributes = p_strueafAttributes;
    }

    public String getLayoutCol1() {
        return getProperty("layoutCol1");
    }

    public String getLayoutCol2() {
        return getProperty("layoutCol2");
    }

    public String getLayoutCol3() {
        return getProperty("layoutCol3");
    }

    public String getLayoutCol4() {
        return getProperty("layoutCol4");
    }

    public String getLayoutCol5() {
        return getProperty("layoutCol5");
    }

    public String getLayoutCol6() {
        return getProperty("layoutCol6");
    }

    public String getLayoutCol7() {
        return getProperty("layoutCol7");
    }

    public String getLayoutCol8() {
        return getProperty("layoutCol8");
    }

    public String getPrefix() {
        return getProperty("prefix");
    }

    public String getErrorposition() {
        return getProperty("errorposition");
    }

    public String getRequiredindicator() {
        return getProperty("requiredindicator");
    }

    public String getHelpplacement() {
        return getProperty("helpplacement");
    }

    public String getHelptextkey() {
        return getProperty("helptextkey");
    }

    public String getProperty(String p_propName) {
        String retval = null;
        retval = m_strueafAttributes.get(p_propName);
        if (retval == null) {
            LayoutContainer layoutContainer = (LayoutContainer) findAncestor(LayoutContainer.class);
            if (layoutContainer != null) {
                HashMap<String, String> params = (HashMap<String, String>) layoutContainer.getParameters();
                retval = params.get(p_propName);
            }
        }
        if (retval == null) {
            Form form = (Form) findAncestor(Form.class);
            if (form != null) {
                HashMap<String, String> params = (HashMap<String, String>) form.getParameters();
                retval = params.get(p_propName);
            }
        }
        if (retval == null) {
            retval = stack.findString("#application.default_" + p_propName);
        }
        return retval;
    }
}
