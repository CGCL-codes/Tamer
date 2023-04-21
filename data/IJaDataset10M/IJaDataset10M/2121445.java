package org.omg.IOP;

/**
 * Holds an integer constant of the TAG_ALTERNATE_IIOP_ADDRESS Component that
 * may occur zero or more times in the Internet of Multiple components profile.
 * The tag contains the possible alternative address (host and port) of
 * the object being defined by IOR profile.
 * This tag is supported since GIOP 1.2.
 *
 * @see TAG_INTERNET_IOP
 * @see TAG_MULTIPLE_COMPONENTS
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public interface TAG_ALTERNATE_IIOP_ADDRESS {

    /**
   * Specifies the TAG_ALTERNATE_IIOP_ADDRESS value, 3.
   */
    int value = 3;
}
