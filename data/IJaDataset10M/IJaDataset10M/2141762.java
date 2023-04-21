package ch.ethz.mxquery.update.store.llImpl;

import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.exceptions.MXQueryException;

/**
 * Dummy end element of the token list.
 * 
 * @author David Alexander Graf
 * 
 */
public class LLEndToken extends LLToken {

    public LLEndToken() {
        this.prev = null;
        token = Token.END_SEQUENCE_TOKEN;
    }

    public Identifier getId() {
        return null;
    }

    public void setId(Identifier id) {
    }

    public int getEventType() {
        return Type.END_SEQUENCE;
    }

    public void setPrev(LLToken prev) {
        this.prev = prev;
    }

    public void setNext(LLToken next) {
    }

    public void insertAfter(LLToken token) {
    }

    public void insertAfter(TokenList tokenList) {
    }

    public String toString() {
        return "</mxSequence>";
    }

    public LLToken copy() throws MXQueryException {
        return new LLEndToken();
    }
}
