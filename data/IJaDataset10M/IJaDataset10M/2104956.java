package com.dfruits.queries.ui.internal.actions;

import org.eclipse.jface.action.Action;
import com.dfruits.queries.ui.QueryObject;

public class TableColumnVisibilityAction extends AbstractQueryObjectAction {

    public TableColumnVisibilityAction(String id, String text, QueryObject queryObject) {
        super(id, text, Action.AS_DROP_DOWN_MENU, queryObject);
    }
}
