package spin;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import spin.io.NewClsFile;

public class ClassManager {

    private int[] keys;

    private int[] classes;

    private int[] ids;

    private int[] counts;

    private String[] names;

    private Color[] colors;

    private boolean[] sleeps;

    public ClassManager() {
        keys = new int[0];
        classes = new int[0];
        ids = new int[0];
        sleeps = new boolean[0];
        names = new String[0];
        colors = new Color[0];
    }

    public void initBlancoClass(int[] _keys) {
        keys = _keys;
        classes = new int[keys.length];
        sleeps = new boolean[keys.length];
        for (int n = 0; n < classes.length; n++) classes[n] = 0;
        update();
    }

    public void classify(int[] _keys, int classid) {
        for (int n = 0; n < _keys.length; n++) {
            int idx = this.getIndexForKey(_keys[n]);
            if (idx != -1) classes[idx] = classid;
        }
        update();
    }

    public void classifyNew(int[] _keys) {
        classify(_keys, generateNewID());
    }

    public Color getColorForKey(int key) {
        int idx = getIndexForKey(key);
        if (idx == -1) return Color.BLACK;
        int id = classes[idx];
        return getColorForID(id);
    }

    public void loadCLSFile(File f) {
        NewClsFile cls = new NewClsFile(f);
        keys = cls.getKeys();
        classes = cls.getClasses();
        sleeps = new boolean[keys.length];
        update();
        int[] _ids = cls.getIds();
        Color[] _colors = cls.getColors();
        for (int n = 0; n < _ids.length; n++) reColor(_ids[n], _colors[n]);
    }

    public void saveCLSFile(File f) {
        Vector<Integer> _keys = new Vector<Integer>();
        Vector<Integer> _classes = new Vector<Integer>();
        for (int n = 0; n < keys.length; n++) if (!sleeps[n]) {
            _keys.add(keys[n]);
            _classes.add(classes[n]);
        }
        int[] ks = new int[_keys.size()];
        int[] cs = new int[_keys.size()];
        for (int n = 0; n < ks.length; n++) {
            ks[n] = _keys.get(n);
            cs[n] = _classes.get(n);
        }
        NewClsFile.saveClsFile(ks, cs, ids, colors, f);
    }

    public void setSleepingKeys(int[] _keys) {
        for (int n = 0; n < keys.length; n++) sleeps[n] = false;
        for (int n = 0; n < _keys.length; n++) {
            int idx = getIndexForKey(_keys[n]);
            if (idx != -1) sleeps[idx] = true;
        }
        update();
    }

    public void reColor(int classid, Color c) {
        int idx = getIndexForID(classid);
        if (idx != -1) {
            colors[idx] = c;
            classesChanged();
        }
    }

    public void reID(int oldID, int newID) {
        if (getIndexForID(newID) != -1) return;
        int idx = getIndexForID(oldID);
        if (idx == -1) return;
        for (int n = 0; n < classes.length; n++) if (classes[n] == oldID) classes[n] = newID;
        ids[idx] = newID;
        sort();
        classesChanged();
    }

    public void reName(int classid, String name) {
        int idx = getIndexForID(classid);
        if (idx == -1) return;
        names[idx] = name;
        classesChanged();
    }

    private void update() {
        Vector<Integer> newids = new Vector<Integer>();
        for (int n = 0; n < classes.length; n++) if (!newids.contains(classes[n])) newids.add(classes[n]);
        String[] newnames = new String[newids.size()];
        Color[] newcolors = new Color[newids.size()];
        for (int n = 0; n < ids.length; n++) {
            int idx = newids.indexOf(ids[n]);
            if (idx != -1) {
                newnames[idx] = names[n];
                newcolors[idx] = colors[n];
            }
        }
        for (int n = 0; n < newnames.length; n++) {
            if (newnames[n] == null) newnames[n] = "C" + newids.get(n);
            if (newcolors[n] == null) {
                newcolors[n] = getNiceColorForID(newids.get(n));
            }
        }
        ids = new int[newids.size()];
        for (int n = 0; n < ids.length; n++) ids[n] = newids.get(n);
        names = newnames;
        colors = newcolors;
        counts = new int[ids.length];
        for (int n = 0; n < counts.length; n++) counts[n] = 0;
        for (int n = 0; n < classes.length; n++) if (!sleeps[n]) for (int i = 0; i < ids.length; i++) if (classes[n] == ids[i]) counts[i]++;
        sort();
        classesChanged();
    }

    private Color getNiceColorForID(int classid) {
        if (classid == 0) return Color.RED;
        if (classid == 1) return Color.BLUE;
        if (classid == 2) return Color.YELLOW;
        if (classid == 3) return Color.GRAY;
        if (classid == 4) return Color.CYAN;
        return new Color((float) (Math.random() / 2 + 0.5), (float) (Math.random() / 2 + 0.5), (float) (Math.random() / 2 + 0.5));
    }

    private int generateNewID() {
        int max = Integer.MIN_VALUE;
        for (int n = 0; n < ids.length; n++) if (ids[n] > max) max = ids[n];
        return max + 1;
    }

    private int getIndexForKey(int key) {
        for (int n = 0; n < keys.length; n++) if (keys[n] == key) return n;
        return -1;
    }

    private int getIndexForID(int classid) {
        for (int n = 0; n < ids.length; n++) if (ids[n] == classid) return n;
        return -1;
    }

    private Color getColorForID(int classid) {
        int idx = getIndexForID(classid);
        if (idx != -1) return colors[idx];
        return Color.BLACK;
    }

    public int[] getKeysForClassID(int classID) {
        Vector<Integer> ks = new Vector<Integer>();
        for (int n = 0; n < keys.length; n++) if (classes[n] == classID) ks.add(keys[n]);
        int[] result = new int[ks.size()];
        for (int n = 0; n < ks.size(); n++) result[n] = ks.get(n);
        return result;
    }

    public int[] getClasses() {
        return classes;
    }

    public Color[] getColors() {
        return colors;
    }

    public int[] getIds() {
        return ids;
    }

    public int[] getKeys() {
        return keys;
    }

    public String[] getNames() {
        return names;
    }

    public int[] getCounts() {
        return counts;
    }

    private void sort() {
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int n = 0; n < ids.length - 1; n++) if (ids[n] > ids[n + 1]) {
                swap(n, n + 1);
                swapped = true;
            }
        }
    }

    private void swap(int idxa, int idxb) {
        int tmpid = ids[idxa];
        int tmpcount = counts[idxa];
        String tmpname = names[idxa];
        Color tmpcolor = colors[idxa];
        ids[idxa] = ids[idxb];
        counts[idxa] = counts[idxb];
        names[idxa] = names[idxb];
        colors[idxa] = colors[idxb];
        ids[idxb] = tmpid;
        counts[idxb] = tmpcount;
        names[idxb] = tmpname;
        colors[idxb] = tmpcolor;
    }

    /** To be overwritten*/
    public void classesChanged() {
    }
}
