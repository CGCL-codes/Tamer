package net.sourceforge.utilities.dataToObject.dataLoader;

public class QueryProcessor {

    public static String processQueryRead(String q, boolean process) {
        return null;
    }

    public static String processQueryWrite(String q, boolean process) {
        return null;
    }

    public static String processQuerySearch(String q, boolean process) {
        return null;
    }

    public static String getNextQueryGroup(String q, String[] group) {
        if (!q.equals("")) {
            group[0] = q.substring(0, q.indexOf(","));
            q = q.substring(q.indexOf(",") + 1);
            group[1] = q.substring(0, q.indexOf(","));
            q = q.substring(q.indexOf(",") + 1);
            group[2] = q.substring(1, (q.indexOf(";") - 1));
            q = q.substring(q.indexOf(";") + 1);
        } else {
            q = "false";
        }
        return q;
    }
}
