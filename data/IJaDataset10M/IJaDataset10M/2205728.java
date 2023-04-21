package ru.vsu.cs.piit.vokod.TableModels;

import javax.swing.DefaultComboBoxModel;
import ru.vsu.cs.piit.vokod.IListListener;
import ru.vsu.cs.piit.vokod.ObservableArrayList;

/**
 *
 * @author Галчонок
 */
public class CBModel extends DefaultComboBoxModel {

    ObservableArrayList list;

    public CBModel(ObservableArrayList data) {
        super();
        if (data == null) data = new ObservableArrayList();
        this.list = data;
        list.AddListener(new IListListener() {

            public void doSave(Object object) {
                fireIntervalAdded(this, 0, list.size());
                fireContentsChanged(this, 0, list.size());
                setSelectedItem(object);
            }

            public void doRemove(Object object) {
                fireIntervalRemoved(this, 0, list.size());
                fireContentsChanged(this, 0, list.size());
            }
        });
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public int getSize() {
        return (list == null ? 0 : list.size());
    }
}
