package org.henkels.drawcode.editors.nsdiagram.model.nodes;

import java.util.ArrayList;
import java.util.List;
import org.henkels.drawcode.codegeneration.lexico.Lexico;
import org.henkels.drawcode.codegeneration.lexico.Token;

public abstract class BaseElementsTokenizer {

    protected List<String> getElementTokenList(String input) {
        List<String> ret = new ArrayList<String>();
        try {
            Lexico lexico = new Lexico();
            lexico.setInput(input);
            Token token = null;
            while ((token = lexico.nextToken()) != null) {
                ret.add(token.getLexeme());
            }
            return ret;
        } catch (Exception e) {
            ret.clear();
            ret.add(input);
            return ret;
        }
    }
}
