package sears.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;
import sears.Version;
import sears.gui.resources.SearsResources;
import sears.tools.SearsResourceBundle;
import sears.tools.Trace;

/**
 * Class JDialogAbout.
 * <br><b>Summary:</b><br>
 * Allows to display the about window.
 */
public class JDialogAbout extends SearsJDialog {

    private static final long serialVersionUID = 5648489779666362013L;

    /**The application icon*/
    private ImageIcon searsIcon;

    private JPanel jAboutPanel = null;

    private JLabel iconField = null;

    private JLabel titleLabel = null;

    private JEditorPane aboutRTFViewer = null;

    /**
     * Constructor JDialogAbout.
     * <br><b>Summary:</b><br>
     * Constructor of the class.
	 * @param title the title for this dialog
     */
    public JDialogAbout(String title) {
        super(SearsResourceBundle.getResource("about_title"));
        searsIcon = SearsResources.getIcon("SearsGUIIcon");
        initialize();
    }

    /**
     * This method initializes this 
     */
    private void initialize() {
        setResizable(false);
        setContentPane(getJAboutPanel());
        jAboutPanel.add(getIconLabel(), BorderLayout.NORTH);
        jAboutPanel.add(getTitleLabel(), BorderLayout.CENTER);
        jAboutPanel.add(getAboutRTFViewer(), BorderLayout.SOUTH);
    }

    /**
	 * this method initializes the about label.
	 * @return a JLabel
	 */
    private JPanel getJAboutPanel() {
        if (jAboutPanel == null) {
            jAboutPanel = new JPanel(new BorderLayout(3, 10));
            jAboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        return jAboutPanel;
    }

    /**
	 * this method initializes the icon label.
	 * @return a JLabel
	 */
    private JLabel getIconLabel() {
        if (iconField == null) {
            iconField = new JLabel(searsIcon);
        }
        return iconField;
    }

    /**
	 * this method initializes the rft viewer.
	 * @return a JEditorPane, an RTF Viewer
	 */
    private JEditorPane getAboutRTFViewer() {
        if (aboutRTFViewer == null) {
            RTFEditorKit rtf = new RTFEditorKit();
            aboutRTFViewer = new JEditorPane();
            aboutRTFViewer.setEditorKit(rtf);
            aboutRTFViewer.setBackground(Color.white);
            aboutRTFViewer.setEditable(false);
            aboutRTFViewer.setDragEnabled(false);
            aboutRTFViewer.setFocusable(true);
            aboutRTFViewer.setMargin(new Insets(3, 5, 3, 5));
            aboutRTFViewer.setMaximumSize(new Dimension(10, 10));
            aboutRTFViewer.setMinimumSize(new Dimension(10, 10));
            URL aboutSearsRTFFile = MainWindow.instance.getClass().getResource("/sears/gui/resources/searsAbout.rtf");
            try {
                InputStream fi = aboutSearsRTFFile.openStream();
                rtf.read(fi, aboutRTFViewer.getDocument(), 0);
            } catch (FileNotFoundException e) {
                Trace.warning("Can't find resource: " + aboutSearsRTFFile);
                aboutRTFViewer.setText("");
            } catch (IOException e) {
                Trace.warning("Can't open or read: " + aboutSearsRTFFile.getFile());
                aboutRTFViewer.setText("");
            } catch (BadLocationException e) {
                aboutRTFViewer.setText("");
            }
        }
        return aboutRTFViewer;
    }

    /**
	 * this method return the title label.
	 * @return a JLabel
	 */
    private JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel("Sears " + Version.VERSION, JLabel.CENTER);
            titleLabel.setFont(new Font("Lucida", Font.PLAIN, 14));
        }
        return titleLabel;
    }

    public void windowClosed(WindowEvent e) {
    }

    protected String getDialogName() {
        return "about";
    }
}
