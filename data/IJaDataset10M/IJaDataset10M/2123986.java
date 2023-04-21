package guitarscales;

import guitarscales.util.BrowserLauncher;
import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.apache.log4j.Category;

/**
 * This the about box
 * @author Wim Deblauwe
 */
public class AboutBoxView extends JDialog {

    static Category cat = Category.getInstance(AboutBoxView.class.getName());

    public AboutBoxView(Frame owner) {
        super(owner, "About...", true);
        setResizable(false);
        constructGUI();
    }

    private void constructGUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("GuitarScales", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22.0f));
        JEditorPane pane = createHTMLPane();
        pane.setBackground(titleLabel.getBackground());
        contentPane.add(titleLabel);
        contentPane.add(Box.createRigidArea(new Dimension(1, 3)));
        contentPane.add(pane);
        contentPane.add(Box.createRigidArea(new Dimension(1, 3)));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AboutBoxView.this.setVisible(false);
            }
        });
        contentPane.add(closeButton);
        contentPane.add(Box.createRigidArea(new Dimension(1, 3)));
    }

    private JEditorPane createHTMLPane() {
        JEditorPane pane = new JEditorPane();
        pane.setEditable(false);
        pane.setContentType("text/html");
        StringBuffer buffer = new StringBuffer("Created by<br>");
        buffer.append("<b>Wim Deblauwe</b>");
        buffer.append("<P>Version: <i>0.2</i><br>");
        buffer.append("Website: <a href=\"http://guitarscales.sourceforge.net\">http://guitarscales.sourceforge.net</a>");
        pane.setText(buffer.toString());
        pane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String url = e.getURL().toExternalForm();
                    cat.debug("Opening browser with url: " + url);
                    BrowserLauncher.displayURL(url);
                }
            }
        });
        return pane;
    }
}
