package de.xirp.ui.dock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IMemento;
import de.xirp.ui.widgets.custom.XTabFolder;
import de.xirp.ui.widgets.custom.XTabItem;
import de.xirp.util.I18n;
import de.xirp.util.II18nHandler;

/**
 * 
 */
public class PartTabFolder extends LayoutPart implements ILayoutContainer {

    private static int tabLocation = -1;

    /**
	 * 
	 */
    @SuppressWarnings("unused")
    private XTabFolder tabFolder;

    /**
	 * 
	 */
    private Map<XTabItem, ILayoutPart> mapTabToPart = new HashMap<XTabItem, ILayoutPart>();

    /**
	 * 
	 */
    private Map<ILayoutPart, CTabPartDragDrop> mapPartToDragMonitor = new HashMap<ILayoutPart, CTabPartDragDrop>();

    /**
	 * 
	 */
    private boolean assignFocusOnSelection = true;

    /**
	 * 
	 */
    private ILayoutPart current;

    /**
	 * inactiveCurrent is only used when restoring the persisted state<br>
	 * of perspective on startup.<br>
	 */
    private ILayoutPart inactiveCurrent;

    /**
	 * 
	 */
    private Composite parent;

    /**
	 * 
	 */
    private boolean active = false;

    /**
	 * listen for mouse down on close button of tabitem
	 */
    private final CTabFolder2Adapter folderCloseListener = new CTabFolder2Adapter() {

        @Override
        public void close(CTabFolderEvent e) {
            ILayoutPart item = mapTabToPart.get(e.item);
            if (item != null) {
                remove(item);
            }
        }
    };

    /**
	 * listen for mouse down on tab to set focus.
	 */
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseDown(MouseEvent e) {
            CTabItem newItem = tabFolder.getItem(new Point(e.x, e.y));
            if (newItem != null) {
                CTabItem oldItem = tabFolder.getSelection();
                if (newItem != oldItem) {
                    return;
                }
            }
            if (PartTabFolder.this.current != null) {
                PartTabFolder.this.current.setFocus();
            }
        }
    };

    /**
	 * 
	 */
    protected class TabInfo {

        /**
		 * 
		 */
        public String tabText;

        /**
		 * 
		 */
        public ILayoutPart part;

        public boolean isKey = false;

        public II18nHandler handler;

        public Object[] args = null;
    }

    /**
	 * 
	 */
    private TabInfo[] invisibleChildren;

    /**
	 * @param location
	 */
    public PartTabFolder(int location) {
        super("PartTabFolder");
        setID(this.toString());
        if (tabLocation == -1) {
            tabLocation = location;
        }
    }

    /**
	 * @see LayoutPart#isViewPane()
	 */
    @Override
    public boolean isViewPane() {
        return false;
    }

    /**
	 * Updates the tab labels to reflect new part names.<br>
	 */
    public void updateTabs() {
        for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
            ILayoutPart part = entry.getValue();
            XTabItem item = entry.getKey();
            String text = part.getName();
            if (text.equals(item.getText())) {
                item.setText(text);
            }
        }
    }

    /**
	 * Add a part at an index.<br>
	 * 
	 * @param name
	 * @param index
	 * @param part
	 */
    public void add(String name, int index, ILayoutPart part, boolean isKey, II18nHandler handler, Object... args) {
        if (active && !(part instanceof PartPlaceholder)) {
            XTabItem tab = createPartTab(part, name, index, isKey, handler, args);
            index = tabFolder.indexOf(tab);
            setSelection(index);
        } else {
            TabInfo info = new TabInfo();
            info.tabText = name;
            info.part = part;
            info.isKey = isKey;
            info.handler = handler;
            info.args = args;
            invisibleChildren = arrayAdd(invisibleChildren, info, index);
            if (active) {
                part.setContainer(this);
            }
        }
    }

    /**
	 * @param child
	 */
    public void add(ILayoutPart child) {
        int index = getItemCount();
        String key = child.getNameKey();
        add(key, index, child, true, child.getI18nHandler(), child.getI18nArgs());
    }

    /**
	 * @return boolean
	 * @see ILayoutContainer#allowsBorder There is already a border
	 *      around the tab folder so no need for<br>
	 *      one from the parts.<br>
	 */
    public boolean allowsBorder() {
        return mapTabToPart.size() <= 1;
    }

    /**
	 * @param array
	 * @param item
	 * @param index
	 * @return TabInfo[]
	 */
    private TabInfo[] arrayAdd(TabInfo[] array, TabInfo item, int index) {
        if (item == null) {
            return array;
        }
        TabInfo[] result = null;
        if (array == null) {
            result = new TabInfo[1];
            result[0] = item;
        } else {
            if (index >= array.length) {
                index = array.length;
            }
            result = new TabInfo[array.length + 1];
            System.arraycopy(array, 0, result, 0, index);
            result[index] = item;
            System.arraycopy(array, index, result, index + 1, array.length - index);
        }
        return result;
    }

    /**
	 * @param array
	 * @param item
	 * @return TabInfo[]
	 */
    private TabInfo[] arrayRemove(TabInfo[] array, ILayoutPart item) {
        if (item == null) {
            return array;
        }
        TabInfo[] result = null;
        int index = -1;
        for (int i = 0, length = array.length; i < length; i++) {
            if (item == array[i].part) {
                index = i;
                break;
            }
        }
        if (index == -1) return array;
        if (array.length > 1) {
            result = new TabInfo[array.length - 1];
            System.arraycopy(array, 0, result, 0, index);
            System.arraycopy(array, index + 1, result, index, result.length - index);
        }
        return result;
    }

    /**
	 * Set the default bounds of a page in a CTabFolder.<br>
	 * 
	 * @param folder
	 * @return Rectangle
	 */
    public static Rectangle calculatePageBounds(XTabFolder folder) {
        if (folder == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        Rectangle bounds = folder.getBounds();
        Rectangle offset = folder.getClientArea();
        bounds.x += offset.x;
        bounds.y += offset.y;
        bounds.width = offset.width;
        bounds.height = offset.height;
        return bounds;
    }

    /**
	 * @see LayoutPart#createControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void createControl(Composite parent) {
        if (tabFolder != null) {
            return;
        }
        this.parent = parent;
        tabFolder = new XTabFolder(parent, tabLocation | SWT.BORDER);
        tabFolder.setSimple(false);
        tabFolder.setTabPosition(SWT.BOTTOM);
        tabFolder.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                ILayoutPart item = mapTabToPart.get(e.item);
                if (item != null) {
                    setSelection(item);
                    if (assignFocusOnSelection) {
                        item.setFocus();
                    }
                }
            }
        });
        tabFolder.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                setControlSize();
            }
        });
        if ((tabLocation & SWT.CLOSE) != 0) {
            tabFolder.addCTabFolder2Listener(folderCloseListener);
        }
        tabFolder.addMouseListener(this.mouseListener);
        tabFolder.setData(this);
        if (invisibleChildren != null) {
            TabInfo[] stillInactive = new TabInfo[0];
            int tabCount = 0;
            for (int i = 0, length = invisibleChildren.length; i < length; i++) {
                TabInfo tabInfo = invisibleChildren[i];
                if (tabInfo.part instanceof PartPlaceholder) {
                    tabInfo.part.setContainer(this);
                    TabInfo[] newStillInactive = new TabInfo[stillInactive.length + 1];
                    System.arraycopy(stillInactive, 0, newStillInactive, 0, stillInactive.length);
                    newStillInactive[stillInactive.length] = tabInfo;
                    stillInactive = newStillInactive;
                } else {
                    createPartTab(tabInfo.part, tabInfo.tabText, tabCount, tabInfo.isKey, tabInfo.handler, tabInfo.args);
                    ++tabCount;
                }
            }
            invisibleChildren = stillInactive;
        }
        active = true;
        if (getItemCount() > 0) {
            int newPage = 0;
            if (current != null) {
                newPage = indexOf(current);
            }
            setSelection(newPage);
        }
    }

    /**
	 * @param part
	 * @param tabName
	 * @param tabIndex
	 * @param isKey 
	 * @param handler 
	 * @param args 
	 * @return CTabItem
	 */
    private XTabItem createPartTab(ILayoutPart part, String tabName, int tabIndex, boolean isKey, II18nHandler handler, Object[] args) {
        XTabItem tabItem;
        if (tabIndex < 0) {
            tabItem = new XTabItem(this.tabFolder, SWT.NONE, handler);
        } else {
            tabItem = new XTabItem(this.tabFolder, SWT.NONE, tabIndex, handler);
        }
        if (isKey) {
            tabItem.setTextForLocaleKey(tabName, args);
        } else {
            tabItem.setText(tabName);
        }
        mapTabToPart.put(tabItem, part);
        part.createControl(this.parent);
        part.setContainer(this);
        if (mapTabToPart.size() == 2) {
            Iterator<ILayoutPart> parts = mapTabToPart.values().iterator();
            ((LayoutPart) parts.next()).setContainer(this);
            ((LayoutPart) parts.next()).setContainer(this);
        }
        return tabItem;
    }

    /**
	 * Remove the ability to d&d using the tab.<br>
	 * 
	 * @param part
	 */
    public void disableDrag(ILayoutPart part) {
        PartDragDrop partDragDrop = mapPartToDragMonitor.get(part);
        if (partDragDrop != null) {
            partDragDrop.dispose();
            mapPartToDragMonitor.remove(part);
        }
        if (mapPartToDragMonitor.size() == 1) {
            partDragDrop = mapPartToDragMonitor.get(this);
            if (partDragDrop != null) {
                partDragDrop.dispose();
                mapPartToDragMonitor.remove(this);
            }
        }
    }

    /**
	 * @see LayoutPart#dispose()
	 */
    @Override
    public void dispose() {
        if (!active) {
            return;
        }
        TabInfo[] newInvisibleChildren = new TabInfo[mapTabToPart.size()];
        if (invisibleChildren != null) {
            newInvisibleChildren = new TabInfo[newInvisibleChildren.length + invisibleChildren.length];
            System.arraycopy(invisibleChildren, 0, newInvisibleChildren, mapTabToPart.size(), invisibleChildren.length);
        }
        for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
            XTabItem item = entry.getKey();
            ILayoutPart part = entry.getValue();
            TabInfo info = new TabInfo();
            info.tabText = item.getText();
            info.part = part;
            newInvisibleChildren[tabFolder.indexOf(item)] = info;
            disableDrag(part);
        }
        invisibleChildren = newInvisibleChildren;
        if (invisibleChildren != null) {
            for (TabInfo info : invisibleChildren) {
                info.part.setContainer(null);
            }
        }
        mapTabToPart.clear();
        if (tabFolder != null) {
            tabFolder.dispose();
        }
        tabFolder = null;
        active = false;
    }

    /**
	 * Enable the view pane to be d&d via its tab.<br>
	 * 
	 * @param pane
	 * @param listener
	 */
    public void enableDrag(ILayoutPart pane, IPartDropListener listener) {
        if (mapPartToDragMonitor.containsKey(pane)) {
            return;
        }
        CTabItem tab = getTab(pane);
        if (tab == null) {
            return;
        }
        CTabPartDragDrop dragSource = new CTabPartDragDrop(pane, this.tabFolder, tab);
        mapPartToDragMonitor.put(pane, dragSource);
        dragSource.addDropListener(listener);
        if (mapPartToDragMonitor.size() == 1) {
            dragSource = new CTabPartDragDrop(this, this.tabFolder, null);
            mapPartToDragMonitor.put(this, dragSource);
            dragSource.addDropListener(listener);
        }
    }

    /**
	 * Open the tracker to allow the user to move the specified part<br>
	 * using keyboard.<br>
	 * 
	 * @param part
	 */
    public void openTracker(LayoutPart part) {
        CTabPartDragDrop dnd = mapPartToDragMonitor.get(part);
        dnd.openTracker();
    }

    /**
	 * @see LayoutPart#getBounds()
	 */
    @Override
    public Rectangle getBounds() {
        return tabFolder.getBounds();
    }

    /**
	 * @see LayoutPart#setBounds(org.eclipse.swt.graphics.Rectangle)
	 */
    @Override
    public void setBounds(Rectangle value) {
        if (tabFolder != null) {
            tabFolder.setBounds(value);
        }
        setControlSize();
    }

    /**
	 * @see LayoutPart#getMinimumHeight()
	 */
    @Override
    public int getMinimumHeight() {
        if (current == null || tabFolder == null || tabFolder.isDisposed()) {
            return super.getMinimumHeight();
        }
        if (getItemCount() > 1) {
            Rectangle trim = tabFolder.computeTrim(0, 0, 0, current.getMinimumHeight());
            return trim.height;
        } else {
            return current.getMinimumHeight();
        }
    }

    /**
	 * @see ILayoutContainer#getChildren()
	 */
    public ILayoutPart[] getChildren() {
        ILayoutPart[] children = new ILayoutPart[0];
        if (invisibleChildren != null) {
            children = new LayoutPart[invisibleChildren.length];
            for (int i = 0, length = invisibleChildren.length; i < length; i++) {
                children[i] = invisibleChildren[i].part;
            }
        }
        int count = mapTabToPart.size();
        if (count > 0) {
            int index = children.length;
            ILayoutPart[] newChildren = new ILayoutPart[children.length + count];
            System.arraycopy(children, 0, newChildren, 0, children.length);
            children = newChildren;
            for (int nX = 0; nX < count; nX++) {
                CTabItem tabItem = tabFolder.getItem(nX);
                children[index] = mapTabToPart.get(tabItem);
                index++;
            }
        }
        return children;
    }

    /**
	 * @see LayoutPart#getControl()
	 */
    @Override
    public Control getControl() {
        return tabFolder;
    }

    /**
	 * Answer the number of children.<br>
	 * 
	 * @return int
	 */
    public int getItemCount() {
        if (active) {
            return tabFolder.getItemCount();
        } else if (invisibleChildren != null) {
            return invisibleChildren.length;
        } else {
            return 0;
        }
    }

    /**
	 * Get the parent control.<br>
	 * 
	 * @return Composite
	 */
    public Composite getParent() {
        return tabFolder.getParent();
    }

    /**
	 * @return int
	 */
    public int getSelection() {
        if (!active) {
            return 0;
        }
        return tabFolder.getSelectionIndex();
    }

    /**
	 * Returns the tab for a part.<br>
	 * 
	 * @param child
	 * @return CTabItem
	 */
    private CTabItem getTab(ILayoutPart child) {
        for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
            XTabItem tab = entry.getKey();
            if (entry.getValue() == child) return tab;
        }
        return null;
    }

    /**
	 * Returns the visible child.<br>
	 * 
	 * @return ILayoutPart
	 */
    public ILayoutPart getVisiblePart() {
        if (current == null) {
            return inactiveCurrent;
        }
        return current;
    }

    /**
	 * @param item
	 * @return int
	 */
    public int indexOf(ILayoutPart item) {
        for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
            XTabItem tab = entry.getKey();
            ILayoutPart part = entry.getValue();
            if (part.equals(item)) return tabFolder.indexOf(tab);
        }
        return 0;
    }

    /**
	 * @see ILayoutContainer#remove
	 */
    public void remove(ILayoutPart child) {
        if (active && !(child instanceof PartPlaceholder)) {
            for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
                XTabItem key = entry.getKey();
                if (entry.getValue().equals(child)) {
                    removeTab(key);
                    break;
                }
            }
        } else if (invisibleChildren != null) {
            invisibleChildren = arrayRemove(invisibleChildren, child);
        }
        if (active) {
            child.setVisible(false);
            child.setContainer(null);
        }
    }

    /**
	 * @param tab
	 */
    private void removeTab(CTabItem tab) {
        ILayoutPart part = mapTabToPart.get(tab);
        if (part != null) {
            disableDrag(part);
        }
        assignFocusOnSelection = false;
        mapTabToPart.remove(tab);
        tab.dispose();
        assignFocusOnSelection = true;
        if (mapTabToPart.size() == 1) {
            Iterator<ILayoutPart> parts = mapTabToPart.values().iterator();
            ((LayoutPart) parts.next()).setContainer(this);
        }
    }

    /**
	 * Reorder the tab representing the specified pane. If a tab<br>
	 * exists under the specified x,y location, then move the tab<br>
	 * before it, otherwise place it as the last tab.<br>
	 * 
	 * @param pane
	 * @param x
	 * @param y
	 */
    public void reorderTab(ILayoutPart pane, int x, int y) {
        CTabItem sourceTab = getTab(pane);
        if (sourceTab == null) {
            return;
        }
        Point location = new Point(1, 1);
        if ((tabFolder.getStyle() & SWT.BOTTOM) != 0) {
            location.y = tabFolder.getSize().y - 4;
        }
        if (x > location.x) {
            location.x = x;
        }
        CTabItem targetTab = tabFolder.getItem(location);
        if (targetTab == null) {
            if (tabFolder.indexOf(sourceTab) != tabFolder.getItemCount() - 1) {
                reorderTab(pane, sourceTab, -1);
            }
            return;
        }
        if (targetTab == sourceTab) {
            return;
        }
        int sourceIndex = tabFolder.indexOf(sourceTab);
        int targetIndex = tabFolder.indexOf(targetTab);
        if (sourceIndex == targetIndex - 1) {
            return;
        }
        reorderTab(pane, sourceTab, targetIndex);
    }

    /**
	 * Reorder the tab representing the specified pane.<br>
	 * 
	 * @param pane
	 * @param sourceTab
	 * @param newIndex
	 */
    private void reorderTab(ILayoutPart pane, CTabItem sourceTab, int newIndex) {
        boolean wasVisible = (tabFolder.getSelection() == sourceTab);
        XTabItem newTab;
        if (newIndex < 0) {
            newTab = new XTabItem(tabFolder, SWT.NONE);
        } else {
            newTab = new XTabItem(tabFolder, SWT.NONE, newIndex);
        }
        mapTabToPart.put(newTab, pane);
        CTabPartDragDrop partDragDrop = mapPartToDragMonitor.get(pane);
        partDragDrop.setTab(newTab);
        String sourceLabel = sourceTab.getText();
        mapTabToPart.remove(sourceTab);
        assignFocusOnSelection = false;
        sourceTab.dispose();
        assignFocusOnSelection = true;
        newTab.setText(sourceLabel);
        if (wasVisible) {
            tabFolder.setSelection(newTab);
            setSelection(pane);
            pane.setFocus();
        }
    }

    /**
	 * Reparent a part. Also reparent visible children...<br>
	 * 
	 * @see LayoutPart#reparent(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void reparent(Composite newParent) {
        if (!newParent.isReparentable()) {
            return;
        }
        Control control = getControl();
        if ((control == null) || (control.getParent() == newParent)) {
            return;
        }
        super.reparent(newParent);
        Iterator<ILayoutPart> enuma = mapTabToPart.values().iterator();
        while (enuma.hasNext()) ((LayoutPart) enuma.next()).reparent(newParent);
    }

    /**
	 * @see ILayoutContainer#replace(ILayoutPart, ILayoutPart)
	 */
    public void replace(ILayoutPart oldChild, ILayoutPart newChild) {
        if ((oldChild instanceof PartPlaceholder) && !(newChild instanceof PartPlaceholder)) {
            replaceChild((PartPlaceholder) oldChild, newChild);
            return;
        }
        if (!(oldChild instanceof PartPlaceholder) && (newChild instanceof PartPlaceholder)) {
            replaceChild(oldChild, (PartPlaceholder) newChild);
            return;
        }
    }

    /**
	 * @param oldChild
	 * @param newChild
	 */
    private void replaceChild(ILayoutPart oldChild, PartPlaceholder newChild) {
        if (active) {
            for (Entry<XTabItem, ILayoutPart> entry : mapTabToPart.entrySet()) {
                XTabItem key = entry.getKey();
                ILayoutPart part = entry.getValue();
                if (part == oldChild) {
                    boolean partIsActive = (current == oldChild);
                    TabInfo info = new TabInfo();
                    info.part = newChild;
                    info.tabText = key.getText();
                    removeTab(key);
                    int index = 0;
                    if (invisibleChildren != null) {
                        index = invisibleChildren.length;
                    }
                    invisibleChildren = arrayAdd(invisibleChildren, info, index);
                    oldChild.setVisible(false);
                    oldChild.setContainer(null);
                    newChild.setContainer(this);
                    if (tabFolder.getItemCount() > 0 && !partIsActive) {
                        setControlSize();
                    }
                    break;
                }
            }
        } else if (invisibleChildren != null) {
            for (int i = 0, length = invisibleChildren.length; i < length; i++) {
                if (invisibleChildren[i].part == oldChild) {
                    invisibleChildren[i].part = newChild;
                }
            }
        }
    }

    /**
	 * @param oldChild
	 * @param newChild
	 */
    private void replaceChild(PartPlaceholder oldChild, ILayoutPart newChild) {
        if (invisibleChildren == null) {
            return;
        }
        for (int i = 0, length = invisibleChildren.length; i < length; i++) {
            TabInfo tabInfo = invisibleChildren[i];
            if (tabInfo.part == oldChild) {
                if (active) {
                    TabInfo info = tabInfo;
                    invisibleChildren = arrayRemove(invisibleChildren, oldChild);
                    oldChild.setContainer(null);
                    info.tabText = newChild.getName();
                    String key = newChild.getNameKey();
                    if (key != null) {
                        info.tabText = key;
                        info.isKey = true;
                        info.handler = newChild.getI18nHandler();
                        info.args = newChild.getI18nArgs();
                    }
                    XTabItem item = createPartTab(newChild, info.tabText, -1, info.isKey, info.handler, info.args);
                    int index = tabFolder.indexOf(item);
                    setSelection(index);
                } else {
                    tabInfo.part = newChild;
                    if (inactiveCurrent != null && inactiveCurrent == oldChild) {
                        current = newChild;
                        inactiveCurrent = null;
                    }
                }
                break;
            }
        }
    }

    /**
	 * @param memento
	 */
    public void restoreState(IMemento memento) {
        String activeTabID = memento.getString(IDockingManagerConsts.TAG_ACTIVE_PAGE_ID);
        IMemento[] children = memento.getChildren(IDockingManagerConsts.TAG_PAGE);
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                IMemento childMem = children[i];
                String partID = childMem.getString(IDockingManagerConsts.TAG_CONTENT);
                String tabText = childMem.getString(IDockingManagerConsts.TAG_LABEL);
                ILayoutPart part = new PartPlaceholder(partID);
                add(tabText, i, part, true, I18n.getGenericI18n());
                part.setContainer(this);
                if (partID.equals(activeTabID)) {
                    inactiveCurrent = part;
                }
            }
        }
    }

    /**
	 * @param memento
	 */
    public void saveState(IMemento memento) {
        if (current != null) {
            memento.putString(IDockingManagerConsts.TAG_ACTIVE_PAGE_ID, current.getID());
        }
        if (mapTabToPart.size() == 0) {
            if (invisibleChildren != null) {
                for (TabInfo info : invisibleChildren) {
                    IMemento childMem = memento.createChild(IDockingManagerConsts.TAG_PAGE);
                    childMem.putString(IDockingManagerConsts.TAG_LABEL, info.tabText);
                    childMem.putString(IDockingManagerConsts.TAG_CONTENT, info.part.getID());
                }
            }
        } else {
            ILayoutPart[] children = getChildren();
            CTabItem keys[] = new CTabItem[mapTabToPart.size()];
            mapTabToPart.keySet().toArray(keys);
            if (children != null) {
                for (ILayoutPart part : children) {
                    IMemento childMem = memento.createChild(IDockingManagerConsts.TAG_PAGE);
                    childMem.putString(IDockingManagerConsts.TAG_CONTENT, part.getID());
                    boolean found = false;
                    for (CTabItem item : keys) {
                        if (mapTabToPart.get(item) == part) {
                            childMem.putString(IDockingManagerConsts.TAG_LABEL, item.getText());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        for (TabInfo info : invisibleChildren) {
                            if (info.part == part) {
                                childMem.putString(IDockingManagerConsts.TAG_LABEL, info.tabText);
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        childMem.putString(IDockingManagerConsts.TAG_LABEL, "LabelNotFound");
                    }
                }
            }
        }
    }

    /**
	 * Set the size of a page in the folder.<br>
	 */
    private void setControlSize() {
        if (current == null || tabFolder == null) {
            return;
        }
        Rectangle bounds;
        if (mapTabToPart.size() > 1) {
            bounds = calculatePageBounds(tabFolder);
        } else {
            bounds = tabFolder.getBounds();
        }
        current.setBounds(bounds);
        current.moveAbove(tabFolder);
    }

    /**
	 * @param index
	 */
    public void setSelection(int index) {
        if (!active) {
            return;
        }
        if (mapTabToPart.size() == 0) {
            setSelection(null);
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index > mapTabToPart.size() - 1) {
            index = mapTabToPart.size() - 1;
        }
        tabFolder.setSelection(index);
        CTabItem item = tabFolder.getItem(index);
        ILayoutPart part = mapTabToPart.get(item);
        setSelection(part);
    }

    /**
	 * @param part
	 */
    private void setSelection(ILayoutPart part) {
        if (!active) {
            return;
        }
        if (part instanceof PartPlaceholder) {
            return;
        }
        if (current != null && current != part) {
            current.setVisible(false);
        }
        current = part;
        if (current != null) {
            setControlSize();
            current.setVisible(true);
        }
    }

    /**
	 * @see LayoutPart#targetPartFor(de.xirp.ui.dock.ILayoutPart)
	 */
    @Override
    public ILayoutPart targetPartFor(ILayoutPart dragSource) {
        return this;
    }

    @Override
    public String getNameKey() {
        return null;
    }
}
