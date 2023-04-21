package org.pojosoft.ria.gwt.client.meta;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Usually used to define an action column within a {@link TreeTableMeta}. The action column is defined as a column
 * that contains one or more actions, each represented by an {@link ActionMeta}, that the end-user can perform against
 * the current row in a TreeTable. For example: Edit, Remove.
 *
 * @author POJO Software
 */
public class ActionColumnMeta extends ColumnMeta implements IsSerializable {

    /**
   * A list of actions, each action is defined by an {@link ActionMeta}, that the end-user can perform against the
   * current row in a TreeTable.
   *
   * gwt.typeArgs <org.pojosoft.ria.gwt.client.meta.ActionMeta>
   */
    public List<ActionMeta> actions;

    protected Object readResolve() {
        if (actions == null) actions = new ArrayList();
        return this;
    }
}
