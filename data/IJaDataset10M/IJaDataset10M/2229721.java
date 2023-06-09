package Messages;

import Utils.HyperLinkListenerImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

/**
 *
 * @author  User
 */
public class Legend extends javax.swing.JDialog {

    /** Creates new form Legend */
    public Legend(java.awt.Frame parent) {
        super(parent, false);
        this.setLocation(parent.getLocation().x + 50, parent.getLocation().y + 100);
        initComponents();
        blnFileNotFound = false;
        jepInfo.addHyperlinkListener(new HyperLinkListenerImpl((jepInfo)));
        this.setSize(650, 700);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jepInfo = new javax.swing.JEditorPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 700));
        jepInfo.setContentType("text/html");
        jepInfo.setEditable(false);
        jScrollPane1.setViewportView(jepInfo);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        pack();
    }

    public void setContentType(String type) {
        jepInfo.setContentType(type);
    }

    public String getContentType() {
        return jepInfo.getContentType();
    }

    public void setData(File file) {
        FileInputStream fisReader;
        InputStreamReader isrReader;
        BufferedReader brReader;
        try {
            fisReader = new FileInputStream(file);
            isrReader = new InputStreamReader(fisReader);
            brReader = new BufferedReader(isrReader);
            String s = brReader.readLine();
            if (!jepInfo.getContentType().equals(CONTENT_TYPE_HTML)) while (s != null) {
                jepInfo.getDocument().insertString(jepInfo.getDocument().getLength(), s + "\n", null);
                s = brReader.readLine();
            }
            if (jepInfo.getContentType().equals(CONTENT_TYPE_HTML)) {
                if (System.getProperty("os.name").toLowerCase().startsWith("windows")) jepInfo.setPage("file:" + File.separator + file.getAbsolutePath()); else jepInfo.setPage("file://" + file.getAbsolutePath());
            }
            if (jepInfo.getDocument().getLength() > 0) jepInfo.setCaretPosition(0);
        } catch (IOException ex) {
            OKMessage okm = new OKMessage((JFrame) this.getOwner(), "<html>Unable to open file<br>" + file.getAbsolutePath() + "!</html>");
            okm.setVisible(true);
            okm = null;
            blnFileNotFound = true;
        } catch (BadLocationException ex1) {
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (blnFileNotFound) this.dispose(); else super.setVisible(visible);
    }

    public static final String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";

    public static final String CONTENT_TYPE_HTML = "text/html";

    private boolean blnFileNotFound;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JEditorPane jepInfo;
}
