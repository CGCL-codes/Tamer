package com.aptana.ide.lexer;

import java.util.Hashtable;
import com.aptana.ide.regex.IRegexRunner;

/**
 * @author Kevin Lindsey
 */
public class RegexTokenList extends TokenList implements IRegexTokenList {

    private IRegexRunner _currentMatcher;

    private Hashtable _matchersByName;

    /**
	 * RegexTokenList
	 * 
	 * @param language
	 */
    public RegexTokenList(String language) {
        super(language);
        this._matchersByName = new Hashtable();
    }

    /**
	 * RegexTokenList
	 */
    public RegexTokenList() {
        this._matchersByName = new Hashtable();
    }

    /**
	 * @see com.aptana.ide.lexer.ITokenList#setCurrentGroup(java.lang.String)
	 */
    public void setCurrentGroup(String groupName) throws LexerException {
        super.setCurrentGroup(groupName);
        if (this._matchersByName.containsKey(groupName) == false) {
            throw new LexerException(Messages.TokenList_Unrecognized_Group_Name + groupName, null);
        }
        this._currentMatcher = (IRegexRunner) this._matchersByName.get(groupName);
    }

    /**
	 * @see com.aptana.ide.lexer.ITokenList#setCurrentGroup(int)
	 */
    public void setCurrentGroup(int index) {
        super.setCurrentGroup(index);
        this._currentMatcher = (IRegexRunner) this._matchersByName.get(this.getCurrentGroup());
    }

    /**
	 * @see com.aptana.ide.lexer.IRegexTokenList#getCurrentMatcher()
	 */
    public IRegexRunner getCurrentMatcher() {
        return this._currentMatcher;
    }

    /**
	 * getGroupMatcher
	 * 
	 * @param groupName
	 * @return IRegexRunner
	 */
    protected IRegexRunner getGroupMatcher(String groupName) {
        IRegexRunner result = null;
        if (this._matchersByName.containsKey(groupName)) {
            result = (IRegexRunner) this._matchersByName.get(groupName);
        }
        return result;
    }

    /**
	 * setGroupMatcher
	 * 
	 * @param groupName
	 * @param matcher
	 */
    protected void setGroupMatcher(String groupName, IRegexRunner matcher) {
        this._matchersByName.put(groupName, matcher);
    }
}
