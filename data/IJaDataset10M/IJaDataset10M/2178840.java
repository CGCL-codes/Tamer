package de.humanfork.treemerge.match;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Q Gramm Profile Cache.
 *
 * There is a first and a second level cache.
 * L1 Cach cache 2 profiles.
 * L2 Cach cache every profile.
 *
 * @author Ralph
 *
 */
public class QGrammCache {

    /**
     * Number of initial generated L2 pages.
     * One page for every q-Gramm level.
     */
    private static final int INITIAL_PAGE_SIZE = 4;

    /**
     * L1 cach A: q-level.
     */
    private int aQ;

    /**
     * L1 cach A: text.
     */
    private String aText;

    /**
     * L1 cach A: q(aQ)-gramm profile for a text.
     */
    private Hashtable<String, Integer> aProfile;

    /**
     * L1 cach B: q-level.
     */
    private int bQ;

    /**
     * L1 cach A: text.
     */
    private String bText;

    /**
     * L1 cach B: q(bQ)-gramm profile for b text.
     */
    private Hashtable<String, Integer> bProfile;

    /**
     * Which L1 cash has the last hit.
     * 0 means a
     * 1 means b
     */
    private int ab = 0;

    /**
     * Second level Cache.
     */
    private List<Hashtable<String, Hashtable<String, Integer>>> cache;

    /**
     * The Constructor.
     */
    public QGrammCache() {
        super();
        this.cache = new ArrayList<Hashtable<String, Hashtable<String, Integer>>>(INITIAL_PAGE_SIZE);
        this.generateQ(INITIAL_PAGE_SIZE);
    }

    /**
     * Generate a new L2 cache page for profiles with spezified q.
     * @param q q from q-gramm
     */
    protected void generateQ(final int q) {
        for (int i = this.cache.size(); i < q; i++) {
            this.cache.add(new Hashtable<String, Hashtable<String, Integer>>(100));
        }
    }

    /**
     * Return a cached q-gramm profile for text.
     * @param text text
     * @param q q-gramm level
     * @return q-gramm profile for text or null
     */
    public Hashtable<String, Integer> getProfile(final String text, final int q) {
        if ((this.aQ == q) && (this.aText.equals(text))) {
            ab = 0;
            return this.aProfile;
        }
        if ((this.bQ == q) && (this.bText.equals(text))) {
            ab = 1;
            return this.bProfile;
        }
        if (this.cache.size() < q) {
            return null;
        } else {
            Hashtable<String, Integer> profile = this.cache.get(q - 1).get(text);
            return profile;
        }
    }

    /**
     * Store a q-gramm profile for text.
     * @param text text
     * @param q q q-gramm level
     * @param profile q-gramm profile for text
     */
    public void putProfile(final String text, final int q, final Hashtable<String, Integer> profile) {
        if (this.ab == 0) {
            this.bQ = q;
            this.bText = text;
            this.bProfile = profile;
        } else {
            this.aQ = q;
            this.aText = text;
            this.aProfile = profile;
        }
        if (this.cache.size() < q) this.generateQ(q);
        this.cache.get(q - 1).put(text, profile);
    }
}
