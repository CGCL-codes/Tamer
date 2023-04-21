package jgnash.engine;

/**
 * Static account utilities
 * 
 * @author Craig Cavanaugh
 * @version $Id: AccountUtils.java 3070 2012-01-04 11:00:43Z ccavanaugh $
 */
public class AccountUtils {

    /**
     * Searches an account tree given the supplied parameters
     * 
     * @param root Base account
     * @param name Account name
     * @param type Account type
     * @param depth Account depth
     * @return matched account if it exists
     */
    protected static Account searchTree(final Account root, final String name, final AccountType type, final int depth) {
        Account match = null;
        for (Account a : root.getChildren()) {
            if (a.getName().equals(name) && a.getAccountType() == type && a.getDepth() == depth) {
                match = a;
            } else if (a.getChildCount() > 0) {
                match = searchTree(a, name, type, depth);
            }
            if (match != null) {
                break;
            }
        }
        return match;
    }

    private AccountUtils() {
    }
}
