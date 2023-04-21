package net.sf.lunareclipse.internal.ui.text;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class LuaCodeScanner extends AbstractScriptScanner {

    private static String[] fgKeywords = { "and", "break", "do", "else", "elseif", "end", "for", "function", "if", "in", "local", "not", "or", "repeat", "then", "until", "while" };

    private static String[] additionalKeywords = { "assert", "collectgarbage", "dofile", "error", "gcinfo", "getglobals", "getmetatable", "getmode", "ipairs", "loadfile", "loadstring", "newproxy", "next", "pairs", "pcall", "print", "rawget", "rawset", "require", "setglobals", "setmetatable", "setmode", "tonumber", "tostring", "type", "unpack", "xpcall" };

    private static String[] fgPseudoVariables = { "true", "false", "self", "nil" };

    private static String fgReturnKeyword = "return";

    private static String fgTokenProperties[] = new String[] { LuaColorConstants.LUA_SINGLE_LINE_COMMENT, LuaColorConstants.LUA_DEFAULT, LuaColorConstants.LUA_KEYWORD, LuaColorConstants.LUA_KEYWORD_RETURN, LuaColorConstants.LUA_NUMBER, LuaColorConstants.LUA_VARIABLE, LuaColorConstants.LUA_CONSTANT_VARIABLE, LuaColorConstants.LUA_INSTANCE_VARIABLE, LuaColorConstants.LUA_PREDEFINED_VARIABLE, LuaColorConstants.LUA_PSEUDO_VARIABLE, LuaColorConstants.LUA_SYMBOLS };

    public LuaCodeScanner(IColorManager manager, IPreferenceStore store) {
        super(manager, store);
        initialize();
    }

    protected String[] getTokenProperties() {
        return fgTokenProperties;
    }

    protected List<IRule> createRules() {
        List<IRule> rules = new ArrayList<IRule>();
        IToken keyword = getToken(LuaColorConstants.LUA_KEYWORD);
        IToken keywordReturn = getToken(LuaColorConstants.LUA_KEYWORD_RETURN);
        IToken comment = getToken(LuaColorConstants.LUA_SINGLE_LINE_COMMENT);
        IToken other = getToken(LuaColorConstants.LUA_DEFAULT);
        IToken number = getToken(LuaColorConstants.LUA_NUMBER);
        IToken pseudoVariable = getToken(LuaColorConstants.LUA_PSEUDO_VARIABLE);
        rules.add(new MultiLineRule("--[[", "]]", comment));
        rules.add(new EndOfLineRule("--", comment));
        rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));
        rules.add(new NumberRule(number));
        WordRule wordRule = new WordRule(new LuaWordDetector(), other);
        for (int i = 0; i < fgKeywords.length; i++) {
            wordRule.addWord(fgKeywords[i], keyword);
        }
        for (int i = 0; i < additionalKeywords.length; i++) {
            wordRule.addWord(additionalKeywords[i], keyword);
        }
        for (int i = 0; i < fgPseudoVariables.length; i++) {
            wordRule.addWord(fgPseudoVariables[i], pseudoVariable);
        }
        wordRule.addWord(fgReturnKeyword, keywordReturn);
        rules.add(wordRule);
        setDefaultReturnToken(other);
        return rules;
    }
}
