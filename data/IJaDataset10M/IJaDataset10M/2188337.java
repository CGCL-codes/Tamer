package whf.framework.web.tag;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import whf.framework.ext.DictManagerImp;
import whf.framework.ext.entity.DictItem;
import whf.framework.i18n.ApplicationResource;
import whf.framework.util.BeanUtils;
import whf.framework.util.StringUtils;

/**
 * 显示存储在pageContext, request, session中的对象的属性值，并能够针对不同的属性类型显示相应的格式，如date, double等
 * @author wanghaifeng
 *
 */
public class AttributeTag extends TagSupport {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static NumberFormat nf = new DecimalFormat("0.00");

    private Object source = "i18n";

    private Object[] i18nParams;

    private String attr;

    private String format;

    private String defaultValue = "";

    private String dictKind;

    private String parameter;

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    /**
	 * @param params The i18nParams to set.
	 */
    public void setI18nParams(Object[] params) {
        i18nParams = params;
    }

    public void setDefaultValue(String emptyValue) {
        this.defaultValue = emptyValue;
    }

    public void setParameter(String param) {
        this.parameter = param;
    }

    public void setDictKind(String dictKind) {
        this.dictKind = dictKind;
    }

    private HttpServletRequest request() {
        return (HttpServletRequest) super.pageContext.getRequest();
    }

    public int doStartTag() throws JspException {
        Object obj = null;
        boolean isI18n = false;
        if (source instanceof String) {
            if (StringUtils.equalsIgnoreCase((String) source, "i18n")) {
                if (this.i18nParams == null) {
                    obj = ApplicationResource.get(pageContext.getRequest().getLocale(), this.attr);
                } else {
                    obj = ApplicationResource.get(pageContext.getRequest().getLocale(), this.attr, this.i18nParams);
                }
                if (obj == null) obj = attr;
                isI18n = true;
            } else {
                obj = super.getValue((String) source);
                if (obj == null) {
                    obj = pageContext.getAttribute((String) source);
                }
                if (obj == null) {
                    obj = request().getAttribute((String) source);
                }
                if (obj == null) {
                    obj = request().getSession().getAttribute((String) source);
                }
                if (obj == null) {
                    obj = request().getParameter((String) source);
                }
                if (obj == null) obj = source;
            }
        } else {
            obj = source;
        }
        String displayValue = "";
        if (obj != null && !isI18n) {
            Object value = null;
            if (obj instanceof Map) {
                Map map = (Map) obj;
                if (!StringUtils.isEmpty(this.attr)) {
                    String mapAttr = "";
                    int index = this.attr.indexOf(".");
                    if (index == -1) {
                        mapAttr = attr;
                        attr = "";
                    } else {
                        mapAttr = this.attr.substring(0, index);
                        this.attr = this.attr.substring(index + 1);
                    }
                    obj = map.get(mapAttr);
                }
            }
            try {
                if (!StringUtils.isEmpty(this.attr)) {
                    value = BeanUtils.getProperty(obj, attr);
                } else {
                    value = obj;
                }
            } catch (Exception e) {
            }
            if (value != null) {
                if (value instanceof Date) {
                    if (StringUtils.isEmpty(this.format)) {
                        displayValue = df.format((Date) value);
                    } else {
                        DateFormat df = new SimpleDateFormat(this.format);
                        try {
                            displayValue = df.format((Date) value);
                        } catch (Exception e) {
                        }
                    }
                } else if (value.getClass() == double.class || value.getClass() == float.class) {
                    if (StringUtils.isEmpty(this.format)) {
                        displayValue = nf.format(((Double) value).doubleValue());
                    } else {
                        double doubleValue = ((Double) value).doubleValue();
                        NumberFormat nf = new DecimalFormat(this.format);
                        try {
                            displayValue = nf.format(doubleValue);
                        } catch (Exception ignore) {
                        }
                    }
                } else {
                    displayValue = value.toString();
                }
            }
        }
        if (isI18n) {
            displayValue = obj == null ? "" : obj.toString();
        }
        if (StringUtils.isEmpty(displayValue)) {
            if (!StringUtils.isEmpty(this.parameter)) {
                displayValue = request().getParameter(this.parameter);
            }
            if (StringUtils.isEmpty(displayValue)) displayValue = this.defaultValue;
        }
        if (!StringUtils.isEmpty(displayValue) && !StringUtils.isEmpty(this.dictKind)) {
            DictItem item = DictManagerImp.getDictManager().findDictItem(this.dictKind, displayValue);
            if (item != null) {
                displayValue = item.getName();
            }
        }
        JspWriter out = super.pageContext.getOut();
        try {
            out.print(displayValue);
        } catch (Exception e) {
            throw new JspException(e);
        }
        this.format = null;
        this.attr = null;
        this.defaultValue = "";
        this.source = "i18n";
        this.i18nParams = null;
        this.dictKind = null;
        return SKIP_BODY;
    }
}
