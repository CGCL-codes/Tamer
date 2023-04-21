package edu.berkeley.guir.quill;

import java.beans.*;
import edu.berkeley.guir.lib.web.WebBrowserFrame;
import edu.berkeley.guir.lib.web.WebBrowserPanel;
import edu.berkeley.guir.quill.util.debug;
import java.awt.*;
import java.net.URL;
import javax.swing.*;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public class ReferenceManager {

    protected static final String REFERENCE_FRAME_PROP = "REFERENCE_FRAME";

    protected static ReferenceManager defaultManager = new ReferenceManager();

    protected static final String REFERENCE_MANUAL = "doc/reference.html";

    protected static final String TUTORIAL = "doc/tutorial.html";

    protected static JFrame infoFrame = null;

    protected ReferenceManager() {
    }

    public static ReferenceManager getManager() {
        return defaultManager;
    }

    public void showTutorial(MainFrame mainFrame) {
        showReference(mainFrame, TUTORIAL, null);
    }

    public void showReferenceManual(MainFrame mainFrame) {
        showReference(mainFrame, (String) null);
    }

    public void showReference(MainFrame mainFrame, Notice notice) {
        showReference(mainFrame, REFERENCE_MANUAL, notice.getReferenceTag());
    }

    public void showReference(MainFrame mainFrame, String referenceTag) {
        showReference(mainFrame, REFERENCE_MANUAL, referenceTag);
    }

    public void showReference(final MainFrame mainFrame, final String fileName, final String referenceTag) {
        mainFrame.moreBusy();
        if (mainFrame.getRootPane().getClientProperty(REFERENCE_FRAME_PROP) == null) {
            infoFrame = new JFrame("Loading Reference Manual");
            Container contents = infoFrame.getContentPane();
            contents.setLayout(new BorderLayout());
            JLabel infoLabel = new JLabel("Loading the reference manual for the first time.  Please wait...");
            int borderWidth = 5;
            infoLabel.setBorder(BorderFactory.createMatteBorder(borderWidth, borderWidth, borderWidth, borderWidth, infoLabel.getBackground()));
            contents.add(infoLabel, BorderLayout.CENTER);
            infoFrame.pack();
            Rectangle mfBounds = mainFrame.getBounds();
            Dimension ifSize = infoFrame.getSize();
            infoFrame.setLocation(mfBounds.x + mfBounds.width / 2 - ifSize.width / 2, mfBounds.y + mfBounds.height / 2 - ifSize.height / 2);
            infoFrame.setVisible(true);
            infoLabel.repaint();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                showReferenceImpl(mainFrame, fileName, referenceTag);
            }
        });
    }

    protected void showReferenceImpl(final MainFrame mainFrame, final String fileName, final String refTag) {
        WebBrowserFrame browserFrame = (WebBrowserFrame) mainFrame.getRootPane().getClientProperty(REFERENCE_FRAME_PROP);
        if (browserFrame == null) {
            browserFrame = new WebBrowserFrame();
            browserFrame.setSize(new Dimension(500, 400));
            browserFrame.setTitle(mainFrame.getGesturePackage().getName() + " - " + "Reference - quill");
            mainFrame.getRootPane().putClientProperty(REFERENCE_FRAME_PROP, browserFrame);
        }
        final URL url = this.getClass().getClassLoader().getResource(fileName);
        if (url == null) {
            mainFrame.message("ERROR: Can't find reference manual (at '" + fileName + "').\n");
            mainFrame.lessBusy();
            return;
        }
        final WebBrowserFrame finalBrowserFrame = browserFrame;
        final WebBrowserPanel finalBrowserPanel = browserFrame.getWebBrowserPanel();
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    finalBrowserPanel.removePropertyChangeListener(finalBrowserPanel.NEW_PAGE_LOADED_EVENT, this);
                    finalBrowserFrame.setState(java.awt.Frame.NORMAL);
                    finalBrowserFrame.setVisible(true);
                    finalBrowserFrame.toFront();
                    if (refTag != null) {
                        finalBrowserPanel.scrollToReference(refTag);
                    }
                    finalBrowserFrame.repaint();
                    infoFrame.setVisible(false);
                } finally {
                    mainFrame.lessBusy();
                }
            }
        };
        finalBrowserPanel.addPropertyChangeListener(finalBrowserPanel.NEW_PAGE_LOADED_EVENT, l);
        String oldUrl = finalBrowserPanel.getCurrentUrl();
        finalBrowserPanel.goToUrl(url);
        String newUrl = finalBrowserPanel.getCurrentUrl();
        if (newUrl.equals(oldUrl)) {
            l.propertyChange(null);
        }
    }

    public static String getFeatureTag(Class featureClass) {
        String wholeName = featureClass.getName();
        int lastDot = wholeName.lastIndexOf('.');
        return wholeName.substring(lastDot + 1);
    }
}
