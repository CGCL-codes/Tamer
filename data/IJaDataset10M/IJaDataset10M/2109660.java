package jscorch;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GamePrefsDialog extends JDialog {

    JComboBox wallTypes;

    JButton ok = new JButton("OK");

    JButton cancel = new JButton("Cancel");

    public GamePrefsDialog(Frame par) {
        super(par, "Game Preferences", true);
        init();
        this.getRootPane().setDefaultButton(ok);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public void init() {
        JPanel contentPane = new JPanel();
        Container wallTypeContainer = new Container();
        Container buttonContainer = new Container();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        wallTypeContainer.setLayout(new BoxLayout(wallTypeContainer, BoxLayout.X_AXIS));
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
        Font f = Preferences.FONT;
        ok.setFont(f);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Preferences.WALL_TYPE = wallTypes.getSelectedIndex();
                dispose();
            }
        });
        cancel.setFont(f);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JLabel wallTypeLabel = new JLabel("Wall type:");
        wallTypeLabel.setFont(f);
        wallTypes = new JComboBox(GameConstants.WALL_TYPES);
        wallTypeLabel.setLabelFor(wallTypes);
        wallTypes.setSelectedIndex(Preferences.WALL_TYPE);
        wallTypes.setFont(f);
        wallTypes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ok.requestFocus();
            }
        });
        wallTypeContainer.add(wallTypeLabel);
        wallTypeContainer.add(wallTypes);
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(cancel);
        buttonContainer.add(ok);
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(wallTypeContainer);
        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPane.add(buttonContainer);
        this.setContentPane(contentPane);
    }
}
