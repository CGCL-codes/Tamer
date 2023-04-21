package net.sf.elbe.ui.actions.proxy;

import net.sf.elbe.core.events.BookmarkUpdateEvent;
import net.sf.elbe.core.events.BookmarkUpdateListener;
import net.sf.elbe.core.events.ConnectionUpdateEvent;
import net.sf.elbe.core.events.ConnectionUpdateListener;
import net.sf.elbe.core.events.EntryModificationEvent;
import net.sf.elbe.core.events.EntryUpdateListener;
import net.sf.elbe.core.events.EventRegistry;
import net.sf.elbe.core.events.SearchUpdateEvent;
import net.sf.elbe.core.events.SearchUpdateListener;
import net.sf.elbe.ui.actions.ElbeAction;
import net.sf.elbe.ui.actions.SelectionUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public abstract class ElbeActionProxy extends Action implements ISelectionChangedListener, EntryUpdateListener, SearchUpdateListener, BookmarkUpdateListener, ConnectionUpdateListener {

    protected ElbeAction action;

    protected ISelectionProvider selectionProvider;

    protected ElbeActionProxy(ISelectionProvider selectionProvider, ElbeAction action, int style) {
        super(action.getText(), style);
        this.selectionProvider = selectionProvider;
        this.action = action;
        super.setImageDescriptor(action.getImageDescriptor());
        super.setActionDefinitionId(action.getCommandId());
        this.selectionProvider.addSelectionChangedListener(this);
        EventRegistry.addConnectionUpdateListener(this);
        EventRegistry.addEntryUpdateListener(this);
        EventRegistry.addSearchUpdateListener(this);
        EventRegistry.addBookmarkUpdateListener(this);
        this.updateAction();
    }

    protected ElbeActionProxy(ISelectionProvider selectionProvider, ElbeAction action) {
        this(selectionProvider, action, Action.AS_PUSH_BUTTON);
    }

    public void dispose() {
        EventRegistry.removeConnectionUpdateListener(this);
        EventRegistry.removeEntryUpdateListener(this);
        EventRegistry.removeSearchUpdateListener(this);
        EventRegistry.removeBookmarkUpdateListener(this);
        this.selectionProvider.removeSelectionChangedListener(this);
        this.action.dispose();
        this.action = null;
    }

    public boolean isDisposed() {
        return this.action == null;
    }

    public final void entryUpdated(EntryModificationEvent entryModificationEvent) {
        if (!this.isDisposed()) {
            this.action.entryUpdated(entryModificationEvent);
            this.updateAction();
        }
    }

    public void searchUpdated(SearchUpdateEvent searchUpdateEvent) {
        if (!this.isDisposed()) {
            this.action.searchUpdated(searchUpdateEvent);
            this.updateAction();
        }
    }

    public void bookmarkUpdated(BookmarkUpdateEvent bookmarkUpdateEvent) {
        if (!this.isDisposed()) {
            this.action.bookmarkUpdated(bookmarkUpdateEvent);
            this.updateAction();
        }
    }

    public final void connectionUpdated(ConnectionUpdateEvent connectionUpdateEvent) {
        if (!this.isDisposed()) {
            this.action.connectionUpdated(connectionUpdateEvent);
            this.updateAction();
        }
    }

    public void inputChanged(Object input) {
        if (!this.isDisposed()) {
            this.action.setInput(input);
            this.selectionChanged(new SelectionChangedEvent(this.selectionProvider, new StructuredSelection()));
        }
    }

    public void selectionChanged(SelectionChangedEvent event) {
        if (!this.isDisposed()) {
            ISelection selection = event.getSelection();
            this.action.setSelectedConnections(SelectionUtils.getConnections(selection));
            this.action.setSelectedBrowserViewCategories(SelectionUtils.getBrowserViewCategories(selection));
            this.action.setSelectedEntries(SelectionUtils.getEntries(selection));
            this.action.setSelectedBrowserEntryPages(SelectionUtils.getBrowserEntryPages(selection));
            this.action.setSelectedSearchResults(SelectionUtils.getSearchResults(selection));
            this.action.setSelectedBrowserSearchResultPages(SelectionUtils.getBrowserSearchResultPages(selection));
            this.action.setSelectedBookmarks(SelectionUtils.getBookmarks(selection));
            this.action.setSelectedSearches(SelectionUtils.getSearches(selection));
            this.action.setSelectedAttributes(SelectionUtils.getAttributes(selection));
            this.action.setSelectedAttributeHierarchies(SelectionUtils.getAttributeHierarchie(selection));
            this.action.setSelectedValues(SelectionUtils.getValues(selection));
            this.updateAction();
        }
    }

    public void updateAction() {
        if (!this.isDisposed()) {
            this.setText(this.action.getText());
            this.setToolTipText(this.action.getText());
            this.setEnabled(this.action.isEnabled());
        }
    }

    public void run() {
        if (!this.isDisposed()) {
            this.action.run();
        }
    }

    public ElbeAction getAction() {
        return action;
    }
}
