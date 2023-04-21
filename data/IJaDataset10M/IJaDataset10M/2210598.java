package salto.fwk.mvc.taglib.table;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.servlet.jsp.JspException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import salto.fwk.mvc.format.Formatter;

/**
 * @author Jerome DENOLF / Salto Consulting
 *
 */
public class ScrollColumnInfos {

    /** titre de la colonne*/
    private String title = "";

    /** attr HTML pour le header*/
    private String headerAttributes = "";

    /** attr HTML pour le header*/
    private String htmlAttributes = "";

    /**
	 * The value to display.
	 */
    private String value;

    private String formatter = null;

    private String customFormat = null;

    private String pattern = null;

    private String property = null;

    private String content = null;

    private String width = null;

    private boolean sortable = true;

    /**
	 * @param property
	 * @param content
	 */
    public ScrollColumnInfos() {
        super();
    }

    /**
	 * @param property2
	 * @param customFormat2
	 * @param formatter2
	 * @param pattern2
	 */
    public ScrollColumnInfos(String property2, String customFormat2, String formatter2, String pattern2, String content) {
        super();
        this.property = property2;
        this.customFormat = customFormat2;
        this.formatter = formatter2;
        this.pattern = pattern2;
        this.content = content;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the headerAttributes
	 */
    public String getHeaderAttributes() {
        return headerAttributes;
    }

    /**
	 * @param headerAttributes the headerAttributes to set
	 */
    public void setHeaderAttributes(String headerAttributes) {
        this.headerAttributes = headerAttributes;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @return Renvoie sortable.
	 */
    public boolean isSortable() {
        return sortable;
    }

    /**
	 * @param sortable sortable � d�finir.
	 */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    /**
	 * 
	 * @param prop
	 * @param currentObj
	 * @param locale : pageContext.getRequest().getLocale()
	 * @return
	 * @throws JspException
	 */
    public String getPropertyValue(Object currentObj, Locale locale) throws JspException {
        String propertyValue = new String();
        if (property != null) {
            try {
                String tmp = null;
                if (this.formatter != null || this.customFormat != null) {
                    Object value = PropertyUtils.getProperty(currentObj, property);
                    if (formatter != null) {
                        Class c = Class.forName(formatter);
                        Formatter f = (Formatter) c.newInstance();
                        tmp = f.format(value, locale);
                    } else if (customFormat != null) {
                        if (value instanceof String) {
                            tmp = (String) value;
                        } else if (value instanceof java.util.Date) {
                            SimpleDateFormat formatter = new SimpleDateFormat(this.customFormat);
                            tmp = formatter.format(value);
                        } else if (value instanceof Double) {
                            DecimalFormat formatter = new DecimalFormat(this.customFormat);
                            tmp = formatter.format(value);
                        }
                    }
                }
                if (tmp == null) {
                    tmp = BeanUtils.getProperty(currentObj, property);
                }
                if (pattern == null) {
                    if (tmp == null || tmp.length() == 0) propertyValue = "&nbsp;"; else propertyValue = tmp;
                } else {
                    propertyValue = MessageFormat.format(this.pattern, new Object[] { tmp });
                }
            } catch (Exception e) {
                throw new JspException("Error accessing property " + property + " of parameter object");
            }
        }
        return propertyValue;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return the content
	 */
    public String getContent() {
        return content;
    }

    /**
	 * @param content the content to set
	 */
    public void setContent(String content) {
        this.content = content;
    }

    /**
	 * @return the width
	 */
    public String getWidth() {
        return width;
    }

    /**
	 * @param width the width to set
	 */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
	 * @return the htmlAttributes
	 */
    public String getHtmlAttributes() {
        return htmlAttributes;
    }

    /**
	 * @param htmlAttributes the htmlAttributes to set
	 */
    public void setHtmlAttributes(String htmlAttributes) {
        this.htmlAttributes = htmlAttributes;
    }
}
