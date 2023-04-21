package cn.myapps.util.xml.converter;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.dynaform.view.ejb.Column;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ColumnConverter implements Converter {

    private String[] propNames = new String[] { "id", "name", "width", "valueScript", "view", "type", "formid", "fieldName", "orderno" };

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Column column = (Column) source;
        for (int i = 0; i < propNames.length; i++) {
            try {
                Object value = PropertyUtils.getProperty(column, propNames[i]);
                if (propNames[i].equals("view")) {
                    writer.startNode("parentView");
                } else {
                    writer.startNode(propNames[i]);
                }
                if (value != null) {
                    if (propNames[i].equals("view")) {
                        context.convertAnother(((ValueObject) value).getId());
                    } else {
                        context.convertAnother(value);
                    }
                }
                writer.endNode();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        throw new UnsupportedOperationException();
    }

    public boolean canConvert(Class type) {
        return type.equals(Column.class);
    }
}
