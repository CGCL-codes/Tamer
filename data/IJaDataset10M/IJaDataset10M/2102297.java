package gov.sns.apps.mpsclient;

import gov.sns.application.*;
import java.net.URL;

/**
 * MPSDocument is XalDocument implementation for the mpsclient application. 
 *
 * @author  tap
 */
class MPSDocument extends XalDocument {

    /** table model for the table of remote mps tools */
    protected MPSTableModel mpsTableModel;

    /** model for this document */
    protected DocumentModel _model;

    /** Create a new empty document */
    public MPSDocument(MPSModel mainModel) {
        this(null, mainModel);
    }

    /** 
     * Create a new document loaded from the URL file 
     * @param url The URL of the file to load into the new document.
     */
    public MPSDocument(java.net.URL url, MPSModel mainModel) {
        setSource(url);
        _model = new DocumentModel(mainModel);
        mpsTableModel = new MPSTableModel(mainModel);
    }

    /**
     * Make a main window by instantiating the my custom window.  Set the text 
     * pane to use the textDocument variable as its document.
     */
    @Override
    public void makeMainWindow() {
        mainWindow = new MPSWindow(this, mpsTableModel);
    }

    /**
     * Save the document to the specified URL.
     * @param url The URL to which the document should be saved.
     */
    @Override
    public void saveDocumentAs(URL url) {
    }

    /**
	 * Generate and set the title for this document.  By default the title is set to 
	 * the file path of the document or the default empty document file path if the document
	 * does not have a file store.
	 */
    @Override
    protected void generateDocumentTitle() {
        setTitle("Browser");
    }

    /**
     * Subclasses should override this method if this document should use a menu definition
	 * other than the default specified in application adaptor.  The document menu inherits the
	 * application menu definition.  This custom path allows the document to modify the
	 * application wide definitions for this document.  By default this method returns null.
     * @return The menu definition properties file path in classpath notation
	 * @see ApplicationAdaptor#getPathToResource
     */
    @Override
    protected String getCustomMenuDefinitionPath() {
        return Application.getAdaptor().getPathToResource("browser_menu");
    }

    /**
     * Convenience method for getting the main window cast to the proper subclass of XalWindow.
     * This allows me to avoid casting the window every time I reference it.
     * @return The main window cast to its dynamic runtime class
     */
    private MPSWindow getWindow() {
        return (MPSWindow) mainWindow;
    }

    /**
	* Get the MPS model
	 * @return The MPS model
	 */
    public DocumentModel getModel() {
        return _model;
    }

    /**
	 * Override the inherited method to dispose of the model before the document is closed.
	 */
    @Override
    protected void willClose() {
        _model.dispose();
    }
}
