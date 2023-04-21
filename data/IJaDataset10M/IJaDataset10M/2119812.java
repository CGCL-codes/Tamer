package org.td4j.swing.binding;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import org.td4j.core.binding.model.ListDataProxy;
import ch.miranet.commons.TK;

public class ListController extends ListSwingWidgetController<JList> {

    private final JList widget;

    private final MyListModel model;

    public ListController(JList widget, ListDataProxy proxy) {
        super(proxy);
        this.widget = TK.Objects.assertNotNull(widget, "widget");
        this.model = new MyListModel();
        widget.setModel(model);
        setAccess();
        updateView();
    }

    protected void updateView0(List<?> newValue) {
        model.setContent(newValue);
    }

    @Override
    public JList getWidget() {
        return widget;
    }

    @Override
    protected void setAccess() {
        widget.setEnabled(canRead());
    }

    static class MyListModel extends AbstractListModel {

        private static final long serialVersionUID = 1L;

        private List<Object> list;

        private MyListModel() {
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }

        private void setContent(List<?> newValue) {
            list = newValue != null ? new ArrayList<Object>(newValue) : null;
            fireContentsChanged(this, 0, list != null ? list.size() - 1 : 0);
        }

        public int getSize() {
            return list != null ? list.size() : 0;
        }
    }
}
