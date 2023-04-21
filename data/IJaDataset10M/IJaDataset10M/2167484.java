package net.sourceforge.seriesdownloader.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
import net.sourceforge.seriesdownloader.Main;
import net.sourceforge.seriesdownloader.controller.showsearch.ShowSearcher;
import net.sourceforge.seriesdownloader.model.Show;

public class SearchController implements ActionListener {

    private JTextField searchField;

    private DefaultListModel target;

    public static void init(JTextField searchField, JButton searchButton, final JList target) {
        ActionListener listener = new SearchController(searchField, target);
        searchField.addActionListener(listener);
        searchButton.addActionListener(listener);
        target.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 2) {
                    Main.addStatus("Adding show " + target.getSelectedValue(), true);
                    WatchListController.addShow((Show) target.getSelectedValue());
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
    }

    private SearchController(JTextField field, JList target) {
        this.searchField = field;
        this.target = new DefaultListModel();
        target.setModel(this.target);
    }

    public void actionPerformed(ActionEvent e) {
        target.clear();
        Main.addStatus("Searching for '" + searchField.getText() + "'", true);
        new Thread() {

            public void run() {
                final Collection<Show> shows = ShowSearcher.search(searchField.getText());
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        for (Show show : shows) {
                            target.addElement(show);
                        }
                    }
                });
                Main.addStatus("Found " + shows.size() + " matches for '" + searchField.getText() + "'", true);
            }
        }.start();
    }
}
