package org.norecess.citkit.lir;

import org.norecess.citkit.visitors.LIRControlDestinationVisitor;
import org.norecess.citkit.visitors.LIRInstructionVisitor;

public class LIRLabel implements LIRInstruction, LIRControlDestination {

    private final String myLabel;

    public LIRLabel(String label) {
        myLabel = label;
    }

    public String getLabel() {
        return myLabel;
    }

    @Override
    public boolean equals(Object other) {
        return (other != null) && getClass().equals(other.getClass()) && equals((LIRLabel) other);
    }

    private boolean equals(LIRLabel other) {
        return myLabel.equals(other.myLabel);
    }

    @Override
    public int hashCode() {
        return myLabel.hashCode();
    }

    @Override
    public String toString() {
        return myLabel;
    }

    public <T> T accept(LIRInstructionVisitor<T> visitor) {
        return visitor.visitLabel(this);
    }

    public <T> T accept(LIRControlDestinationVisitor<T> visitor) {
        return visitor.visitLabel(this);
    }
}
