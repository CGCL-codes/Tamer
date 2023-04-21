package de.blitzcoder.collide.gui.fileexplorer;

import de.blitzcoder.collide.icons.Icon;
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author blitzcoder
 */
public class FileTableModel extends AbstractTableModel implements Serializable {

    private File dir;

    private File[] files;

    private static ImageIcon bmaxIcon = Icon.load("file_bmax.png");

    private static ImageIcon standardIcon = Icon.load("file_standard.png");

    private static ImageIcon imageIcon = Icon.load("file_image.png");

    private static ImageIcon textIcon = Icon.load("file_text.png");

    private static ImageIcon packageIcon = Icon.load("file_package.png");

    private static ImageIcon fontIcon = Icon.load("file_font.png");

    private static ImageIcon appIcon = Icon.load("file_app.png");

    private static HashMap<String, ImageIcon> icons;

    static {
        icons = new HashMap<String, ImageIcon>();
        icons.put("bmx", bmaxIcon);
        icons.put("sh", appIcon);
        icons.put("bat", appIcon);
        icons.put("cmd", appIcon);
        icons.put("", appIcon);
        icons.put("exe", appIcon);
        icons.put("debug", appIcon);
        icons.put("ttf", fontIcon);
        icons.put("zip", packageIcon);
        icons.put("rar", packageIcon);
        icons.put("tar", packageIcon);
        icons.put("bz", packageIcon);
        icons.put("bz2", packageIcon);
        icons.put("gz", packageIcon);
        icons.put("cab", packageIcon);
        icons.put("png", imageIcon);
        icons.put("jpg", imageIcon);
        icons.put("jpeg", imageIcon);
        icons.put("tif", imageIcon);
        icons.put("tiff", imageIcon);
        icons.put("bmp", imageIcon);
        icons.put("ico", imageIcon);
        icons.put("txt", textIcon);
        icons.put("conf", textIcon);
        icons.put("ini", textIcon);
        icons.put("properties", textIcon);
        icons.put("props", textIcon);
        icons.put("dat", textIcon);
        icons.put("bak", textIcon);
        icons.put("manifest", textIcon);
        icons.put("c", textIcon);
        icons.put("cpp", textIcon);
        icons.put("h", textIcon);
        icons.put("hpp", textIcon);
    }

    public FileTableModel() {
    }

    public void setDir(File dir) {
        this.dir = dir;
        files = null;
        fireTableDataChanged();
    }

    public File[] getFiles() {
        if (this.dir == null) return new File[] {};
        if (files != null) {
            return files;
        }
        files = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return !name.equals(".") && !name.equals("..") && !name.endsWith("~") && !new File(dir, name).isDirectory() && !new File(dir, name).isHidden();
            }
        });
        Comparator<File> c = new Comparator<File>() {

            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        if (files != null) Arrays.sort(files, c); else return null;
        return getFiles();
    }

    public int getRowCount() {
        return getFiles() != null ? getFiles().length : 0;
    }

    public int getColumnCount() {
        return 2;
    }

    public Class getColumnClass(int index) {
        switch(index) {
            case 0:
                return JLabel.class;
            case 1:
                return String.class;
        }
        return String.class;
    }

    public String getColumnName(int index) {
        switch(index) {
            case 0:
                return "";
            case 1:
                return "Name";
            default:
                return "Error!";
        }
    }

    public File getCurrentDir() {
        return dir;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        getFiles();
        switch(columnIndex) {
            case 0:
                return getFileIconLabel(files[rowIndex]);
            case 1:
                return files[rowIndex].getName();
        }
        return "Error";
    }

    private JLabel getFileIconLabel(File file) {
        JLabel label = new JLabel();
        String extension = "";
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            extension = extension.toLowerCase();
        }
        ImageIcon icon = icons.get(extension);
        if (icon == null) icon = standardIcon;
        label.setIcon(icon);
        return label;
    }
}
