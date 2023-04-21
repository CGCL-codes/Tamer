package net.sourceforge.strueaf.tags;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.strueaf.components.Form;
import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;

public class FormTag extends org.apache.struts2.views.jsp.ui.FormTag implements StrueafFieldTag {

    private static final long serialVersionUID = 8892290711583398174L;

    private HashMap<String, String> m_strueafAttributes;

    public FormTag() {
        m_strueafAttributes = new HashMap<String, String>();
    }

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Form(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        setStrueafAttributes(m_strueafAttributes);
    }

    public void setStrueafAttributes(HashMap<String, String> p_strueafAttributes) {
        Form comp = ((Form) component);
        comp.setStrueafAttributes(m_strueafAttributes);
    }

    public void setLayoutCol1(String p_layoutCol1) {
        m_strueafAttributes.put("layoutCol1", p_layoutCol1);
    }

    public void setLayoutCol2(String p_layoutCol2) {
        m_strueafAttributes.put("layoutCol2", p_layoutCol2);
    }

    public void setLayoutCol3(String p_layoutCol3) {
        m_strueafAttributes.put("layoutCol3", p_layoutCol3);
    }

    public void setLayoutCol4(String p_layoutCol4) {
        m_strueafAttributes.put("layoutCol4", p_layoutCol4);
    }

    public void setLayoutCol5(String p_layoutCol5) {
        m_strueafAttributes.put("layoutCol5", p_layoutCol5);
    }

    public void setLayoutCol6(String p_layoutCol6) {
        m_strueafAttributes.put("layoutCol6", p_layoutCol6);
    }

    public void setLayoutCol7(String p_layoutCol7) {
        m_strueafAttributes.put("layoutCol7", p_layoutCol7);
    }

    public void setLayoutCol8(String p_layoutCol8) {
        m_strueafAttributes.put("layoutCol8", p_layoutCol8);
    }

    public void setPrefix(String p_prefix) {
        m_strueafAttributes.put("prefix", p_prefix);
    }

    public void setErrorposition(String p_errorposition) {
        m_strueafAttributes.put("errorposition", p_errorposition);
    }

    public void setRequiredindicator(String p_requiredindicator) {
        m_strueafAttributes.put("requiredindicator", p_requiredindicator);
    }

    public void setHelpplacement(String p_helpplacement) {
        m_strueafAttributes.put("helpplacement", p_helpplacement);
    }

    public void setHelptextkey(String p_helptextkey) {
    }
}
