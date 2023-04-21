package org.opt4j.ea;

import org.opt4j.config.annotations.Info;
import org.opt4j.ea.Nsga2.Tournament;

/**
 * Module for the {@link Nsga2} {@code Selector}.
 * 
 * 
 * @see "A Fast Elitist Non-Dominated Sorting Genetic Algorithm for
 *      Multi-Objective Optimization: NSGA-II, K. Deb, Samir Agrawal, Amrit
 *      Pratap, and T. Meyarivan, Parallel Problem Solving from Nature, 2000"
 * @author lukasiewycz
 * 
 */
@Info("A Fast Elitist Non-Dominated Sorting Genetic Algorithm for Multi-Objective Optimization")
public class Nsga2Module extends SelectorModule {

    @Info("The tournament value")
    protected int tournament = 0;

    /**
	 * Returns the tournament value.
	 * 
	 * @return the tournament value
	 */
    public int getTournament() {
        return tournament;
    }

    /**
	 * Sets the tournament value.
	 * 
	 * @param tournament
	 *            the tournament to set
	 */
    public void setTournament(int tournament) {
        this.tournament = tournament;
    }

    public void configure() {
        bindSelector(Nsga2.class);
        bindConstant(Tournament.class).to(tournament);
    }
}
