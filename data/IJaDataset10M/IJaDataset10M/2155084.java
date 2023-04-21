package com.bluesky.plum.uimodels.render.html.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.bluesky.javawebbrowser.domain.html.tags.form.input.Text;
import com.bluesky.plum.richdomain.RichDomainField;

public class HDateTimePicker extends com.bluesky.plum.uimodels.standard.components.DateTimePicker {

    Text textbox;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

    public HDateTimePicker(RichDomainField field) {
        super(field);
        textbox = new Text();
        setDatetime((Date) getField().value);
    }

    @Override
    public Object getNativeComponent() {
        textbox.setName(getId());
        return textbox;
    }

    @Override
    public Date getDatetime() {
        try {
            return dateFormat.parse(textbox.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setDatetime(Date datetime) {
        textbox.setValue(dateFormat.format(datetime));
    }

    public static void main(String[] args) {
        String dateString = "2001/03/09";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(dateString);
            System.out.println("Converted string to date : " + convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
