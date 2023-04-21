package business.bibtex;

import business.bibtex.exceptions.BibtexException;

/**
 * Parses bibtex input to Publication objects. Consists of a static part for external calling
 * and a dynamic part for actual parsing.
 * @author pdressel
 *
 */
public class BibtexParser {

    private String fInputString;

    private Integer fParseIndex, fCurrentLine;

    private String fErrorMessage;

    public BibtexParser(String pInputString) {
        fInputString = pInputString.intern().trim();
        fParseIndex = 0;
        fCurrentLine = 1;
        fErrorMessage = "";
    }

    /**
   * Checks whether another reference needs to be parsed.
   * @return
   */
    public boolean hasNextPublication() {
        return (peek('@').trim().length() > 0);
    }

    /**
   * Returns the next publication object represented in the bibtex input.
   * Throws exception if none is found
   * @return A Publication object representing the parsed bibtex code
   * @throws BibtexException Thrown on parse errors.
   */
    public Publication getNextPublication() throws BibtexException {
        Publication result = new Publication();
        seekDeclaration();
        BibtexType bibtexType = extractBibtexType();
        if (bibtexType.getBibtexName().toLowerCase().equals("string")) {
            seek('}');
            return getNextPublication();
        }
        result.setBibtexType(bibtexType);
        fErrorMessage = "Searching for entry reference: ";
        String preceding = seek(',');
        if (preceding.contains("=") || preceding.trim().equals("")) {
            throw new BibtexException(fErrorMessage + "Found '" + preceding + "' instead of reference on line " + fCurrentLine + ".");
        }
        result.setReference(preceding.trim());
        result.setBibtexFields(extractBibtexFields());
        return result;
    }

    /**
   * Seeks for the beginning of a bibtex reference. Fails if something else is found first.
   * @throws BibtexException
   */
    protected void seekDeclaration() throws BibtexException {
        fErrorMessage = "Searching for entry declaration: ";
        String preceding = seek('@');
        if (preceding.length() > 0) {
            throw new BibtexException(fErrorMessage + "Found '" + preceding + "' instead of '@' on line " + fCurrentLine + ".");
        }
    }

    /** 
   * Returns the bibtex type at the current parse position.
   * @return
   * @throws BibtexException
   */
    protected BibtexType extractBibtexType() throws BibtexException {
        BibtexType result = new BibtexType();
        fErrorMessage = "Searching for bibtex type: ";
        String preceding = seek('{');
        if (preceding.length() == 0) {
            throw new BibtexException(fErrorMessage + "Name is empty on line " + fCurrentLine + ".");
        }
        result.setBibtexName(preceding.toUpperCase());
        return result;
    }

    /**
   * Returns the bibtex fields at the current parse position
   * @return
   * @throws BibtexException
   */
    protected BibtexFieldMap extractBibtexFields() throws BibtexException {
        BibtexFieldMap result = new BibtexFieldMap();
        fErrorMessage = "Searching for contents of bibtex entry: ";
        seek('\n');
        while (peek('}').length() != 1) {
            BibtexField bibtexField = new BibtexField();
            fErrorMessage = "Searching for key of bibtex entry: ";
            String key = seek('=');
            if (key.length() == 0) {
                throw new BibtexException(fErrorMessage + "Key is empty on line " + fCurrentLine + ".");
            }
            bibtexField.setKey(new BibtexKey(key));
            fErrorMessage = "Searching for value of bibtex entry: ";
            String value = seek(',').replaceAll("\n", "").trim();
            bibtexField.setValue(value);
            result.put(bibtexField);
        }
        if (result.size() == 0) {
            throw new BibtexException(fErrorMessage + "Reference contains no elements.");
        }
        seek('}');
        return result;
    }

    /**
   * Return the next character. Parse index is incremented. Whitespaces are trimmed.
   * @return
   * @throws BibtexException
   */
    protected Character getNextCharacter() throws BibtexException {
        try {
            Character next = new Character(fInputString.charAt(fParseIndex++));
            if (next.equals('\n')) {
                fCurrentLine++;
            }
            if (next.equals(' ') || next.equals('\t')) {
                Character followup = new Character(fInputString.charAt(fParseIndex));
                while (followup.equals(' ') || followup.equals('\t')) {
                    fParseIndex++;
                    followup = new Character(fInputString.charAt(fParseIndex));
                }
            }
            if (next.equals('\t')) {
                return ' ';
            }
            return next;
        } catch (IndexOutOfBoundsException e) {
            throw new BibtexException(fErrorMessage + "Unexpectedly reached end.");
        }
    }

    /**
   * Seeks for a specific character beginning at the current parse index.
   * @param target
   * @return The String preceding the sought charachter.
   * @throws BibtexException
   */
    protected String seek(Character target) throws BibtexException {
        StringBuffer result = new StringBuffer();
        boolean escaped = false;
        while (true) {
            Character current = getNextCharacter();
            if (current.equals('\\')) {
                escaped = true;
                result.append(current);
            } else if (!current.equals('{') && target.equals(',') && peek('}').length() == 1) {
                return result.toString().trim();
            } else if (current.equals(target) && !(target.equals('}') && escaped)) {
                return result.toString().trim();
            } else if (current.equals('{') && !escaped) {
                fErrorMessage = "Bracket count error: ";
                escaped = false;
                result.append(seek('}'));
            } else if (current.equals('"') && !escaped && !target.equals('}')) {
                fErrorMessage = "Quote count error: ";
                escaped = false;
                result.append(seek('"'));
            } else {
                escaped = false;
                result.append(current);
            }
        }
    }

    /**
   * The same as seek, but does not increment parse index.
   * @param target
   * @return
   */
    protected String peek(Character target) {
        Integer oldIndex = fParseIndex;
        Integer oldLine = fCurrentLine;
        String result = "";
        try {
            result = seek(target);
        } catch (BibtexException be) {
            return result;
        }
        fParseIndex = oldIndex;
        fCurrentLine = oldLine;
        return result + target;
    }

    /**
   * Returns everything from the current parse index.
   */
    public String toString() {
        return fInputString.substring(fParseIndex);
    }

    /**
   * Parses a String containing Bibtex. Returns a Vector of Publication objects representing
   * the publications in the parameter. If there are errors in the Bibtex syntax, a BibtexException
   * is thrown.
   * 
   * @author Philipp Dressel
   * @param pBibtexText A String containing bibtex entries.
   * @return A Vector of Publication objects representing the Entries in the bibtex String.
   * @throws BibtexException
   */
    public static Publications parse(String pInputString) throws BibtexException {
        BibtexParser parser = new BibtexParser(pInputString);
        Publications results = new Publications();
        while (parser.hasNextPublication()) {
            results.add(parser.getNextPublication());
        }
        return results;
    }
}
