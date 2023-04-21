package org.omg.DsObservationAccess;

/**
 * Interface definition: ObservationRemote.
 * 
 * @author OpenORB Compiler
 */
public interface ObservationRemoteOperations extends org.omg.DsObservationAccess.AbstractManagedObjectOperations {

    /**
     * Read accessor for observation_code attribute
     * @return the attribute value
     */
    public String observation_code();

    /**
     * Operation get_observation_time
     */
    public org.omg.DsObservationAccess.TimeSpan get_observation_time();

    /**
     * Operation get_observed_subject
     */
    public org.omg.DsObservationAccess.ObservedSubject get_observed_subject();

    /**
     * Operation get_root_observation
     */
    public org.omg.DsObservationAccess.ObservationRemote get_root_observation();

    /**
     * Operation get_path_from_root
     */
    public org.omg.CORBA.Any get_path_from_root();

    /**
     * Operation get_all_qualifiers
     */
    public org.omg.CORBA.Any[] get_all_qualifiers();

    /**
     * Operation get_qualifiers
     */
    public org.omg.CORBA.Any[] get_qualifiers(String[] qualifier_names) throws org.omg.DsObservationAccess.InvalidCodes;

    /**
     * Operation is_this_root
     */
    public boolean is_this_root();

    /**
     * Operation is_this_atomic
     */
    public boolean is_this_atomic();
}
