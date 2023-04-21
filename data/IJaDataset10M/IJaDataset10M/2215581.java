package org.omg.TerminologyServices;

/**
 * Interface definition: LinguisticGroupAccess.
 * 
 * @author OpenORB Compiler
 */
public class LinguisticGroupAccessPOATie extends LinguisticGroupAccessPOA {

    private LinguisticGroupAccessOperations _tie;

    private org.omg.PortableServer.POA _poa;

    /**
     * Constructor
     */
    public LinguisticGroupAccessPOATie(LinguisticGroupAccessOperations tieObject) {
        _tie = tieObject;
    }

    /**
     * Constructor
     */
    public LinguisticGroupAccessPOATie(LinguisticGroupAccessOperations tieObject, org.omg.PortableServer.POA poa) {
        _tie = tieObject;
        _poa = poa;
    }

    /**
     * Get the delegate
     */
    public LinguisticGroupAccessOperations _delegate() {
        return _tie;
    }

    /**
     * Set the delegate
     */
    public void _delegate(LinguisticGroupAccessOperations delegate_) {
        _tie = delegate_;
    }

    /**
     * _default_POA method
     */
    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) return _poa; else return super._default_POA();
    }

    /**
     * Operation get_linguistic_group
     */
    public org.omg.TerminologyServices.LinguisticGroupInfo get_linguistic_group(String linguistic_group_id) throws org.omg.TerminologyServices.LinguisticGroupNotInCodingSchemeVersion {
        return _tie.get_linguistic_group(linguistic_group_id);
    }

    /**
     * Read accessor for coding_scheme_id attribute
     */
    public org.omg.NamingAuthority.AuthorityId coding_scheme_id() {
        return _tie.coding_scheme_id();
    }

    /**
     * Read accessor for version_id attribute
     */
    public String version_id() {
        return _tie.version_id();
    }

    /**
     * Read accessor for language_id attribute
     */
    public String language_id() {
        return _tie.language_id();
    }

    /**
     * Read accessor for is_default_version attribute
     */
    public boolean is_default_version() {
        return _tie.is_default_version();
    }

    /**
     * Read accessor for is_complete_scheme attribute
     */
    public boolean is_complete_scheme() {
        return _tie.is_complete_scheme();
    }

    /**
     * Read accessor for coding_scheme_version_if attribute
     */
    public org.omg.TerminologyServices.CodingSchemeVersion coding_scheme_version_if() {
        return _tie.coding_scheme_version_if();
    }

    /**
     * Read accessor for presentation_if attribute
     */
    public org.omg.TerminologyServices.PresentationAccess presentation_if() {
        return _tie.presentation_if();
    }

    /**
     * Read accessor for linguistic_group_if attribute
     */
    public org.omg.TerminologyServices.LinguisticGroupAccess linguistic_group_if() {
        return _tie.linguistic_group_if();
    }

    /**
     * Read accessor for systemization_if attribute
     */
    public org.omg.TerminologyServices.SystemizationAccess systemization_if() {
        return _tie.systemization_if();
    }

    /**
     * Read accessor for advanced_query_if attribute
     */
    public org.omg.TerminologyServices.AdvancedQueryAccess advanced_query_if() {
        return _tie.advanced_query_if();
    }
}
