package com.google.gwt.dev.shell;

import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.dev.cfg.Rule;
import com.google.gwt.dev.cfg.Rules;
import com.google.gwt.dev.javac.StandardGeneratorContext;
import com.google.gwt.dev.jdt.RebindOracle;
import com.google.gwt.dev.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements rebind logic in terms of a variety of other well-known oracles.
 */
public class StandardRebindOracle implements RebindOracle {

    /**
   * Makes the actual deferred binding decision by examining rules.
   */
    private final class Rebinder {

        private final Set<Rule> usedRules = new HashSet<Rule>();

        private final List<String> usedTypeNames = new ArrayList<String>();

        public String rebind(TreeLogger logger, String typeName, ArtifactAcceptor artifactAcceptor) throws UnableToCompleteException {
            genCtx.setPropertyOracle(propOracle);
            String result = tryRebind(logger, typeName);
            if (artifactAcceptor != null) {
                ArtifactSet newlyGeneratedArtifacts = genCtx.finish(logger);
                if (!newlyGeneratedArtifacts.isEmpty()) {
                    artifactAcceptor.accept(logger, newlyGeneratedArtifacts);
                }
            }
            if (result == null) {
                result = typeName;
            }
            return result;
        }

        private String tryRebind(TreeLogger logger, String typeName) throws UnableToCompleteException {
            if (usedTypeNames.contains(typeName)) {
                String[] cycle = Util.toArray(String.class, usedTypeNames);
                Messages.UNABLE_TO_REBIND_DUE_TO_CYCLE_IN_RULES.log(logger, cycle, null);
                throw new UnableToCompleteException();
            }
            usedTypeNames.add(typeName);
            if (rules.isEmpty()) {
                logger.log(TreeLogger.DEBUG, "No rules are defined, so no substitution can occur", null);
                return null;
            }
            for (Iterator<Rule> iter = rules.iterator(); iter.hasNext(); ) {
                Rule rule = iter.next();
                TreeLogger branch = Messages.TRACE_CHECKING_RULE.branch(logger, rule, null);
                if (rule.isApplicable(branch, genCtx, typeName)) {
                    if (!usedRules.contains(rule)) {
                        usedRules.add(rule);
                        Messages.TRACE_RULE_MATCHED.log(logger, null);
                        return rule.realize(logger, genCtx, typeName);
                    } else {
                    }
                } else {
                    Messages.TRACE_RULE_DID_NOT_MATCH.log(logger, null);
                }
            }
            return null;
        }
    }

    private final Map<String, String> cache = new HashMap<String, String>();

    private final StandardGeneratorContext genCtx;

    private final PropertyOracle propOracle;

    private final Rules rules;

    public StandardRebindOracle(PropertyOracle propOracle, Rules rules, StandardGeneratorContext genCtx) {
        this.propOracle = propOracle;
        this.rules = rules;
        this.genCtx = genCtx;
    }

    public String rebind(TreeLogger logger, String typeName) throws UnableToCompleteException {
        return rebind(logger, typeName, null);
    }

    public String rebind(TreeLogger logger, String typeName, ArtifactAcceptor artifactAcceptor) throws UnableToCompleteException {
        String result = cache.get(typeName);
        if (result != null && result.contains("_designTime")) {
            result = null;
        }
        if (result == null) {
            logger = Messages.TRACE_TOPLEVEL_REBIND.branch(logger, typeName, null);
            Rebinder rebinder = new Rebinder();
            result = rebinder.rebind(logger, typeName, artifactAcceptor);
            cache.put(typeName, result);
            Messages.TRACE_TOPLEVEL_REBIND_RESULT.log(logger, result, null);
        }
        return result;
    }
}
