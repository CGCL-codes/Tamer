package edu.upmc.opi.caBIG.caTIES.client.vr.FilterFlowGraph.dialogs;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import edu.upmc.opi.caBIG.caTIES.client.vr.constraints.FieldConstraint;
import edu.upmc.opi.caBIG.caTIES.client.vr.constraints.SearchTermConstraint;

/**
 * Displays interface for modification of TextConstraint. Handles saving of the
 * modifications.
 * 
 * @see edu.upmc.opi.caBIG.caTIES.client.vr.constraints.SearchTermConstraint
 * TextConstraint
 */
public class SearchTermConstraintDialog extends AbstractConstraintDialog {

    public static void main(String arg[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        SearchTermConstraintDialog d = new SearchTermConstraintDialog(null, new SearchTermConstraint("test", "test", true, new String[] { SearchTermConstraint.ANY_SECTION }), false);
        d.pack();
        d.setVisible(true);
    }

    /**
     * Constructor for TextConstraintDialog.
     * 
     * @param fc FieldConstraint
     * @param f Frame
     */
    public SearchTermConstraintDialog(Frame f, FieldConstraint fc, boolean textSearchDisabled) {
        super(f, fc);
        if (textSearchDisabled) {
            ((SearchTermConstraintPanel) constraintPanel).disableTextSearch();
        }
    }

    /**
     * Method getCopyOfConstraint.
     * 
     * @param fc FieldConstraint
     * 
     * @return FieldConstraint
     */
    protected FieldConstraint getCopyOfConstraint(FieldConstraint fc) {
        return new SearchTermConstraint((SearchTermConstraint) fc);
    }

    /**
     * Method getConstraintPanel.
     * 
     * @param fc FieldConstraint
     * 
     * @return JPanel
     */
    protected JPanel getConstraintPanel(FieldConstraint fc) {
        return new SearchTermConstraintPanel(this, (SearchTermConstraint) fc);
    }

    /**
     * Method handleSaveAction.
     * 
     * @param fc FieldConstraint
     * @param newfc FieldConstraint
     * 
     * @return boolean
     */
    protected boolean handleSaveAction(FieldConstraint fc, FieldConstraint newfc) {
        ((SearchTermConstraint) fc).copy((SearchTermConstraint) newfc);
        return true;
    }
}
