package corina.prefs.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import corina.core.App;

public class DataPrefsPanel extends JComponent implements ActionListener {

    private JTextField folder;

    private Component parent;

    private JFileChooser chooser = new JFileChooser();

    public DataPrefsPanel() {
        setLayout(new BorderLayout());
        chooser.setDialogTitle("Choose new data folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(App.prefs.getPref("corina.dir.data")));
        String oldFolder = App.prefs.getPref("corina.dir.data");
        folder = new JTextField(new File(oldFolder).getAbsolutePath());
        folder.setEditable(false);
        folder.setColumns(30);
        JLabel l = new JLabel("Data is stored in folder:");
        JButton change = new JButton("Change...");
        l.setLabelFor(change);
        change.addActionListener(this);
        Container c = new Container();
        c.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 4));
        c.add(l);
        c.add(folder);
        c.add(change);
        add(c, BorderLayout.NORTH);
    }

    private Runnable showdialog = new Runnable() {

        public void run() {
            int rv = chooser.showDialog(parent, "OK");
            if (rv != JFileChooser.APPROVE_OPTION) return;
            String newFolder = chooser.getSelectedFile().getPath();
            App.prefs.setPref("corina.dir.data", newFolder);
            folder.setText(newFolder);
        }
    };

    public void actionPerformed(ActionEvent e) {
        parent = getTopLevelAncestor();
        SwingUtilities.invokeLater(showdialog);
    }
}
