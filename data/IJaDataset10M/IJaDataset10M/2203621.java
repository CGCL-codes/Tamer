package collabed.util;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.JFileChooser;
import java.io.*;

/**
 * A collection of miscellaneous file I/O procedures that are used throughout 
 * the grewpedit package repeatedly, and/or do not share any unique
 * connection to an individual class in the package.
 *
 *@author Kenroy Granville / Tim Hickey
 *@version "%I%, %G%"
 */
public class FileIO {

    public static File open(JFileChooser fc, File file, Component parent) {
        try {
            fc.setSelectedFile(file);
            if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) return fc.getSelectedFile();
        } catch (Exception e) {
            Util.error("Open Failed!\n" + file, e, parent);
        }
        return null;
    }

    public static File[] openAll(JFileChooser fc, File file, Component parent) {
        try {
            fc.setSelectedFile(file);
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                fc.setMultiSelectionEnabled(false);
                return files;
            }
        } catch (Exception e) {
            Util.error("OpenAll Failed!\n" + file, e, parent);
        }
        return null;
    }

    public static File open(JFileChooser fc, File file, JTextComponent tc) {
        try {
            if ((file = open(fc, file, (Component) tc)) != null && load(file, tc)) return file;
        } catch (Exception e) {
            Util.error("Open Failed!" + file, e, tc);
        }
        return null;
    }

    public static boolean load(File file, JTextComponent tc) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) buff.append(line + "\n");
            tc.setText(buff.toString());
            reader.close();
            return true;
        } catch (Exception e) {
            Util.error("Load Failed!\n" + file, e, tc);
            return false;
        }
    }

    public static boolean save(File file, String str) {
        try {
            if (file != null) {
                BufferedReader reader = new BufferedReader(new StringReader(str));
                PrintWriter writer = new PrintWriter(new FileWriter(file));
                String line;
                while ((line = reader.readLine()) != null) writer.println(line);
                reader.close();
                writer.close();
                return true;
            }
        } catch (Exception e) {
            Util.error("SaveAs Failed!\n" + file, e, null);
        }
        return false;
    }

    /**
      Opens a file chooser dialog for saving files with <i>file</i> selected
      by default. The dialog allows the user to change the file to which
      the text in the <i>ta</i> will be saved or simply cancel the request.
    **/
    public static boolean save(File file, JTextComponent tc) {
        return save(file, tc.getText());
    }

    /**
      Opens a file chooser dialog for saving files with <i>file</i>
      selected by default. It returns the file selected by the user
      or null if save is canceled.
   **/
    public static File saveAs(JFileChooser fc, File file, Component parent) {
        try {
            fc.setSelectedFile(file);
            if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) if ((file = fc.getSelectedFile()).exists()) {
                String msg = "An item named \"" + file + "\" already " + "exists.\nDo you want to replace it?";
                Object[] options = { "Replace", "Cancel" };
                int choice = JOptionPane.showOptionDialog(parent, msg, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (choice == 0) return file; else return saveAs(fc, file, parent);
            } else return file;
        } catch (Exception e) {
            Util.error("SaveAs Failed!\n" + file, e, parent);
        }
        return null;
    }

    /**
      Opens a file chooser dialog for saving files with <i>file</i> selected
      by default. The dialog allows the user to change the file to which
      the text in the <i>tc</i> will be saved or simply cancel the request.
    **/
    public static File saveAs(JFileChooser fc, File file, JTextComponent tc) {
        try {
            if ((file = saveAs(fc, file, (Component) tc)) != null) if (save(file, tc)) return file;
        } catch (Exception e) {
            Util.error("saveAs Failed!\n" + file, e, tc);
        }
        return null;
    }

    /** Closes file. **/
    public static boolean close(File file, JTextComponent tc, boolean saved) {
        try {
            if (file == null || !saved) {
                String msg = "The text in \"" + file + "\" file has changed.\n" + "Do you want to save the changes?";
                Object[] options = { "Yes", "No", "Cancel" };
                int choice = JOptionPane.showOptionDialog(tc, msg, "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (choice == 0) save(file, tc); else if (choice == 2) return false;
            }
            return true;
        } catch (Exception e) {
            Util.error("Close Failed!\n" + file, e, tc);
        }
        return false;
    }

    /**
    * Filtes out files that end in any of the Strings in <b>types</b>.
    * Unless acceptDirs is true then folders are also filtered out.
    ***/
    public static class TypeFilter implements FilenameFilter {

        String[] types;

        boolean acceptDirs;

        public TypeFilter(String[] types, boolean acceptDirs) {
            this.types = types;
            this.acceptDirs = acceptDirs;
        }

        public boolean accept(File dir, String name) {
            for (int i = 0; i < types.length; i++) if (name.toLowerCase().endsWith(types[i]) || (acceptDirs && new File(dir, name).isDirectory())) return true;
            return false;
        }
    }
}
