package com.tensegrity.apidemo.gui.treemodel.executionhandler;

import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import org.palo.api.Database;
import org.palo.api.Dimension;
import com.tensegrity.apidemo.gui.CubeCreationDialog;
import com.tensegrity.apidemo.gui.PaloBrowser;

/**
 * <code>DatabaseNodeExecutionHandler</code>
 * This execution handler registers for <code>Database</code>s. Whenever the
 * user <i>executes</i> a database (i.e. performs a click with the right
 * mouse button on it), a dialog appears, asking the user if she wants to
 * create a new dimension or a new cube.
 * If she chooses to create a new dimension, she is asked for the name of the
 * new dimension and, provided she has sufficient rights and the dimension does
 * not already exist, the new dimension is created in the database.
 * If she chooses to create a cube, a dialog appears where she can choose
 * which dimensions are to be used for the new cube. She can assign a name to
 * the cube and create it in the database.
 * 
 * @author Philipp Bouillon
 * @version $Id: DatabaseNodeExecutionHandler.java,v 1.2 2007/05/24 08:02:24 PhilippBouillon Exp $
 */
public class DatabaseNodeExecutionHandler implements TreeNodeExecutionHandler {

    /**
	 * If the execute operation was performed on a database, a new cube or a new
	 * dimension can be created. The user is asked first, which of those
	 * elements she'd like to create. Then, either a dialog for dimension
	 * creation, or for cube creation will be displayed. 
	 */
    public boolean handle(PaloBrowser browser, DefaultMutableTreeNode node) {
        if (!(node.getUserObject() instanceof Database)) {
            return false;
        }
        Database db = (Database) node.getUserObject();
        int choice = JOptionPane.showConfirmDialog(browser, "Would you like to create a Dimension? (Selecting \"No\" will create a cube)", "Create a Dimension or a Cube", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.CANCEL_OPTION) {
            return true;
        } else if (choice == JOptionPane.YES_OPTION) {
            String name = JOptionPane.showInputDialog(browser, "Please enter a name for the new Dimension:", "New Dimension");
            if (name != null) {
                Dimension d = db.getDimensionByName(name);
                if (d != null) {
                    JOptionPane.showMessageDialog(browser, "A dimension with this name already exists in this database. Please choose a different name.");
                    return true;
                }
                try {
                    db.addDimension(name);
                    browser.getTree().update();
                } catch (RuntimeException e) {
                    if (e.getMessage().indexOf("insufficient access rights") != -1) {
                        JOptionPane.showMessageDialog(browser, "You do not have sufficient rights to create a dimension in this database.", "Insufficient Access Rights", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(browser, "An error occured: " + e.getLocalizedMessage(), "An Error Occured", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            CubeCreationDialog ccd = new CubeCreationDialog(browser, db);
            Rectangle r = browser.getBounds();
            int w = 400;
            int h = 250;
            ccd.setBounds(r.x + (r.width - w) / 2, r.y + (r.height - h) / 2, w, h);
            ccd.setVisible(true);
        }
        return true;
    }
}
