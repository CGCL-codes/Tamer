package org.omg.PersonIdService;

/**
 * Struct definition: Candidate.
 * 
 * @author OpenORB Compiler
*/
public final class Candidate implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member id
     */
    public String id;

    /**
     * Struct member confidence
     */
    public float confidence;

    /**
     * Struct member profile
     */
    public org.omg.PersonIdService.Trait[] profile;

    /**
     * Default constructor
     */
    public Candidate() {
    }

    /**
     * Constructor with fields initialization
     * @param id id struct member
     * @param confidence confidence struct member
     * @param profile profile struct member
     */
    public Candidate(String id, float confidence, org.omg.PersonIdService.Trait[] profile) {
        this.id = id;
        this.confidence = confidence;
        this.profile = profile;
    }
}
