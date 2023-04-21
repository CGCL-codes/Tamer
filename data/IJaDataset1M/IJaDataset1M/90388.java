package edumips64.core;

import java.util.*;

/**
 *
 * @author mancausoft, Vanni
 */
public class ParserMultiWarningException extends ParserMultiException {

    public void add(String description, int row, int column, String line) {
        ParserWarning tmp = new ParserWarning(description, row, column, line);
        tmp.setError(false);
        exception.add(tmp);
    }
}
