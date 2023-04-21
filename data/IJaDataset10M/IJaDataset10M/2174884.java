package org.posterita.taglib;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.apache.struts.taglib.bean.WriteTag;
import org.apache.struts.util.RequestUtils;
import org.posterita.core.TmkJSPEnv;
import org.posterita.model.MBank;

public class BankName extends WriteTag {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public String formatValue(Object valueToFormat) throws JspException {
        if (valueToFormat instanceof Integer == false) throw new JspException("Expected java.lang.Integer, obtained" + valueToFormat.getClass().getName());
        Integer id = (Integer) valueToFormat;
        return format(id);
    }

    private String format(Integer integerValueToFormat) {
        if (integerValueToFormat == null) return "";
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Properties ctx = TmkJSPEnv.getCtx(request);
        MBank bankName = new MBank(ctx, integerValueToFormat.intValue(), null);
        return bankName.getName();
    }

    @SuppressWarnings("deprecation")
    public int doStartTag() throws JspException {
        if (ignore) {
            if (RequestUtils.lookup(pageContext, name, scope) == null) return (SKIP_BODY);
        }
        Object value = RequestUtils.lookup(pageContext, name, property, scope);
        if (value == null) return (SKIP_BODY);
        String output = formatValue(value);
        org.apache.struts.util.ResponseUtils.write(pageContext, output);
        return (SKIP_BODY);
    }
}
