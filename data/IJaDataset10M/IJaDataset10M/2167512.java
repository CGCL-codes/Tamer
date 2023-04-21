package org.omegahat.Probability.Distributions;

import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Simulation.MCMC.*;

public interface Density {

    public double PDF(Object state);

    public double logPDF(Object state);
}
