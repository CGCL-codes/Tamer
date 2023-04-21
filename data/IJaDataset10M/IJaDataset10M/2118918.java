package org.judo.filter;

import java.util.ArrayList;
import java.util.List;
import org.judo.propertyobject.PropertyObject;

public class ResultsFilter {

    public static List filterResults(Filter filter, List list) {
        ArrayList newList = new ArrayList();
        for (Object obj : list) {
            PropertyObject newObj = new PropertyObject();
            PropertyObject oldObj = (PropertyObject) obj;
            newObj.copyProps(oldObj);
            newList.add(filter.filter(newObj));
        }
        return newList;
    }
}
