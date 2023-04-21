package org.tranche.gui.hash;

import org.tranche.gui.*;
import org.tranche.project.ProjectSummary;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import org.tranche.gui.project.ProjectPool;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class HashSuggestionWindow extends JWindow {

    private JTextArea hashTextArea;

    private JPanel panel = new JPanel();

    public HashSuggestionWindow(JTextArea hashTextArea) {
        this.hashTextArea = hashTextArea;
        setMinimumSize(new Dimension(0, 200));
        setSize(new Dimension(0, 200));
        setAlwaysOnTop(true);
        setFocusable(false);
        setFocusableWindowState(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        add(new GenericScrollPane(panel));
    }

    public void refresh() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    ProjectPool.waitForStartup();
                    ArrayList<JPanel> suggestions = new ArrayList<JPanel>();
                    for (final ProjectSummary pfs : ProjectPool.getProjectSummaries()) {
                        if (pfs.hash.toString().startsWith(hashTextArea.getText())) {
                            final JPanel panel = new JPanel();
                            panel.setLayout(new GridBagLayout());
                            panel.setBackground(Color.WHITE);
                            panel.addMouseListener(new MouseAdapter() {

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    panel.setBackground(Styles.COLOR_LIGHT_BLUE);
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                    panel.setBackground(Color.WHITE);
                                }

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    hashTextArea.setText(pfs.hash.toString());
                                    setVisible(false);
                                }
                            });
                            GridBagConstraints gbc = new GridBagConstraints();
                            JLabel titleLabel = new GenericLabel(pfs.title);
                            titleLabel.setFont(Styles.FONT_11PT_BOLD);
                            gbc.anchor = GridBagConstraints.NORTHWEST;
                            gbc.fill = GridBagConstraints.HORIZONTAL;
                            gbc.gridwidth = GridBagConstraints.REMAINDER;
                            gbc.weightx = 1;
                            gbc.insets = new Insets(3, 5, 0, 5);
                            panel.add(titleLabel, gbc);
                            JLabel hashLabel = new GenericLabel(pfs.hash.toString());
                            hashLabel.setFont(Styles.FONT_10PT);
                            hashLabel.setHorizontalAlignment(GenericLabel.LEFT);
                            gbc.insets = new Insets(1, 10, 3, 5);
                            panel.add(hashLabel, gbc);
                            suggestions.add(panel);
                        }
                    }
                    if (!suggestions.isEmpty()) {
                        panel.removeAll();
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.anchor = GridBagConstraints.NORTHWEST;
                        gbc.gridwidth = GridBagConstraints.REMAINDER;
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.weightx = 1;
                        gbc.weighty = 0;
                        for (JPanel p : suggestions) {
                            panel.add(p, gbc);
                        }
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.weighty = 1;
                        panel.add(Box.createVerticalStrut(1), gbc);
                        setVisible(true);
                    } else {
                        setVisible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }
}
