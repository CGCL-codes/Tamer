package gov.nasa.luv;

import static gov.nasa.luv.Constants.PLEXIL_EXEC;
import static gov.nasa.luv.Constants.PLEXIL_SIM;
import static gov.nasa.luv.Constants.PLEXIL_TEST;
import static gov.nasa.luv.Constants.PROP_CFGWIN_LOC;
import static gov.nasa.luv.Constants.PROP_CFGWIN_SIZE;
import gov.nasa.luv.ExecSelect.AppSettings;
import gov.nasa.luv.ExecSelect.PlexilFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * This class was modeled after the CheckNodeTreeExample, CheckNode, CheckRenerer
 * classes from Tame Swing examplescreated by Nobuo Tamemasa.
 *
 * http://devdaily.com/java/swing/tame/
 * 
@author Jason Ho
@version 1.0 12/23/10
*/
public class LibraryLoader extends JFrame implements ItemListener {

    private static LibraryLoader frame;

    private static boolean error;

    private JPanel topSection;

    private JScrollPane checkBoxList;

    private JScrollPane previewArea;

    private JPanel buttonPane;

    private ArrayList<CheckNode> nodes;

    private JTextArea preview;

    private JTree main_tree;

    private DynamicTree dyn_tree;

    public LibraryLoader() {
    }

    /** Construct an LibraryLoader. 
     *
     * @param title the title of this LibraryLoader
     */
    public LibraryLoader(String title) throws FileNotFoundException {
        super(title);
        error = false;
        createCheckList();
        if (!error) {
            createPreviewArea();
            createTopSection();
            createButtons();
            getContentPane().add(topSection, BorderLayout.NORTH);
            getContentPane().add(checkBoxList, BorderLayout.WEST);
            getContentPane().add(dyn_tree, BorderLayout.WEST);
            getContentPane().add(previewArea, BorderLayout.EAST);
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
        }
    }

    private void createCheckList() throws FileNotFoundException {
        nodes = new ArrayList<CheckNode>();
        nodes.add(new CheckNode(new File(Constants.PLEXIL_HOME + System.getProperty("file.separator") + "examples" + System.getProperty("file.separator"))));
        dyn_tree = new DynamicTree();
        dyn_tree.addObject(nodes.get(0));
        checkBoxList = new JScrollPane(dyn_tree);
        checkBoxList.setPreferredSize(new Dimension(450, 50));
    }

    private void createPreviewArea() throws FileNotFoundException {
        preview = new JTextArea();
        preview.setPreferredSize(new Dimension(435, 50));
        setPreviewOfLibraries();
        preview.setEditable(false);
        previewArea = new JScrollPane(preview);
    }

    private void createTopSection() {
        JLabel topMessage = new JLabel();
        topMessage.setText(getTopMessage());
        topMessage.setFont(topMessage.getFont().deriveFont(Font.PLAIN, 12.0f));
        topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.PAGE_AXIS));
        topSection.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topSection.add(topMessage);
    }

    private void createButtons() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ButtonActionListener());
        JButton libButton = new JButton("Add Library");
        libButton.addActionListener(new ButtonActionListener());
        JButton clearButton = new JButton("Clear Libraries");
        clearButton.addActionListener(new ButtonActionListener());
        JButton createCFGButton = new JButton("OK");
        createCFGButton.addActionListener(new ButtonActionListener());
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createHorizontalStrut(3));
        buttonPane.add(libButton);
        buttonPane.add(Box.createHorizontalStrut(3));
        buttonPane.add(clearButton);
        buttonPane.add(Box.createHorizontalStrut(3));
        buttonPane.add(createCFGButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
    }

    private void addLibrary(File lib) {
        if (lib != null) {
            CheckNode node = new CheckNode(lib);
            nodes.add(node);
            nodes.get(0).add(node);
            dyn_tree.addObject(node);
        }
    }

    public void removeAllNodes() {
        Luv.getLuv().getLibLoad().dyn_tree.clear();
        Luv.getLuv().getLibLoad().getPreview().setText(null);
        nodes.removeAll(nodes);
        nodes.add(new CheckNode(new File(Constants.PLEXIL_HOME + System.getProperty("file.separator") + "examples" + System.getProperty("file.separator"))));
    }

    protected void selectLibraries() throws FileNotFoundException {
        ArrayList<String> libArr = Luv.getLuv().getExecSelect().getSettings().getLibArray();
        ArrayList<String> uniqArr = new ArrayList<String>();
        boolean dup = false;
        for (int i = 0; i < libArr.size(); i++) if (!libArr.get(i).equals("")) {
            for (int j = 0; j < uniqArr.size(); j++) {
                if (uniqArr.get(j).equals(libArr.get(i))) dup = true;
            }
            if (!dup) {
                Luv.getLuv().getLibLoad().addLibrary(new File(libArr.get(i)));
                uniqArr.add(libArr.get(i));
            }
        }
    }

    private void setPreviewOfLibraries() throws FileNotFoundException {
        ArrayList<File> lines = new ArrayList<File>();
        if (!lines.isEmpty()) {
            preview.setText("");
            for (File line : lines) {
                preview.append(line.getAbsolutePath() + "\n");
            }
        } else {
            if (preview != null) {
                preview.setText("");
            }
        }
    }

    private String getTopMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><p align=left>");
        sb.append("<br></br>");
        sb.append("<br></br>");
        sb.append("</p></html>");
        return sb.toString();
    }

    public JTextArea getPreview() {
        return preview;
    }

    public ArrayList<File> getLibraryList() {
        File selected = null;
        ArrayList<File> list = new ArrayList<File>();
        for (CheckNode node : nodes) {
            selected = (File) node.getUserObject();
            list.add(selected);
        }
        return list;
    }

    class ButtonActionListener implements ActionListener {

        ButtonActionListener() {
        }

        public void actionPerformed(ActionEvent ev) {
            if (ev.getActionCommand().equals("Cancel")) {
                frame.setVisible(false);
            } else if (ev.getActionCommand().equals("Add Library")) {
                JFileChooser fc = new JFileChooser(Constants.PLEXIL_HOME);
                ExecSelect.PlexilFilter pf = Luv.getLuv().getExecSelect().new PlexilFilter("PLX");
                if (pf != null) {
                    fc.addChoosableFileFilter(pf);
                }
                int returnVal = fc.showDialog(null, "Choose File");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    frame.addLibrary(file);
                    Luv.getLuv().getStatusMessageHandler().showStatus("Added Library " + file.getAbsolutePath());
                }
            } else if (ev.getActionCommand().equals("Clear Libraries")) {
                Object[] options = { "Yes", "No" };
                int clear = JOptionPane.showOptionDialog(Luv.getLuv(), "Are you sure you want to clear all Libraries?", "Clear Libraries", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (clear == 0) {
                    Luv.getLuv().getLibLoad().removeAllNodes();
                }
            } else if (ev.getActionCommand().equals("OK")) {
                Luv.getLuv().getLibLoad().getPreview().setText("");
                ArrayList<File> parentSelected = new ArrayList<File>();
                Iterator<CheckNode> it = Luv.getLuv().getLibLoad().nodes.iterator();
                while (it.hasNext()) {
                    CheckNode node = it.next();
                    if (node.getUserObject() instanceof File) {
                        File selected = (File) node.getUserObject();
                        parentSelected.add(selected.getAbsoluteFile());
                        Luv.getLuv().getLibLoad().getPreview().append(selected.getAbsolutePath() + "\n");
                    }
                }
                frame.setVisible(false);
            }
        }
    }

    /** Creates an instance of a LibraryLoader.  */
    public void open() throws FileNotFoundException {
        if (frame != null && frame.isVisible()) {
            frame.setVisible(false);
        }
        frame = Luv.getLuv().getLibLoad();
        if (!error) {
            frame.setPreferredSize(Luv.getLuv().getProperties().getDimension(PROP_CFGWIN_SIZE));
            frame.setLocation(Luv.getLuv().getProperties().getPoint(PROP_CFGWIN_LOC));
            frame.pack();
            frame.setVisible(true);
        }
    }

    /** {@inheritDoc} */
    public void itemStateChanged(ItemEvent e) {
        try {
            if (frame != null) {
                frame.setPreviewOfLibraries();
            }
        } catch (FileNotFoundException ex) {
            Luv.getLuv().getStatusMessageHandler().displayErrorMessage(ex, "ERROR: file not found");
        }
    }

    public class NodeSelectionListener extends MouseAdapter {

        JTree tree;

        NodeSelectionListener(JTree tree) {
            this.tree = tree;
        }

        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath path = tree.getPathForRow(row);
            if (path != null) {
                CheckNode node = (CheckNode) path.getLastPathComponent();
                boolean isSelected = !(node.isSelected());
                node.setSelected(isSelected);
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                if (row == 0) {
                    tree.revalidate();
                    tree.repaint();
                }
            }
        }
    }
}
