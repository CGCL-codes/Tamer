package kotan.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    JProgressBar progressBar;

    public StatusBar() {
        initComponents();
    }

    private void initComponents() {
        progressBar = new JProgressBar();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 3, 15);
        add(progressBar, gbc);
    }

    public void setActive(boolean b) {
        progressBar.setValue(0);
        progressBar.setIndeterminate(b);
    }
}
