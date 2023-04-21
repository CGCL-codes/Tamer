package ec.gp.ge;

import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;
import ec.coevolve.*;
import ec.util.*;

/** 
    GEProblem is a special replacement for Problem which performs GE mapping.  You do not subclass
    from GEProblem.  Rather, create a GPProblem subclass and set it to be the 'problem' parameter of the GEProblem.
    The GEProblem will convert the GEIndividual into a GPIndividual, then pass this GPIndividual to the GPProblem
    to be evaluated.
        
    <p>The procedure is as follows.  Let's say your GPProblem is the Artificial Ant problem.  Instead of saying...
        
    <p><tt>eval.problem = ec.app.ant.Ant<br>
    eval.problem = ec.app.ant.Ant<br>
    eval.problem.data = ec.app.ant.AntData<br>
    eval.problem.moves = 400<br>
    eval.problem.file = santafe.trl
    </tt>

    <p>... you instead make your problem a GEProblem like this:

    <p><tt>eval.problem = ec.gp.ge.GEProblem</tt>
        
    <p>... and then you hang the Ant problem, and all its subsidiary data, as the 'problem' parameter from the GEProblem like so:

    <p><tt>eval.problem.problem = ec.app.ant.Ant<br>
    eval.problem.problem.data = ec.app.ant.AntData<br>
    eval.problem.problem.moves = 400<br>
    eval.problem.problem.file = santafe.trl
    </tt>

    <p>Everything else should be handled for you.  GEProblem is also compatible with the MasterProblem procedure
    for distributed evaluation, and is also both a SimpleProblemForm and a GroupedProblemForm.  We've got you covered.

    <p><b>Parameters</b><br>
    <table>
    <tr><td valign=top><i>base</i>.<tt>problem</tt><br>
    <font size=-1>classname, inherits from GPProblem</font></td>
    <td valign=top>(The GPProblem which actually performs the evaluation of the mapped GPIndividual)</td></tr>
    </table>
*/
public class GEProblem extends Problem implements SimpleProblemForm, GroupedProblemForm {

    public static final String P_PROBLEM = "problem";

    public GPProblem problem;

    public void setup(EvolutionState state, Parameter base) {
        problem = (GPProblem) state.parameters.getInstanceForParameter(base.push(P_PROBLEM), null, GPProblem.class);
        problem.setup(state, base.push(P_PROBLEM));
    }

    public Object clone() {
        GEProblem other = (GEProblem) (super.clone());
        other.problem = (GPProblem) (problem.clone());
        return other;
    }

    public void prepareToEvaluate(final EvolutionState state, final int threadnum) {
        problem.prepareToEvaluate(state, threadnum);
    }

    public void finishEvaluating(final EvolutionState state, final int threadnum) {
        problem.finishEvaluating(state, threadnum);
    }

    public void initializeContacts(EvolutionState state) {
        problem.initializeContacts(state);
    }

    public void reinitializeContacts(EvolutionState state) {
        problem.reinitializeContacts(state);
    }

    public void closeContacts(EvolutionState state, int result) {
        problem.closeContacts(state, result);
    }

    public boolean canEvaluate() {
        return problem.canEvaluate();
    }

    public void preprocessPopulation(final EvolutionState state, Population pop, final boolean countVictoriesOnly) {
        if (!(problem instanceof GroupedProblemForm)) state.output.fatal("GEProblem's underlying Problem is not a GroupedProblemForm");
        ((GroupedProblemForm) problem).preprocessPopulation(state, pop, countVictoriesOnly);
    }

    public void postprocessPopulation(final EvolutionState state, Population pop, final boolean countVictoriesOnly) {
        ((GroupedProblemForm) problem).preprocessPopulation(state, pop, countVictoriesOnly);
    }

    /** Default version assumes that every individual is a GEIndividual.
        The underlying problem.evaluate() must be prepared for the possibility that some
        GPIndividuals handed it are in fact null, meaning that they couldn't be extracted
        from the GEIndividual string.  You should assign them bad fitness in some appropriate way.
    */
    public void evaluate(final EvolutionState state, final Individual[] ind, final boolean[] updateFitness, final boolean countVictoriesOnly, final int[] subpops, final int threadnum) {
        GPIndividual[] gpi = new GPIndividual[ind.length];
        for (int i = 0; i < gpi.length; i++) {
            GEIndividual indiv = (GEIndividual) ind[i];
            GESpecies species = (GESpecies) (ind[i].species);
            gpi[i] = species.map(state, indiv, threadnum);
        }
        ((GroupedProblemForm) problem).evaluate(state, gpi, updateFitness, countVictoriesOnly, subpops, threadnum);
        for (int i = 0; i < gpi.length; i++) {
            ind[i].fitness = gpi[i].fitness;
            ind[i].evaluated = gpi[i].evaluated;
        }
    }

    public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum) {
        if (!(problem instanceof SimpleProblemForm)) state.output.fatal("GEProblem's underlying Problem is not a SimpleProblemForm");
        GEIndividual indiv = (GEIndividual) ind;
        GESpecies species = (GESpecies) (ind.species);
        GPIndividual gpi = species.map(state, indiv, threadnum);
        if (gpi == null) {
            KozaFitness fitness = (KozaFitness) (ind.fitness);
            fitness.setStandardizedFitness(state, Float.MAX_VALUE);
        } else {
            ((SimpleProblemForm) problem).evaluate(state, gpi, subpopulation, threadnum);
            ind.fitness = gpi.fitness;
            ind.evaluated = gpi.evaluated;
        }
    }

    public void describe(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum, final int log) {
        GEIndividual indiv = (GEIndividual) ind;
        GESpecies species = (GESpecies) (ind.species);
        GPIndividual gpi = species.map(state, indiv, threadnum);
        if (gpi != null) {
            problem.describe(state, gpi, subpopulation, threadnum, log);
            ind.fitness = gpi.fitness;
            ind.evaluated = gpi.evaluated;
        }
    }
}
