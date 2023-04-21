package net.sf.metarbe;

/**
 * Manages rule session 
 */
public interface RuleRuntime {

    /**
	 * Creates new rule session object depending on its type
	 * @param aRuleLayout layout to be given to session 
	 * @param aSessionType type of session {@link RuleSessionType}
	 * @return rule session object
	 */
    RuleSession createRuleSession(RuleSemanticModel aRuleLayout, RuleSessionType aSessionType) throws SessionFactoryIsNotSetException;

    /**
	 * Registers rule session factory. This factory will be used to create instances of sessions
	 * @param aSessionFactory session factory object
	 * @param aSessionType one of session types
	 */
    void registerRuleSessionFactory(RuleSessionFactory aSessionFactory, RuleSessionType aSessionType);

    /**
	 * Derigesters factroy from runtime. If there is no factory rigestered for given type 
	 * SessionFactoryIsNotSetException thrown
	 * @param aSessionType
	 */
    void deregisterRuleSessionFactory(RuleSessionType aSessionType) throws SessionFactoryIsNotSetException;

    /**
	 * Creates a rule layout object
	 * @return object
	 */
    RuleSemanticModel createRuleSemanticModel();

    /**
	 * Gets rule set metadata 
	 * @return rule set metadata object
	 */
    RuleSetManager getRuleSetManager();
}
