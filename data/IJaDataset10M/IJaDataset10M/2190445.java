package com.loribel.commons.swing.action;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.loribel.commons.abstraction.GB_LongAction;
import com.loribel.commons.abstraction.GB_SimpleListAdd;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.Info;

/**
 * Demo.
 *
 * @author Gr�gory Borelli.
 */
public class GB_LongActionScanDisk implements GB_LongAction {

    private Component parent;

    private File dir;

    private GB_SimpleListAdd readOnlyList;

    private long countDir = 0;

    private long countFiles = 0;

    private long countReadOnly = 0;

    private long countWrite = 0;

    public GB_LongActionScanDisk(Component a_parent, GB_SimpleListAdd a_readOnlyList) {
        parent = SwingUtilities.getWindowAncestor(a_parent);
        readOnlyList = a_readOnlyList;
    }

    public boolean doGuiBefore(Component a_parent) {
        JFileChooser l_fileChooser = new JFileChooser();
        l_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        while (dir == null) {
            int r = l_fileChooser.showOpenDialog(parent);
            if (r != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(parent, "Operation canceled");
                return false;
            }
            dir = l_fileChooser.getSelectedFile();
        }
        return true;
    }

    public Object doAction() {
        doActionOnDirectory(dir);
        return null;
    }

    protected void doActionOnDirectory(File a_dir) {
        Info.setTitle(a_dir.getAbsolutePath());
        countDir++;
        File[] l_files = a_dir.listFiles();
        int len = CTools.getSize(l_files);
        for (int i = 0; i < len; i++) {
            File l_file = l_files[i];
            if (l_file.isDirectory()) {
                doActionOnDirectory(l_file);
            } else {
                doActionOnFile(l_file);
            }
        }
    }

    protected void doActionOnFile(File a_file) {
        countFiles++;
        Info.setInfo("" + countFiles + " - " + a_file.getName());
        if (a_file.canWrite()) {
            countWrite++;
            readOnlyList.add(a_file);
        } else {
            countReadOnly++;
        }
    }

    public void doGuiAfter(Component a_parent, Object a_value) {
        String l_msg = "Explorated directory: " + countDir + "\n" + "Total Files: " + countFiles + "\n" + "  Read-only: " + countReadOnly + "\n" + "  Writable: " + countWrite + "\n";
        JOptionPane.showMessageDialog(parent, l_msg);
    }
}
