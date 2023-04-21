package logic;

/**
 * This class is responsible for displaying numbers of bytes
 * @author mateusz
 * 
 */
public class BytesDisplayer {

    public static String bytesToString(long bytes) {
        if (bytes < 1024) {
            return String.valueOf(bytes) + "B";
        } else if (bytes < 1024 * 1024) {
            return String.valueOf(bytes / 1024) + "KB";
        } else if (bytes < 1024 * 1024 * 10) {
            return String.valueOf(bytes / (1024 * 1024)) + "." + String.valueOf((bytes % (1024 * 1024)) / ((1024 * 1024) / 10)) + "MB";
        } else {
            return String.valueOf(bytes / (1024 * 1024)) + "MB";
        }
    }
}
