package uk.co.weft.fisherman.entities;

/**
 * Reference data; type of violation of the rules.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class Violation extends Entity {

    /** the name of the key field in my table - in this case char( 4) */
    public static final String KEYFN = "violation_id";

    /** the name of my description field */
    public static final String DESCFN = "vi_desc";

    /** the name of my table */
    public static final String TABLENAME = "VIOLATION";

    /** The type of violation which is a reused tag violation */
    public static final String TYPEREUSED = "reused";

    /** The type of violation which is an invalid licence violation */
    public static final String TYPELICENCE = "licence";

    /** The type of violation which is an out-of-zone violation */
    public static final String TYPEZONE = "zone";

    /** The type of violation which is an unissued tag violation */
    public static final String TYPEBADSTATUS = "badstate";

    /** The type of violation which is an overweight violation */
    public static final String TYPEOVERWEIGHT = "ovweight";
}
