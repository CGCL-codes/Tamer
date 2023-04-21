package org.unitils.dbmaintainer.script;

/**
 * A state of a parser that can handle a character and knows when the state ends and is transfered to another state.
 * For example, an in-block-comment state knows when the block-comment ends and then transfers control to the initial state.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public interface ParsingState {

    /**
     * Handles the next character in the script.
     *
     * @param previousChar The previous char, 0 if none
     * @param currentChar  The current char
     * @param nextChar     The next char, 0 if none
     * @param statement    The statement that is built, not null
     * @return The next parsing state, null if the end of the statement is reached
     */
    ParsingState handleNextChar(char previousChar, char currentChar, char nextChar, StringBuilder statement);
}
