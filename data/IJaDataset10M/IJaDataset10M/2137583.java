package org.posterita.model;

import java.util.Properties;
import org.compiere.model.PO;
import org.posterita.exceptions.OperationException;

public abstract class UDIPO {

    private PO po;

    private Properties ctx;

    public Properties getCtx() {
        return ctx;
    }

    public UDIPO(PO po) {
        this.po = po;
        this.ctx = po.getCtx();
    }

    public int getID() {
        return po.get_ID();
    }

    public int getAD_Org_ID() {
        return po.getAD_Org_ID();
    }

    public void save() throws OperationException {
        boolean saved = po.save();
        if (!saved) throw new OperationException("Cannot save PO object, Class: " + po.getClass().getName());
    }

    public PO getPO() {
        return po;
    }

    public void setPO(PO po) {
        this.po = po;
    }
}
