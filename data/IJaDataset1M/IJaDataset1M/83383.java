package org.wdcode.core.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ArrayList扩展 加个Collection可变参数的构造 用于初始化 主要用于spring的注入
 * @author WD
 * @since JDK6
 * @version 1.0 2010-05-04
 */
public final class WdArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 9049869507713029117L;

    /**
	 * 构造方法
	 * @param c Collection可变参数
	 */
    public WdArrayList(Collection<? extends E>... c) {
        super();
        for (int i = 0; i < c.length; i++) {
            addAll(c[i]);
        }
    }
}
