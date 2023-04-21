package com.cosmos.acacia.crm.gui.contactbook;

import java.util.UUID;
import java.util.List;
import javax.ejb.EJB;
import com.cosmos.acacia.crm.bl.contactbook.PositionTypesListRemote;
import com.cosmos.acacia.crm.data.contacts.PositionType;
import com.cosmos.acacia.gui.AbstractTreeEnabledTablePanel;
import com.cosmos.acacia.gui.AcaciaToStringConverter;
import com.cosmos.acacia.gui.BaseTreePanel;

/**
 *
 * @author  Petar Milev
 */
public class PositionsHierarchyTreePanel extends BaseTreePanel<PositionType> {

    private PositionTypesListPanel positionTypesListPanel;

    @EJB
    private PositionTypesListRemote formSession;

    /** Creates new form ProductCategoriesPanel */
    public PositionsHierarchyTreePanel(UUID parentDataObjectId) {
        super(parentDataObjectId);
    }

    /**
     * Tries to update the model by calling the business logic.
     * On fail - shows error message to the user, and returns null.
     * On success returns the updated position (child)
     *
     * @param newParent
     * @param newChildren
     * @return
     */
    @Override
    protected PositionType updateParent(PositionType newParent, PositionType newChildren) {
        try {
            PositionType updatedPosition = getFormSession().updateParent(newParent, newChildren);
            return updatedPosition;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    @Override
    protected void initData() {
        positionTypesListPanel = new PositionTypesListPanel(getParentDataObjectId(), true);
        toStringConverter = new AcaciaToStringConverter("${positionTypeName}");
        getTree().setToStringConverter(toStringConverter);
        List<PositionType> positionTypes = positionTypesListPanel.getItems();
        setSkipFirst(false);
        refreshTreeModel(positionTypes);
    }

    @Override
    protected void onTableRefreshed() {
        List<PositionType> positionTypes = getFormSession().getInternalOrganizationPositionTypes(getOrganizationDataObjectId());
        refreshTreeModel(positionTypes);
    }

    protected PositionTypesListRemote getFormSession() {
        if (formSession == null) {
            try {
                formSession = getBean(PositionTypesListRemote.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return formSession;
    }

    @Override
    public AbstractTreeEnabledTablePanel<PositionType> getListPanel() {
        return positionTypesListPanel;
    }

    @Override
    public PositionType getParent(PositionType child) {
        throw new UnsupportedOperationException("ToDO");
    }

    @Override
    public void setParent(PositionType entity, PositionType parent) {
        throw new UnsupportedOperationException("ToDO");
    }

    @Override
    protected String getRootNodeDisplay() {
        return getResourceMap().getString("Tree.rootNodeDisplay");
    }
}
