package riafswing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import riaf.facade.IRowStyleable;
import riaf.models.ModelEntry;

/**
 * The {@link RListModel} class serves as internal model for components which have list of contents.
 */
public class RListModel<T extends ModelEntry> extends AbstractListModel implements ComboBoxModel, IRowStyleable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4689831910141426686L;

    protected final List<RowWrapper<T>> items = new ArrayList<RowWrapper<T>>();

    protected transient RowWrapper<T> selectedItem;

    /**
	 * If <code>true</code> when model data becomes available the selected item will be set.
	 */
    protected boolean forceSelectionIfEmpty = false;

    @Override
    public T getElementAt(int index) {
        return get(index);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getSelectedItem() {
        if (selectedItem != null) {
            return selectedItem.getData();
        }
        return null;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        RowWrapper<T> newItem = getWrapper(anItem);
        if ((selectedItem != null && !selectedItem.equals(newItem)) || (selectedItem == null && newItem != null)) {
            selectedItem = newItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public void addRowStyleClass(int row, String styleClass) {
        RowWrapper<T> wrapper = getWrapper(row);
        if (wrapper != null) {
            wrapper.addStyleClass(styleClass);
        }
    }

    @Override
    public void removeRowStyleClass(int row, String styleClass) {
        RowWrapper<T> wrapper = getWrapper(row);
        if (wrapper != null) {
            wrapper.removeRowStyleClass(styleClass);
        }
    }

    @Override
    public void clearRowStyleClasses(int row) {
        RowWrapper<T> wrapper = getWrapper(row);
        if (wrapper != null) {
            wrapper.clearRowStyleClasses();
        }
    }

    @Override
    public List<String> getRowStyleClasses(int row) {
        RowWrapper<T> wrapper = getWrapper(row);
        if (wrapper != null) {
            return wrapper.getStyleClasses();
        }
        return null;
    }

    /**
	 * Sets the flag determining should a selection be forced when model data is available 
	 * @param forceSelectionIfEmpty the forceSelectionIfEmpty to set
	 */
    public void setForceSelectionIfEmpty(boolean forceSelectionIfEmpty) {
        this.forceSelectionIfEmpty = forceSelectionIfEmpty;
    }

    /**
	 * @return the forceSelectionIfEmpty
	 */
    public boolean isForceSelectionIfEmpty() {
        return forceSelectionIfEmpty;
    }

    public void setSeletedItemById(String id) {
        setSelectedItem(findById(id));
    }

    public void setSelectedItem(int index) {
        setSelectedItem(get(index));
    }

    public T findById(String id) {
        for (RowWrapper<T> wrapper : items) {
            if (equaled(id, wrapper.getData().getId())) {
                return wrapper.getData();
            }
        }
        return null;
    }

    public T findByContent(String content) {
        for (RowWrapper<T> wrapper : items) {
            if (equaled(content, wrapper.getData().getContent())) {
                return wrapper.getData();
            }
        }
        return null;
    }

    public int indexOf(Object element) {
        if (element != null) {
            for (int i = 0; i < items.size(); i++) {
                if (element.equals(items.get(i).getData())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public T get(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        } else {
            return items.get(index).getData();
        }
    }

    public void insert(T element, int index) {
        if (element != null) {
            if (index < 0 || index >= items.size()) {
                add(element);
            } else {
                items.add(index, new RowWrapper<T>(element));
                fireIntervalAdded(this, index, index);
            }
        }
    }

    public void remove(T element) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getData().equals(element)) {
                remove(i);
                break;
            }
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= items.size()) {
            return;
        }
        items.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public void add(T element) {
        if (element != null) {
            if (items.add(new RowWrapper<T>(element))) {
                fireIntervalAdded(this, items.size() - 1, items.size() - 1);
                if (items.size() == 1 && selectedItem == null && forceSelectionIfEmpty == false) {
                    setSelectedItem(element);
                }
            }
        }
    }

    public void clear() {
        if (items.size() > 0) {
            int firstIndex = 0;
            int lastIndex = items.size() - 1;
            items.clear();
            selectedItem = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedItem = null;
        }
    }

    /**
	 * @param items the items to set
	 */
    public void setItems(Collection<T> items) {
        clear();
        addAll(items);
    }

    public void addAll(Collection<T> items) {
        if (items == null || items.isEmpty()) return;
        for (T item : items) {
            if (this.items.add(new RowWrapper<T>(item))) {
                if (items.size() == 1 && selectedItem == null && forceSelectionIfEmpty) {
                    setSelectedItem(item);
                }
            }
        }
        fireIntervalAdded(this, 0, items.size() - 1);
    }

    private RowWrapper<T> getWrapper(int index) {
        if (index > -1 && index < items.size()) return items.get(index); else return null;
    }

    private boolean equaled(Object o1, Object o2) {
        return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
    }

    private RowWrapper<T> getWrapper(Object data) {
        if (data != null) {
            for (int i = 0; i < items.size(); i++) {
                if (data.equals(items.get(i).getData())) {
                    return items.get(i);
                }
            }
        }
        return null;
    }

    private static final class RowWrapper<T> {

        private T data;

        private List<String> styleClasses;

        public RowWrapper(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void addStyleClass(String styleClass) {
            if (styleClasses == null) {
                styleClasses = new ArrayList<String>();
            }
            if (!styleClasses.contains(styleClass)) {
                styleClasses.add(styleClass);
            }
        }

        public void clearRowStyleClasses() {
            if (styleClasses != null) styleClasses.clear();
        }

        public List<String> getStyleClasses() {
            return styleClasses;
        }

        public void removeRowStyleClass(String styleClass) {
            if (styleClasses != null) {
                styleClasses.remove(styleClass);
            }
        }
    }
}
