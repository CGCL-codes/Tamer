package adqlParser;

import adqlParser.query.ADQLObject;
import adqlParser.query.SearchHandler;

public class UCDSearchHandler implements SearchHandler {

    protected static UCDSearchHandler currentInstance = new UCDSearchHandler();

    public static final UCDSearchHandler getInstance() {
        return currentInstance;
    }

    public boolean match(ADQLObject obj) {
        return (obj instanceof UCDFunction && ((UCDFunction) obj).getColumn() == null);
    }
}
