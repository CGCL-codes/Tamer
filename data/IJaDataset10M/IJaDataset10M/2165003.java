package org.pustefixframework.webservices.jsonws.serializers;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import org.pustefixframework.webservices.jsonws.CustomJSONObject;
import org.pustefixframework.webservices.jsonws.SerializationContext;
import org.pustefixframework.webservices.jsonws.SerializationException;
import org.pustefixframework.webservices.jsonws.Serializer;

public class CalendarSerializer extends Serializer {

    @Override
    public Object serialize(SerializationContext ctx, Object obj) throws SerializationException {
        if (obj instanceof Calendar) {
            String json = "new Date(" + ((Calendar) obj).getTimeInMillis() + ")";
            return new CustomJSONObject(json);
        } else if (obj instanceof Date) {
            String json = "new Date(" + ((Date) obj).getTime() + ")";
            return new CustomJSONObject(json);
        } else throw new SerializationException("Can't serialize object of instance " + obj.getClass().getName());
    }

    @Override
    public void serialize(SerializationContext ctx, Object obj, Writer writer) throws SerializationException, IOException {
        if (obj instanceof Calendar) {
            writer.write("new Date(" + ((Calendar) obj).getTimeInMillis() + ")");
        } else if (obj instanceof Date) {
            writer.write("new Date(" + ((Date) obj).getTime() + ")");
        } else throw new SerializationException("Can't serialize object of instance " + obj.getClass().getName());
    }
}
