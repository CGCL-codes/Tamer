package net.gcalc.juu;

import java.util.Deque;
import net.gcalc.juu.environment.Environment;
import net.gcalc.juu.parser.Node;

class Disjunction extends Folder<Boolean> {

    Disjunction(Node node) {
        super(node);
        assert (node.getId() == JJTDISJUNCTION);
    }

    protected Environment start(Deque<Call> stack, Environment env) {
        value = false;
        return env;
    }

    protected Boolean fold(Object data) {
        if (data instanceof Boolean) {
            return value || (Boolean) data;
        }
        throw new RuntimeException();
    }
}
