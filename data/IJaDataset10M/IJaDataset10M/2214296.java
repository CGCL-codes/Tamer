package cz.rsj.moll.ec.gp;

import cz.rsj.moll.ec.ExternalEvolve;

public class ExternalGPEvolve extends ExternalEvolve {

    private final StringEval eval;

    /** Restores an evolution from checkpoint 
	 */
    @Override
    protected void preRun() {
        ((ExternalStringEvalProblem) state.evaluator.p_problem).evaluator = eval;
    }

    /** Initialize a new evolution given the arguments 
	 */
    public ExternalGPEvolve(String[] args, StringEval eval) {
        super(args);
        this.eval = eval;
    }
}
