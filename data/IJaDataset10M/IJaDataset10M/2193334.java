package indiji.struct;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.Map.Entry;

/**
 * A datstructure to store a command and several key-value-pairs.
 * May include recursive definitions. Can parse strings of the
 * form: <br><i>command?key1=val1&key2=val2&key3={subparams}&...</i><br>
 * An example for a machine learning model description:<br>
 * <i>xval?k=3&model={svm?type=linear&c=[1,10,100]}</i><br>
 * 
 * @author Pascal Lehwark
 *
 */
public class Params {

    private String cmd;

    private HashMap<String, String> params;

    /**
	 * Default Constructor.
	 */
    public Params() {
        clear();
    }

    /**
	 * Creates a new instance by parsing the given String.
	 * @param p
	 */
    public Params(String p) {
        this();
        if (p != null) try {
            int depth = 0;
            String act = "";
            String k = "";
            String v = "";
            boolean isincmd = true;
            for (int n = 0; n < p.length(); n++) {
                char c = p.charAt(n);
                if (c == '?' && depth == 0) {
                    isincmd = false;
                    cmd = act;
                    act = "";
                } else if (c == '=' && depth == 0) {
                    k = act;
                    act = "";
                } else if (c == '&' && depth == 0) {
                    v = act;
                    act = "";
                    params.put(k, v);
                } else if (c == '{') {
                    act += "{";
                    depth++;
                } else if (c == '}') {
                    act += "}";
                    depth--;
                } else act += c;
            }
            if (isincmd) cmd = act; else if (act.length() > 0) params.put(k, act);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Add all key-value-pairs from given Params to this instance.
	 * @param p The Params to adapt from.
	 * @param overwrite If true, overwrites values for existing keys. 
	 */
    public void adapt(Params p, boolean overwrite) {
        if (p != null && p.getCommand() != null && overwrite) cmd = new String(p.getCommand());
        for (Entry<String, String> e : p.getParams().entrySet()) if (!params.containsKey(e.getKey()) || overwrite) params.put(new String(e.getKey()), new String(e.getValue()));
    }

    /**
	 * Set the command.
	 * @param cmd
	 */
    public void setCommand(String cmd) {
        this.cmd = cmd;
    }

    /**
	 * Get the key-value-pairs.
	 * @return
	 */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
	 * Get a new instance that is a copy of this.
	 * @return
	 */
    public Params getCopy() {
        return new Params(this.toString());
    }

    /**
	 * Get all keys whose values are like '[...]'
	 * @return
	 */
    public HashSet<String> getSplitables() {
        HashSet<String> result = new HashSet<String>();
        for (Entry<String, String> e : params.entrySet()) if (e.getValue().startsWith("[")) result.add(new String(e.getKey()));
        return result;
    }

    /**
	 * Returns true if at least one key is splittable (is like '[...]').
	 * @return
	 */
    public boolean isSplittable() {
        for (Entry<String, String> e : params.entrySet()) if (e.getValue().startsWith("[")) return true;
        return false;
    }

    /**
	 * Returns true, if the given key is splittable (like '[...]').
	 * @param key
	 * @return
	 */
    public boolean isSplitable(String key) {
        String v = params.get(key);
        return (v != null && v.startsWith("["));
    }

    /**
	 * Get all permutations of splittable key-values.
	 * Example new Params("c1?v1=[1,2]&v2=[5,6]").splittByAll() will
	 * create a list of Params (c1?v1=1&v2=5),(c1?v1=1&v2=6),(c1?v1=2&v2=5),(c1?v1=2&v2=6).
	 * @return
	 */
    public Vector<Params> splitByAll() {
        Vector<Params> result = new Vector<Params>();
        try {
            Vector<Vector<Tuple<String, String>>> all = new Vector<Vector<Tuple<String, String>>>();
            for (Entry<String, String> e : params.entrySet()) {
                String k = e.getKey();
                String v = e.getValue();
                Vector<Tuple<String, String>> tmp = new Vector<Tuple<String, String>>();
                all.add(tmp);
                if (v == null || !v.startsWith("[")) tmp.add(new Tuple<String, String>(k, v)); else {
                    String[] t = v.substring(1, v.length() - 1).split(",");
                    for (int n = 0; n < t.length; n++) tmp.add(new Tuple<String, String>(k, t[n]));
                }
            }
            int[] maxcounts = new int[all.size()];
            int[] counts = new int[all.size()];
            for (int n = 0; n < maxcounts.length; n++) {
                maxcounts[n] = all.get(n).size() - 1;
                counts[n] = 0;
            }
            while (counts != null) {
                Params p = new Params();
                if (cmd != null) p.setCommand(new String(cmd));
                for (int n = 0; n < all.size(); n++) {
                    Tuple<String, String> tup = all.get(n).get(counts[n]);
                    p.set(tup.getKey(), tup.getValue());
                }
                result.add(p);
                counts = inc(counts, maxcounts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int[] inc(int[] counts, int[] maxcounts) {
        int[] result = new int[counts.length];
        for (int n = 0; n < counts.length; n++) result[n] = counts[n];
        boolean ok = false;
        for (int n = counts.length - 1; n >= 0; n--) {
            if (counts[n] < maxcounts[n]) {
                result[n] = counts[n] + 1;
                for (int i = n + 1; i < counts.length; i++) result[i] = 0;
                ok = true;
                break;
            }
        }
        return ok ? result : null;
    }

    /**
	 * Clear this Params (remove all key-value-pairs, set command to null).
	 */
    public void clear() {
        cmd = null;
        params = new HashMap<String, String>();
    }

    /**
	 * Get the command.
	 * @return
	 */
    public String getCommand() {
        return cmd;
    }

    /**
	 * Add (overwrite) a key-value-pair.
	 * @param key
	 * @param val
	 */
    public void set(String key, Object val) {
        params.put(key, val != null ? val.toString() : null);
    }

    /**
	 * Get a new Params-Object by parsing value for given key.
	 * Example: cm?v1=3&v2={csub?vsub=3} - here, v2 contains 
	 * sub-params (determined by "{...}"). 
	 * @param key
	 * @return
	 */
    public Params getSubParams(String key) {
        String tmp = this.getString(key);
        if (tmp != null) {
            if (tmp.startsWith("{") && tmp.endsWith("}")) tmp = tmp.substring(1, tmp.length() - 1);
            return new Params(tmp);
        }
        return null;
    }

    /**
	 * Get the value for given key as string.
	 * @param key
	 * @return
	 */
    public String getString(String key) {
        return params.get(key);
    }

    public String getString(String key, String defaultvalue) {
        String result = params.get(key);
        return result != null ? result : defaultvalue;
    }

    public boolean getBool(String key, boolean defaultvalue) {
        String result = params.get(key);
        return result != null ? result.equals("true") : defaultvalue;
    }

    /**
	 * Get the value for given key as Integer.
	 * @param key
	 * @return
	 */
    public Integer getInt(String key) {
        try {
            String val = params.get(key);
            if (val != null) return Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Get the value for given key as Integer.
	 * @param key
	 * @return
	 */
    public Integer getInt(String key, Integer defaultvalue) {
        Integer i = getInt(key);
        return i == null ? defaultvalue : i;
    }

    public final Float getFloat(final String key, final Float defaultvalue) {
        final String s = params.get(key);
        if (s == null) return defaultvalue; else try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Get the value for given key as Float.
	 * @param key
	 * @return
	 */
    public Float getFloat(String key) {
        try {
            return Float.parseFloat(params.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Get the string representation for this Params.
	 * Example: 'com?v1=4&v3=hello&...
	 * @param p
	 * @return
	 */
    public static String encode(Params p) {
        StringBuilder sb = new StringBuilder(100);
        try {
            if (p.getCommand() != null) sb.append(p.getCommand().trim());
            if (p.getParams().size() > 0) {
                if (p.getCommand() != null) sb.append("?");
                Vector<Tuple<String, String>> sp = new Vector<Tuple<String, String>>();
                for (Entry<String, String> e : p.getParams().entrySet()) if (e.getKey() != null && e.getValue() != null) sp.add(new Tuple<String, String>(e.getKey().trim(), e.getValue().trim()));
                Collections.sort(sp, new Comparator<Tuple<String, String>>() {

                    @Override
                    public int compare(Tuple<String, String> o1, Tuple<String, String> o2) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                });
                for (int n = 0; n < sp.size(); n++) {
                    sb.append(sp.get(n).getKey() + "=" + sp.get(n).getValue());
                    if (n < sp.size() - 1) sb.append("&");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString().trim();
    }

    public String toString() {
        return Params.encode(this);
    }

    /**
	 * Removes all (comma-separated) keys (and their values).
	 * @param cskeys
	 */
    public void removeAll(String cskeys) {
        if (cskeys != null) for (String s : cskeys.split(",")) params.remove(s);
    }

    /**
	 * Returns true, if command is null or command is empty string or no key-value pairs. 
	 * @return
	 */
    public boolean isEmpty() {
        return (cmd == null || cmd.trim().length() == 0) && (params == null || params.size() == 0);
    }

    public void remove(String key) {
        params.remove(key);
    }

    public String toExtString() {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("CMD='" + cmd + "'\n");
        for (Entry<String, String> e : params.entrySet()) sb.append("\t'" + e.getKey() + "'='" + e.getValue() + "'\n");
        return sb.toString();
    }

    public String renderTEX() {
        return toString().replaceAll("[{]", "\\\\\\\\{").replaceAll("[}]", "\\\\\\\\}").replaceAll("&", " \\\\\\\\& ");
    }
}
