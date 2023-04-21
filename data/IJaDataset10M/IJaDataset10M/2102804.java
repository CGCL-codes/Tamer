package org.surveyforge.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.jxpath.DynamicPropertyHandler;

/**
 * @author jgonzalez
 */
public class DataDynamicPropertyHandler implements DynamicPropertyHandler {

    public Object getProperty(Object object, String propertyName) {
        Data data = (Data) object;
        return data.getComponentData(propertyName).getData();
    }

    public String[] getPropertyNames(Object object) {
        Data data = (Data) object;
        List<String> propertyNames = new ArrayList<String>();
        for (Data componentData : data.getComponentData()) {
            propertyNames.add(componentData.getIdentifier());
        }
        return propertyNames.toArray(new String[] {});
    }

    public void setProperty(Object object, String propertyName, Object value) {
        Data data = (Data) object;
        data.getComponentData(propertyName).setData((Serializable) value);
    }
}
