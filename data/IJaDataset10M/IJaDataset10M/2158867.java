package org.geoforge.guitlc.dialog.tablespace.popupmenu;

import org.geoforge.guitlc.dialog.tablespace.table.TblManageAbs;
import org.geoforge.guitlc.dialog.tablespace.action.*;
import org.geoforge.guitlc.dialog.tablespace.menuitem.*;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PopTableManageChildrenWorkspace extends PopTableManageChildrenAbs {

    public PopTableManageChildrenWorkspace(TblManageAbs tbl) {
        super(tbl);
        super._mimChildCopy_ = new MimChildCopyWorkspace();
        super._mimChildRename_ = new MimChildMoveWorkspace();
        super._mimChildDelete_ = new MimChildDeleteWorkspace();
    }

    @Override
    public void setSelectedRow(int intRow) {
        super.setSelectedRow(intRow);
        if (super._tbl_ == null) return;
        if (this._tbl_.getModel() == null) return;
        String strSource = (String) super._tbl_.getModel().getValueAt(super._row_, super._tbl_.getIdColNumber());
        ActionChildCloneWorkspace.s_getInstance().setChild(strSource);
        ActionChildMoveWorkspace.s_getInstance().setChild(strSource);
        ActionChildDeleteWorkspace.s_getInstance().setChild(strSource);
    }
}
