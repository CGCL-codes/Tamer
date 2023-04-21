package JOptFrame;

import java.util.Vector;
import JOptFrame.Util.Pair;

public class Heuristic<R extends Representation, M extends Memory> implements Runnable {

    private final double INFINITE = 100000000;

    protected Solution<R> log_solution;

    protected Population<R> log_solution_set;

    protected long log_timelimit;

    protected double log_efv;

    public static final int SAFE_SEARCH_TOLERANCE = 2;

    public Heuristic() {
        log_solution = null;
    }

    /**
     * safeSearch é um método Solução -> Solução, parametrizado pelo tempo
     * máximo de execução desejado para a heurística. Para garantir que a
     * heurística terminará no tempo limite desejado o método é iniciado em uma
     * thread que é encerrada logo após esse tempo.\n Caso o método termine
     * normalmente sua solução é retornada; caso contrário uma solução NULL é
     * retornada.
     */
    public void run() {
    }

    public Solution<R> search(final Solution<R> s, double timelimit, double target_f) {
        Solution<R> s2 = s.clone();
        exec(s2, timelimit, target_f);
        return s2;
    }

    public Solution<R> search(final Solution<R> s, double timelimit) {
        return search(s, timelimit, 0);
    }

    public Solution<R> search(final Solution<R> s) {
        return search(s, INFINITE, 0);
    }

    public Pair<Solution<R>, Evaluation<M>> search(final Solution<R> s, final Evaluation<M> e, double timelimit, double target_f) {
        Solution<R> s2 = s.clone();
        Evaluation<M> e2 = e.clone();
        exec(s2, e2, timelimit, target_f);
        return new Pair<Solution<R>, Evaluation<M>>(s2, e2);
    }

    public Pair<Solution<R>, Evaluation<M>> search(final Solution<R> s, final Evaluation<M> e, double timelimit) {
        return search(s, e, timelimit, 0);
    }

    public Pair<Solution<R>, Evaluation<M>> search(final Solution<R> s, final Evaluation<M> e) {
        return search(s, e, INFINITE, 0);
    }

    public Population<R> search(final Population<R> p, double timelimit, double target_f) {
        Population<R> pop = new Population<R>();
        for (int i = 0; i < p.size(); i++) pop.push_back(p.at(i).clone());
        exec(pop, timelimit, target_f);
        return pop;
    }

    public Population<R> search(final Population<R> p, double timelimit) {
        return search(p, timelimit, 0);
    }

    public Population<R> search(final Population<R> p) {
        return search(p, INFINITE, 0);
    }

    public Pair<Population<R>, Vector<Evaluation<M>>> search(final Population<R> p, final Vector<Evaluation<M>> ev, double timelimit, double target_f) {
        Population<R> p2 = new Population<R>();
        for (int i = 0; i < p.size(); i++) p2.push_back(p.at(i).clone());
        Vector<Evaluation<M>> ev2 = new Vector<Evaluation<M>>();
        for (int i = 0; i < p.size(); i++) ev2.add(ev.elementAt(i).clone());
        exec(p2, ev2, timelimit, target_f);
        return new Pair<Population<R>, Vector<Evaluation<M>>>(p2, ev2);
    }

    public Pair<Population<R>, Vector<Evaluation<M>>> search(final Population<R> p, final Vector<Evaluation<M>> ev, double timelimit) {
        return search(p, ev, timelimit, 0);
    }

    public Pair<Population<R>, Vector<Evaluation<M>>> search(final Population<R> p, final Vector<Evaluation<M>> ev) {
        return search(p, ev, INFINITE, 0);
    }

    public void exec(Solution<R> s, double timelimit, double target_f) {
        Population<R> p = new Population<R>();
        p.push_back(s);
        exec(p, timelimit, target_f);
    }

    public void exec(Solution<R> s, Evaluation<M> e, double timelimit, double target_f) {
        Population<R> p = new Population<R>();
        Vector<Evaluation<M>> ev = new Vector<Evaluation<M>>();
        p.push_back(s);
        ev.add(e);
        exec(p, ev, timelimit, target_f);
    }

    public void exec(Population<R> p, double timelimit, double target_f) {
        Solution<R> s = (p.at(0));
        exec(s, timelimit, target_f);
    }

    public void exec(Population<R> p, Vector<Evaluation<M>> ev, double timelimit, double target_f) {
        Solution<R> s = (p.at(0));
        Evaluation<M> e = ev.elementAt(0);
        exec(s, e, timelimit, target_f);
    }
}
