package packjacket.gui;

import com.l2fprod.common.swing.JDirectoryChooser;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.VerticalLayout;
import packjacket.RunnerClass;
import packjacket.xml.Pack;
import packjacket.xml.XFile;

/**
 * Class that has utilities for GUI things.
 * @author Amandeep Grewal
 * @author Manodasan Wignarajah
 */
public class GUIUtils {

    private static JFileChooser fc = new JFileChooser();

    private static JDirectoryChooser dc = new JDirectoryChooser();

    /**
     * Ask to save a file from user
     * @param filterL which files allowed to be saved
     * @return file chosen
     */
    public static File save(FileNameExtensionFilter filterL) {
        ArrayList<FileFilter> filters = new ArrayList<FileFilter>();
        filters.add(filterL);
        JFileChooser chooser = fc;
        for (FileFilter filter : chooser.getChoosableFileFilters()) if (!filter.equals(chooser.getAcceptAllFileFilter())) chooser.removeChoosableFileFilter(filter);
        if (filters != null && !filters.isEmpty()) {
            for (FileFilter filter : filters) try {
                chooser.addChoosableFileFilter(filter);
            } catch (java.lang.NullPointerException e) {
            }
            chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
            try {
                chooser.setFileFilter(filters.iterator().next());
            } catch (java.lang.NullPointerException e) {
            }
        }
        int state;
        try {
            if (UIManager.getLookAndFeel().toString().contains("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                boolean useNimbus = false;
                for (LookAndFeelInfo li : UIManager.getInstalledLookAndFeels()) if (li.getClassName().equals("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")) {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    useNimbus = true;
                    break;
                }
                if (!useNimbus) UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(chooser);
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
                state = chooser.showSaveDialog(RunnerClass.mf);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else state = chooser.showSaveDialog(RunnerClass.mf);
        } catch (Exception e) {
            state = chooser.showSaveDialog(RunnerClass.mf);
        }
        if (filters != null && !filters.isEmpty()) chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
        if (state == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(RunnerClass.mf, "An error occured while trying to save that file!", "Error", JOptionPane.ERROR_MESSAGE);
            return save(filterL);
        } else if (state == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.getAbsolutePath().endsWith("." + filterL.getExtensions()[0])) f = new File(f.getAbsolutePath() + "." + filterL.getExtensions()[0]);
            if (f.exists()) {
                int option = JOptionPane.showConfirmDialog(RunnerClass.mf, "This file exists.\nWould you like to overwrite it?", "Overwrite", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                } else if (option == JOptionPane.NO_OPTION) return save(filterL); else return null;
            }
            return f;
        } else return null;
    }

    /**
     * Prompts user for file to open. Ensures file exists.
     * @param filters the filters to use when selecting the file
     * @param isFC if the file-chooser to be shown is a file chooser or a directory chooser
     * @return the file selected by user
     */
    public static File open(Collection<FileFilter> filters, boolean isFC) {
        JFileChooser chooser;
        if (isFC) chooser = GUIUtils.fc; else chooser = dc;
        for (FileFilter filter : chooser.getChoosableFileFilters()) if (!filter.equals(chooser.getAcceptAllFileFilter())) chooser.removeChoosableFileFilter(filter);
        if (filters != null && !filters.isEmpty()) {
            for (FileFilter filter : filters) chooser.addChoosableFileFilter(filter);
            chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
            try {
                chooser.setFileFilter(filters.iterator().next());
            } catch (java.lang.NullPointerException e) {
            }
        }
        int state;
        try {
            if (isFC && UIManager.getLookAndFeel().toString().contains("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                boolean useNimbus = false;
                for (LookAndFeelInfo li : UIManager.getInstalledLookAndFeels()) if (li.getClassName().equals("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel")) {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    useNimbus = true;
                    break;
                }
                if (!useNimbus) UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(chooser);
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
                state = chooser.showOpenDialog(RunnerClass.mf);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else state = chooser.showOpenDialog(RunnerClass.mf);
        } catch (Exception e) {
            state = chooser.showOpenDialog(RunnerClass.mf);
        }
        if (filters != null && !filters.isEmpty()) chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
        if (state == JFileChooser.ERROR_OPTION) {
            JOptionPane.showMessageDialog(RunnerClass.mf, "An error occured while trying to open that file!", "Error", JOptionPane.ERROR_MESSAGE);
            return open(filters, isFC);
        } else if (state == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.exists()) {
                JOptionPane.showMessageDialog(RunnerClass.mf, "That file does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return open(filters, isFC);
            } else if (!accept(f, filters)) {
                JOptionPane.showMessageDialog(RunnerClass.mf, "The file type is currently not supported or is not an accepted file type!", "Error", JOptionPane.ERROR_MESSAGE);
                return open(filters, isFC);
            }
            return f;
        } else return null;
    }

    /**
     * Determines whether this file is accepted by the specified collection of filters
     * @param f The file to determine whether to accept
     * @param filters The filtesr to check whether they accept the file
     * @return boolean representation of whether any of the filters accept the file
     */
    public static boolean accept(File f, Collection<FileFilter> filters) {
        if (filters == null || filters.isEmpty()) return true;
        for (FileFilter filter : filters) if (filter.accept(f)) return true;
        return false;
    }

    /**
     * Prompts user for file to open.
     * @param filters the filters to use when selecting the file
     * @param tc where the path will be displayed
     */
    public static void open(Collection<FileFilter> filters, JTextComponent tc) {
        File f = open(filters, true);
        if (f != null) tc.setText(f.getPath());
    }

    /**
     * Prompts user for file to open.
     * @param filter what files can be opened
     * @return file chosen
     */
    public static File open(FileFilter[] filter) {
        ArrayList<FileFilter> filters = new ArrayList<FileFilter>();
        for (FileFilter f : filter) filters.add(f);
        return open(filters, true);
    }

    /**
     * Prompts user for file to open.
     * @param filters the filters to use when selecting the file
     * @param tc where the path will be displayed
     */
    public static void open(FileFilter filter, JTextComponent tc) {
        ArrayList<FileFilter> filters = new ArrayList<FileFilter>();
        filters.add(filter);
        open(filters, tc);
    }

    /**
     * Prompts user for file to open.
     * @param ext type of files to allow, either <code>images</code>, <code>text</code>, or <code>all</code>
     * @param tc where the path will be displayed
     */
    public static void open(String ext, JTextComponent tc) {
        if (ext.equalsIgnoreCase("images")) open(new javax.swing.filechooser.FileNameExtensionFilter("Images (.bmp, .gif, .jpg, .png)", new String[] { "bmp", "gif", "jpeg", "jpg", "png" }), tc); else if (ext.equalsIgnoreCase("text")) open(new javax.swing.filechooser.FileNameExtensionFilter("Text (.txt, .html)", new String[] { "txt", "html", "htm" }), tc); else if (ext.equalsIgnoreCase("all")) open(new ArrayList<FileFilter>(), tc);
    }

    /**
     * Ask user for directory to open
     * @param tc the field to be filled in
     */
    public static void openDir(JTextComponent tc) {
        File f = open(null, false);
        if (f != null) tc.setText(f.getPath());
    }

    /**
     * Makes a panel with components that are always shown, and some that only shown when suer wants to, and are initially hidden
     * @param shown containing all components that are always shown
     * @param hidden containing all components that only shown when user wants to
     * @param okBtnListener the listener on the OK button
     * @param cancelBtnListener the listener on the Cancel button
     * @return the panel with the mandatory shown, and a button to show optional
     */
    public static JPanel makeCollapserPanel(JComponent shown, JComponent hidden, ActionListener okBtnListener, ActionListener cancelBtnListener) {
        return makeCollapserPanel(shown, hidden, true, okBtnListener, cancelBtnListener);
    }

    /**
     * Makes a panel with components that are always shown, and some that only shown when suer wants to, and are initially hidden
     * @param shown containing all components that are always shown
     * @param hidden containing all components that only shown when user wants to
     * @param addOK to add the buttons or not
     * @param okBtnListener the listener on the OK button
     * @param cancelBtnListener the listener on the Cancel button
     * @return the panel with the mandatory shown, and a button to show optional
     */
    public static JPanel makeCollapserPanel(JComponent shown, JComponent hidden, boolean addOK, ActionListener okBtnListener, ActionListener cancelBtnListener) {
        JXCollapsiblePane cp = new JXCollapsiblePane();
        cp.add(hidden);
        final JPanel entire = new JPanel();
        class BooleanCustom {

            public boolean data;
        }
        final BooleanCustom okToPack = new BooleanCustom();
        okToPack.data = false;
        cp.setCollapsed(true);
        cp.addPropertyChangeListener(JXCollapsiblePane.ANIMATION_STATE_KEY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("propertychange-collapser " + evt.getNewValue());
                RunnerClass.mf.fitContentResize();
                if (evt.getNewValue().equals("expanded") || evt.getNewValue().equals("collapsed")) {
                    okToPack.data = false;
                    RunnerClass.mf.setMinimumSize(RunnerClass.mf.getPreferredSize());
                } else if (Math.abs(entire.getPreferredSize().hashCode() - RunnerClass.mf.getContentPane().getSize().hashCode()) < 5000) okToPack.data = true;
            }
        });
        Action tog = cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
        tog.putValue(JXCollapsiblePane.COLLAPSE_ICON, UIManager.getIcon("Tree.expandedIcon"));
        tog.putValue(JXCollapsiblePane.EXPAND_ICON, UIManager.getIcon("Tree.collapsedIcon"));
        JButton collapseBtn = new JButton(tog);
        collapseBtn.setHorizontalAlignment(SwingConstants.LEFT);
        collapseBtn.setText("Optional advanced features");
        collapseBtn.setFont(collapseBtn.getFont().deriveFont(11f));
        collapseBtn.setBorderPainted(false);
        collapseBtn.setFocusable(false);
        JPanel bottom = new JPanel() {

            @Override
            public void repaint() {
                super.repaint();
                if (okToPack.data) RunnerClass.mf.pack();
            }
        };
        bottom.setLayout(new VerticalLayout());
        bottom.add(new JSeparator(), BorderLayout.NORTH);
        bottom.add(collapseBtn);
        bottom.add(cp);
        if (addOK) {
            JPanel okBar = new JPanel();
            okBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
            okBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JButton okBtn = new JButton("OK");
            okBtn.setIcon(new javax.swing.ImageIcon(RunnerClass.mf.getClass().getResource("/resources/ok.png")));
            okBtn.addActionListener(okBtnListener);
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setIcon(new javax.swing.ImageIcon(RunnerClass.mf.getClass().getResource("/resources/cancel.png")));
            cancelBtn.addActionListener(cancelBtnListener);
            okBar.add(okBtn);
            okBar.add(cancelBtn);
            bottom.add(okBar);
        }
        entire.setLayout(new BorderLayout());
        entire.add(shown, BorderLayout.CENTER);
        entire.add(bottom, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
            }
        });
        return entire;
    }

    /**
     * Gets the DefaultComboBoxModel for the combo box where the user gets to choose the file, or dir in the added packs
     * @param dirOnly determines whether to add only dirs or only files
     * @return the DefaultComboBoxModel for the combo box
     */
    public static DefaultComboBoxModel getAllFilesForComboBoxModel(boolean dirOnly) {
        ArrayList<String> fileS = new ArrayList<String>();
        fileS.add("$INSTALL_PATH/");
        try {
            for (Pack p : RunnerClass.mf.packsPanel.packs.list) for (XFile f : p.file) {
                String targetDir = f.packs_file_targetdir + (!(f.packs_file_targetdir.endsWith("/") || f.packs_file_targetdir.endsWith("\\")) ? "/" : "");
                if (f.sourceFileOption && !dirOnly) fileS.add(targetDir + (f.renameTargetFileOption ? f.packs_file_renameTarget : new File(f.packs_file_sourceFile).getName())); else if (f.sourceDirOption) {
                    if (dirOnly) fileS.add(targetDir + new File(f.packs_file_sourceDir).getName());
                    addFiles(new File(f.packs_file_sourceDir), fileS, targetDir + new File(f.packs_file_sourceDir).getName() + "/", dirOnly);
                }
            }
        } catch (Exception e) {
        }
        return (new DefaultComboBoxModel(fileS.toArray()));
    }

    /**
     * Gets the DefaultComboBoxModel for the combo box
     * @return the DefaultComboBoxModel for the combo box
     */
    public static DefaultComboBoxModel getFilesforComboBoxModel() {
        ArrayList<String> fileS = new ArrayList<String>();
        fileS.add("");
        for (XFile f : packjacket.RunnerClass.mf.packsPanel.gen.files.list) {
            String targetDir = f.packs_file_targetdir + (!(f.packs_file_targetdir.endsWith("/") || f.packs_file_targetdir.endsWith("\\")) ? "/" : "");
            if (f.sourceFileOption) fileS.add(targetDir + (f.renameTargetFileOption ? f.packs_file_renameTarget : new File(f.packs_file_sourceFile).getName())); else if (f.sourceDirOption) addFiles(new File(f.packs_file_sourceDir), fileS, targetDir + new File(f.packs_file_sourceDir).getName() + "/", false);
        }
        return (new DefaultComboBoxModel(fileS.toArray()));
    }

    /**
     * Filters the DefaultComboBox to make sure there are only images in it
     * @return
     */
    public static DefaultComboBoxModel filterModel() {
        DefaultComboBoxModel model = getAllFilesForComboBoxModel(false);
        for (int a = 0; a < model.getSize(); a++) {
            String modelElement = model.getElementAt(a).toString();
            if (!(modelElement.endsWith(".ico") || modelElement.endsWith(".jpg") || modelElement.endsWith(".png") || modelElement.endsWith(".bmp") || modelElement.endsWith(".pct") || modelElement.endsWith(".gif"))) {
                model.removeElement(model.getElementAt(a));
                a--;
            }
        }
        if (model.getSize() == 0) model.addElement("$INSTALL_PATH/");
        return model;
    }

    /**
     * Adds the files/directoires in the dir
     * @param dir the dir to look in for the files/directories
     * @param files the list to add the files to
     * @param targetDir the current target dir
     * @param dirOnly Whether it is dirOnly or not
     */
    public static void addFiles(File dir, ArrayList<String> files, String targetDir, boolean dirOnly) {
        for (File f : dir.listFiles()) if (f.isDirectory()) {
            if (dirOnly) files.add(targetDir + f.getName());
            addFiles(f, files, targetDir + f.getName() + "/", dirOnly);
        } else if (!dirOnly) files.add(targetDir + f.getName());
    }
}
