package com.safi.workshop.sqlexplorer.preview;

import org.eclipse.swt.widgets.Composite;
import com.safi.workshop.sqlexplorer.ExplorerException;

/**
 * Previewer classes are used by the DataPreviewView to present read-only previews of data
 * that would not comfortably fit inline in the results tabs, eg an XML tree or an image.
 * 
 * A Previewer must register a class of PreviewerFactory.Resolver with the singleton
 * PreviewerFactory in order for it to be used by SQLExplorer.
 * 
 * @author John Spackman
 */
public interface Previewer {

    /**
   * Called to create the controls to display the data. When SQLExplorer wants to display
   * data it will use the factory to find a Previewer by MIME type or by the type of the
   * object to be displayed; note that Previewer's may be given object types that are NOT
   * necessarily a supported Object type (EG the user is able to override content types on
   * a per column basis) so it is important that the previewer detects cautiously what
   * <code>data</code> is and reacts gracefully if it is unrecognised.
   * 
   * @param parent
   * @param data
   *          the object to display
   */
    public void createControls(Composite parent, Object data) throws ExplorerException;

    /**
   * Called when resources are about to be disposed
   */
    public void dispose();
}
