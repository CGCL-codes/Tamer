package org.dbwiki.data.security;

import java.io.File;

/**
 * This class wraps a policy and performs access control checks to determine 
 * whether an operation is allowed.  Checks are based on matching URLs to policy rules,
 * which no longer works in the current DBWiki system.
 * @author jcheney
 *
 */
public class Security {

    private Policy policy = new Policy();

    public void loadPolicy(File file) {
        policy.loadFromFile(file);
    }

    public boolean checkUpdate(String username, String updType, String URL) {
        Boolean mark = false;
        boolean finalDecision = false;
        if (policy.getConflict()) {
            if (mark == false && policy.positiveRules(username, updType, URL)) {
                finalDecision = true;
                mark = true;
            } else if (mark == false && policy.negativeRules(username, updType, URL)) {
                finalDecision = false;
                mark = true;
            }
        } else {
            if (mark == false && policy.negativeRules(username, updType, URL)) {
                finalDecision = false;
                mark = true;
            } else if (mark == false && policy.positiveRules(username, updType, URL)) {
                finalDecision = true;
                mark = true;
            }
        }
        if (mark == false) {
            finalDecision = policy.getDefault();
        }
        return finalDecision;
    }
}
