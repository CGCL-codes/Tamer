package net.javlov.policy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.javlov.Option;
import net.javlov.Policy;
import net.javlov.State;

/**
 * Current impl only works when all options are allowed for every state.
 * 
 * @author matthijs
 *
 */
public class FixedPolicy implements Policy {

    protected Map<State, double[]> probs, cumprobs;

    protected List<Option> options;

    public FixedPolicy(List<Option> options) {
        probs = new HashMap<State, double[]>();
        cumprobs = new HashMap<State, double[]>();
        this.options = options;
    }

    public void setOptionProbabilities(State s, double[] p) {
        if (p.length != options.size()) throw new IllegalArgumentException("Length of probability array must equal the number of possible options");
        probs.put(s, p);
        double[] cp = new double[p.length];
        cp[0] = p[0];
        for (int i = 1; i < p.length; i++) cp[i] = cp[i - 1] + p[i];
        cumprobs.put(s, cp);
    }

    /*** Interface overrides ***/
    @Override
    public <T> Option getOption(State<T> s) {
        double cp[] = cumprobs.get(s);
        if (cp == null || cp.length == 0) throw new UnknownStateException("No mapping for state " + s);
        double r = Math.random();
        int i;
        for (i = 0; i < cp.length; i++) if (r < cp[i]) break;
        return options.get(i);
    }

    @Override
    public <T> Option getOption(State<T> s, double[] qvalues) {
        return getOption(s);
    }

    @Override
    public <T> double[] getOptionProbabilities(State<T> s, double[] qvalues) {
        double p[] = probs.get(s);
        if (p == null || p.length == 0) throw new UnknownStateException("No mapping for state " + s);
        return p;
    }

    @Override
    public void init() {
        probs.clear();
        cumprobs.clear();
        for (Option o : options) o.init();
    }

    @Override
    public void reset() {
        probs.clear();
        cumprobs.clear();
        for (Option o : options) o.reset();
    }

    public static class UnknownStateException extends RuntimeException {

        private static final long serialVersionUID = 7020296302777564703L;

        public UnknownStateException() {
        }

        public UnknownStateException(String msg) {
            super(msg);
        }
    }
}
