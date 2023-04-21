package picoevo.core.evolution;

/**
 * This operator should enclose all commands to perform an evolution 
 * for a given object. Usually such an operator should be defined for
 * a World or a Population and perform *one* single evolution step (in
 * the case of generational-like evolution). This operator may be useful
 * only in the scope of synchronous evolution (i.e. one full step after
 * another).
 */
public abstract class EvolveOperator extends Operator {

    public EvolveOperator(String __name) {
        super(__name);
    }

    /** evolve one step for the given object (usually : a Population) */
    public abstract void evolve(Object __o);
}
