package fitlibrary.parser;

import fitlibrary.exception.parse.CouldNotParseException;
import fitlibrary.typed.Typed;

public class FailingDelegateParser extends DelegateParser {

    public FailingDelegateParser(Class<?> type) {
        super(type);
    }

    @Override
    public Object parse(String s, Typed typed) throws Exception {
        throw new CouldNotParseException(type, s);
    }
}
