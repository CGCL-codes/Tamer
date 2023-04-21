package org.norecess.citkit.tir.expressions;

import org.norecess.citkit.tir.ExpressionTIR;
import org.norecess.citkit.tir.IPosition;
import org.norecess.citkit.types.HobbesType;
import org.norecess.citkit.types.VoidType;
import org.norecess.citkit.visitors.ExpressionTIRVisitor;

/**
 * The TIR for a break statement.
 * 
 * @author Jeremy D. Frens
 * 
 */
public class BreakETIR implements ExpressionTIR {

    /**
     * The position of code that lead to the TIR.
     */
    private final IPosition myPosition;

    /**
     * Constructs a break statement. The existence of the object is enough so no
     * other information is required.
     * 
     * @param position
     *            the position of the break statement in the source code.
     */
    public BreakETIR(IPosition position) {
        myPosition = position;
    }

    /**
     * Basic constructor.
     * 
     */
    public BreakETIR() {
        this(null);
    }

    public HobbesType getType() {
        return VoidType.VOID_TYPE;
    }

    public <T> T accept(ExpressionTIRVisitor<T> visitor) {
        return visitor.visitBreakETIR(this);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BreakETIR);
    }

    @Override
    public int hashCode() {
        return 666;
    }

    @Override
    public String toString() {
        return "break";
    }

    /**
     * Retrieves the position of the code that generated the TIR.
     * 
     * @return the position of the code in the source code.
     */
    public IPosition getPosition() {
        return myPosition;
    }
}
