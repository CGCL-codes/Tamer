package fedora.services.sipcreator.metadata;

import javax.swing.JPanel;

/**
 * This class defines a very simple interface for a GUI component used to
 * display and interact with Metadata objects.  The simplicity of the
 * interface is couple with the fact that if the implementor of this
 * abstract class wants to provide any sort of interaction between the
 * user and the Metadata object, it needs to be programmed from scratch.
 * <br>
 * The key methods, <code>updateFromMetadata()</code> and
 * <code>updateMetadata()</code> are used to ensure that changes to the
 * model propogate to the UI and vice versa at the appropriate times. Thus,
 * the implementor does not have to worry about the fact that the interface
 * requires the user to expose the underlying Metadata via the <code>
 * getMetadata()</code> method.
 * <br><br>
 * @author Andy Scukanec - (ags at cs dot cornell dot edu)
 */
public abstract class MetadataPanel extends JPanel {

    /**
     * Returns the underlying Metadata object that provides this UI component
     * with its data model.
     * <br><br>
     * @return The underlying Metadata object.
     */
    public abstract Metadata getMetadata();

    /**
     * This method will be called whenever changes have been made to the
     * underlying data model as a way of letting this MetadataPanel object
     * know that it should probably refresh the data in its view. 
     */
    public abstract void updateFromMetadata();

    /**
     * This method is a way of asking the MetadataPanel object to push any
     * unsaved information into the underlying Metadata object.  This will
     * be used, for example, right before saving a SIP file so that all of
     * the data to be saved will be in the Metadata objects rather than in
     * the UI.
     */
    public abstract void updateMetadata();
}
