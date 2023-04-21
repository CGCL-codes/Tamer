package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * @author Calvin Hopkins and Charlie Powell
 *
 */
public class DirectorySelectPanel extends JPanel implements ActionListener {

    protected JButton browseButton;

    protected JTextField directoryBox;

    private boolean validDestination;

    private JFrame mw;

    public DirectorySelectPanel(MainWindow mw) {
        this.validDestination = false;
        this.mw = mw;
        this.setLayout(new BorderLayout());
        browseButton = new JButton("Browse...");
        browseButton.addActionListener(this);
        this.directoryBox = new JTextField("...", 50);
        this.directoryBox.setEditable(false);
        this.directoryBox.setMinimumSize(new Dimension(75, 1));
        this.add(directoryBox, BorderLayout.CENTER);
        this.add(browseButton, BorderLayout.EAST);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        System.out.println("Browse Button Pressed");
        ((MainWindow) mw).callBrowsePressed();
    }

    public void setValidDestination() {
        this.validDestination = true;
    }

    public boolean isValidDestination() {
        return validDestination;
    }

    public void updateTextBox(String s) {
        directoryBox.setText(s);
        this.paint(getGraphics());
    }
}
