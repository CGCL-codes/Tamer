package com.syrtsov.ddao.mapper;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Created by: Pavel Syrtsov
 * Date: Apr 8, 2007
 * Time: 4:45:07 PM
 */
public class ArrayResultSetMapper extends CollectionResultSetMapper {

    private Class itemType;

    public ArrayResultSetMapper(ResultSetMapper resultSetMapper, Class<?> arrayClass) {
        super(resultSetMapper, Collection.class);
        itemType = arrayClass.getComponentType();
    }

    public Object getResult() {
        Collection list = getList();
        Object[] array = (Object[]) Array.newInstance(itemType, list.size());
        return list.toArray(array);
    }
}
