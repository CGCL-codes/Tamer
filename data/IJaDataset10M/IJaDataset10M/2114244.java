package org.apache.myfaces.trinidad.change;

import java.util.List;
import javax.faces.component.UIComponent;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;

/**
 * Change specialization for adding a child component.
 * While applying this Change, the child component is re-created and added to
 *  the list of children.
 * @version $Name:  $ ($Revision: 244 $) $Date: 2008-11-25 18:45:49 -0500 (Tue, 25 Nov 2008) $
 */
public class AddChildComponentChange extends AddComponentChange {

    /**
   * Constructs an AddChildChange that appends the specified child component.
   * @param childComponent The child component that is to be appended.
   * @throws IllegalArgumentException if specified childComponent is null.
   */
    public AddChildComponentChange(UIComponent childComponent) {
        this(null, childComponent);
    }

    /**
   * Constructs an AddChildChange with the specified child component and the
   *  the identifier of the neighbour. If the neighbour is not found
   *  when applying this Change, or is <code>null<code>< the child is
   *  appended to the end of the list of children.
  *  @param insertBeforeId The identifier of the sibling before which this new 
  *         child is to be inserted.
   * @param childComponent The child component that is to be added.
   * @throws IllegalArgumentException if specified childComponent is null
   */
    public AddChildComponentChange(String insertBeforeId, UIComponent childComponent) {
        super(childComponent);
        _insertBeforeId = insertBeforeId;
    }

    /**
   * Returns the identifier of the sibling before which this new child needs to
   *  be inserted.
   */
    public String getInsertBeforeId() {
        return _insertBeforeId;
    }

    /**
   * {@inheritDoc}
   */
    @SuppressWarnings("unchecked")
    @Override
    public void changeComponent(UIComponent uiComponent) {
        UIComponent child = getComponent();
        if (child == null) return;
        String newChildId = child.getId();
        List<UIComponent> children = uiComponent.getChildren();
        UIComponent removableChild = ChangeUtils.getChildForId(uiComponent, newChildId);
        if (removableChild != null) {
            _LOG.info("ATTEMPT_ADD_CHILD_WITH_DUPLICATE_ID", newChildId);
            children.remove(removableChild);
        }
        if (_insertBeforeId == null) {
            children.add(child);
        } else {
            int index = ChangeUtils.getChildIndexForId(uiComponent, _insertBeforeId);
            if (index == -1) {
                children.add(child);
            } else {
                children.add(index, child);
            }
        }
    }

    private final String _insertBeforeId;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(AddChildComponentChange.class);

    private static final long serialVersionUID = 1L;
}
