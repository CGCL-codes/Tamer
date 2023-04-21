package net.sf.doolin.gui.field.support.swing;

import java.util.List;
import net.sf.doolin.gui.field.FieldMultipleSelection;
import net.sf.doolin.gui.field.event.EventAction;
import net.sf.doolin.gui.field.support.MultipleSelectionSupport;
import net.sf.doolin.gui.swing.select.SelectableList;
import net.sf.doolin.gui.swing.select.SelectedEvent;
import net.sf.doolin.gui.swing.select.SelectedEventListener;

/**
 * Support for the <code>{@link FieldMultipleSelection}</code> based on a
 * <code>{@link SelectableList}</code>.
 * 
 * @author Damien Coraboeuf
 * @version $Id: SwingMultipleSelectionSupport.java,v 1.1 2007/08/07 16:47:05 guinnessman Exp $
 * @param <T>
 *            Type of item
 */
public class SwingMultipleSelectionSupport<T> extends AbstractSwingInfoFieldSupport<FieldMultipleSelection, SelectableList<T>> implements MultipleSelectionSupport<T> {

    private SelectableList<T> list;

    @Override
    protected SelectableList<T> createComponent() {
        list = new SelectableList<T>();
        list.setLabelProvider(getField().getLabelProvider());
        list.setVisibleRows(getField().getVisibleRows());
        @SuppressWarnings("unchecked") List<T> items = getField().getItemProvider().getItems();
        list.setItems(items);
        return list;
    }

    public void setItems(List<T> items) {
        list.setItems(items);
    }

    public List<T> getSelection() {
        return list.getSelectedItems();
    }

    public void setSelection(List<T> collection) {
        list.setSelectedItems(collection);
    }

    public void bindEditEvent(final EventAction eventAction) {
        list.addSelectedEventListener(new SelectedEventListener() {

            public void itemSelected(SelectedEvent event) {
                eventAction.execute(getView(), getField(), null);
            }
        });
    }
}
