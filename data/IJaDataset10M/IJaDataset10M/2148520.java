package org.dolmen.swing.tables.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a {@link TableColumnModel TableColumnModel} with a list of bean to represent
 * Data to show
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class BeanTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(BeanTableModel.class);

    /**
	 * The list of beans
	 */
    protected List<Object> fBeans;

    public BeanTableModel() {
        fBeans = createBeanList();
    }

    /**
	 * adda list of beans
	 * 
	 * @param aList
	 * @return
	 */
    public int addAllBeans(List<?> aList) {
        for (int i = 0; i < aList.size(); i++) {
            fBeans.add(aList.get(i));
        }
        fireTableDataChanged();
        return aList.size();
    }

    /**
	 * add a Bean
	 * 
	 * @param aBean
	 * @return
	 */
    public Object addBean(Object aBean) {
        fBeans.add(aBean);
        fireTableDataChanged();
        return aBean;
    }

    /**
	 * must be implemented by subclasses to create a real list
	 * 
	 * @return
	 */
    protected abstract List<Object> createBeanList();

    /**
	 * get a Bean
	 * 
	 * @param aRowIndex
	 * @return
	 */
    public Object getBean(int aRowIndex) {
        if (aRowIndex < 0 || aRowIndex > fBeans.size()) {
            logger.warn("getBean: no bean at index " + aRowIndex);
            return null;
        } else {
            return fBeans.get(aRowIndex);
        }
    }

    public int getRowCount() {
        return fBeans.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > fBeans.size()) {
            logger.warn("rowIndex is higher than number of beans");
            return null;
        } else {
            return innerGetValueAt(fBeans.get(rowIndex), columnIndex);
        }
    }

    protected Object innerGetValueAt(Object aBean, int columnIndex) {
        return null;
    }

    /**
	 * remove all beans
	 * 
	 */
    public void removeAll() {
        fBeans.clear();
        fireTableDataChanged();
    }

    /**
	 * remove a bean
	 * 
	 * @param aBean
	 * @return
	 */
    public Object removeBean(Object aBean) {
        fBeans.remove(aBean);
        fireTableDataChanged();
        return aBean;
    }

    /**
	 * replace an existing bean
	 * 
	 * @param aRowIndex
	 * @param aBean
	 * @return
	 */
    public Object setBean(int aRowIndex, Object aBean) {
        fBeans.set(aRowIndex, aBean);
        fireTableDataChanged();
        return aBean;
    }
}
