package com.uusee.framework.util.query;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序条件包装器
 * 
 * @author wangrui
 *
 */
public final class SortWrapper {

    List<Sort> sorts = new ArrayList<Sort>();

    public void addSort(String property, String order) {
        sorts.add(new Sort(property, order));
    }

    public List<Sort> getSorts() {
        return sorts;
    }
}
