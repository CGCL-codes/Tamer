package mipt.gui.data.choice.list;

import mipt.gui.data.choice.DataListChooser;

/**
 * @author: Administrator
 */
public class ListChooser implements DataListChooser {

    protected ListManager manager;

    protected boolean isSilent = false;

    /**
 * ListChooser constructor comment.
 */
    public ListChooser(ListManager man) {
        super();
        manager = man;
    }

    /**
 * chooseAll method comment.
 */
    public void chooseAll() {
        int size = manager.getList().getModel().getSize();
        if (size == 0) return;
        manager.getList().setSelectionInterval(0, size - 1);
    }

    /**
 * dataChanged method comment.
 */
    public void dataChanged(mipt.data.Data dataToUpdate) {
        int index = ((ListChoiceModel) manager.getModel()).indexOf(dataToUpdate);
        manager.getListModel().valueChanged(dataToUpdate, index, index);
    }

    /**
 * getChosenCount method comment.
 */
    public int getChosenCount() {
        return manager.getList().getSelectedIndices().length;
    }

    /**
 * return the first selected value or null if nothing selected
 */
    public mipt.data.Data getChosenData() {
        int index = manager.getList().getSelectedIndex();
        if (index < 0) return null;
        return getData(index);
    }

    /**
 * Return an array of the values for the selected cells.
 * The returned values are sorted in increasing index order.
 */
    public mipt.data.Data[] getChosenList() {
        int[] indices = manager.getList().getSelectedIndices();
        int n = indices.length;
        mipt.data.Data[] datas = new mipt.data.Data[n];
        for (int i = 0; i < n; i++) {
            datas[i] = getData(indices[i]);
        }
        return datas;
    }

    /**
 * Evdokimov: see getChosen*
 * @return mipt.data.Data
 * @param index int
 */
    public final mipt.data.Data getData(int index) {
        return ((ListChoiceModel) manager.getModel()).getData(index);
    }

    /**
 * makeVisible method comment.
 */
    public void makeVisible(mipt.data.Data dataToView) {
        int index = ((ListChoiceModel) manager.getModel()).indexOf(dataToView);
        if (index >= 0) {
            manager.getList().ensureIndexIsVisible(index);
        }
    }

    /**
 * sets selected and scrolls to the dataToChoose
 */
    public void setChosenData(mipt.data.Data dataToChoose) {
        int index = ((ListChoiceModel) manager.getModel()).indexOf(dataToChoose);
        if (index < 0) return;
        isSilent = true;
        manager.getList().setSelectedIndex(index);
        isSilent = false;
    }

    /**
 * selects all datatoChoose//, and then makes visible the first
 *  element at dataToChoose
 */
    public void setChosenList(mipt.data.Data[] dataToChoose) {
        isSilent = true;
        manager.getList().clearSelection();
        for (int i = 0; i < dataToChoose.length; i++) {
            setChosenData(dataToChoose[i]);
        }
        isSilent = false;
    }
}
