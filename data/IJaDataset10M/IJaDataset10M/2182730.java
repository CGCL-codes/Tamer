package org.hl7.types;

/**
 * 
 * A name for a person, organization, place or thing. A
 * sequence of name parts, such as given name or family
 * name, prefix, suffix, etc. Examples for entity name
 * values are "Jim Bob Walton, Jr.", "Health Level Seven,
 * Inc.", "Lake Tahoe", etc. An entity name may be as simple
 * as a character string or may consist of several entity name
 * parts, such as, "Jim", "Bob", "Walton", and "Jr.", "Health
 * Level Seven" and "Inc.", "Lake" and "Tahoe".
 * 
 * @generatedComment
 */
public interface EN extends LIST<ENXP> {

    /**
   * 
   * A set of codes advising a system or user which name
   * in a set of like names to select for a given purpose.
   * A name without specific use code might be a default
   * name useful for any purpose, but a name with a specific
   * use code would be preferred for that respective purpose.
   * 
   * @generatedComment
   */
    DSET<CS> use();

    IVL<TS> useablePeriod();

    ST formatted();
}
