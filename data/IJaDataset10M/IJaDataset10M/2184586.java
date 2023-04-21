package jimporter.options;

import org.gjt.sp.jedit.AbstractOptionPane;

/**
 * This abstract class represent an option that will be displayed in the 
 * JImporter "options" dialog.  It defines classes that allow the option panes
 * to class these options so they can create themselves.
 */
public abstract class JImporterOption {

    /** The jEdit property where the human-readable name of the option is stored. */
    protected String labelProperty;

    /**
     * Standard constructor.
     *
     * @param labelProperty a <code>String</code> containing the jEdit property
     * name of the location where the human readable name of this option will be
     * stored.
     */
    public JImporterOption(String labelProperty) {
        this.labelProperty = labelProperty;
    }

    /**
     * Create the current option on in the JImporter option panes.
     *
     * @param jiop a <code>JImporterOptionPane</code> value which the option
     * will add itself to.
     */
    public abstract void createVisualPresentation(JImporterOptionPane jiop);
}
