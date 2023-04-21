package common.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.TextUtilities;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.jEdit;

/**
 * A component that contains a JList with a list of path,
 * and two buttons add and remove to add paths.
 * This can be used in option panes.
 * The property given in the constructor contains the list of paths
 * separated by the File.pathSeparator
 * Calling save() will save the paths to that property.
 *
 * @author Matthieu Casanova
 */
public class VFSPathFileList extends JPanel {

    private JList searchList;

    private DefaultListModel model;

    private JButton add;

    private JButton remove;

    private String pathProperty;

    private JScrollPane scroll;

    public VFSPathFileList() {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        model = new DefaultListModel();
        searchList = new JList(model);
        searchList.setPrototypeCellValue("a");
        add = new JButton(jEdit.getProperty("common.add"));
        remove = new JButton(jEdit.getProperty("common.remove"));
        ActionListener actionListener = new MyActionListener();
        add.addActionListener(actionListener);
        remove.addActionListener(actionListener);
        Box box = Box.createHorizontalBox();
        box.add(add);
        box.add(Box.createHorizontalStrut(6));
        box.add(remove);
        box.add(Box.createHorizontalGlue());
        scroll = new JScrollPane(searchList);
        add(scroll);
        add(box);
    }

    public VFSPathFileList(String pathProperty) {
        this();
        this.pathProperty = pathProperty;
        scroll.setBorder(BorderFactory.createTitledBorder(jEdit.getProperty(pathProperty + ".label")));
        String property = jEdit.getProperty(pathProperty, "");
        StringTokenizer tokenizer = new StringTokenizer(property, File.pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            String s = tokenizer.nextToken();
            addPath(s);
        }
    }

    public void save() {
        if (pathProperty != null) {
            jEdit.setProperty(pathProperty, getPaths());
        }
    }

    public String getPaths() {
        int size = model.size();
        Collection<String> paths = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            String path = (String) model.getElementAt(i);
            paths.add(path);
        }
        return TextUtilities.join(paths, File.pathSeparator);
    }

    private void addPath(String path) {
        if (path != null && !path.isEmpty() && !model.contains(path)) model.addElement(path);
    }

    private class MyActionListener implements ActionListener {

        private String lastBrowserDialog;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {
                String[] strings = GUIUtilities.showVFSFileDialog(jEdit.getActiveView(), lastBrowserDialog, VFSBrowser.CHOOSE_DIRECTORY_DIALOG, true);
                if (strings != null) {
                    for (String string : strings) {
                        lastBrowserDialog = string;
                        addPath(string);
                    }
                }
            }
            if (e.getSource() == remove) {
                int selectedIndex = searchList.getSelectedIndex();
                if (selectedIndex != -1) model.remove(selectedIndex);
            }
        }
    }
}
