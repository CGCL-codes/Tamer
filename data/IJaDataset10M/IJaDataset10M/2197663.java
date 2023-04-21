package au.edu.diasb.chico.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An extension to the standard ListFactoryBean with a extra setter that
 * takes a tree of Collections and flattens it to form the source list.
 * 
 * @author scrawley
 */
public class ListFactoryBean extends org.springframework.beans.factory.config.ListFactoryBean {

    public void setSourceCollectionTree(Collection<?> tree) {
        List<Object> flattenedList = new ArrayList<Object>(tree.size());
        flatten(tree, flattenedList);
        setSourceList(flattenedList);
    }

    private void flatten(Collection<?> tree, List<Object> flattenedList) {
        for (Object obj : tree) {
            if (obj != null) {
                if (obj instanceof Collection<?>) {
                    flatten((Collection<?>) obj, flattenedList);
                } else {
                    flattenedList.add(obj);
                }
            }
        }
    }
}
