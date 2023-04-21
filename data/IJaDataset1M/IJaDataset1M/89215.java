package net.sf.elbe.ui.editors.searchresult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.elbe.core.events.EntryModificationEvent;
import net.sf.elbe.core.events.EntryUpdateListener;
import net.sf.elbe.core.events.EventRegistry;
import net.sf.elbe.core.internal.model.Attribute;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.ISearchResult;
import net.sf.elbe.core.model.ModelModificationException;
import net.sf.elbe.core.model.AttributeHierachie;
import net.sf.elbe.ui.ELBEUIConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;

public class SearchResultEditorCursor extends TableCursor implements ISelectionProvider, EntryUpdateListener {

    private TableViewer viewer;

    public SearchResultEditorCursor(TableViewer viewer) {
        super(viewer.getTable(), SWT.NONE);
        this.viewer = viewer;
        setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION));
        setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        EventRegistry.addEntryUpdateListener(this);
        initSelectionChecker();
        initSelectionProvider();
    }

    private void initSelectionChecker() {
        this.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                checkSelection();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                checkSelection();
            }

            private void checkSelection() {
                if (viewer != null && viewer.getColumnProperties() != null && viewer.getColumnProperties().length - 1 < getColumn()) {
                    setSelection(getRow(), viewer.getColumnProperties().length - 1);
                }
            }
        });
    }

    public boolean setFocus() {
        return super.setFocus();
    }

    private void initSelectionProvider() {
        this.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                for (Iterator it = selectionChangesListenerList.iterator(); it.hasNext(); ) {
                    ((ISelectionChangedListener) it.next()).selectionChanged(new SelectionChangedEvent(SearchResultEditorCursor.this, getSelection()));
                }
            }
        });
    }

    public void dispose() {
        EventRegistry.removeEntryUpdateListener(this);
        this.viewer = null;
        super.dispose();
    }

    public void entryUpdated(EntryModificationEvent event) {
        this.viewer.refresh();
        this.redraw();
    }

    public String getSelectedProperty() {
        if (!this.isDisposed() && this.getRow() != null && this.viewer != null && this.viewer.getColumnProperties() != null && this.viewer.getColumnProperties().length >= this.getColumn() + 1) {
            String property = (String) this.viewer.getColumnProperties()[this.getColumn()];
            return property;
        }
        return null;
    }

    public AttributeHierachie getSelectedAttributeHierarchie() {
        if (!this.isDisposed() && this.getRow() != null && this.viewer != null && this.viewer.getColumnProperties() != null && this.viewer.getColumnProperties().length >= this.getColumn() + 1) {
            Object o = this.getRow().getData();
            String property = (String) this.viewer.getColumnProperties()[this.getColumn()];
            if (o instanceof ISearchResult && !ELBEUIConstants.DN.equals(property)) {
                ISearchResult sr = (ISearchResult) o;
                AttributeHierachie ah = sr.getAttributeWithSubtypes(property);
                if (ah == null) {
                    try {
                        ah = new AttributeHierachie(sr.getEntry(), property, new IAttribute[] { new Attribute(sr.getEntry(), property) });
                    } catch (ModelModificationException e) {
                        e.printStackTrace();
                    }
                }
                return ah;
            }
        }
        return null;
    }

    public ISearchResult getSelectedSearchResult() {
        if (!this.isDisposed() && this.getRow() != null) {
            Object o = this.getRow().getData();
            if (o instanceof ISearchResult) {
                return (ISearchResult) o;
            }
        }
        return null;
    }

    private List selectionChangesListenerList = new ArrayList();

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (!this.selectionChangesListenerList.contains(listener)) {
            this.selectionChangesListenerList.add(listener);
        }
    }

    public ISelection getSelection() {
        ISearchResult searchResult = this.getSelectedSearchResult();
        AttributeHierachie ah = this.getSelectedAttributeHierarchie();
        String property = this.getSelectedProperty();
        List list = new ArrayList();
        if (searchResult != null) list.add(searchResult);
        if (ah != null) list.add(ah);
        if (property != null) {
            list.add(property);
        }
        return new StructuredSelection(list);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        if (this.selectionChangesListenerList.contains(listener)) {
            this.selectionChangesListenerList.remove(listener);
        }
    }

    public void setSelection(ISelection selection) {
    }
}
