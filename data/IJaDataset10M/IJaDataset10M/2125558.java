package net.sf.beatrix.module.extractor.category;

public class InstructionCategory {

    private String name;

    private int numOccurences;

    public InstructionCategory(String name) {
        numOccurences = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void logOccurence() {
        numOccurences++;
    }

    public int getNumOccurences() {
        return numOccurences;
    }

    @Override
    public String toString() {
        return name + ": " + numOccurences;
    }
}
