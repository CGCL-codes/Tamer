package au.edu.diasb.annotation.danno.test;

import au.edu.diasb.annotation.danno.db.RDFDBContainerPool;
import au.edu.diasb.annotation.danno.impl.jena.JenaAnnoteaTypeFactory;
import au.edu.diasb.annotation.danno.impl.jena.RDBContainerPool;
import au.edu.diasb.annotation.danno.model.TripleStoreException;

/**
 * Unit tests for the DannoController class with RDB.  The actual tests are defined
 * in the base class.
 * 
 * @author scrawley
 */
public class DannoControllerRDBTest extends DannoControllerTests {

    protected RDFDBContainerPool createPool() throws TripleStoreException {
        JenaAnnoteaTypeFactory tf = new JenaAnnoteaTypeFactory();
        return new RDBContainerPool(tf, TestProperties.getProperties());
    }
}
