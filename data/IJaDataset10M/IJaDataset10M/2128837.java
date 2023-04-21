package org.watchrecords.elements.list;

import java.util.List;
import org.apache.wicket.markup.html.form.Button;

public abstract class EditorButton extends Button {

    /**
     * 
     */
    private static final long serialVersionUID = 4793799738719560526L;

    private transient ListEditorItem<?> parent;

    public EditorButton(String id) {
        super(id);
    }

    protected final ListEditorItem<?> getItem() {
        if (parent == null) {
            parent = findParent(ListEditorItem.class);
        }
        return parent;
    }

    protected final List<?> getList() {
        return getEditor().items;
    }

    protected final ListEditor<?> getEditor() {
        return (ListEditor<?>) getItem().getParent();
    }

    @Override
    protected void onDetach() {
        parent = null;
        super.onDetach();
    }
}
