package com.sodad.weka.classifiers.bayes.net.search.global;

import java.util.Enumeration;
import java.util.Vector;
import com.sodad.weka.classifiers.bayes.BayesNet;
import com.sodad.weka.core.Instances;
import com.sodad.weka.core.Option;
import com.sodad.weka.core.RevisionUtils;
import com.sodad.weka.core.TechnicalInformation;
import com.sodad.weka.core.TechnicalInformationHandler;
import com.sodad.weka.core.Utils;
import com.sodad.weka.core.TechnicalInformation.Field;
import com.sodad.weka.core.TechnicalInformation.Type;

/** 
 <!-- globalinfo-start -->
 * This Bayes Network learning algorithm uses tabu search for finding a well scoring Bayes network structure. Tabu search is hill climbing till an optimum is reached. The following step is the least worst possible step. The last X steps are kept in a list and none of the steps in this so called tabu list is considered in taking the next step. The best network found in this traversal is returned.<br/>
 * <br/>
 * For more information see:<br/>
 * <br/>
 * R.R. Bouckaert (1995). Bayesian Belief Networks: from Construction to Inference. Utrecht, Netherlands.
 * <p/>
 <!-- globalinfo-end -->
 * 
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;phdthesis{Bouckaert1995,
 *    address = {Utrecht, Netherlands},
 *    author = {R.R. Bouckaert},
 *    institution = {University of Utrecht},
 *    title = {Bayesian Belief Networks: from Construction to Inference},
 *    year = {1995}
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 * 
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -L &lt;integer&gt;
 *  Tabu list length</pre>
 * 
 * <pre> -U &lt;integer&gt;
 *  Number of runs</pre>
 * 
 * <pre> -P &lt;nr of parents&gt;
 *  Maximum number of parents</pre>
 * 
 * <pre> -R
 *  Use arc reversal operation.
 *  (default false)</pre>
 * 
 * <pre> -P &lt;nr of parents&gt;
 *  Maximum number of parents</pre>
 * 
 * <pre> -R
 *  Use arc reversal operation.
 *  (default false)</pre>
 * 
 * <pre> -N
 *  Initial structure is empty (instead of Naive Bayes)</pre>
 * 
 * <pre> -mbc
 *  Applies a Markov Blanket correction to the network structure, 
 *  after a network structure is learned. This ensures that all 
 *  nodes in the network are part of the Markov blanket of the 
 *  classifier node.</pre>
 * 
 * <pre> -S [LOO-CV|k-Fold-CV|Cumulative-CV]
 *  Score type (LOO-CV,k-Fold-CV,Cumulative-CV)</pre>
 * 
 * <pre> -Q
 *  Use probabilistic or 0/1 scoring.
 *  (default probabilistic scoring)</pre>
 * 
 <!-- options-end -->
 *
 * @author Remco Bouckaert (rrb@xm.co.nz)
 * @version $Revision: 1.5 $
 */
public class TabuSearch extends HillClimber implements TechnicalInformationHandler {

    /** for serialization */
    static final long serialVersionUID = 1176705618756672292L;

    /** number of runs **/
    int m_nRuns = 10;

    /** size of tabu list **/
    int m_nTabuList = 5;

    /** the actual tabu list **/
    Operation[] m_oTabuList = null;

    /**
	 * Returns an instance of a TechnicalInformation object, containing 
	 * detailed information about the technical background of this class,
	 * e.g., paper reference or book this class is based on.
	 * 
	 * @return the technical information about this class
	 */
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;
        result = new TechnicalInformation(Type.PHDTHESIS);
        result.setValue(Field.AUTHOR, "R.R. Bouckaert");
        result.setValue(Field.YEAR, "1995");
        result.setValue(Field.TITLE, "Bayesian Belief Networks: from Construction to Inference");
        result.setValue(Field.INSTITUTION, "University of Utrecht");
        result.setValue(Field.ADDRESS, "Utrecht, Netherlands");
        return result;
    }

    /**
	 * search determines the network structure/graph of the network
	 * with the Tabu search algorithm.
	 * 
	 * @param bayesNet the network to use
	 * @param instances the instances to use
	 * @throws Exception if something goes wrong
	 */
    protected void search(BayesNet bayesNet, Instances instances) throws Exception {
        m_oTabuList = new Operation[m_nTabuList];
        int iCurrentTabuList = 0;
        double fBestScore;
        double fCurrentScore = calcScore(bayesNet);
        BayesNet bestBayesNet;
        fBestScore = fCurrentScore;
        bestBayesNet = new BayesNet();
        bestBayesNet.m_Instances = instances;
        bestBayesNet.initStructure();
        copyParentSets(bestBayesNet, bayesNet);
        for (int iRun = 0; iRun < m_nRuns; iRun++) {
            Operation oOperation = getOptimalOperation(bayesNet, instances);
            performOperation(bayesNet, instances, oOperation);
            if (oOperation == null) {
                throw new Exception("Panic: could not find any step to make. Tabu list too long?");
            }
            m_oTabuList[iCurrentTabuList] = oOperation;
            iCurrentTabuList = (iCurrentTabuList + 1) % m_nTabuList;
            fCurrentScore += oOperation.m_fScore;
            if (fCurrentScore > fBestScore) {
                fBestScore = fCurrentScore;
                copyParentSets(bestBayesNet, bayesNet);
            }
            if (bayesNet.getDebug()) {
                printTabuList();
            }
        }
        copyParentSets(bayesNet, bestBayesNet);
        bestBayesNet = null;
    }

    /** copyParentSets copies parent sets of source to dest BayesNet
	 * @param dest destination network
	 * @param source source network
	 */
    void copyParentSets(BayesNet dest, BayesNet source) {
        int nNodes = source.getNrOfNodes();
        for (int iNode = 0; iNode < nNodes; iNode++) {
            dest.getParentSet(iNode).copy(source.getParentSet(iNode));
        }
    }

    /** check whether the operation is not in the tabu list
	 * @param oOperation operation to be checked
	 * @return true if operation is not in the tabu list
	 */
    boolean isNotTabu(Operation oOperation) {
        for (int iTabu = 0; iTabu < m_nTabuList; iTabu++) {
            if (oOperation.equals(m_oTabuList[iTabu])) {
                return false;
            }
        }
        return true;
    }

    /** print tabu list for debugging purposes.
	 */
    void printTabuList() {
        for (int i = 0; i < m_nTabuList; i++) {
            Operation o = m_oTabuList[i];
            if (o != null) {
                if (o.m_nOperation == 0) {
                    System.out.print(" +(");
                } else {
                    System.out.print(" -(");
                }
                System.out.print(o.m_nTail + "->" + o.m_nHead + ")");
            }
        }
        System.out.println();
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
     * @return the Tabu List length
     */
    public int getTabuList() {
        return m_nTabuList;
    }

    /**
     * Sets the Tabu List length.
     * @param nTabuList The nTabuList to set
     */
    public void setTabuList(int nTabuList) {
        m_nTabuList = nTabuList;
    }

    /**
	 * Returns an enumeration describing the available options.
	 *
	 * @return an enumeration of all the available options.
	 */
    public Enumeration listOptions() {
        Vector newVector = new Vector(4);
        newVector.addElement(new Option("\tTabu list length", "L", 1, "-L <integer>"));
        newVector.addElement(new Option("\tNumber of runs", "U", 1, "-U <integer>"));
        newVector.addElement(new Option("\tMaximum number of parents", "P", 1, "-P <nr of parents>"));
        newVector.addElement(new Option("\tUse arc reversal operation.\n\t(default false)", "R", 0, "-R"));
        Enumeration enu = super.listOptions();
        while (enu.hasMoreElements()) {
            newVector.addElement(enu.nextElement());
        }
        return newVector.elements();
    }

    /**
	 * Parses a given list of options. <p/>
	 *
	 <!-- options-start -->
	 * Valid options are: <p/>
	 * 
	 * <pre> -L &lt;integer&gt;
	 *  Tabu list length</pre>
	 * 
	 * <pre> -U &lt;integer&gt;
	 *  Number of runs</pre>
	 * 
	 * <pre> -P &lt;nr of parents&gt;
	 *  Maximum number of parents</pre>
	 * 
	 * <pre> -R
	 *  Use arc reversal operation.
	 *  (default false)</pre>
	 * 
	 * <pre> -P &lt;nr of parents&gt;
	 *  Maximum number of parents</pre>
	 * 
	 * <pre> -R
	 *  Use arc reversal operation.
	 *  (default false)</pre>
	 * 
	 * <pre> -N
	 *  Initial structure is empty (instead of Naive Bayes)</pre>
	 * 
	 * <pre> -mbc
	 *  Applies a Markov Blanket correction to the network structure, 
	 *  after a network structure is learned. This ensures that all 
	 *  nodes in the network are part of the Markov blanket of the 
	 *  classifier node.</pre>
	 * 
	 * <pre> -S [LOO-CV|k-Fold-CV|Cumulative-CV]
	 *  Score type (LOO-CV,k-Fold-CV,Cumulative-CV)</pre>
	 * 
	 * <pre> -Q
	 *  Use probabilistic or 0/1 scoring.
	 *  (default probabilistic scoring)</pre>
	 * 
	 <!-- options-end -->
	 *
	 * @param options the list of options as an array of strings
	 * @throws Exception if an option is not supported
	 */
    public void setOptions(String[] options) throws Exception {
        String sTabuList = Utils.getOption('L', options);
        if (sTabuList.length() != 0) {
            setTabuList(Integer.parseInt(sTabuList));
        }
        String sRuns = Utils.getOption('U', options);
        if (sRuns.length() != 0) {
            setRuns(Integer.parseInt(sRuns));
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
        options[current++] = "-L";
        options[current++] = "" + getTabuList();
        options[current++] = "-U";
        options[current++] = "" + getRuns();
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
        return "This Bayes Network learning algorithm uses tabu search for finding a well scoring " + "Bayes network structure. Tabu search is hill climbing till an optimum is reached. The " + "following step is the least worst possible step. The last X steps are kept in a list and " + "none of the steps in this so called tabu list is considered in taking the next step. " + "The best network found in this traversal is returned.\n\n" + "For more information see:\n\n" + getTechnicalInformation().toString();
    }

    /**
	 * @return a string to describe the Runs option.
	 */
    public String runsTipText() {
        return "Sets the number of steps to be performed.";
    }

    /**
	 * @return a string to describe the TabuList option.
	 */
    public String tabuListTipText() {
        return "Sets the length of the tabu list.";
    }

    /**
	 * Returns the revision string.
	 * 
	 * @return		the revision
	 */
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 1.5 $");
    }
}
