package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.list.ListSelectionModelMgrImpl;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.itsnat.comp.table.ItsNatTableHeaderCellUI;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.JoystickModeManager;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTableHeaderImpl extends ItsNatElementComponentImpl implements ItsNatTableHeader {

    protected boolean enabled = true;

    protected ItsNatTableHeaderCellRenderer renderer;

    protected ItsNatTableImpl tableComp;

    protected ListSelectionModelMgrImpl selModelMgr;

    /**
     * Creates a new instance of ItsNatTableHeaderImpl
     */
    public ItsNatTableHeaderImpl(ItsNatTableImpl tableComp, Element headerElem) {
        super(headerElem, null, tableComp.getItsNatComponentManagerImpl());
        this.tableComp = tableComp;
        ItsNatComponentManagerImpl componentMgr = getItsNatComponentManagerImpl();
        setItsNatTableHeaderCellRenderer(componentMgr.createDefaultItsNatTableHeaderCellRenderer());
    }

    public void setDefaultModels() {
        super.setDefaultModels();
        setDefaultListSelectionModel();
    }

    public void setDefaultListSelectionModel() {
        ListSelectionModel selModel = new DefaultListSelectionModel();
        selModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setListSelectionModel(selModel);
    }

    public Class getStructureClass() {
        return null;
    }

    public Object createDefaultStructure() {
        return null;
    }

    public void enableEventListeners() {
    }

    public ItsNatTable getItsNatTable() {
        return tableComp;
    }

    public ItsNatTableImpl getItsNatTableImpl() {
        return tableComp;
    }

    public ItsNatTableHeaderUI getItsNatTableHeaderUI() {
        return (ItsNatTableHeaderUI) compUI;
    }

    public ItsNatTableHeaderCellRenderer getItsNatTableHeaderCellRenderer() {
        return renderer;
    }

    public void setItsNatTableHeaderCellRenderer(ItsNatTableHeaderCellRenderer renderer) {
        this.renderer = renderer;
    }

    public void bindDataModel() {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void unbindDataModel() {
        throw new ItsNatException("INTERNAL ERROR");
    }

    public void initialSyncUIWithDataModel() {
    }

    public Object createDefaultModelInternal() {
        return null;
    }

    protected ParamTransport[] getInternalParamTransports(String type) {
        return null;
    }

    public ListSelectionModel getListSelectionModel() {
        if (selModelMgr == null) {
            return null;
        }
        return selModelMgr.getListSelectionModel();
    }

    public void unsetListSelectionModel() {
        if (selModelMgr != null) {
            selModelMgr.dispose();
        }
    }

    public void setListSelectionModel(ListSelectionModel selectionModel) {
        unsetListSelectionModel();
        int size = getItsNatTable().getTableModel().getColumnCount();
        this.selModelMgr = ListSelectionModelMgrImpl.newListSelectionModelMgr(selectionModel, size);
    }

    public int getSelectedIndex() {
        return getListSelectionModel().getMinSelectionIndex();
    }

    public void setSelectedIndex(int index) {
        getListSelectionModel().setSelectionInterval(index, index);
    }

    public ItsNatTableHeaderCellUI processEvent(Node nodeClicked, boolean toggle, boolean extend) {
        ItsNatTableHeaderUI headerUI = getItsNatTableHeaderUI();
        ItsNatTableHeaderCellUI colCellInfo = headerUI.getItsNatTableHeaderCellUIFromNode(nodeClicked);
        if (colCellInfo == null) {
            return null;
        }
        int column = colCellInfo.getIndex();
        changeColumnSelection(column, toggle, extend);
        return colCellInfo;
    }

    public void changeColumnSelection(int columnIndex, boolean toggle, boolean extend) {
        ListSelectionModel csm = getListSelectionModel();
        boolean selected = csm.isSelectedIndex(columnIndex);
        selModelMgr.changeSelectionModel(columnIndex, toggle, extend, selected);
    }

    public void copyHeaderValuesFromDataModelToUI() {
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        TableModel dataModel = getItsNatTable().getTableModel();
        int columns = dataModel.getColumnCount();
        for (int i = 0; i < columns; i++) {
            String columnName = dataModel.getColumnName(i);
            compUI.setElementValueAt(i, columnName, false, false);
        }
    }

    public void setDOMColumnCount(int cols) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        int oldCols = -1;
        if (joystickMode != null) oldCols = compUI.getLength();
        if ((joystickMode != null) && (cols < oldCols)) removeInternalEventListenerJoystickMode(cols, oldCols - 1);
        compUI.setLength(cols);
        if ((joystickMode != null) && (cols > oldCols)) addInternalEventListenerJoystickMode(oldCols, cols - 1);
    }

    public void insertDOMColumn(int index, Object value) {
        Element elem = getItsNatTableHeaderUI().insertElementAt(index, value);
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (joystickMode != null) addInternalEventListenerJoystickMode(index, elem);
    }

    public void removeDOMColumn(int index) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (joystickMode != null) removeInternalEventListenerJoystickMode(index);
        getItsNatTableHeaderUI().removeElementAt(index);
    }

    public Element[] getContentElementList(Element[] elemList, int colCount) {
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        int from = elemList.length - colCount;
        for (int i = 0; i < colCount; i++) elemList[i + from] = compUI.getContentElementAt(i);
        return elemList;
    }

    public void addInternalEventListenerJoystickMode(int index, Element elem) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (!joystickMode.mustAddRemove()) return;
        Element contentElem = getItsNatTable().getItsNatTableStructure().getHeaderColumnContentElement(this, index, elem);
        joystickMode.addInternalEventListener(contentElem);
    }

    public void addInternalEventListenerJoystickMode(int index) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (!joystickMode.mustAddRemove()) return;
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        Element contentElem = compUI.getContentElementAt(index);
        joystickMode.addInternalEventListener(contentElem);
    }

    public void removeInternalEventListenerJoystickMode(int index) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (!joystickMode.mustAddRemove()) return;
        ItsNatTableHeaderUI compUI = getItsNatTableHeaderUI();
        Element contentElem = compUI.getContentElementAt(index);
        joystickMode.removeInternalEventListener(contentElem);
    }

    public void addInternalEventListenerJoystickMode(int fromIndex, int toIndex) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (!joystickMode.mustAddRemove()) return;
        for (int i = fromIndex; i <= toIndex; i++) addInternalEventListenerJoystickMode(i);
    }

    public void removeInternalEventListenerJoystickMode(int fromIndex, int toIndex) {
        JoystickModeManager joystickMode = getItsNatTableImpl().getJoystickModeManager();
        if (!joystickMode.mustAddRemove()) return;
        for (int i = fromIndex; i <= toIndex; i++) removeInternalEventListenerJoystickMode(i);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }
}
