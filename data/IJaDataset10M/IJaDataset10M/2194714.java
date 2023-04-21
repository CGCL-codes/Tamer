package icamodel.bits;

import icamodel.framework.ModelBit;
import icamodel.utils.Doer;
import icamodel.utils.ModelException;
import icamodel.utils.ModelParameter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * the bit that is the root of the whole model. There can be only one of these.
 * @author Louise
 */
public class ModelRoot extends ModelBit {

    private static ModelRoot theOnlyRoot;

    private int lastWriter = 0;

    private PrintWriter resultWriter;

    private boolean outputResults = true;

    /** Creates a new instance of ModelRoot. Constructor is private so that
     * we can control how many are made.
     */
    private ModelRoot() {
        super("Root");
        addValidParameter("TotalRealisations", "integer");
        addValidParameter("TimePeriodsPerRealisation", "integer");
        addValidParameter("TimePeriodsPerYear", "integer");
    }

    /**
     * Initialise the ModelRoot after creation
     * @throws icamodel.utils.ModelException if something goes wrong
     */
    public void initialise() throws ModelException {
        setRoot(this);
        addChild(External.getInstance());
        addChild(Liabilities.getInstance());
        addChild(Assets.getInstance());
        addChild(Corporate.getInstance());
        addChild(BusinessLogic.getInstance());
    }

    /**
     * Get the only permitted instance of ModelRoot
     * @return the only permitted instance of  <code>ModelRoot</code>
     */
    public static synchronized ModelRoot getInstance() {
        if (theOnlyRoot == null) {
            theOnlyRoot = new ModelRoot();
        }
        return theOnlyRoot;
    }

    protected void checkForInitProblems() {
        if (!isParameter("TotalRealisations")) {
            addInitProblem("The total number of Realisations must be set");
        }
        if (!isParameter("TimePeriodsPerRealisation")) {
            addInitProblem("The number of time Periods per Realisation must be set");
        }
        if (!isParameter("TimePeriodsPerYear")) {
            addInitProblem("The number of time Periods per year must be set");
        }
        Doer findExternal = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof External;
            }
        };
        if (countChildren(findExternal) != 1) {
            addInitProblem("Must have exactly one External ModelBit");
        }
        Doer findLiabs = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof Liabilities;
            }
        };
        if (countChildren(findLiabs) != 1) {
            addInitProblem("Must have exactly one Liabilities ModelBit");
        }
        Doer findAssets = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof Assets;
            }
        };
        if (countChildren(findAssets) != 1) {
            addInitProblem("Must have exactly one Assets ModelBit");
        }
        Doer findCorporate = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof Corporate;
            }
        };
        if (countChildren(findCorporate) != 1) {
            addInitProblem("Must have exactly one Corporate ModelBit");
        }
        Doer findBusinessLogic = new Doer() {

            public boolean isAccepted(ModelBit kid) {
                return kid instanceof BusinessLogic;
            }
        };
        if (countChildren(findBusinessLogic) != 1) {
            addInitProblem("Must have exactly one BusinessLogic ModelBit");
        }
    }

    /**
     * Do the right things at the start of a realisation,
     * before the children are reset
     * @param rnum The realisation number
     */
    protected void startReset(int rnum) {
    }

    /**
     * Do the right things at the start of a realisation, after the children
     * have been reset
     * @param rnum the realisation number
     */
    protected void endReset(int rnum) {
        if (resultWriter != null) {
            resultWriter.close();
        }
    }

    /**
     * do the right things for a time period, before the children
     * are updated
     * @param rnum realisation number
     * @param tnum time period
     */
    protected void startUpdate(int rnum, int tnum) {
    }

    /**
     * do the right things for a time period after the children have been updated.
     * @param rnum realisation
     * @param tnum time period
     */
    protected void endUpdate(int rnum, int tnum) {
    }

    /**
     * How many realisations are we going to be running?
     * @return the number of realisations we will run
     */
    public int getTotalRealisations() {
        Integer tr = (Integer) this.getParameterValue("TotalRealisations");
        return tr.intValue();
    }

    /**
     * How many time periods per realisation?
     * @return time periods per realisation
     */
    public int getTimePeriodsPerRealisation() {
        Integer tr = (Integer) this.getParameterValue("TimePeriodsPerRealisation");
        return tr.intValue();
    }

    /**
     * how many time periods per year?
     * @return time periods per year.
     */
    public int getTimePeriodsPerYear() {
        Integer tr = (Integer) this.getParameterValue("TimePeriodsPerYear");
        return tr.intValue();
    }

    /**
     * set whether results will be written to files
     * @param yesorno true to write results out to files, false otherwise
     */
    public void setOutputResultsToFile(boolean yesorno) {
        outputResults = yesorno;
    }

    /**
     * Get the destination for the results
     * @param rnum realisation number
     * @throws java.io.IOException if something goes wrong
     * @return null if results are not to be written to file, otherwise the destination
     */
    public PrintWriter getResultWriter(int rnum) throws IOException {
        if (!outputResults) {
            resultWriter = null;
        } else {
            if (rnum != lastWriter) {
                if (resultWriter != null) {
                    resultWriter.close();
                }
                String fileName = "Results_" + rnum + ".txt";
                resultWriter = new PrintWriter(new FileWriter("results" + File.separator + fileName));
                lastWriter = rnum;
            }
        }
        return resultWriter;
    }

    protected String checkParameterValue(ModelParameter param) {
        String prob = null;
        String pname = param.getName();
        if (pname.equals("TotalRealisations") || pname.equals("TimePeriodsPerRealisation") || pname.equals("TimePeriodsPerYear")) {
            Integer v = (Integer) param.getValue();
            if (v.intValue() <= 0) {
                prob = pname + " must be greater than zero: " + v;
            }
        }
        return prob;
    }

    public void clearSpecial() {
    }
}
