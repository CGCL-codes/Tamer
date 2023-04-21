package gnu.hylafax;

import java.awt.Dimension;
import java.util.HashMap;

/**
 * This class serves as a store of common pagesizes, or create your own!
 * 
 * @version $Revision: 168 $
 * @author Joe Phillips <jaiger@innovationsw.com>
 * @author Steven Jardine <steve@mjnservices.com>
 */
public class Pagesize extends Dimension {

    private static HashMap sizes = new HashMap();

    /** a US Letter size page */
    public static final Pagesize letter = new Pagesize(216, 279);

    /** a US Letter size page */
    public static final Pagesize LETTER = letter;

    /** North American Letter size page */
    public static final Pagesize naletter = letter;

    /** North American Letter size page */
    public static final Pagesize NALETTER = letter;

    /** US Letter size page */
    public static final Pagesize usletter = letter;

    /** US Letter size page */
    public static final Pagesize USLETTER = letter;

    /** a US Legal size page */
    public static final Pagesize legal = new Pagesize(216, 356);

    /** a US Legal size page */
    public static final Pagesize LEGAL = legal;

    /** a US Legal size page */
    public static final Pagesize uslegal = legal;

    /** a US Legal size page */
    public static final Pagesize USLEGAL = legal;

    /** a US Ledger size page */
    public static final Pagesize ledger = new Pagesize(279, 432);

    /** a US Ledger size page */
    public static final Pagesize LEDGER = ledger;

    /** a US Ledger size page */
    public static final Pagesize usledger = ledger;

    /** a US Ledger size page */
    public static final Pagesize USLEDGER = ledger;

    /** an A3 size page */
    public static final Pagesize a3 = new Pagesize(297, 420);

    /** an A3 size page */
    public static final Pagesize A3 = a3;

    /** an A4 size page */
    public static final Pagesize a4 = new Pagesize(210, 297);

    /** an A4 size page */
    public static final Pagesize A4 = a4;

    /** an A5 size page */
    public static final Pagesize a5 = new Pagesize(148, 210);

    /** an A5 size page */
    public static final Pagesize A5 = a5;

    /** an A6 size page */
    public static final Pagesize a6 = new Pagesize(105, 148);

    /** an A6 size page */
    public static final Pagesize A6 = a6;

    /** an B4 size page */
    public static final Pagesize b4 = new Pagesize(250, 353);

    /** an B4 size page */
    public static final Pagesize B4 = b4;

    /** a US Executive size page */
    public static final Pagesize executive = new Pagesize(190, 254);

    /** a US Executive size page */
    public static final Pagesize EXECUTIVE = executive;

    /** a US Executive size page */
    public static final Pagesize usexecutive = executive;

    /** a US Executive size page */
    public static final Pagesize USEXECUTIVE = executive;

    static {
        sizes.put("letter", letter);
        sizes.put("us-letter", letter);
        sizes.put("us-let", letter);
        sizes.put("na-let", letter);
        sizes.put("legal", legal);
        sizes.put("us-legal", legal);
        sizes.put("us-leg", legal);
        sizes.put("na-leg", legal);
        sizes.put("ledger", ledger);
        sizes.put("us-ledger", ledger);
        sizes.put("us-led", ledger);
        sizes.put("na-led", ledger);
        sizes.put("executive", executive);
        sizes.put("us-executive", executive);
        sizes.put("us-exe", executive);
        sizes.put("na-exe", executive);
        sizes.put("a3", a3);
        sizes.put("a4", a4);
        sizes.put("a5", a5);
        sizes.put("a6", a6);
        sizes.put("b4", b4);
    }

    /**
     * default constructor
     */
    public Pagesize() {
        super();
    }

    /**
     * copy constructor
     */
    public Pagesize(Dimension d) {
        super(d);
    }

    /**
     * constructor specifying width and height respectively
     */
    public Pagesize(int width, int height) {
        super(width, height);
    }

    /**
     * lookup a pagesize by its name
     */
    public static Pagesize getPagesize(String key) {
        return (Pagesize) sizes.get(key.toLowerCase());
    }

    /**
     * add a (non-null) mapping from name to pagesize
     */
    public static void putPagesize(String key, Pagesize p) {
        if ((p == null) || (key == null)) return;
        sizes.put(key.toLowerCase(), p);
    }
}
