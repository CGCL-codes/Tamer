package nox.ui.common;

import java.awt.Container;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AboutDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 显示关于信息JEditorPane
	 */
    private JEditorPane aboutDoc;

    public AboutDialog() {
        this.setTitle("Welcome to NoX's world!");
        Container contentPane = getContentPane();
        aboutDoc = new JEditorPane();
        aboutDoc.setEditable(false);
        try {
            String url = "file:/" + System.getProperty("user.dir") + System.getProperty("file.separator") + SystemPath.DOCS_RESOURCE_PATH + "About.htm";
            aboutDoc.setPage(url);
        } catch (IOException e1) {
        }
        aboutDoc.setEditable(false);
        aboutDoc.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e_m_hylink) {
                try {
                    if (e_m_hylink.getEventType().toString().equals("ACTIVATED")) aboutDoc.setPage(e_m_hylink.getURL());
                } catch (IOException ex_m_hylink) {
                }
            }
        });
        contentPane.add(aboutDoc);
    }
}
