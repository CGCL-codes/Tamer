package net.jalbum.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import se.datadosen.component.JPlainButton;

/**
 *
 * @author david
 */
public class JTabComponent extends JPanel {

    private JLabel tabTitle = new JLabel();

    private JLabel changedIndicator = new JLabel("*");

    private JButton closeButton = new JPlainButton(new ImageIcon(JTextPadDocument.class.getResource("res/cls_12px.png")), new ImageIcon(JTextPadDocument.class.getResource("res/cls1_12px.png")));

    public JTabComponent() {
        init();
    }

    private void init() {
        setOpaque(false);
        closeButton.setToolTipText("Close");
        changedIndicator.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(tabTitle, BorderLayout.WEST);
        titlePanel.add(changedIndicator, BorderLayout.EAST);
        add(titlePanel, BorderLayout.WEST);
        add(closeButton, BorderLayout.EAST);
        changedIndicator.setVisible(false);
    }

    public JButton getCloseButton() {
        return closeButton;
    }

    public String getTitle() {
        return tabTitle.getText();
    }

    public void setTitle(String title) {
        this.tabTitle.setText(title);
    }

    public boolean isChanged() {
        return changedIndicator.isVisible();
    }

    public void setChanged(boolean changed) {
        this.changedIndicator.setVisible(changed);
    }
}
