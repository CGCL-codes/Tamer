package net.sf.jturingmachine.common.graph;

import net.sf.jturingmachine.common.graph.Transition;

public class TransducerTransition extends Transition {

    private char outputCharacter;

    TransducerTransition() {
        super();
    }

    public void setOutputCharacter(char outputCharacter) {
        this.outputCharacter = outputCharacter;
    }

    public char getOutputCharacter() {
        return this.outputCharacter;
    }
}
