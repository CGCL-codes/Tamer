package jsync.helpers;

import jConfigLib.files.IniFile;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;
import jsync.types.ConnectionEntry;

public class ConnectionsHelper {

    private static List<String> connectionFiles = null;

    public static void initConnectionFiles() {
        connectionFiles = listConnectionFiles();
    }

    public static List<String> getConnectionFiles() {
        return connectionFiles;
    }

    private static List<String> listConnectionFiles() {
        String path = ConfigHelper.connectionsFolder();
        File connectionsFolder = new File(path);
        List<String> files = new ArrayList<String>();
        if (connectionsFolder.exists() && connectionsFolder.isDirectory()) {
            File[] filesInFolder = connectionsFolder.listFiles();
            if (filesInFolder != null) {
                for (File file : filesInFolder) {
                    if (file.isFile() && file.canRead() && file.getName().endsWith(".conf")) {
                        String filePath = file.getAbsolutePath();
                        files.add(filePath);
                    }
                }
            }
        }
        Collections.sort(files);
        return files;
    }

    public static List<ConnectionEntry> getConnectionEntries() {
        List<ConnectionEntry> entries = null;
        List<String> connectionFiles = ConnectionsHelper.getConnectionFiles();
        for (String connectionFile : connectionFiles) {
            if (entries == null) entries = new ArrayList<ConnectionEntry>();
            IniFile file = new IniFile(connectionFile);
            HashMap<String, String> excludes = file.getSection("excludes");
            List<String> excludeList = null;
            if (excludes != null) {
                for (String exclude : excludes.values()) {
                    if (excludeList == null) excludeList = new ArrayList<String>();
                    if (excludeList.contains(exclude) == false) excludeList.add(exclude);
                }
            }
            HashMap<String, String> connections = file.getSection("connections");
            Set<Entry<String, String>> connectionSet = connections.entrySet();
            Iterator<Map.Entry<String, String>> connectionIterator = connectionSet.iterator();
            while (connectionIterator.hasNext()) {
                Map.Entry<String, String> sectionEntry = (Map.Entry<String, String>) connectionIterator.next();
                String source = (String) sectionEntry.getKey();
                String target = (String) sectionEntry.getValue();
                String srcValue = file.getValue("sources", source);
                if (srcValue == null) srcValue = source;
                List<String> targetList = ConfigHelper.getListFromLine(target);
                for (String t : targetList) {
                    String targetValue = file.getValue("targets", t);
                    if (targetValue == null) targetValue = target;
                    entries.add(new ConnectionEntry(srcValue, targetValue, excludeList));
                }
            }
        }
        return entries;
    }
}
