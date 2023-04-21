package jp.jparc.apps.monsampler;

import java.util.*;
import java.util.logging.*;
import java.net.*;
import javax.swing.text.*;
import javax.swing.event.*;
import gov.sns.xal.smf.application.*;
import gov.sns.xal.smf.*;

/**
 * DemoDocument is a custom AcceleratorDocument for the demo application.  Each document instance 
 * manages a single plain text document.  The document manages the data that is 
 * displayed in the window.  In this example we display information about the accelerator or 
 * the selected sequence as appropriate based on the most recent user action.
 *
 * @author  t6p
 */
public class MonSamplerDocument extends AcceleratorDocument {

    /**
     * The document for the text pane in the main window.
     */
    protected PlainDocument textDocument;

    private MonitorDigitizerController controller;

    /** Create a new empty document */
    public MonSamplerDocument(boolean caget, boolean caput) {
        this(null, caget, caput);
        controller = new MonitorDigitizerController(caget, caput);
    }

    /** 
     * Create a new document loaded from the URL file 
     * @param url The URL of the file to load into the new document.
     */
    public MonSamplerDocument(java.net.URL url, boolean cagetFlag, boolean caputFlag) {
        setSource(url);
        makeTextDocument();
        controller = new MonitorDigitizerController(cagetFlag, caputFlag);
    }

    public MonitorDigitizerController getController() {
        return controller;
    }

    /**
     * Make a main window by instantiating the my custom window.  Set the text 
     * pane to use the textDocument variable as its document.
     */
    @Override
    public void makeMainWindow() {
        mainWindow = new MonSamplerWindow(this);
    }

    /**
     * Save the document to the specified URL.
     * @param url The URL to which the document should be saved.
     */
    @Override
    public void saveDocumentAs(URL url) {
    }

    /**
     * Convenience method for getting the main window cast to the proper subclass of XalWindow.
     * This allows me to avoid casting the window every time I reference it.
     * @return The main window cast to its dynamic runtime class
     */
    private MonSamplerWindow myWindow() {
        return (MonSamplerWindow) mainWindow;
    }

    /** 
     * Instantiate a new PlainDocument that servers as the document for the text pane.
     * Create a handler of text actions so we can determine if the document has 
     * changes that should be saved.
     */
    private void makeTextDocument() {
        textDocument = new PlainDocument();
        textDocument.addDocumentListener(new DocumentListener() {

            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                setHasChanges(true);
            }

            public void removeUpdate(DocumentEvent evt) {
                setHasChanges(true);
            }

            public void insertUpdate(DocumentEvent evt) {
                setHasChanges(true);
            }
        });
    }

    /**
     * Handle the accelerator changed event by displaying the elements of the 
     * accelerator in the main window.
     */
    @Override
    public void acceleratorChanged() {
        if (accelerator != null) {
            StringBuffer description = new StringBuffer("Selected Accelerator: " + accelerator.getId() + '\n');
            description.append("Sequences:\n");
            Iterator sequenceIter = accelerator.getSequences().iterator();
            while (sequenceIter.hasNext()) {
                AcceleratorSeq sequence = (AcceleratorSeq) sequenceIter.next();
                description.append('\t' + sequence.getId() + '\n');
            }
            final String message = "Accelerator changed to: \"" + accelerator.getId() + "\" with path: \"" + acceleratorFilePath + "\"";
            System.out.println(message);
            Logger.getLogger("global").log(Level.INFO, message);
        }
    }

    /**
     * Handle the selected sequence changed event by displaying the elements of the 
     * selected sequence in the main window.
     */
    @Override
    public void selectedSequenceChanged() {
        if (selectedSequence != null) {
            myWindow().repaint();
        }
    }
}
