package org.omegahat.Simulation.RandomGenerators;

import java.util.Vector;

/**
 * creates instances of CollingsPRNG that are garanteed to be mutually
 * independent
 *
 * @author  Greg Warnes
 * @author  $Author: warnes $
 * @version $Revision: 16 $, $Date: 2002-11-02 18:10:22 -0500 (Sat, 02 Nov 2002) $
 *
 * <body>
 * 
 * Java implemenation of Bruce J. Colling's Compound Random Number Generator 
 * see Collings, Bruce J. (1987), "Compund Random Number Generators,"
 * JASA 82, 398 pp 525-527.
 * <p>
 * each individual random stream, random numbers are produced by
 * using one generator to randomly select one of NUMGEN generators to
 * produce the requested random number.  Each generator is multiplicative
 * congruential with the multiplicative constants selected randomly with
 * replacement from a pool of 112 "good" constants.  This (virtually)
 * assures that the random stream for each processor is independent.
 * Each stream should have a period of length 4.6E18.  
 *                                                                           
 ****************************************************************************
 * Modification Log:                                                         
 *                                                                           
 * January 4, 1997 -- Initial revision - Gregory R. Warnes                   
 *                                                                           
 * June 28, 1999   -- Initial Java Version - Gregory R Warnes                
 *                                                                           
 * $Log$
 * Revision 1.2  2002/11/02 23:10:22  warnes
 * - Removed dependency on VisualNumerics' JNL package, which is no longer available.
 * - Updated email address in files
 * - Recompiled everything
 * - Modified RunExamples.sh to tell the user which example is being executed.
 *
 * Revision 1.1.1.1  2001/04/04 17:16:34  warneg
 * Initial checkin of Hydra formatted for distribution.
 *
 *
 * Revision 1.2  2000/04/15 22:25:39  warnes
 * *** empty log message ***
 *
 * Revision 1.1.1.1  1999/09/21 17:10:05  warnes
 * initial
 *
 * Revision 1.2  1999/07/16 21:06:54  warnes
 * Renamed things yet again.  Added IDL and supporting java code for PRNGFActory PRNGState CollingsPRNGState.
 *
 * Revision 1.1  1999/07/16 14:25:06  warnes
 * Unified naming, shoved all java source into .jweb files
 *
 * Revision 1.2  1999/07/13 20:07:50  warnes
 * *** empty log message ***
 *
 * Revision 1.2  1999/07/07 13:49:40  warnes
 *  - Modified PRNGAdministrator class to return PRNGStates rather than
 *    returning an instance of the PRNG.
 *  - Created a Factory, PRNGFactory, that uses reflectance to instantiate
 *    the correct class using the PRNGState.
 *  - Modified PRNGState to include a method that returns the fully-qualified
 *    class name to make instantiation possible.
 *  - Changed (almost) all "private" declarations to "package".
 *
 * Revision 1.1  1999/07/02 21:34:58  warnes
 * *** empty log message ***
 *
 * Revision 1.4  1999/07/02 18:13:01  warnes
 * Renamed class Constants to class CollingsPRNGConstants
 *
 * Revision 1.3  1999/07/02 17:50:52  warnes
 * added semicolon to package declaration; began converting comments to javadoc
 *
 * Revision 1.2  1999/07/02 17:25:15  warnes
 * added package declaration to all files; added PseudoRandomUnivariate interface to return doubles
 *
 * Revision 1.1.1.1  1999/07/02 15:55:24  warnes
 * Random number generator administrator and utilities and variable generators that will go in to omegahat later
 *
 * Revision 1.1.1.1  1999/07/02 15:53:47  warnes
 * Random number generator administrator and utilities and variable generators that will go in to omegahat later
 *
 * Revision 1.2  1999/07/01 14:14:18  warnes
 * Modified to use LinearCongruential PRNG instead of
 * StatelessLinearCongruential PRNG for intialization.  This corrected
 * intializing all generators exactly the same!
 *
 * Revision 1.1  1999/07/01 13:42:29  warnes
 * Initial revision
 *
 * Revision 1.2  1999/06/30 20:35:07  warnes
 * *** empty log message ***
 *
 ****************************************************************************/
public class CollingsPRNGAdministrator implements PRNGAdministrator {

    /** 
     * Seed for the congruential generator used to pick the
     * multiplicative constants and to intialize the seeds for each
     * component generator.
     *
     * <body>
     * This seed must be in [1,2^32-1] 
     * </body>
     */
    private int Seed = 3141579;

    /** 
     * Multiplicative constant for the congruential generator used to pick the
     * multiplicative constants and to intialize the seeds for each
     * component generator.
     */
    private int Mult = CollingsPRNGConstants.pool[0];

    /** 
     * Modulus constant for the congruential generator used to pick the
     * multiplicative constants and to intialize the seeds for each
     * component generator.
     */
    private static int Mod = CollingsPRNGConstants.Modulus;

    /**
     * Number of generators that each CollingsPRNG will use 
     */
    private int NumGen = 64;

    /**
     * Multiplicative Congruential Generator used by the administrator
     * to select constants and seeds for the created CollingsPRNG's. 
     *
     * <body> 
     * This is NOT the seed used for the created CollingsPRNG's, rather
     *        it is used to select those seeds.
     * </body>
     */
    MultiplicativeCongruentialPRNG LC;

    /**
     * Default Class Constructor 
     */
    public CollingsPRNGAdministrator() {
        LC = new MultiplicativeCongruentialPRNG(Seed, Mult, Mod);
    }

    /** 
     * Class constructer that allows specification of the seed used
     * to intialize the state of the created PRNG's 
     * @param seed seed value used for initializing constructed generators
     */
    public CollingsPRNGAdministrator(int seed) {
        if (seed != 0) Seed = seed;
        LC = new MultiplicativeCongruentialPRNG(Seed, Mult, Mod);
    }

    /** 
     * Class constructer that allows specification of the seed used to
     * intialize the state of the created PRNG's, and the number of
     * component generators used by each CollingsPRNG created.
     *
     * @param seed seed value used for initializing constructed generators
     * @param numGen the number of component generators used by each
     * constructed generator 
     */
    public CollingsPRNGAdministrator(int seed, int numGen) {
        Seed = seed;
        NumGen = numGen;
        LC = new MultiplicativeCongruentialPRNG(Seed, Mult, Mod);
    }

    /** 
     * Class constructer that allows specification of the seed used to
     * intialize the state of the created PRNG's, the number of
     * component generators used by each CollingsPRNG created.
     *
     * @param seed seed value used for initializing constructed generators
     * @param numGen the number of component generators used by each
     * constructed generator 
     * @param mult multiplicative constant for PRNG used for initialization
     */
    public CollingsPRNGAdministrator(int seed, int numGen, int mult) {
        Seed = seed;
        NumGen = numGen;
        Mult = mult;
        LC = new MultiplicativeCongruentialPRNG(Seed, Mult, Mod);
    }

    public int getSeed() {
        return Seed;
    }

    public int getNumGen() {
        return NumGen;
    }

    public int getMult() {
        return Mult;
    }

    private PRNGState makeGeneratorState() {
        CollingsPRNGState newGen = new CollingsPRNGState();
        newGen.PRNGName = "org.omegahat.Simulation.RandomGenerators.CollingsPRNG";
        newGen.NumGen = NumGen;
        newGen.MixerSeed = LC.nextInt();
        newGen.MixerMult = CollingsPRNGConstants.pool[(int) (LC.nextDouble() * CollingsPRNGConstants.pool.length - 1)];
        newGen.ComponentSeed = new int[NumGen];
        newGen.ComponentMult = new int[NumGen];
        for (int n = 0; n < NumGen; n++) {
            newGen.ComponentSeed[n] = LC.nextInt();
            newGen.ComponentMult[n] = CollingsPRNGConstants.pool[(int) (LC.nextDouble() * CollingsPRNGConstants.pool.length - 1)];
        }
        return (PRNGState) newGen;
    }

    public PRNGState registerPRNGState() {
        CollingsPRNGState PRNG = (CollingsPRNGState) makeGeneratorState();
        if (listeners() != null) {
            notifyListeners(PRNG);
        }
        return PRNG;
    }

    public PRNGState registerPRNGState(Object ignored) {
        return registerPRNGState();
    }

    protected java.util.Vector listeners;

    public void notifyListeners(PRNGState state) {
        PRNGAdministratorEvent event = new PRNGAdministratorEvent(state, this);
        for (java.util.Enumeration e = listeners().elements(); e.hasMoreElements(); ) {
            ((PRNGAdministratorListener) e.nextElement()).PRNGStateRegistered(event);
        }
    }

    public int addListener(PRNGAdministratorListener l) {
        if (listeners() == null) {
            listeners(new java.util.Vector(1));
        }
        listeners().addElement(l);
        return (listeners().size());
    }

    public boolean removeListener(PRNGAdministratorListener l) {
        if (listeners() == null || listeners().contains(l)) return (false);
        listeners().remove(l);
        return (true);
    }

    public java.util.Vector listeners() {
        return (listeners);
    }

    public java.util.Vector listeners(Vector v) {
        listeners = v;
        return (listeners());
    }
}
