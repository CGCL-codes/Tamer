package dscript;

import java.util.HashMap;
import java.util.Vector;

public class ActionHashIterator {

    private static HashMap hm = new HashMap();

    private ThingTypeContainer ttc;

    private String[] arg_alts;

    private int[] args;

    private int ret;

    private String name;

    private String[] hash_array;

    private int last_success = 0;

    public static boolean USE_BEST_MATCH = true;

    private ActionHashIterator(String nm, int[] ags, String[] a_a, int rt, ThingTypeContainer tt) {
        ttc = tt;
        name = nm;
        args = ags;
        arg_alts = a_a;
        ret = rt;
        hash_array = getStringArray(nm, ags, a_a, rt, tt);
    }

    public int getLength() {
        return hash_array.length;
    }

    public int bestSearch() {
        return USE_BEST_MATCH ? last_success : (-1);
    }

    public void foundAt(int i) {
        if ((i > hash_array.length - 1) || (i < 0)) {
            last_success = 0;
        } else {
            last_success = i;
        }
    }

    public void dump() {
        System.out.println("Hashes in '" + name + "'");
        for (int i = 0; i < hash_array.length; i++) {
            System.out.println("[" + i + "]:" + hash_array[i]);
        }
    }

    public String get(int i) {
        if ((i < 0) || (i > hash_array.length - 1)) {
            return "";
        }
        return hash_array[i];
    }

    private static String getActionHashHash(String nm, int[] ags, String[] a_a, int rt) {
        StringBuffer sb = new StringBuffer(100);
        sb.append(nm);
        for (int i = 0; i < ags.length; i++) {
            sb.append(ags[i]).append(a_a[i]);
        }
        sb.append(rt);
        return sb.toString();
    }

    private static String[] getStringArray(String name, int[] args, String[] args_alt, int ret, ThingTypeContainer tt) {
        String[][] permutes = getStringArrays(name, args, args_alt, ret, tt);
        String[] rets = Var.getVarAlternates(ret);
        Vector perms = new Vector();
        permute(permutes, perms, name, rets);
        Object[] o = perms.toArray();
        String[] ss = new String[o.length];
        System.arraycopy(o, 0, ss, 0, o.length);
        return ss;
    }

    private static void permute(String[][] s, Vector v, String name, String[] rets) {
        String[] combs = new String[s.length];
        permute(combs, s, v, name, rets, 0);
    }

    private static void permute(String[] combs, String[][] s, Vector v, String name, String[] rets, int i) {
        if (s.length == i) {
            putHashes(name, combs, rets, v);
        } else {
            for (int j = 0; j < s[i].length; j++) {
                combs[i] = s[i][j];
                permute(combs, s, v, name, rets, i + 1);
            }
        }
    }

    private static String[][] getStringArrays(String name, int[] args, String[] args_alt, int ret, ThingTypeContainer tt) {
        String[][] bigArray = new String[args.length][];
        for (int i = 0; i < args.length; i++) {
            if (!args_alt[i].equals("")) {
                if (args[i] == Var.GROUP) {
                    bigArray[i] = Var.getGroupAlternates(args_alt[i]);
                    continue;
                } else if (args[i] == Var.THING) {
                    bigArray[i] = tt.get(args_alt[i]).getSearchAncestors();
                    continue;
                }
            }
            bigArray[i] = Var.getVarAlternates(args[i]);
        }
        return bigArray;
    }

    private static void putHashes(String name, String[] args, String[] rets, Vector v) {
        for (int i = 0; i < rets.length; i++) {
            v.addElement(getHash(name, args, rets[i]));
        }
    }

    public static String getHash(String name, String[] args, String ret) {
        StringBuffer sb = new StringBuffer(100);
        sb.append(name);
        sb.append(":args");
        for (int i = 0; i < args.length; i++) {
            sb.append(':').append(args[i]);
        }
        sb.append(":ret:").append(ret);
        return sb.toString();
    }

    public static ActionHashIterator getActionHashIterator(String nm, int[] ags, String[] a_a, int rt, ThingTypeContainer tt) {
        String ahh = getActionHashHash(nm, ags, a_a, rt);
        if (hm.containsKey(ahh)) {
            return (ActionHashIterator) hm.get(ahh);
        }
        ActionHashIterator nw = new ActionHashIterator(nm, ags, a_a, rt, tt);
        hm.put(ahh, nw);
        return nw;
    }
}
