package com.gdma.good2go.communication;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class DateParser implements ObjectFactory {

    @Override
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof String) {
            String v = (String) value;
            v = v.replace('.', ':');
            String[] parts = v.split(":");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.parseInt(parts[0]));
            c.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
            c.set(Calendar.DATE, Integer.parseInt(parts[2]));
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[3]));
            c.set(Calendar.MINUTE, Integer.parseInt(parts[5]));
            c.set(Calendar.SECOND, Integer.parseInt(parts[6]));
            c.set(Calendar.MILLISECOND, Integer.parseInt(parts[7]));
            Date ret = c.getTime();
            return ret;
        }
        return null;
    }
}
