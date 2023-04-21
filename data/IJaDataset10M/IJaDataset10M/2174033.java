package hackerz.virtualDataStructure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import util.StringUtil;

/**
 * The abstract data type for all files on server harddrives, including the
 * local terminal. This class contains information that subclasses share.
 * @author Steffen Gates
 */
public abstract class File implements Comparable {

    public enum FileType {

        EXECUTABLE, DATA, DIRECTORY
    }

    ;

    public FileType type;

    public String name;

    public int size;

    public int compressionLevel = 0;

    public int encryptionLevel = 0;

    public int savings;

    public String permission;

    public String date;

    public boolean compressed = false;

    public boolean encrypted = false;

    /**
     * Default permissions are universal (777). This can be changed as needed
     * after creation with the chmod method in Terminal or directly.
     * @param name
     * @param size
     */
    public File(String name, int size) {
        this.name = name;
        this.size = size;
        date = getDateTime();
        permission = "-rw-r--r--";
    }

    /**
     * Comparisons are done by file name alone, so avoid duplicates.
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (((File) o).name.equalsIgnoreCase(name)) {
            return 0;
        }
        return -1;
    }

    /**
     * Set the permissions of this file.
     * NOTE: This will need to be modified to verify valid values and translate
     * numeric types into textual types as needed.
     * @param perm
     * @return
     */
    public boolean setPermission(String perm) {
        permission = perm;
        return true;
    }

    /**
     * Used by other structures for the ls command.
     * @return
     */
    public String toString() {
        String tmp = StringUtil.padRight(permission, 12) + StringUtil.padRight(size + "", 6) + StringUtil.padRight(date, 22);
        switch(type) {
            case EXECUTABLE:
                return tmp + StringUtil.padRight(name, 25);
            case DIRECTORY:
                return tmp + StringUtil.padRight(name + "/", 25);
            case DATA:
                return tmp + StringUtil.padRight(name, 25);
        }
        return null;
    }

    /**
     * Set the default date of this file to the real world creation time. The time
     * can be overwritten as needed.
     * @return
     */
    protected String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Compress the file based on compression level of the software.
     * @param level
     */
    public void compress(int level) {
        if (compressed) {
            return;
        }
        if (level == 0) {
            return;
        }
        compressed = true;
        savings = size;
        size = (int) (size * (1 - level / 10.0f));
        compressionLevel = level;
        savings -= size;
    }

    /**
     * Decompress the file
     */
    public boolean decompress(int level) {
        if (compressed && level <= compressionLevel) {
            size += savings;
            compressed = false;
            compressionLevel = 0;
            return true;
        }
        return false;
    }

    /**
     * Encrypt the file based on level of encryption software
     */
    public void encrypt(int level) {
        if (level == 0) {
            return;
        }
        encrypted = true;
        encryptionLevel = level;
    }

    /**
     * Decrypt the file based on level of encryption software
     */
    public void decrypt(int level) {
        if (level <= encryptionLevel) {
            encrypted = false;
            encryptionLevel = 0;
        }
    }
}
