package org.jackysoft.web.ui.data;

import java.util.Collection;

public class DefaultTableModel extends AbstractTableModel {

    /**
	 * @param  data  Collection<?> 集合数据 一个bean的集合
	 * @param  props PropertyTitle[]一个数组根据数组顺序显示表格列
	 * 其中是属性和标题的对应
	 * */
    public DefaultTableModel(Collection<?> data) {
        super(data);
    }
}
