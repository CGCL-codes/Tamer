package blueprint4j.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class BindDate extends BindField implements BindDateInterface {

    private static SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyyMMddHHmmssSS");

    public BindDate(String p_name, String p_description, Bindable p_bindable) {
        super(p_name, p_description, p_bindable);
    }

    public Date get() {
        try {
            if (getSerializable() == null || getSerializable().length() == 0) {
                return null;
            }
            return simple_date_format.parse(getSerializable());
        } catch (ParseException pe) {
            Log.critical.out(pe);
            pe.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public String getSerializable() {
        if (getObject() != null) {
            return simple_date_format.format((Date) getObject());
        }
        return null;
    }

    public void set(Date value) {
        setObject(value);
    }
}
