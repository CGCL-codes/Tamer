package edu.isi.div2.metadesk.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import edu.isi.div2.metadesk.util.Icons;

/**
 * @author Sameer Maggon (maggon@isi.edu)
 * @version $Id: ExportHierarchyAsRdf.java,v 1.1 2005/05/24 16:33:23 maggon Exp $
 */
public class ExportHierarchyAsRdf extends AbstractAction {

    public ExportHierarchyAsRdf() {
        super("Export Hierarchy as RDF", Icons.getExportIcon());
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent arg0) {
    }
}
