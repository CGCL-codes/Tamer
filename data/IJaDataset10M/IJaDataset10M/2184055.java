package org.servingMathematics.mathematics.interfaces.numbers;

import org.servingMathematics.mathematics.interfaces.MathObject;

public interface MathNumber extends MathObject {

    public boolean equalsZero();

    public MathNumber getComplexValue();

    public Real getRealPart();

    public Real getImaginaryPart();
}
