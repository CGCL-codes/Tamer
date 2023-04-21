package se.inera.ifv.medcert.user.certificate.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.apache.commons.lang.StringUtils.*;

/**
 * @author Pär Wenåker
 *
 */
public class DateEditor extends java.beans.PropertyEditorSupport {

    @Override
    public String getAsText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date) getValue();
        return date != null ? sdf.format(date) : "";
    }

    @Override
    public void setAsText(String s) {
        if (isBlank(s)) {
            setValue(null);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            setValue(sdf.parse(s));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
