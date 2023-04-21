package org.ujac.web.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.ujac.form.FormField;
import org.ujac.form.Option;
import org.ujac.form.RadioGroupFormField;

/**
 * Name: RadioGroupFieldRenderer<br>
 * Description: A renderer for radio group fields.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2005/03/06 23:47:53  lauerc
 * <br>Log: Replaced type RadioButtonFormField by type RadioGroupFormField.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2274 $
 */
public class RadioGroupFieldRenderer extends BaseInputFieldRenderer {

    /**
   * @see org.ujac.web.tag.InputFieldRenderer#getFieldType()
   */
    public String getFieldType() {
        return "radiogroup";
    }

    /**
   * Renders the given input field
   * @param ctx The page context.
   * @param writer The writer to render the field to.
   * @param field The form field to render.
   * @param attributes The special attributes, defined by the custom tag.
   * @exception IOException In case the JSP output failed.
   */
    public void renderField(PageContext ctx, JspWriter writer, FormField field, Map attributes) throws IOException {
        RadioGroupFormField radioGroupField = (RadioGroupFormField) field;
        Object value = field.getValue();
        List options = radioGroupField.getOptions();
        if (options != null) {
            int numOptions = options.size();
            for (int i = 0; i < numOptions; i++) {
                Option option = (Option) options.get(i);
                writer.print("<input type=\"radio\" name=\"" + field.getName() + "\" value=\"" + option.getValue() + "\"");
                writeAttributes(ctx, writer, attributes);
                if ((value != null) && (value.equals(option.getValue()))) {
                    writer.print(" checked=\"checked\"");
                }
                writer.print("/>");
                writer.print(option.getText());
                writer.print("<br/>");
            }
        }
        writer.print("</select>");
    }
}
