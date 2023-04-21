package com.bluemarsh.jswat.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the use of a command alias.
 *
 * @author Nathan Fiedler
 */
public class AliasInputProcessor implements InputProcessor {

    @Override
    public boolean canProcess(String input, CommandParser parser) {
        int index = input.indexOf(' ');
        String alias;
        if (index > 0) {
            alias = input.substring(0, index);
        } else {
            alias = input;
        }
        return parser.getAlias(alias) != null;
    }

    @Override
    public boolean expandsInput() {
        return false;
    }

    @Override
    public List<String> process(String input, CommandParser parser) throws CommandException {
        List<String> output = new ArrayList<String>(1);
        int index = input.indexOf(' ');
        if (index > 0) {
            String alias = input.substring(0, index);
            alias = parser.getAlias(alias);
            alias += input.substring(index);
            output.add(alias);
        } else {
            String alias = parser.getAlias(input);
            output.add(alias);
        }
        return output;
    }
}
