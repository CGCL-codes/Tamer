package org.identifylife.key.store.oxm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author dbarnier
 *
 */
public class XsDateTimeAdapter extends XmlAdapter<String, Date> {

    private static DateFormat format = null;

    static {
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Date unmarshal(String date) throws Exception {
        return format.parse(date);
    }

    public String marshal(Date date) throws Exception {
        return format.format(date);
    }
}
