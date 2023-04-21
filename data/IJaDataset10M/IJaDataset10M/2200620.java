package com.xy.sframe.component.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**

 *

 * <p>Title: ArrayList HashMap</p>

 * <p>Description: ����һ���̳�HashMap���࣬��һ��key��Ӧ����һ��ArrayList��

 * ��һ��key���Զ�Ӧ���ֵ����HashMap��ͬkey��ֵ��putʱ����ֵȡ���档</p>

 * <p>Copyright: Copyright (c) 2006</p>

 * <p>Company: resoft</p>

 * @author luwp

 * @version 1.0

 */
public class ArrayHashMap extends HashMap {

    public ArrayHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayHashMap() {
        super();
    }

    /**

   * put(Object key, Object value)���׼��HashMapһ�������value�����浽һ��ArrayList�С�

   * <p>

   * ����Ӧ��ע��get(Object key)����ֵ��һ��ArrayList����

   * @param key

   * @param value

   * @return

   */
    public Object put(Object key, Object value) {
        ArrayList list = (ArrayList) get(key);
        if (list == null) {
            list = new ArrayList();
            super.put(key, list);
        }
        list.add(value);
        return null;
    }

    /**

   * ��֧�ִ˲�����

   * @param t

   */
    public void putAll(Map t) {
    }

    /**

   * �����׼�ĺ����һ�µģ���������ArrayList�в������еĶ������Ƚϡ�

   * @param value

   * @return

   */
    public boolean containsValue(Object value) {
        Iterator it = values().iterator();
        ArrayList list;
        while (it.hasNext()) {
            list = (ArrayList) it.next();
            for (int i = 0; i < list.size(); i++) {
                if (value.equals(list.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
