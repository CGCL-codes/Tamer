package org.soda.dpws.util;

import java.util.List;
import org.soda.dpws.ScopeMatchRule;
import org.soda.dpws.registry.discovery.WSDConstants;

/**
 * 
 * 
 */
public class Tools {

    static boolean isIncluded(List<?> a, List<?> b, ScopeMatchRule matchRule) {
        if (a == null || a.isEmpty()) return true;
        if (b == null || b.isEmpty()) return false;
        for (int i = 0; i < a.size(); i++) {
            int j;
            for (j = 0; j < b.size(); j++) {
                if (matchRule == null) {
                    if (a.get(i).equals(b.get(j))) {
                        j = 0;
                        break;
                    }
                } else if (matchRule.match((String) a.get(i), (String) b.get(j))) {
                    j = 0;
                    break;
                }
            }
            if (j != 0) return false;
        }
        return true;
    }

    /**
   * @param types
   * @param scopes
   * @param matchRule
   * @param devTypes
   * @param devScopes
   * @return <tt>true</tt> on success
   */
    public static final boolean probeMatch(List<?> types, List<?> scopes, ScopeMatchRule matchRule, List<?> devTypes, List<?> devScopes) {
        return isIncluded(types, devTypes, null) && ((devScopes != null && !devScopes.isEmpty()) ? isIncluded(scopes, devScopes, matchRule) : scopes == null || scopes.size() == 0 || (scopes.size() == 1 && matchRule.match((String) scopes.get(0), WSDConstants.WSD_DEFAULT_SCOPE)));
    }
}
