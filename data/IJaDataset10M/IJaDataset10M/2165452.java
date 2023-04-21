package com.meschbach.psi.example.dprimecore.prime.immed;

import com.meschbach.psi.example.dprimecore.prime.CompletionClosure;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author "Mark Eschbach" &lt;meschbach@gmail.com&gt;
 */
public class DivideWork extends ImmediateWorkUnit {

    CompletionClosure closure;

    BigDecimal divisor;

    BigDecimal dividend;

    public DivideWork(CompletionClosure closure, BigDecimal divisor, BigDecimal dividend) {
        this.closure = closure;
        this.divisor = divisor;
        this.dividend = dividend;
    }

    @Override
    public void doWork() {
        BigDecimal result[] = dividend.divideAndRemainder(divisor, new MathContext(32, RoundingMode.UP));
        closure.completedResult(result[1]);
    }
}
