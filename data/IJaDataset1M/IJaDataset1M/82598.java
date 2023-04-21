package jlibs.nblr.editor.debug;

import jlibs.nbp.Chars;
import jlibs.nbp.NBParser;

public abstract class DebuggableNBParser extends NBParser {

    protected DebuggableNBParser(Debugger debugger, int maxLookAhead, int startingRule) {
        super(maxLookAhead, startingRule);
        debugger.currentNode(stack[free - 2], stack[free - 1]);
    }

    public int[] getStack() {
        return stack;
    }

    public int free() {
        return free;
    }

    public Chars getBuffer() {
        return buffer;
    }
}
