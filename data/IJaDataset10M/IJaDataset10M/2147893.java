package org.tcpfile.update.updatescripts;

import org.tcpfile.main.Misc;

/**
 * To compare version strings.
 * Only accepts form:
 * (\\d*\\[a-z]*.?)+
 * @author Stivo
 *
 */
public class Versions implements Comparable<Versions> {

    String version;

    public static final Versions current = new Versions(Misc.VERSIONNUMBER);

    public Versions(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(Versions o) {
        if (version.equals(o.version)) return 0;
        String[] a = version.split("\\.");
        String[] b = o.version.split("\\.");
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(b[i])) continue;
            if (areCombos(a[i], b[i])) {
                int comp = compareCombos(a[i], b[i]);
                if (comp != 0) return comp;
            }
        }
        return 0;
    }

    private int compareCombos(String a, String b) {
        assert (areCombos(a, b));
        int aint = Integer.valueOf(a.replaceAll("[a-z]", ""));
        int bint = Integer.valueOf(b.replaceAll("[a-z]", ""));
        if (bint != aint) return aint - bint;
        String achars = a.replace("\\d", "");
        String bchars = b.replace("\\d", "");
        if (!achars.equals(bchars)) return achars.compareTo(bchars);
        return 0;
    }

    public boolean before(Versions o) {
        return this.compareTo(o) < 0;
    }

    public boolean beforeOrEquals(Versions o) {
        return this.compareTo(o) <= 0;
    }

    private boolean areCombos(String a, String b) {
        return isCombo(a) && isCombo(b);
    }

    private boolean isCombo(String a) {
        return a.matches("\\d*[a-z]*");
    }
}
