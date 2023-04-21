package com.jgeppert.struts2.jquery.views.freemarker.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TextFieldModel;
import com.jgeppert.struts2.jquery.components.DatePicker;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see DatePicker
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 */
public class DatePickerModel extends TextFieldModel {

    public DatePickerModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    protected Component getBean() {
        return new DatePicker(stack, req, res);
    }
}
