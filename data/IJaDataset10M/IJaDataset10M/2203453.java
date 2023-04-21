package com.leclercb.taskunifier.gui.components.about;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;

public class AboutDialog extends JDialog {

    private static AboutDialog INSTANCE;

    public static AboutDialog getInstance() {
        if (INSTANCE == null) INSTANCE = new AboutDialog();
        return INSTANCE;
    }

    private AboutDialog() {
        super(MainFrame.getInstance().getFrame());
        this.initialize();
    }

    private void initialize() {
        this.setModal(true);
        this.setTitle(Translations.getString("general.about"));
        this.setSize(600, 350);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        if (this.getOwner() != null) this.setLocationRelativeTo(this.getOwner());
        AboutPanel aboutPanel = new AboutPanel();
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        this.add(aboutPanel, BorderLayout.CENTER);
        this.initializeButtonsPanel();
    }

    private void initializeButtonsPanel() {
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.this.setVisible(false);
            }
        };
        JButton okButton = new TUOkButton(listener);
        JPanel panel = new TUButtonsPanel(okButton);
        this.add(panel, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(okButton);
    }
}
