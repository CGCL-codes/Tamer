package parser;

import base.CObject;
import java.util.LinkedList;

public class CInclude extends CObject {

    @Override
    public CObject Parse(LinkedList<String> Tokens) {
        Tokens.removeFirst();
        CParser.Process2(CParser.ParseFunction(Tokens).ToString());
        return null;
    }
}
