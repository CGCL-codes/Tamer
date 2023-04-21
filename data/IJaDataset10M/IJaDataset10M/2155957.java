package com.meschbach.psi.example.dprimecore.prime.immed;

import com.meschbach.psi.example.dprimecore.prime.PrimalityCheck;
import java.math.BigDecimal;

/**
 * An <code>ImmediatePrimalityCheck</code> is a facade to perform a primality
 * check immediately.
 * 
 * @author "Mark Eschbach" &lt;meschbach@gmail.com&gt;
 */
public class ImmediatePrimalityCheck {

    ImmediateWorkFactory factory;

    public ImmediatePrimalityCheck() {
        factory = new ImmediateWorkFactory();
    }

    public boolean isPrime(BigDecimal value) {
        PrimalityCheck<ImmediateWorkUnit, PrimeState> pc = new PrimalityCheck<ImmediateWorkUnit, PrimeState>(factory, value);
        pc.setPrimeState(new PrimeState());
        ImmediateWorkUnit iwu;
        do {
            iwu = pc.nextOperation();
            if (!iwu.isDone()) {
                iwu.doWork();
            }
        } while (!iwu.isDone());
        return iwu.isPrime();
    }
}
