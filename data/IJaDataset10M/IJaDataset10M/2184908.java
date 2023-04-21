package pe.com.bn.sach.tag;

import javax.servlet.jsp.JspException;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

public class OutTagFormat4 extends OutSupport {

    private String value_;

    private String default_;

    private String escapeXml_;

    public OutTagFormat4() {
        super();
        init();
    }

    public int doStartTag() throws JspException {
        evaluateExpressions();
        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String sRounA2dec(double nro) {
        String snro = sRounDecimal("" + nro, 4) + "";
        int pos = snro.lastIndexOf(".");
        String pd = snro.substring(pos + 1, snro.length());
        if (pd.length() == 1) {
            snro = snro + "000";
        }
        if (pd.length() == 2) {
            snro = snro + "00";
        }
        if (pd.length() == 3) {
            snro = snro + "0";
        }
        return snro;
    }

    public Double sRounDecimal(String nro1, int d) {
        double m = Math.pow(10, d);
        double nro = Double.parseDouble(nro1 + "");
        double nro2 = nro * m;
        double resp = (Math.round(nro2) / m);
        return new Double("" + resp);
    }

    public void setValue(String value_) {
        try {
            if (value_ != null) this.value_ = sRounA2dec(Double.parseDouble(value_)); else this.value_ = value_;
        } catch (Exception e) {
            this.value_ = sRounA2dec(Double.parseDouble("666"));
        }
    }

    public void setDefault(String default_) {
        this.default_ = default_;
    }

    public void setEscapeXml(String escapeXml_) {
        this.escapeXml_ = escapeXml_;
    }

    private void init() {
        value_ = default_ = escapeXml_ = null;
    }

    private void evaluateExpressions() throws JspException {
        try {
            value = ExpressionUtil.evalNotNull("out", "value", value_, Object.class, this, pageContext);
        } catch (NullAttributeException ex) {
            value = null;
        }
        try {
            def = (String) ExpressionUtil.evalNotNull("out", "default", default_, String.class, this, pageContext);
        } catch (NullAttributeException ex) {
            def = null;
        }
        escapeXml = true;
        Boolean escape = ((Boolean) ExpressionUtil.evalNotNull("out", "escapeXml", escapeXml_, Boolean.class, this, pageContext));
        if (escape != null) escapeXml = escape.booleanValue();
    }
}
