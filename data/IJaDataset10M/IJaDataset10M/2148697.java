package gui.dutsForms;

import gui.xmlClasses.Text;
import javax.swing.JPanel;

/** 
 * Generic class for DUTS forms.
 * A form can manage by its own update and create operations
 * for its data.
 * 
 * @author Julien Wollscheid for FARS Design
 * 
 */
public abstract class DUTSForm extends JPanel {

    /**
	 * The serial version id of this class
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * the DUTSLabels to display on the GUI
	 */
    private Text DUTSLabels;

    public DUTSForm(Text dUTSLabels) {
        super();
        DUTSLabels = dUTSLabels;
    }

    protected Text getDUTSLabels() {
        return DUTSLabels;
    }

    /** 
	 * Hook method
	 * Can be overridden to implement create operation using
	 * data in form.
	 */
    public void create() {
    }

    /** 
	 * Hook method
	 * Can be overridden to implement update operation using
	 * data in form.
	 */
    public void update() {
    }

    /**
	 * Hook method
	 * Can be overridden to catch data to display on the form. 
	 */
    public void populate() {
    }

    /**
	 * Hook method
	 * Can be overridden to clear all fields in the form
	 */
    public void clearFields() {
    }
}
