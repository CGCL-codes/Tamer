package ch.ethz.mxquery.model;

import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.util.ObjectObjectPair;
import ch.ethz.mxquery.util.Set;

public abstract class TokenBasedIterator extends Iterator {

    protected TokenInterface currentToken = null;

    /**
	 * The method to generate the expected token - needs to be overwritten by the implementations
	 * @throws MXQueryException
	 */
    protected abstract void init() throws MXQueryException;

    public TokenBasedIterator() {
        super(null, null);
    }

    public TokenBasedIterator(Context ctx, QueryLocation location) {
        super(ctx, location);
        currentToken = Token.START_SEQUENCE_TOKEN;
    }

    public TokenBasedIterator(Context ctx, int minExpected, XDMIterator[] subIters, QueryLocation location) throws IllegalArgumentException {
        super(ctx, minExpected, subIters, location);
        currentToken = Token.START_SEQUENCE_TOKEN;
    }

    public TokenBasedIterator(Context ctx, XDMIterator[] subIters, int expected, QueryLocation location) throws IllegalArgumentException {
        super(ctx, subIters, expected, location);
        currentToken = Token.START_SEQUENCE_TOKEN;
    }

    public TokenBasedIterator(Context ctx, XDMIterator[] subIters, QueryLocation location) {
        super(ctx, subIters, location);
        currentToken = Token.START_SEQUENCE_TOKEN;
    }

    /**
	 * generate next Token, needs to be implemented by the respective iterator
	 * @return The first call will give the type of first Token of the result, the second the second and so on. 
	 * When the last token has been produced, the type of an END_SEQUENCE token will be returned
	 */
    public TokenInterface next() throws MXQueryException {
        switch(called) {
            case 0:
                init();
                called++;
                break;
            case 1:
                this.close(true);
                currentToken = Token.END_SEQUENCE_TOKEN;
                called++;
                break;
        }
        return currentToken;
    }

    protected void resetImpl() throws MXQueryException {
        super.resetImpl();
        currentToken = Token.START_SEQUENCE_TOKEN;
    }
}
