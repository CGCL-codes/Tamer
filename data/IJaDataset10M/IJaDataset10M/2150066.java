package hackerz.hardware;

import hackerz.virtualDataStructure.Directory;
import hackerz.virtualDataStructure.File;
import hackerz.virtualDataStructure.File.FileType;
import util.StringUtil;

/**
 * The harddrive class both holds file objects and contains methods for accessing
 * and printing those files
 * @author Steffen Gates
 */
public class HardDrive {

    private int capacity;

    private int data;

    private int apps;

    private Directory current;

    public Directory root;

    /**
     * Instantiate with the initial capacity.
     * @param size
     */
    public HardDrive(int size) {
        this.capacity = size;
        data = 0;
        apps = 0;
        current = new Directory("root", null);
        root = current;
    }

    /**
     * Return free space
     * @return
     */
    public int getFree() {
        return capacity - (data + apps);
    }

    /**
     * Return used space
     * @return
     */
    public int getUsed() {
        return (data + apps);
    }

    /**
     * Return used space as a percentage of total space.
     * @return
     */
    public int getUsedRatio() {
        float tmp = (data + apps) / (float) capacity;
        tmp *= 10.0;
        tmp = (float) Math.ceil(tmp);
        return (int) tmp;
    }

    /**
     * Save a supplied file to the harddrive
     * @param file
     */
    public void save(File file) {
        for (File f : current.contents) {
            if (f.compareTo(file) == 0) {
            }
            if ((data + apps) + file.size > capacity) {
            }
        }
        current.contents.add(file);
        if (file.type == FileType.DATA) {
            current.data += file.size;
            data += file.size;
        } else {
            current.apps += file.size;
            apps += file.size;
        }
    }

    /**
     * Save a supplied director to the harddrive
     * @param Directory
     */
    public void saveDir(Directory dir) {
        for (Directory d : current.subfolders) {
            if (d.compareTo(dir) == 0) {
            }
        }
        current.subfolders.add(dir);
    }

    /**
     * Remove a supplied directory from the harddrive
     * @param Directory
     */
    public void rmdir(Directory dir, boolean flag) {
        for (File f : dir.contents) {
            rm(f, dir);
        }
        for (Directory d : dir.subfolders) {
            rmdir(d, false);
        }
        if (flag) {
            current.subfolders.remove(dir);
        }
    }

    /**
     * Remove a file from the harddrive
     * @param File
     */
    public void rm(File file, Directory dir) {
        if (dir == current) {
            dir.contents.remove(file);
        }
        if (file.type == FileType.DATA) {
            data -= file.size;
            dir.data -= file.size;
        } else {
            apps -= file.size;
            dir.apps -= file.size;
        }
    }

    /**
     * Return a file stored on the drive.
     * @param name
     * @return
     */
    public File load(String name) {
        for (File f : current.contents) {
            if (f.name.equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Return a file stored on the drive.
     * @param name
     * @return
     */
    public Directory loadDir(String name) {
        for (Directory d : current.subfolders) {
            if (d.name.equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Set current dir to given directory
     * @param Directory
     */
    public void setDir(Directory d) {
        if (d != null) current = d;
    }

    /**
     * Get current dir
     * @return
     */
    public Directory getCurrentDir() {
        return current;
    }

    /**
     * Return a list of all files as an array of strings
     * @param all
     * @return
     */
    public String[] getFileList(boolean all) {
        String[] list = new String[2 + current.subfolders.size() + current.contents.size()];
        list[0] = StringUtil.padRight("Permission", 12) + StringUtil.padRight("Size", 6) + StringUtil.padRight("Date", 22) + StringUtil.padRight("File/Folder", 25);
        list[1] = "----------------------------------------------------------------------";
        String[] dirlist = getDirList(true);
        for (int i = 2, j = 0; j < dirlist.length; i++, j++) {
            list[i] = dirlist[j];
        }
        for (int i = 2 + current.subfolders.size(); i < list.length; i++) {
            list[i] = current.contents.get(i - 2 - current.subfolders.size()).toString();
        }
        return list;
    }

    /**
     * Return a list of all directories as an array of strings
     * @param all
     * @return
     */
    public String[] getDirList(boolean all) {
        String[] list = new String[current.subfolders.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = current.subfolders.get(i).toString();
        }
        return list;
    }

    /**
     * Return drive usage statistics as an array of strings
     * @return
     */
    public String[] capacityFigures() {
        String[] tmp = new String[4];
        tmp[0] = "Used: " + StringUtil.padLeft((apps + data) + "/" + capacity, 10);
        tmp[1] = "Free: " + StringUtil.padLeft((capacity - (apps + data)) + "", 10);
        tmp[2] = "Exec: " + StringUtil.padLeft(apps + "", 10);
        tmp[3] = "Data: " + StringUtil.padLeft(data + "", 10);
        return tmp;
    }

    /**
     * Return usage information as a 2d bar graph
     * @return
     */
    public String[] capacityGraph() {
        String[] result = new String[15];
        int appsg = (int) Math.ceil((apps / (float) capacity) * 10);
        int datag = (int) Math.ceil((data / (float) capacity) * 10);
        int free = 10 - (appsg + datag);
        String graph = "";
        for (int i = 10; i > 0; i--) {
            graph = StringUtil.padLeft((i * (capacity / 10)) + "", 3) + "| ";
            if (i <= appsg) {
                graph += "*  ";
            } else {
                graph += "   ";
            }
            if (i <= datag) {
                graph += "*  ";
            } else {
                graph += "   ";
            }
            if (i <= free) {
                graph += "* ";
            } else {
                graph += "  ";
            }
            graph += " |";
            result[10 - i] = graph;
        }
        result[10] = "   ------------\n";
        result[11] = "     E  D  F\n";
        result[12] = "     X  A  R\n";
        result[13] = "     E  T  E\n";
        result[14] = "     C  A  E";
        return result;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int size) {
        if (size < capacity) {
            return;
        }
        capacity = size;
    }
}
