package com.kbframework.ui.builder;

import com.kbframework.ui.model.GridModelImpl;
import com.kbframework.ui.model.IGridModel;

/**
 * @author jin
 * </DD></DT>
 * <DT><b>日期�?</b></DT><DD>Nov 2, 2009</DD>
 * <DT><b>描述�?</b></DT><DD>描述该文件做�?�?</DD>
 * @version V0.1   
 */
public class GridPanelJsonBuilder {

    /**
	 * 初始化表格数据开头部分�??
	 * 固定格式“{tatalProperty:xx,root:[{...},{...}]}�?
	 * 其中tatalProperty与表格的js定义数据的store要一致，否则js无法辨认该数据表格的数据格式
	 * 无法展现数据表格数据�?
	 * @param dataNumber
	 */
    public GridPanelJsonBuilder(int dataNumber) {
        this.data.append("{tatalProperty:" + dataNumber + ",root:[");
    }

    /** 定义数据表格json串的载体data属�?? 为StringBuilder类为提高效率�? */
    private StringBuilder data = new StringBuilder();

    /**
	 * 增加表格数据的方法�??
	 * ����ݱ�������м������?
	 * @param gd 为实现com.taiji.egov.ui.GridModel接口的表格数据模型��ݱ������?
	 */
    public void addGridData(IGridModel gd) {
        this.data.append(((GridModelImpl) gd).getDat().substring(0, ((GridModelImpl) gd).getDat().length() - 1) + "}" + ",");
    }

    /** �?终获得表格json字符串方�? */
    public void sendGrid() {
        if (this.data.toString().lastIndexOf(",") == this.data.toString().length() - 1) {
            this.data.delete(this.data.toString().length() - 1, this.data.toString().length());
        }
        this.data.append("]}");
    }

    public String toString() {
        return this.data.toString();
    }
}
