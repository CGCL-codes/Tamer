package org.vikamine.kernel.formula.exception;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import org.vikamine.kernel.formula.ParserElementBuilder;

/**
 * {@link AmbiguousTokenException} is an exception when a parsed token is
 * ambiguous.
 * 
 * @author Tobias Vogele
 */
public class AmbiguousTokenException extends ParseException {

    private static final long serialVersionUID = -960431827244204847L;

    protected String token;

    protected List possibleBuilder = new LinkedList();

    public AmbiguousTokenException(String s, int errorOffset) {
        super(s, errorOffset);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List getPossibleBuilder() {
        return possibleBuilder;
    }

    public void setPossibleBuilder(List possibleBuilder) {
        this.possibleBuilder = possibleBuilder;
    }

    public AmbiguousTokenException(String s, int errorOffset, String token, List possibleBuilder) {
        super(s, errorOffset);
        this.token = token;
        this.possibleBuilder = possibleBuilder;
    }

    public AmbiguousTokenException(String s, int errorOffset, String token) {
        super(s, errorOffset);
        this.token = token;
    }

    public void addPossibleBuilder(ParserElementBuilder builder) {
        getPossibleBuilder().add(builder);
    }
}
