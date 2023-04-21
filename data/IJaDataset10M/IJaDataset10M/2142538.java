package org.openconcerto.ui.component;

import org.openconcerto.ui.component.combo.ISearchableCombo;
import org.openconcerto.utils.change.CollectionChangeEvent;
import org.openconcerto.utils.change.IListDataEvent;
import org.openconcerto.utils.model.DefaultIMutableListModel;
import org.openconcerto.utils.model.Reloadable;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * An IMutableListModel with items from {@link ITextComboCache}.
 * 
 * @author Sylvain CUAZ
 * @see #load(Runnable)
 */
public class IComboCacheListModel extends DefaultIMutableListModel<String> implements Reloadable {

    private final ITextComboCache cache;

    private final ListDataListener l;

    public IComboCacheListModel(final ITextComboCache c) {
        this.cache = c;
        this.l = new ListDataListener() {

            @SuppressWarnings("unchecked")
            public void contentsChanged(ListDataEvent e) {
                if (e.getIndex0() < 0) return;
                final CollectionChangeEvent evt = ((IListDataEvent) e).getCollectionChangeEvent();
                this.remove(evt);
                this.add(evt.getItemsAdded());
            }

            public void intervalAdded(ListDataEvent e) {
                this.add(getList().subList(e.getIndex0(), e.getIndex1() + 1));
            }

            public void intervalRemoved(ListDataEvent e) {
                this.remove(((IListDataEvent) e).getCollectionChangeEvent());
            }

            private void add(Collection<String> toAdd) {
                for (final String s : toAdd) {
                    IComboCacheListModel.this.cache.addToCache(s);
                }
            }

            @SuppressWarnings("unchecked")
            private void remove(CollectionChangeEvent evt) {
                for (final String s : (Collection<String>) evt.getItemsRemoved()) IComboCacheListModel.this.cache.deleteFromCache(s);
            }
        };
    }

    public final void load(final Runnable r, final boolean readCache) {
        if (this.cache.isValid()) {
            new SwingWorker<List<String>, Object>() {

                @Override
                protected List<String> doInBackground() throws Exception {
                    return IComboCacheListModel.this.cache.loadCache(readCache);
                }

                @Override
                protected void done() {
                    removeListDataListener(IComboCacheListModel.this.l);
                    removeAllElements();
                    try {
                        addAll(get());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    addListDataListener(IComboCacheListModel.this.l);
                    if (r != null) r.run();
                }
            }.execute();
        }
    }

    @Override
    public void reload() {
        this.load(null, false);
    }

    /**
     * Load this and only afterwards call
     * {@link ISearchableCombo#initCache(org.openconcerto.utils.model.IListModel)}.
     * 
     * @param combo the combo to initialise.
     */
    public void initCacheLater(final ISearchableCombo<String> combo) {
        this.load(new Runnable() {

            @Override
            public void run() {
                combo.initCache(IComboCacheListModel.this);
            }
        }, true);
    }
}
