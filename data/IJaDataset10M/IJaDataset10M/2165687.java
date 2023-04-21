package it.negro.jajb.conversions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import it.dangelo.javabinding.converters.ConversionException;
import it.dangelo.javabinding.converters.Converter;
import it.negro.jajb.json.JSONArray;

public class MultiArrayFloatConverter implements Converter {

    public Object marshall(Object arg0) throws ConversionException {
        JSONArray jsonArray = new JSONArray();
        try {
            JSONArray element = new JSONArray();
            if (arg0.getClass().isArray() && !(arg0.getClass().getComponentType().isPrimitive())) {
                for (int i = 0; i < ((Object[]) arg0).length; i++) {
                    element = (JSONArray) this.marshall(((Object[]) arg0)[i]);
                    jsonArray.put(i, element);
                }
            } else {
                for (int i = 0; i < ((float[]) arg0).length; i++) jsonArray.put(i, ((float[]) arg0)[i]);
            }
        } catch (Exception e) {
            throw new ConversionException(e.getMessage(), e);
        }
        return jsonArray;
    }

    public Object unmarshall(Object arg0) throws ConversionException {
        ArrayList<Object> list = null;
        Class<?> componentType = null;
        try {
            Object element = null;
            JSONArray jsonArray = (JSONArray) arg0;
            list = new ArrayList<Object>();
            componentType = Integer.TYPE;
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.get(i) instanceof JSONArray) {
                    element = this.unmarshall(jsonArray.get(i));
                    list.add(element);
                    componentType = element.getClass();
                } else {
                    element = new float[jsonArray.length()];
                    for (int j = 0; j < jsonArray.length(); j++) ((float[]) element)[j] = (float) jsonArray.getDouble(i);
                    return element;
                }
            }
        } catch (Exception e) {
            throw new ConversionException(e.getMessage(), e);
        }
        return list.toArray((Object[]) Array.newInstance(componentType, list.size()));
    }
}
