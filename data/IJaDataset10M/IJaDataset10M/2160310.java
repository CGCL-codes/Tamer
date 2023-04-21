package au.edu.monash.merc.capture.struts2.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

public class ActDateConvertor extends StrutsTypeConverter {

    private static final String YYYYMMDD_DATA_FORMAT = "yyyy-MM-dd";

    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values == null || values.length == 0) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD_DATA_FORMAT);
            Date adate = sdf.parse(values[0]);
            return new Timestamp(adate.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(Map context, Object fromObj) {
        String date = null;
        if (fromObj != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD_DATA_FORMAT);
            date = sdf.format((Date) fromObj);
        }
        return date;
    }
}
