package deesel.parser.exceptions;

import deesel.parser.ASTNode;
import deesel.parser.ParseException;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class SymbolBasedParseException extends ParseException {

    private static Logger log = Logger.getLogger(SymbolBasedParseException.class);

    public SymbolBasedParseException(Exception e, ASTNode node, String symbol, String message) {
        super(e, node, message + "\n     symbol: " + symbol + "\n     location: " + node.getFirstToken());
    }

    public SymbolBasedParseException(ASTNode node, String symbol, String message) {
        super(node, message + "\n     symbol: " + symbol + "\n     location: " + node.getFirstToken());
    }
}
