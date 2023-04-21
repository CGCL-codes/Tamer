package saf.fighter.fdl;

import org.antlr.runtime.tree.CommonTree;

public class InvalidAttributeMessage {

    private int lineNr;

    private String invalidAttributeName;

    private String possibleAttributes;

    private String message;

    public InvalidAttributeMessage(String message) {
        assert message != null : "Specifying a message is required!";
        this.message = message;
        this.possibleAttributes = "";
    }

    /** @require invalidAttribute != null */
    public InvalidAttributeMessage(CommonTree invalidAttribute, String possibleAttributes) {
        assert invalidAttribute != null : "Broken requirement!";
        this.lineNr = invalidAttribute.getLine();
        this.invalidAttributeName = invalidAttribute.getText();
        this.possibleAttributes = "\n" + possibleAttributes;
        this.message = this.invalidAttributeName + "(line " + this.lineNr + ") is invalid!";
    }

    public int getInvalidAttributeLineNr() {
        return lineNr;
    }

    public String getInvalidAttributeName() {
        return invalidAttributeName;
    }

    public String getPossibleAttributes() {
        return possibleAttributes;
    }

    public String toString() {
        return message + possibleAttributes;
    }

    public boolean equals(InvalidAttributeMessage other) {
        return this.toString() == other.toString();
    }
}
