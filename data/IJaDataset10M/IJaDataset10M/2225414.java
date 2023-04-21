package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.GUtilities.ReadData;

public class Binomial_BetaBinomial_Example_NKC_Bimode {

    public static void main(String[] argv) throws java.io.IOException {
        CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
        PRNG prng = new CollingsPRNG(a.registerPRNGState());
        UnnormalizedDensity target = new Binomial_BetaBinomial_Likelihood_Bimode();
        double[][] stateMat = ReadData.readDataAsColumnMatrix("NKC.states.in", 4, true, true);
        int numComponents = stateMat.length;
        MultiDoubleState state0 = new MultiDoubleState(numComponents);
        for (int i = 0; i < numComponents; i++) state0.add(stateMat[i]);
        double[][] var = ReadData.readDataAsColumnMatrix("NKC.variance.in", 4, true, true);
        double scaleFactor = 1.4 * Math.pow(numComponents, -2.0 / (4.0 + 4.0));
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) var[i][j] = var[i][j] * scaleFactor;
        HastingsCoupledProposal proposal = new NormalKernelProposal(var, prng);
        double[] minb = { 0.0, 0.0, 0.0, Double.NEGATIVE_INFINITY };
        double[] maxb = { 1.0, 1.0, 1.0, Double.POSITIVE_INFINITY };
        CustomHastingsCoupledSampler mcmc = mcmc = new BoundedHastingsCoupledSampler(state0, numComponents, target, proposal, prng, minb, maxb, true);
        ThinningProxyListener pL = new ThinningProxyListener(numComponents);
        MCMCListenerHandle pLh = mcmc.registerListener(pL);
        MCMCListenerWriter l1 = new StrippedListenerWriter("NKC.states.out");
        MCMCListenerHandle lh1 = pL.registerListener(l1);
        MCMCListenerWriter l2 = new DistanceWriter("NKC.distance.out");
        MCMCListenerHandle lh2 = mcmc.registerListener(l2);
        mcmc.iterate(Integer.parseInt(argv[0]));
        l1.flush();
        l1.close();
        l2.flush();
        l2.close();
    }
}
