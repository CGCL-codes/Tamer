package au.edu.diasb.annotation.danno.impl.jena;

import au.edu.diasb.annotation.danno.db.RDFDBContainer;
import au.edu.diasb.annotation.danno.db.RDFDBContainerPool;
import au.edu.diasb.annotation.danno.model.TripleStoreException;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.tdb.store.GraphTDB;

/**
 * The current implementation of TDB is described by the HPL folks as "not transactional"
 * without elaborating on what (if any) ACID properties are implemented.  This class tries
 * to work-around some of TDB's shortcomings, but I don't think that 'abort' will rollback
 * uncommitted changes, and I don't think that atomicity and durability are guaranteed in
 * the face of hardware / power failure in the middle of an update.
 * <p>
 * For safety's sake, this class should be used as a (Gang of Four) singleton.
 * 
 * @author scrawley
 *
 */
public class TDBContainer extends JenaRDFDBContainer implements RDFDBContainer {

    public TDBContainer(JenaAnnoteaTypeFactory typeFactory, Model model, RDFDBContainerPool pool, boolean readWrite) {
        super(model, typeFactory, pool, readWrite);
    }

    /**
     * Caution: this method will FAIL for TDB 0.7.x
     */
    @Override
    public synchronized void abort() {
        try {
            super.abort();
            sync();
        } catch (JenaException ex) {
            throw new TripleStoreException(ex);
        }
    }

    @Override
    public synchronized void commit() {
        try {
            super.commit();
            sync();
        } catch (JenaException ex) {
            throw new TripleStoreException(ex);
        }
    }

    /**
     * Flush any changes to TDB's in-memory model cache or memory-mapped
     * files to disc.
     */
    private void sync() {
        Graph graph = getModel().getGraph();
        ((GraphTDB) graph).sync(true);
    }
}
