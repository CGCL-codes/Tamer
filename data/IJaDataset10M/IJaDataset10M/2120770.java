package org.ccnx.ccn.utils.explorer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.io.CCNFileInputStream;
import org.ccnx.ccn.protocol.ContentName;

/**
 * Class for retrieving content on a separate thread.  This is called by the
 * ContentExplorer for displaying txt in the preview pane, popup windows, saving
 * content to a local filesystem and for interfacing with media players (upcoming).
 */
public class LocalSaveContentRetriever implements Runnable {

    private ContentName name = null;

    private CCNHandle handle = null;

    private int readsize = 1024;

    private JEditorPane htmlPane = null;

    /**
	 * Constructor for the LocalSaveContentRetriever.
	 * 
	 * @param h CCNHandle to use for downloading the content
	 * @param n ContentName of the content object name to download
	 * @param p Preview pane to show download status
	 */
    public LocalSaveContentRetriever(CCNHandle h, ContentName n, JEditorPane p) {
        handle = h;
        name = n;
        htmlPane = p;
    }

    /**
	 * run() method for the thread that saves content to a filesystem.  This method
	 * creates and displays a JFileChooser.  The user selects a location to save the
	 * content.  If the selected file cannot be created or written to, the method
	 * returns and the content is not retrieved.  Status for this operation is displayed
	 * in the ContentExplorer preview pane.
	 * 
	 */
    public void run() {
        if (name == null) {
            System.err.println("Must set file name for retrieval");
            return;
        }
        if (handle == null) {
            System.err.println("Must set CCNHandle");
            return;
        }
        if (htmlPane == null) {
            System.err.println("Must set JEditorPane");
            return;
        }
        JFrame frame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        File f = null;
        chooser.setCurrentDirectory(null);
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            f = chooser.getSelectedFile();
        } else {
            htmlPane.setText("Save File cancelled");
            return;
        }
        boolean overwrite = false;
        try {
            if (f.exists()) {
                htmlPane.setText("Overwriting contents of " + f.getPath());
                overwrite = true;
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
            } else {
                htmlPane.setText("The ContentExplorer is unable to write the content to the specified file.");
                return;
            }
        } catch (IOException e) {
            System.err.println("could not create " + f.getPath() + " for saving content to filesystem");
            htmlPane.setText("could not create " + f.getPath() + " for saving content to filesystem");
        }
        try {
            if (!overwrite) htmlPane.setText("saving " + name + " to " + f.getCanonicalPath()); else htmlPane.setText("overwriting contents of " + f.getCanonicalPath() + " to save " + name);
            CCNFileInputStream fis = new CCNFileInputStream(name, handle);
            FileOutputStream output = new FileOutputStream(f);
            byte[] buffer = new byte[readsize];
            int readcount = 0;
            int readtotal = 0;
            while ((readcount = fis.read(buffer)) != -1) {
                readtotal += readcount;
                output.write(buffer, 0, readcount);
                output.flush();
            }
            htmlPane.setText("Saved " + name + " to " + f.getCanonicalPath());
        } catch (Exception e) {
            htmlPane.setText("Could not save " + name + " to " + f.getPath() + " This may be a prefix for an object or just may not be available at this time.");
            System.err.println("Could not retrieve file: " + name);
        }
    }
}
