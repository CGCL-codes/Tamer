package com.dfruits.queries.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredSelection;
import com.dfruits.dto.ITransferObject;

public class QueryObjectSelection implements IStructuredSelection {

    private List<ITransferObject> selection = new ArrayList<ITransferObject>();

    public QueryObjectSelection(List<ITransferObject> selection) {
        this.selection = selection;
    }

    public QueryObjectSelection(ITransferObject initial) {
        if (initial != null) {
            selection.add(initial);
        }
    }

    public QueryObjectSelection() {
        this((ITransferObject) null);
    }

    public List<ITransferObject> getSelection() {
        return selection;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Object getFirstElement() {
        return selection == null || selection.isEmpty() ? null : selection.get(0);
    }

    public Iterator iterator() {
        return toList().iterator();
    }

    public int size() {
        return selection == null ? 0 : selection.size();
    }

    public Object[] toArray() {
        return toList().toArray();
    }

    public List toList() {
        return selection;
    }
}
