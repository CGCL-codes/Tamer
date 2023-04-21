package uk.co.weft.fisherman.entities;

/**
 * A person's postal - surface mail - address.
 */
public abstract class Address extends Entity {

    /** the key field in my table */
    public static final String KEYFN = "address_id";

    /** the name of my responsible person field */
    public static final String PERSONFN = Person.KEYFN;

    /** the name of my first line field */
    public static final String LINE1FN = "address1";

    /** the name of my second line field */
    public static final String LINE2FN = "address2";

    /** the name of my third line field */
    public static final String LINE3FN = "address3";

    /** the name of my fourth line field */
    public static final String LINE4FN = "address4";

    /** the name of my post code field */
    public static final String POSTCODEFN = "postcode";

    /** the name of my iscurrent field */
    public static final String ISCURRENTFN = "current";

    /** the name of my table */
    public static final String TABLENAME = "ADDRESS";
}
