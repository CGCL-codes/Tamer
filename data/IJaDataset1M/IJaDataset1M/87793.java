package acide.configuration.lexicon.tokens;

import java.io.Serializable;
import acide.configuration.utils.ObjectList;

/**
 * <p>
 * ACIDE - A Configurable IDE lexicon configuration token manager.
 * </p>
 * <p>
 * Handles the lexicon configuration token handling methods and classes.
 * </p>
 * <p>
 * The ACIDE - A Configurable IDE lexicon configuration has a token group list,
 * in other words, tokens with the same configuration are grouped together.
 * Below this level, each token group has another token list which defines its
 * configuration.
 * </p>
 * 
 * @version 0.8
 * @see Serializable
 */
public class AcideLexiconTokenManager implements Serializable {

    /**
	 * ACIDE - A Configurable IDE lexicon configuration token manager class
	 * serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ACIDE - A Configurable IDE lexicon configuration token manager object
	 * list.
	 */
    private ObjectList _list;

    /**
	 * Creates a new ACIDE - A Configurable IDE lexicon configuration token
	 * manager.
	 */
    public AcideLexiconTokenManager() {
        super();
        _list = new ObjectList();
    }

    /**
	 * Returns a token group from the list at the given index.
	 * 
	 * @param index
	 *            index to get.
	 * @return a token group from the list at the given index.
	 */
    public AcideLexiconTokenGroup getTokenGroupAt(int index) {
        return (AcideLexiconTokenGroup) _list.getObjectAt(index);
    }

    /**
	 * Inserts a new token in the ACIDE - A Configurable IDE lexicon
	 * configuration token manager object list.
	 * 
	 * @param tokenGroup
	 *            new value to set.
	 */
    public void insertTokenGroup(AcideLexiconTokenGroup tokenGroup) {
        _list.insert(_list.size(), tokenGroup);
    }

    /**
	 * Returns the ACIDE - A Configurable IDE lexicon configuration token group
	 * manager list size.
	 * 
	 * @return the ACIDE - A Configurable IDE lexicon configuration token group
	 *         manager list size.
	 */
    public int getSize() {
        return _list.size();
    }

    /**
	 * Inserts a token into the ACIDE - A Configurable IDE lexicon configuration
	 * token group manager list.
	 * 
	 * @param tokenGroup
	 *            new token group.
	 * @param token
	 *            token name.
	 */
    public void insertToken(AcideLexiconTokenGroup tokenGroup, String token) {
        boolean found = false;
        boolean found2 = false;
        int index = 0;
        for (int index1 = 0; index1 < getSize(); index1++) {
            String s1 = getTokenGroupAt(index1).getName();
            String s2 = tokenGroup.getName();
            if (s1.equals(s2)) {
                found = true;
                index = index1;
            }
            for (int index2 = 0; index2 < getTokenGroupAt(index1).getSize(); index2++) {
                String s3 = token;
                String s4 = getTokenGroupAt(index1).getTokenAt(index2);
                if (s3.equals(s4)) {
                    found2 = true;
                }
            }
        }
        if (!found) {
            if (!found2) {
                insertTokenGroup(tokenGroup);
            }
        } else {
            if (!found2) {
                getTokenGroupAt(index).insertToken(token);
            }
        }
    }

    /**
	 * Removes a token from the ACIDE - A Configurable IDE lexicon configuration
	 * token manager object list.
	 * 
	 * @param token
	 *            token to remove.
	 */
    public void removeTokenAs(String token) {
        for (int index1 = 0; index1 < getSize(); index1++) {
            for (int index2 = 0; index2 < getTokenGroupAt(index1).getSize(); index2++) {
                String tokenFromList = getTokenGroupAt(index1).getTokenAt(index2);
                if (token.equals(tokenFromList)) {
                    getTokenGroupAt(index1).removeTokenAt(index2);
                }
            }
            if (getTokenGroupAt(index1).getSize() == 0) {
                removeTokenGroupAt(index1);
            }
        }
    }

    /**
	 * Returns a token from the ACIDE - A Configurable IDE lexicon configuration
	 * token manager object list.
	 * 
	 * @param token
	 *            token name.
	 * @return a token from the list.
	 */
    public AcideLexiconTokenGroup getTokenAt(String token) {
        for (int index1 = 0; index1 < getSize(); index1++) for (int index2 = 0; index2 < getTokenGroupAt(index1).getSize(); index2++) {
            String tokenFromList = getTokenGroupAt(index1).getTokenAt(index2);
            if (token.equals(tokenFromList)) return getTokenGroupAt(index1);
        }
        return null;
    }

    /**
	 * Removes a token group from the ACIDE - A Configurable IDE lexicon
	 * configuration token group manager object list.
	 * 
	 * @param index
	 *            position to remove.
	 */
    public void removeTokenGroupAt(int index) {
        _list.removeAt(index);
    }

    /**
	 * Returns the ACIDE - A Configurable IDE lexicon configuration token
	 * manager object list.
	 * 
	 * @return the ACIDE - A Configurable IDE lexicon configuration token
	 *         manager object list.
	 */
    public ObjectList getList() {
        return _list;
    }

    /**
	 * Sets a new value to the ACIDE - A Configurable IDE lexicon configuration
	 * token manager object list.
	 * 
	 * @param list
	 *            new value to set.
	 */
    public void setList(ObjectList list) {
        _list = list;
    }
}
