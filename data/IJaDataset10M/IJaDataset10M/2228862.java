package xbird.xquery.parser;

import xbird.xquery.XQueryException;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"> </DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class SyntaxError extends XQueryException {

    private static final long serialVersionUID = 1299833687278209888L;

    protected int line = -1;

    protected int column = -1;

    public SyntaxError(String errCode) {
        super(errCode);
    }

    public SyntaxError(String errCode, Throwable cause) {
        super(errCode, cause);
    }

    public SyntaxError(String errCode, String msg, Throwable cause) {
        super(errCode, msg, cause);
    }

    public SyntaxError(String errCode, int line, int column) {
        super(errCode);
        this.line = line;
        this.column = column;
    }

    @Override
    public String getMessage() {
        if (this.line == -1) {
            return super.getMessage();
        } else {
            return super.getMessage() + " - Encountered line " + this.line + ", column " + this.column;
        }
    }
}
