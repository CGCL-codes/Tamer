package org.omg.PersonIdService;

/**
 * Struct definition: TaggedProfile.
 * 
 * @author OpenORB Compiler
*/
public final class TaggedProfile implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member id
     */
    public String id;

    /**
     * Struct member profile
     */
    public org.omg.PersonIdService.Trait[] profile;

    /**
     * Default constructor
     */
    public TaggedProfile() {
    }

    /**
     * Constructor with fields initialization
     * @param id id struct member
     * @param profile profile struct member
     */
    public TaggedProfile(String id, org.omg.PersonIdService.Trait[] profile) {
        this.id = id;
        this.profile = profile;
    }
}
