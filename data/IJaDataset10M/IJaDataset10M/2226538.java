package be.abeel.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class MultiFileBean extends GridBagPanel {

    private static final long serialVersionUID = -1341965704591306937L;

    private DefaultListModel listModel;

    /**
     * Get the selected files that are currently in the MultiFileBean
     * 
     * @return list of selected files
     */
    public File[] getFiles() {
        File[] out = new File[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) out[i] = (File) listModel.getElementAt(i);
        return out;
    }

    /**
     * Add a file filter to accept files.
     * 
     * @param f
     */
    public void addFilter(FileFilter f) {
        chooser.addChoosableFileFilter(f);
    }

    private JFileChooser chooser = new JFileChooser("Choose datafiles");

    /**
     * Construct a new MultiFileBean
     * 
     */
    public MultiFileBean() {
        chooser.setMultiSelectionEnabled(true);
        final MultiFileBean _self = this;
        this.setBorder(BorderFactory.createTitledBorder("Input data parameters"));
        this.setLayout(new GridBagLayout());
        final JList list = new JList(new DefaultListModel());
        list.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listModel = (DefaultListModel) list.getModel();
        JButton fileButton = new JButton("Add files...");
        fileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor(_self));
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] tmp = chooser.getSelectedFiles();
                    for (File f : tmp) listModel.addElement(f);
                }
            }
        });
        JButton removeButton = new JButton("Remove selected...");
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Object[] tmp = list.getSelectedValues();
                for (Object o : tmp) {
                    listModel.removeElement(o);
                }
            }
        });
        JButton clearButton = new JButton("Clear all");
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                listModel.clear();
            }
        });
        JLabel dataLabel = new JLabel("Datafile(s)");
        dataLabel.setVerticalAlignment(JLabel.TOP);
        this.add(dataLabel, gc);
        gc.gridx++;
        gc.gridheight = 3;
        JScrollPane listPane = new JScrollPane(list);
        listPane.setPreferredSize(new Dimension(600, 100));
        this.add(listPane, gc);
        gc.gridheight = 1;
        gc.gridx++;
        this.add(fileButton, gc);
        gc.gridy++;
        this.add(removeButton, gc);
        gc.gridy++;
        this.add(clearButton, gc);
    }
}
