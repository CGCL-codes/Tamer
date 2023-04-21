package net.sf.buildbox.parser.core;

import net.sf.buildbox.parser.model.TerminalSymbol;
import java.util.Map;

/**
 * @author Petr Kozelka
 */
class SpecialTerminalSymbol extends TerminalSymbol {

    SpecialTerminalSymbol(String name) {
        setName("<<" + name + ">>");
    }

    @Override
    public Map<String, String> matches(String input) {
        return null;
    }
}
