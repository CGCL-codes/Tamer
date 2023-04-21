package application;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OverviewPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    OverviewPanel() {
        super();
        this.setBackground(Color.WHITE);
        JPanel infoText;
        this.setLayout(new BorderLayout());
        infoText = new JPanel();
        infoText.setBackground(Color.WHITE);
        JLabel header = new JLabel("Einen Überblick über die eigenen Läufe anzeigen");
        header.setFont(new java.awt.Font("Dialog", 1, 18));
        infoText.add(header, BorderLayout.NORTH);
        this.add(infoText, BorderLayout.NORTH);
    }
}
