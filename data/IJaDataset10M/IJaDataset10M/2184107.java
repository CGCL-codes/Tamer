package com.hp.hpl.jena.reasoner.rulesys;

import com.hp.hpl.jena.graph.Capabilities;
import com.hp.hpl.jena.reasoner.*;
import java.util.*;

/**
 * Reasoner configuration for the OWL mini reasoner.
 * Key limitations over the normal OWL configuration are:
 * <UL>
 * <li>omits the someValuesFrom => bNode entailments</li>
 * <li>avoids any guard clauses which would break the find() contract</li>
 * <li>omits inheritance of range implications for XSD datatype ranges</li>
 * </UL>
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.6 $ on $Date: 2006/03/22 13:52:20 $
 */
public class OWLMiniReasoner extends GenericRuleReasoner implements Reasoner {

    /** The location of the OWL rule definitions on the class path */
    protected static final String MINI_RULE_FILE = "etc/owl-fb-mini.rules";

    /** The parsed rules */
    protected static List miniRuleSet;

    /**
     * Return the rule set, loading it in if necessary
     */
    public static List loadRules() {
        if (miniRuleSet == null) miniRuleSet = loadRules(MINI_RULE_FILE);
        return miniRuleSet;
    }

    /**
     * Constructor
     */
    public OWLMiniReasoner(ReasonerFactory factory) {
        super(loadRules(), factory);
        setOWLTranslation(true);
        setMode(HYBRID);
    }

    /**
     * Return the Jena Graph Capabilties that the inference graphs generated
     * by this reasoner are expected to conform to.
     */
    public Capabilities getGraphCapabilities() {
        if (capabilities == null) {
            capabilities = new BaseInfGraph.InfFindSafeCapabilities();
        }
        return capabilities;
    }
}
