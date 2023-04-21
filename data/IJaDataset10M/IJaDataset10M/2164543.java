package net.sf.springlayout.web.layout.taglib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import net.sf.springlayout.util.DateFormatUtil;

/**
 * Generates a date field bound to the specified path and field definition
 * 
 * @author Rob Monie, Adam Boas
 * 
 */
public class DateFieldTag extends InputFieldTag {

    private static final long serialVersionUID = 7966433596363629518L;

    private boolean validate = true;

    private Calendar minimumDate;

    private Calendar maximumDate;

    private Calendar defaultDate;

    private SimpleDateFormat jsDateFormatter = new SimpleDateFormat("yyyy, M -1, d");

    public DateFieldTag() {
        super();
    }

    /**
    * Sets the defaultDate for this DateFieldTag.
    * @param defaultDate Sets the defaultDate for this DateFieldTag.
    */
    public void setDefaultDate(Calendar defaultDate) {
        this.defaultDate = defaultDate;
    }

    /**
    * Sets the minimum  date that this DateField instance will allow
    * @param minimumDate that this DateField instance will allow
    */
    public void setMinimumDate(Calendar minimumDate) {
        this.minimumDate = minimumDate;
    }

    /**
    * Sets the maximum  date that this DateField instance will allow
    * @param maximumDate that this DateField instance will allow
    */
    public void setMaximumDate(Calendar maximumDate) {
        this.maximumDate = maximumDate;
    }

    protected void appendAfterFieldIcons(StringBuffer output, String confirmedPath) {
        this.appendDatePicker(output, confirmedPath);
        if (!this.renderLiveDatePicker()) {
            output.append("<script type=\"text/javascript\">");
            output.append("LAYOUT.addReadyListener(function(){LAYOUT.disableCalendar('");
            output.append(confirmedPath);
            output.append("');});");
            output.append("</script>");
        }
        super.appendAfterFieldIcons(output, confirmedPath);
    }

    /**
    * Returns a string containing HTML for rendering a date picker.
    * 
    * @param dateFormatter for converting date into string
    * @return a string containing HTML for rendering a date picker
    */
    private void appendDatePicker(StringBuffer output, String confirmedPath) {
        output.append("<script type=\"text/javascript\">");
        Locale locale = this.getRequestContext().getLocale();
        SimpleDateFormat dateFormatter = DateFormatUtil.getLocalizedDateFormat(locale);
        if (this.defaultDate != null) {
            String defaultDateField = "";
            defaultDateField = dateFormatter.format(this.defaultDate.getTime());
            output.append("if (LAYOUT.getElementValue('");
            output.append(confirmedPath);
            output.append("')==''){LAYOUT.getElement('");
            output.append(confirmedPath);
            output.append("').value='");
            output.append(defaultDateField);
            output.append("';};");
        }
        output.append("LAYOUT.addReadyListener(function(){LAYOUT.makeCalendar('");
        output.append(confirmedPath);
        output.append("'");
        output.append(getDateSpecificValidationRule());
        output.append(");});");
        output.append("</script>");
    }

    /**
    * Returns <code>true</code> if a live (interactive) date picker
    * should be rendered, or <code>false</code> of a locked one should
    * be rendered instead.
    * 
    * @return <code>true</code> if a live (interactive) date picker
    *         should be rendered
    */
    private boolean renderLiveDatePicker() {
        return !(this.isDisabled() || this.status == null);
    }

    private String getDateSpecificValidationRule() {
        StringBuffer output = new StringBuffer();
        if (this.minimumDate != null && this.maximumDate != null) {
            output.append(",new Date(");
            output.append(jsDateFormatter.format(this.minimumDate.getTime()));
            output.append("),new Date(");
            output.append(jsDateFormatter.format(this.maximumDate.getTime()));
            output.append(")");
        } else if (this.minimumDate != null) {
            output.append(",new Date(");
            output.append(jsDateFormatter.format(this.minimumDate.getTime()));
            output.append(")");
        } else if (this.maximumDate != null) {
            output.append(",null,new Date(");
            output.append(jsDateFormatter.format(this.maximumDate.getTime()));
            output.append(")");
        }
        return output.toString();
    }

    /**
    *Sets the validate for this DateFieldTag.
    * @param validate Sets the validate for this DateFieldTag.
    */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    protected String getBaseCssClass() {
        return "dateField";
    }
}
