package org.tex4java.tex.scanner.tokens;

import org.tex4java.Manager;

/**
 * ParameterToken = catcode 6.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.1.1.1 $
 */
public class ParameterToken extends Token {

    /**
   * Creates and returns a ParameterToken (catcode 6).
   * 
   * @param categories Categories
   * @param firstChar Read character.
   * @param input The InputHandler
   * @return ParameterToken
   */
    public Token create(Manager manager, char firstChar) {
        ParameterToken t = new ParameterToken();
        t.character = firstChar;
        return t;
    }

    /**
   * Returns the Token as a String (for debugging).
   * 
   * @return String
   */
    public String toString() {
        return "ParameterToken.";
    }

    /**
   * Return a Token as a String, short version (for debugging).
   * 
   * @return String
   */
    public String toStringShort() {
        return "##";
    }
}
