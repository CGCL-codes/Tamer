package march;

import java.util.*;

public class DataManager {

    private static ArrayList tabData = new ArrayList();

    public static int registerTab(Data d) {
        tabData.add(d);
        return tabData.size() - 1;
    }

    public static Data getTabData(int index) {
        return (Data) tabData.get(index);
    }

    public static String getFileNameFromPath(String path) {
        int index = path.lastIndexOf("\\") + 1;
        return path.substring(index, path.length() - 4);
    }
}
