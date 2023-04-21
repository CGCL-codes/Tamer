package org.javagui.swt;

import java.util.Map;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.javagui.IDataProvider;
import org.javagui.swt.widgets.TableViewer;
import org.javagui.util.BeanUtils;

/**
 * @author dranson	Email: dranson@163.com
 * @date 2008-6-11
 */
public class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

    /**
	 * 表格
	 */
    private TableViewer table;

    /**
	 * 数据规则辅助类集合
	 */
    private Map<String, IDataProvider> map;

    /**
	 * 构造函数
	 * @param tableViewer
	 * @param map
	 */
    public TableLabelProvider(TableViewer tableViewer, Map<String, IDataProvider> map) {
        this.table = tableViewer;
        this.map = map;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (columnIndex == 0) return table.getCellText(columnIndex);
        String fieldName = table.getProperty(columnIndex);
        String value = BeanUtils.toDisplay(BeanUtils.invokeRead(element, fieldName));
        IDataProvider checkProvider = map == null ? null : map.get(fieldName);
        if (checkProvider != null) {
            Map<String, String> labelMap = checkProvider.getDataMap();
            if (value != null && labelMap != null) {
                String lable = labelMap.get(value);
                return lable == null ? value : lable;
            }
        }
        return value == null ? "" : value;
    }
}
