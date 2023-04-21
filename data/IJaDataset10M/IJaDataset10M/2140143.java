package annot.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutDialog extends JOptionPane {

    private static final long serialVersionUID = 5587394354629439712L;

    public AboutDialog() {
        super();
    }

    public void showDialog() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(new JLabel(new ImageIcon(SwingResourceManager.getImage(this.getClass(), "about.jpg"))), BorderLayout.WEST);
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(new JLabel("Blast2KEGG 1.0", SwingConstants.CENTER), BorderLayout.NORTH);
        innerPanel.add(new JLabel("<html>Qi Wang<br>&copy;2011 Rendware, Inc.</html>", SwingConstants.CENTER), BorderLayout.CENTER);
        JHyperlinkLabel btnUrl = new JHyperlinkLabel("http://www.rendware.com/tvk/Blast2KEGG.html", "http://www.rendware.com/tvk/Blast2KEGG.html");
        innerPanel.add(btnUrl, BorderLayout.SOUTH);
        outerPanel.add(innerPanel, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(null, outerPanel, "About", JOptionPane.PLAIN_MESSAGE);
    }
}
