package org.semanticorm.sesame;

import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailWrapper;
import org.openrdf.sail.inferencer.InferencerConnection;

public class IndexingSail extends SailWrapper {

    public IndexingSail(Sail baseSail) {
        super(baseSail);
    }

    @Override
    public IndexingSailConnection getConnection() throws SailException {
        try {
            InferencerConnection con = (InferencerConnection) super.getConnection();
            return new IndexingSailConnection(con);
        } catch (ClassCastException e) {
            throw new SailException(e.getMessage(), e);
        }
    }

    @Override
    public void initialize() throws SailException {
        super.initialize();
    }
}
