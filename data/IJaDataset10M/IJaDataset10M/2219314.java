package com.certesystems.swingforms.links;

import java.awt.Component;
import javax.swing.JTable;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.commons.jxpath.JXPathContext;
import com.certesystems.swingforms.grid.Grid;
import com.certesystems.swingforms.gridset.foreignKey.GridSetSelectionPanel;
import com.certesystems.swingforms.tools.Messages;

public class LinkFKGridSet extends Link {

    public LinkFKGridSet() {
    }

    public void comboToField(JXPathContext context) throws Exception {
        Grid grid = getContainer().getSelectedItem();
        Persistent o = null;
        if (grid != null) {
            o = grid.getRegister();
            if (o != null) {
                ObjectContext oc = (ObjectContext) context.getValue("objectContext");
                o = oc.localObject(o.getObjectId(), o);
            }
        }
        context.setValue(getMapping(), o);
    }

    public void fieldToCombo(JXPathContext context, boolean requery) {
        Object o = context.getValue(getMapping());
        getContainer().setSelectedItem((Persistent) o);
    }

    public void setEditable(boolean value) {
        getContainer().setEnabled(value);
    }

    public GridSetSelectionPanel getContainer() {
        return (GridSetSelectionPanel) super.getContainer();
    }

    public void verifyCombo() throws Exception {
        if (!getField().isNullEnabled() && getContainer().getSelectedItem() == null) {
            throw new Exception(Messages.getString("LinkFKGridSet.required") + getDescription() + "\"");
        }
    }

    public Expression createExpression() {
        Grid grid = getContainer().getSelectedItem();
        if (grid != null) {
            return ExpressionFactory.matchExp(getMapping().split("/")[0], grid.getRegister());
        } else {
            return null;
        }
    }

    public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
        LinkFKGridSet link = (LinkFKGridSet) arg1;
        GridSetSelectionPanel comp = link.getContainer();
        return comp;
    }

    public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        LinkFKGridSet link = (LinkFKGridSet) arg1;
        GridSetSelectionPanel comp = link.getContainer();
        comp.setEnabled(true);
        comp.setOpaque(true);
        return comp;
    }

    public void setForeignKey(Grid grid) {
        getContainer().setSelectedItem(grid);
    }
}
