package reconcile.weka.classifiers.bayes.net.search.local;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import reconcile.weka.classifiers.bayes.BayesNet;
import reconcile.weka.classifiers.bayes.net.ParentSet;
import reconcile.weka.core.Instances;
import reconcile.weka.core.Option;
import reconcile.weka.core.Utils;

/** RepeatedHillClimber searches for Bayesian network structures by
 * repeatedly generating a random network and apply hillclimber on it.
 * The best network found is returned.  
 * 
 * @author Remco Bouckaert (rrb@xm.co.nz)
 * Version: $Revision: 1.1 $
 */
public class RepeatedHillClimber extends HillClimber {

    /** number of runs **/
    int m_nRuns = 10;

    /** random number seed **/
    int m_nSeed = 1;

    /** random number generator **/
    Random m_random;

    /**
	* search determines the network structure/graph of the network
	* with the repeated hill climbing.
	**/
    protected void search(BayesNet bayesNet, Instances instances) throws Exception {
        m_random = new Random(getSeed());
        double fBestScore;
        double fCurrentScore = 0.0;
        for (int iAttribute = 0; iAttribute < instances.numAttributes(); iAttribute++) {
            fCurrentScore += calcNodeScore(iAttribute);
        }
        BayesNet bestBayesNet;
        fBestScore = fCurrentScore;
        bestBayesNet = new BayesNet();
        bestBayesNet.m_Instances = instances;
        bestBayesNet.initStructure();
        copyParentSets(bestBayesNet, bayesNet);
        for (int iRun = 0; iRun < m_nRuns; iRun++) {
            generateRandomNet(bayesNet, instances);
            super.search(bayesNet, instances);
            fCurrentScore = 0.0;
            for (int iAttribute = 0; iAttribute < instances.numAttributes(); iAttribute++) {
                fCurrentScore += calcNodeScore(iAttribute);
            }
            if (fCurrentScore > fBestScore) {
                fBestScore = fCurrentScore;
                copyParentSets(bestBayesNet, bayesNet);
            }
        }
        copyParentSets(bayesNet, bestBayesNet);
        bestBayesNet = null;
        m_Cache = null;
    }

    void generateRandomNet(BayesNet bayesNet, Instances instances) {
        int nNodes = instances.numAttributes();
        for (int iNode = 0; iNode < nNodes; iNode++) {
            ParentSet parentSet = bayesNet.getParentSet(iNode);
            while (parentSet.getNrOfParents() > 0) {
                parentSet.deleteLastParent(instances);
            }
        }
        if (getInitAsNaiveBayes()) {
            int iClass = instances.classIndex();
            for (int iNode = 0; iNode < nNodes; iNode++) {
                if (iNode != iClass) {
                    bayesNet.getParentSet(iNode).addParent(iClass, instances);
                }
            }
        }
        int nNrOfAttempts = m_random.nextInt(nNodes * nNodes);
        for (int iAttempt = 0; iAttempt < nNrOfAttempts; iAttempt++) {
            int iTail = m_random.nextInt(nNodes);
            int iHead = m_random.nextInt(nNodes);
            if (bayesNet.getParentSet(iHead).getNrOfParents() < getMaxNrOfParents() && addArcMakesSense(bayesNet, instances, iHead, iTail)) {
                bayesNet.getParentSet(iHead).addParent(iTail, instances);
            }
        }
    }

    /** copyParentSets copies parent sets of source to dest BayesNet
	 * @param dest: destination network
	 * @param source: source network
	 */
    void copyParentSets(BayesNet dest, BayesNet source) {
        int nNodes = source.getNrOfNodes();
        for (int iNode = 0; iNode < nNodes; iNode++) {
            dest.getParentSet(iNode).copy(source.getParentSet(iNode));
        }
    }

    /**
    * @return number of runs
    */
    public int getRuns() {
        return m_nRuns;
    }

    /**
     * Sets the number of runs
     * @param nRuns The number of runs to set
     */
    public void setRuns(int nRuns) {
        m_nRuns = nRuns;
    }

    /**
	* @return random number seed
	*/
    public int getSeed() {
        return m_nSeed;
    }

    /**
	 * Sets the random number seed
	 * @param nSeed The number of the seed to set
	 */
    public void setSeed(int nSeed) {
        m_nSeed = nSeed;
    }

    /**
	 * Returns an enumeration describing the available options.
	 *
	 * @return an enumeration of all the available options.
	 */
    public Enumeration listOptions() {
        Vector newVector = new Vector(4);
        newVector.addElement(new Option("\tNumber of runs\n", "U", 1, "-U <integer>"));
        newVector.addElement(new Option("\tRandom number seed\n", "A", 1, "-A <seed>"));
        Enumeration enu = super.listOptions();
        while (enu.hasMoreElements()) {
            newVector.addElement(enu.nextElement());
        }
        return newVector.elements();
    }

    /**
	 * Parses a given list of options. Valid options are:<p>
	 *
	 * For other options see search algorithm.
	 *
	 * @param options the list of options as an array of strings
	 * @exception Exception if an option is not supported
	 */
    public void setOptions(String[] options) throws Exception {
        String sRuns = Utils.getOption('U', options);
        if (sRuns.length() != 0) {
            setRuns(Integer.parseInt(sRuns));
        }
        String sSeed = Utils.getOption('A', options);
        if (sSeed.length() != 0) {
            setSeed(Integer.parseInt(sSeed));
        }
        super.setOptions(options);
    }

    /**
	 * Gets the current settings of the search algorithm.
	 *
	 * @return an array of strings suitable for passing to setOptions
	 */
    public String[] getOptions() {
        String[] superOptions = super.getOptions();
        String[] options = new String[7 + superOptions.length];
        int current = 0;
        options[current++] = "-U";
        options[current++] = "" + getRuns();
        options[current++] = "-A";
        options[current++] = "" + getSeed();
        for (int iOption = 0; iOption < superOptions.length; iOption++) {
            options[current++] = superOptions[iOption];
        }
        while (current < options.length) {
            options[current++] = "";
        }
        return options;
    }

    /**
	 * This will return a string describing the classifier.
	 * @return The string.
	 */
    public String globalInfo() {
        return "This Bayes Network learning algorithm repeatedly uses hill climbing starting " + "with a randomly generated network structure and return the best structure of the " + "various runs.";
    }

    /**
	 * @return a string to describe the Runs option.
	 */
    public String runsTipText() {
        return "Sets the number of times hill climbing is performed.";
    }

    /**
	 * @return a string to describe the Seed option.
	 */
    public String seedTipText() {
        return "Initialization value for random number generator." + " Setting the seed allows replicability of experiments.";
    }
}
