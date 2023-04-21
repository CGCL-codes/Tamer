package org.openscience.cdk.test.internet;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.applications.swing.AtomicTable;
import org.openscience.cdk.applications.swing.MoleculeViewer2D;
import org.openscience.cdk.internet.DADMLReader;
import org.openscience.cdk.tools.LoggingTool;

/**
 * @cdk.module  test-extra
 * @cdk.require dadml
 */
public class DADMLTest {

    private LoggingTool logger;

    public DADMLTest(String superdb, String type, String index) {
        logger = new LoggingTool(this);
        LoggingTool.configureLog4j();
        try {
            logger.info("SuperDB: ", superdb);
            logger.info("index type: ", type);
            logger.info("index: ", index);
            DADMLReader reader = new DADMLReader(superdb);
            reader.setQuery(type, index);
            Molecule m = (Molecule) reader.read(new Molecule());
            MoleculeViewer2D mv = new MoleculeViewer2D(m);
            mv.display();
            AtomicTable at = new AtomicTable(m);
            at.display();
        } catch (Exception exc) {
            logger.error(exc.toString());
        }
    }
}
