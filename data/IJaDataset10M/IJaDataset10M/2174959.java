package itSIMPLE;

import java.util.ArrayList;
import javax.swing.JComboBox;

public class ItComboBox extends JComboBox {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6052896352827411231L;

    private ArrayList<Object> dataList;

    public ItComboBox() {
        super();
        dataList = new ArrayList<Object>();
    }

    public void addItem(Object item, Object dataItem) {
        super.addItem(item);
        this.dataList.add(dataItem);
    }

    public Object getDataItem(int index) {
        return dataList.get(index);
    }

    public void removeAllItems() {
        super.removeAllItems();
        dataList = new ArrayList<Object>();
    }

    public void removeItemAt(int index) {
        super.removeItemAt(index);
        dataList.remove(index);
    }

    public void setDataItem(int index, Object dataItem) {
        dataList.set(index, dataItem);
    }
}
