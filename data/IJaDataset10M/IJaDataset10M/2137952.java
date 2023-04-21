package edu.tufts.vue.fsm;

/**
 When a Repository is included in a federated search, it may not be able to digest
 the search criteria, type, and properties the user enters for all repositories.  An
 adjuster can fix up the input and will be called just before the repository is asked
 to perform the search.
 */
public interface QueryAdjuster {

    public Query adjustQuery(org.osid.repository.Repository repository, java.io.Serializable searchCriteria, org.osid.shared.Type searchType, org.osid.shared.Properties searchProperties);
}
