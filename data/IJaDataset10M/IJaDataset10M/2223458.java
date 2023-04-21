package JessTab;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import edu.stanford.smi.protege.util.SystemUtilities;
import javax.swing.event.HyperlinkListener;

/**
 * <p>Title: JessTab</p>
 *
 * <p>Description: Your description</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: Link�ping University</p>
 *
 * @author Henrik Eriksson
 * @version 1.0
 */
public class NoJessInstalledPanel extends JPanel {

    BorderLayout borderLayout1 = new BorderLayout();

    JLabel jLabel2 = new JLabel();

    JEditorPane jEditorPane1 = new JEditorPane();

    JScrollPane jScrollPane1 = new JScrollPane();

    public NoJessInstalledPanel() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", Font.BOLD | Font.ITALIC, 24));
        jLabel2.setForeground(Color.gray);
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Welcome to JessTab");
        jEditorPane1.setMinimumSize(new Dimension(100, 100));
        jEditorPane1.setPreferredSize(new Dimension(420, 200));
        jEditorPane1.setEditable(false);
        jEditorPane1.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    SystemUtilities.showHTML(e.getURL().toString());
                }
            }
        });
        java.net.URL u = NoJessInstalledPanel.class.getResource("NoJessInstalledPanel.html");
        if (u != null) jEditorPane1.setPage(u);
        jScrollPane1.setMinimumSize(new Dimension(100, 100));
        jScrollPane1.setPreferredSize(new Dimension(200, 200));
        jScrollPane1.getViewport().add(jEditorPane1);
        this.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        this.add(jLabel2, java.awt.BorderLayout.NORTH);
    }
}
