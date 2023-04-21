package org.swhite.JGlider;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutThisHouseDialog extends JDialog implements ActionListener {

    public AboutThisHouseDialog(Frame owner, String houseName, HouseCreator houseCreator) {
        super(owner, "About " + houseName, true);
        setSize(400, 200);
        Container contentPane = getContentPane();
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);
        if (houseCreator.getAuthorsName() != null) {
            JLabel nameLabel = new JLabel("Author's Name: ");
            JLabel authorsName = new JLabel(houseCreator.getAuthorsName());
            add(nameLabel, 0, 0, 1, 1, 0, 0);
            add(authorsName, 1, 0, 1, 1, 100, 0);
        }
        if (houseCreator.getAuthorsEmail() != null) {
            JLabel emailLabel = new JLabel("Author's Email: ");
            JLabel authorsEmail = new JLabel(houseCreator.getAuthorsEmail());
            add(emailLabel, 0, 1, 1, 1, 0, 0);
            add(authorsEmail, 1, 1, 1, 1, 100, 0);
        }
        if (houseCreator.getAuthorsWebpage() != null) {
            JLabel webpageLabel = new JLabel("Author's Webpage: ");
            JLabel authorsWebpage = new JLabel(houseCreator.getAuthorsWebpage());
            add(webpageLabel, 0, 2, 1, 1, 0, 0);
            add(authorsWebpage, 1, 2, 1, 1, 100, 0);
        }
        if (houseCreator.getAuthorsComment() != null) {
            JLabel commentLabel = new JLabel("Author's Comments: ");
            JTextArea comment = new JTextArea(houseCreator.getAuthorsComment());
            comment.setEditable(false);
            JScrollPane pane = new JScrollPane(comment);
            add(commentLabel, 0, 3, 2, 1, 100, 0);
            add(pane, 0, 4, 2, 1, 100, 100);
        }
        JButton okayButton = new JButton("Okay");
        okayButton.addActionListener(this);
        add(okayButton, 0, 5, 2, 1, 0, 0);
    }

    public void add(Component c, int x, int y, int w, int h, int weightx, int weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 0;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.insets = new Insets(2, 2, 2, 2);
        getContentPane().add(c, constraints);
    }

    public void actionPerformed(ActionEvent event) {
        dispose();
    }
}
