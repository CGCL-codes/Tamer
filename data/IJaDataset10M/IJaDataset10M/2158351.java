package gov.lanl.RAD;

import org.omg.DfResourceAccessDecision.*;
import java.util.Properties;
import gov.lanl.Utility.*;

/**
 * Provides methods to other objects to retrieve PolicyEvaluators and
 * Decision Combinators corresponding to resourcenames.  Uses the Admin
 * interface for this
 */
public class PolicyEvaluatorLocatorImpl extends PolicyEvaluatorLocatorPOA {

    PolicyEvaluatorLocatorBasicAdmin PELBA;

    PolicyEvaluatorLocatorNameAdmin PELNA;

    PolicyEvaluatorLocatorPatternAdmin PELPA;

    static NameService ns;

    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(PolicyEvaluatorLocatorImpl.class.getName());

    /**
     * Binds to BasicAdmin and NameAdmin so that it can use them to retreive
     * PolicyEvaluators and Decision Combinators corresponding to resourcenames
     * @param theOrb
     * @param name specifies the name of the object
     * @param props 
     */
    public PolicyEvaluatorLocatorImpl(org.omg.CORBA.ORB theOrb, String name, Properties props) {
        _this_object(theOrb);
        String configFile = props.getProperty("PolicyCfg", "policy");
        PolicyMgr.getInstance(theOrb, configFile);
        try {
            String evaluatorBasicAdmin = props.getProperty("EvaluatorBasicAdmin", "PELBA");
            PolicyEvaluatorLocatorBasicAdminImpl PELBAImpl = new PolicyEvaluatorLocatorBasicAdminImpl(theOrb, evaluatorBasicAdmin, props.getProperty("EvaluatorBasic", "PELBA"));
            PELBA = PELBAImpl._this(theOrb);
            String evaluatorNameAdmin = props.getProperty("EvaluatorNameAdmin", "PELNA");
            PolicyEvaluatorLocatorNameAdminImpl PELNAImpl = new PolicyEvaluatorLocatorNameAdminImpl(theOrb, evaluatorNameAdmin, props.getProperty("EvaluatorName", "PELNA"));
            PELNA = PELNAImpl._this(theOrb);
            String evaluatorPatternAdmin = props.getProperty("EvaluatorPatternAdmin", "PELPA");
            PolicyEvaluatorLocatorPatternAdminImpl PELPAImpl = new PolicyEvaluatorLocatorPatternAdminImpl(evaluatorPatternAdmin, props.getProperty("EvaluatorPattern", "PELPA"));
            PELPA = PELPAImpl._this(theOrb);
        } catch (Exception e) {
            cat.error("constructor failed ", e);
        }
        cat.info("returning from PolicyEvaluatorLocatorImpl");
    }

    /**
     * @return reference to BasicAdmin object
     */
    public PolicyEvaluatorLocatorBasicAdmin basic_admin() {
        return PELBA;
    }

    /**
     * @return reference to NameAdmin object
     */
    public PolicyEvaluatorLocatorNameAdmin name_admin() {
        return PELNA;
    }

    /**
     * @return reference to PatternAdmin object
     */
    public PolicyEvaluatorLocatorPatternAdmin pattern_admin() {
        return PELPA;
    }

    /**
     * Returns PolicyDecisionEvaluators based on the resouce_name
     * @param resource_name the name corresponding to which the
     * PolicyDecisionEvaluators are to be returned
     * @return PolicyDecisionEvaluators corresponding to the input resource_name
     */
    public PolicyDecisionEvaluators get_policy_decision_evaluators(ResourceName resource_name) throws RadComponentError {
        cat.info("Executing get_policy_decision_evaluators");
        NamedPolicyEvaluator[] NPEs = null;
        try {
            NPEs = PELNA.get_evaluators(resource_name);
        } catch (InvalidResourceName e) {
            cat.error(e);
        }
        try {
            if (NPEs == null || NPEs.length == 0) NPEs = PELPA.get_evaluators_by_pattern(resource_name);
        } catch (InvalidResourceNamePattern p) {
            cat.error("evaluators: " + p);
        } catch (PatternNotRegistered pn) {
            cat.error("evaluators: " + pn);
        }
        if (NPEs == null || NPEs.length == 0) NPEs = PELBA.get_default_evaluators();
        cat.info("Obtained NPEs");
        DecisionCombinator dc = null;
        try {
            dc = PELNA.get_combinator(resource_name);
        } catch (InvalidResourceName e) {
            cat.error(e);
        }
        try {
            if (dc == null) dc = PELPA.get_combinator_by_pattern(resource_name);
        } catch (InvalidResourceNamePattern p) {
            cat.error("combinator: " + p);
        } catch (PatternNotRegistered pn) {
            cat.error("combinator: " + pn);
        }
        if (dc == null) dc = PELBA.get_default_combinator();
        cat.info("Obtained DC");
        PolicyDecisionEvaluators PDEs = new PolicyDecisionEvaluators(NPEs, dc);
        return PDEs;
    }

    /**
     * Creates a PolicyEvaluatorLocator object and then waits forever
     * for operation requests which are invoked on the object
     */
    public static void main(String args[]) {
        ConfigProperties props = new ConfigProperties();
        props.setProperties("policy.cfg", args);
        PolicyEvaluatorLocatorImpl PELobject = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);
        String serverName = "pel";
        ns = new NameService(orb, "");
        try {
            PELobject = new PolicyEvaluatorLocatorImpl(orb, serverName, props);
            PolicyEvaluatorLocator pel = PELobject._this(orb);
            if (ns != null) ns.register(pel, serverName);
            orb.run();
        } catch (org.omg.CORBA.SystemException se) {
            cat.error(se);
        }
    }
}
