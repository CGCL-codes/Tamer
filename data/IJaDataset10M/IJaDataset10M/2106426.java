package ie.ucd.searchengine.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Creates a Help Contents dialog
 * @author Brendan Maguire
 *
 */
public class HelpContentsDialog extends JDialog {

    private JPanel jContentPane = null;

    private JScrollPane scrollPane = null;

    private JEditorPane editorPane = null;

    private static final String HELP_CONTENTS = "ReadMe.html";

    private String helpContents = HELP_CONTENTS;

    /**
	 * Creates a Help dialog with default help contents
	 * @param owner Owning frame
	 */
    public HelpContentsDialog(Frame owner) {
        super(owner);
        initialize();
    }

    /**
	 * Creates a Help dialog 
	 * @param owner Owning frame
	 */
    public HelpContentsDialog(Frame owner, String helpContents) {
        super(owner);
        this.helpContents = helpContents;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(600, 600);
        this.setTitle("Help Contents");
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getEditorPane());
        }
        return scrollPane;
    }

    /**
	 * This method initializes editorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
    private JEditorPane getEditorPane() {
        if (editorPane == null) {
            editorPane = new JEditorPane();
            editorPane.setContentType("text/html");
            editorPane.setText(loadHelpContents());
            editorPane.setEditable(false);
        }
        return editorPane;
    }

    /**
	 * Load the help contents
	 * @return Returns the help contents
	 */
    private String loadHelpContents() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(helpContents));
            StringBuffer b = new StringBuffer();
            String s = "";
            while ((s = br.readLine()) != null) {
                b.append(s);
            }
            return b.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
