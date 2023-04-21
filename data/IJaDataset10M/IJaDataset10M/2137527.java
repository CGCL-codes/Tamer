package org.rubypeople.rdt.refactoring.core;

public class SelectionInformation {

    int startOfSelection;

    int endOfSelection;

    String source;

    public SelectionInformation(int startOfSelection, int endOfSelection, String source) {
        this.startOfSelection = startOfSelection;
        this.endOfSelection = endOfSelection;
        this.source = source;
    }

    public int getEndOfSelection() {
        return endOfSelection;
    }

    public String getSource() {
        return source;
    }

    public int getStartOfSelection() {
        return startOfSelection;
    }
}
