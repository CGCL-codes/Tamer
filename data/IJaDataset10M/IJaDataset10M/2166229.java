package edu.vt.eng.swat.workflow.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.vt.eng.swat.workflow.db.base.entity.Category2;
import edu.vt.eng.swat.workflow.db.base.entity.DescriptionEntity;

public class DataHelper {

    public static List<String> convert(List<? extends DescriptionEntity<?>> toConvertList) {
        List<String> list = new ArrayList<String>();
        if (toConvertList != null) {
            for (DescriptionEntity<?> item : toConvertList) {
                list.add(item.getDescription());
            }
        }
        return list;
    }

    public static Map<Long, List<Category2>> convertToMap(List<Category2> category2List) {
        Map<Long, List<Category2>> map = new HashMap<Long, List<Category2>>();
        if (category2List != null) {
            for (Category2 item : category2List) {
                List<Category2> list = addToList(map.get(item.getCategory1Id()), item);
                map.put(item.getCategory1Id(), list);
            }
        }
        return map;
    }

    private static List<Category2> addToList(List<Category2> list, Category2 element) {
        if (list == null) {
            list = new ArrayList<Category2>();
        }
        list.add(element);
        return list;
    }
}
