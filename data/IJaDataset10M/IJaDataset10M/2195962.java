package org.coode.oae.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.coode.oae.ui.utils.ExceptionUtils;
import org.protege.editor.core.ui.list.MListItem;
import org.protege.editor.core.ui.list.MListSectionHeader;

public class StaticListModel<I> implements ListModel {

    protected static final class MySectionHeader implements MListSectionHeader {

        String header;

        public String getName() {
            return this.header;
        }

        public boolean canAdd() {
            return true;
        }
    }

    private MySectionHeader myHeader = new MySectionHeader();

    public static final class StaticListItem<E> implements MListItem {

        private final E item;

        public E getItem() {
            return this.item;
        }

        public StaticListItem(E b) {
            ExceptionUtils.checkNullArg(b);
            this.item = b;
        }

        public String getTooltip() {
            return this.item.toString();
        }

        public boolean handleDelete() {
            return false;
        }

        public void handleEdit() {
        }

        public boolean isDeleteable() {
            return false;
        }

        public boolean isEditable() {
            return false;
        }
    }

    private final List<Object> delegate = new ArrayList<Object>();

    private final Set<ListDataListener> listeners = new HashSet<ListDataListener>();

    private final Collection<I> modelElements;

    public StaticListModel(Collection<I> elements, String sectionHeader) {
        this.modelElements = elements;
        if (sectionHeader != null) {
            this.myHeader.header = sectionHeader;
        } else {
            this.myHeader = null;
        }
    }

    protected void init() {
        this.delegate.clear();
        if (this.myHeader != null) {
            this.delegate.add(this.myHeader);
        }
        for (I bm : this.modelElements) {
            this.delegate.add(new StaticListItem<I>(bm));
        }
        notifyListeners();
    }

    protected void notifyListeners() {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.delegate.size() - 1);
        for (ListDataListener l : this.listeners) {
            l.contentsChanged(event);
        }
    }

    public void addListDataListener(ListDataListener l) {
        ExceptionUtils.checkNullArg(l);
        this.listeners.add(l);
    }

    public Object getElementAt(int index) {
        return this.delegate.get(index);
    }

    public int getSize() {
        return this.delegate.size();
    }

    public void removeListDataListener(ListDataListener l) {
        this.listeners.remove(l);
    }
}
