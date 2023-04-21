package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.service.options.IValueChecker;
import javax.swing.*;

/**
 * @author xBlackCat
 */
class ComboBoxEditorModel<T> extends AbstractListModel<T> implements ComboBoxModel<T> {

    private final IValueChecker<T> checker;

    private T selected;

    @SuppressWarnings({ "unchecked" })
    public ComboBoxEditorModel(IValueChecker<T> checker, T selected) {
        this.checker = checker;
        if (selected != null && checker.isValueCorrect(selected)) {
            this.selected = selected;
        } else {
            this.selected = getElementAt(0);
        }
    }

    @Override
    public int getSize() {
        return checker.getPossibleValues().size();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void setSelectedItem(Object anItem) {
        T i = (T) anItem;
        if (checker.isValueCorrect(i)) {
            selected = i;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public T getElementAt(int index) {
        return checker.getPossibleValues().get(index);
    }

    @Override
    public T getSelectedItem() {
        return selected;
    }
}
