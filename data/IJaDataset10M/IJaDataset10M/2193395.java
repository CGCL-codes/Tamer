package net.sourceforge.javautil.grammar.impl.parser;

import net.sourceforge.javautil.common.CollectionUtil;
import net.sourceforge.javautil.common.StringUtil;
import net.sourceforge.javautil.grammar.impl.parser.patterns.IGrammarLexerDefinition;
import net.sourceforge.javautil.grammar.impl.parser.patterns.IGrammarPartToken;
import net.sourceforge.javautil.grammar.impl.parser.patterns.IGrammarTokenDefinition;
import net.sourceforge.javautil.lexer.LexerInputPosition;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class GrammarToken implements CharSequence, Comparable<CharSequence> {

    protected final IGrammarLexerDefinition definition;

    protected final String[] definitionTypes;

    protected final char[] characters;

    protected final LexerInputPosition location;

    public GrammarToken(IGrammarLexerDefinition definition, char[] token, LexerInputPosition location, String... definitionTypes) {
        this.characters = token;
        this.location = location;
        this.definitionTypes = definitionTypes;
        this.definition = definition;
    }

    public IGrammarLexerDefinition getPrimaryLexerDefinition() {
        return this.definition;
    }

    public int getTypeCount() {
        return definitionTypes.length;
    }

    public String getType(int idx) {
        return definitionTypes[idx];
    }

    public boolean isType(String type) {
        return CollectionUtil.contains(definitionTypes, type);
    }

    public boolean isOneOf(String... types) {
        for (String type : types) {
            if (this.isType(type)) return true;
        }
        return false;
    }

    public LexerInputPosition getLocation() {
        return location;
    }

    public char[] getCharacters() {
        return characters;
    }

    public char charAt(int index) {
        return characters[index];
    }

    public int length() {
        return characters.length;
    }

    public CharSequence subSequence(int start, int end) {
        return end - start == 0 ? "" : new String(characters, start, end - start);
    }

    public String toString() {
        return "[" + StringUtil.join(definitionTypes, ',') + "]: " + location + ": " + new String(characters);
    }

    public int compareTo(CharSequence o) {
        return StringUtil.compare(this, o);
    }
}
