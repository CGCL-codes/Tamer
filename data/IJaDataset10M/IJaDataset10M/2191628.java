package util.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import udf.string.Permutations;
import util.io.FileInput;

public class ParseDeliciousEntries {

    /**
	 * @param args
	 */
    static int _date = 0;

    static int _user = 1;

    static int _url = 2;

    static int _tag = 3;

    private static Hashtable<String, LinkedList<String>> ordered = new Hashtable<String, LinkedList<String>>();

    public static void init(String path) {
        FileInput in = new FileInput(path);
        String line = in.readString();
        while (line != null) {
            String t[] = line.split("\t");
            String hyphen = (String) t[0];
            List<String> order = Arrays.asList(hyphen.split("_"));
            Collections.sort(order);
            StringBuffer sorted_ = new StringBuffer();
            for (int i = 0; i < order.size(); i++) {
                sorted_.append(order.get(i) + "_");
            }
            String sorted = sorted_.toString();
            sorted_ = null;
            if (sorted.endsWith("_")) {
                sorted = sorted.substring(0, sorted.length() - 1);
            }
            LinkedList<String> originals = new LinkedList<String>();
            if (ordered.containsKey(sorted)) {
                originals = ordered.get(sorted);
                if (!originals.contains(hyphen)) {
                    originals.add(hyphen);
                }
                ordered.put(sorted, originals);
            } else {
                originals.add(hyphen);
                ordered.put(sorted, originals);
            }
            line = in.readString();
        }
    }

    public static void bookmarkProcess(LinkedList<String> tags) {
        HashSet<String> permuta = Permutations.permutations(tags);
        HashSet<String> hyphens_founds = new HashSet<String>();
        Iterator<String> iter = permuta.iterator();
        while (iter.hasNext()) {
            String perm = iter.next();
            if (ordered.containsKey(perm)) {
                hyphens_founds.addAll(ordered.get(perm));
            }
        }
        Iterator<String> iter2 = hyphens_founds.iterator();
        while (iter2.hasNext()) {
            String temp = iter2.next();
            System.out.println(temp + "\t" + 1);
        }
        hyphens_founds.clear();
        permuta.clear();
    }

    public static void readBookmarks(String file) {
        FileInput in = new FileInput(file);
        String line = in.readString();
        String current_bookmark = "";
        LinkedList<String> tags = new LinkedList<String>();
        while (line != null) {
            String t[] = line.split("\t");
            if (_tag >= t.length) {
                line = in.readString();
                continue;
            }
            String temporal_bookmark = t[_user] + '\t' + t[_url];
            if (temporal_bookmark.equals(current_bookmark) && !temporal_bookmark.equals("")) {
                tags.add(t[_tag]);
            } else {
                bookmarkProcess(tags);
                tags = new LinkedList<String>();
                tags.add(t[_tag]);
            }
            current_bookmark = temporal_bookmark;
            line = in.readString();
        }
    }

    public static void main(String[] args) {
        String delicious = "/home/sergio/data/delicious/delicious_clean.txt";
        String hyphens = "/home/sergio/data/delicious_hyphen/hyphens_clean.txt";
        init(hyphens);
        readBookmarks(delicious);
    }
}
