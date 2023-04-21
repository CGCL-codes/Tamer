package it.freax.fpm.core.solver.core;

import it.freax.fpm.core.solver.specs.Spec;

/**
 * This is the abstract Solver for the freax package manager system.
 * This allows other installer-localized Solvers to have a common interface
 * to talk to for the bad job of solving the dlhell.
 * 
 * @author kLeZ-hAcK
 */
public abstract class DependencySolver implements Runnable {

    private final Spec spec;

    public DependencySolver(Spec spec) {
        this.spec = spec;
    }

    public Spec getSpec() {
        return spec;
    }

    public abstract void Solve();
}
