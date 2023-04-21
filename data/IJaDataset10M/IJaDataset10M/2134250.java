package in.co.codedoc.framework;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class UploadedFiles {

    private static final HashMap<String, File> EMPTY = new HashMap<String, File>();

    public static void AddFile(String name, File file) {
        HashMap<String, File> temp = requestFiles.get();
        if (temp == null) {
            requestFiles.set(temp = new HashMap<String, File>());
        }
        temp.put(name, file);
    }

    public static HashMap<String, File> GetFiles() {
        HashMap<String, File> temp = requestFiles.get();
        return temp != null ? temp : EMPTY;
    }

    public static void ClearUploadedFiles() {
        if (requestFiles.get() != null) {
            HashMap<String, File> temp = requestFiles.get();
            Iterator<String> names = temp.keySet().iterator();
            while (names.hasNext()) {
                temp.get(names.next()).delete();
            }
        }
    }

    private static ThreadLocal<HashMap<String, File>> requestFiles = new ThreadLocal<HashMap<String, File>>();
}
