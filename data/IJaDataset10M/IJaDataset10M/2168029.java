package org.datanucleus.store.rdbms.query;

import java.text.CharacterIterator;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.util.Imports;

/**
 * Parser for JPQL queries, extending the basic JDOQL parser for supporting the identifier ?1, ?2 syntax.
 * 
 * @version $Revision: 1.2 $
 */
public class JPQLParser extends Parser {

    /**
     * Constructor.
     * @param input Input string
     * @param imports Any imports
     */
    public JPQLParser(String input, Imports imports) {
        super(input, imports);
    }

    /**
     * Override the identifier parse to allow for ?1, ?2 syntax
     * @return The identifier
     */
    public String parseIdentifier() {
        String identifier = super.parseIdentifier();
        if (identifier != null) {
            return identifier;
        }
        skipWS();
        char c = ci.current();
        if (!Character.isJavaIdentifierStart(c) && !(c == '?')) {
            return null;
        }
        StringBuffer id = new StringBuffer();
        id.append(c);
        while (Character.isJavaIdentifierPart(c = ci.next())) {
            id.append(c);
        }
        return id.toString();
    }

    /**
     * Parse a Character literal.
     * @return the Character parsed. null if single quotes is found
     * @throws NucleusUserException if an invalid character is found or the CharacterIterator is finished
     */
    public Character parseCharacterLiteral() {
        skipWS();
        if (ci.current() != '\'') {
            return null;
        }
        char c = ci.next();
        if (c == CharacterIterator.DONE) {
            throw new NucleusUserException("Invalid character literal: " + input);
        }
        if (ci.next() != '\'') {
            throw new NucleusUserException("Invalid character literal: " + input);
        }
        ci.next();
        return new Character(c);
    }

    /**
     * Parse a String literal.
     * @return the String parsed. null if single quotes or double quotes is found
     * @throws NucleusUserException if an invalid character is found or the CharacterIterator is finished
     */
    public String parseStringLiteral() {
        skipWS();
        char quote = ci.current();
        if (quote != '"' && quote != '\'') {
            return null;
        }
        StringBuffer lit = new StringBuffer();
        char c;
        while ((c = ci.next()) != quote) {
            if (c == CharacterIterator.DONE) {
                throw new NucleusUserException("Invalid string literal: " + input);
            }
            lit.append(c);
        }
        ci.next();
        return lit.toString();
    }
}
