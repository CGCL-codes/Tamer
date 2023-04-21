package ecosim;

import java.io.File;
import javax.swing.JTextArea;

/**
 *  The sigma confidence interval.
 * 
 *  @author Andrew Warner
 */
public class SigmaConfidence extends ConfidenceInterval {

    /**
     *  Constructor for objects of class SigmaConfidence.
     *
     *  @param value The inputted value from hillclimbing that includes theoptimized omega, sigma, and npop.
     *  @param results Results of this confidence interval.
     *  @param masterVariables The MasterVariables.
     *  @param input File containing the values fred method to examine.
     *  @param output Narrative to write to.
     */
    public SigmaConfidence(FredOutVal value, BinningAndFred results, MasterVariables masterVariables, File input, File output) {
        super("sigma", value, results, masterVariables, input, output);
        this.incPerOM = 6;
        this.upperBound = 100.0;
        this.incUpper = (int) (incPerOM * Math.abs(Math.log10(value.getSigma() / upperBound)));
        this.incLower = (int) (incPerOM * Math.abs(Math.log10(value.getSigma() / lowerBound)));
    }

    /**
     *  Finds the upper bound of the confidence interval for sigma.
     *
     *  @return FredOutVal containing the upper bound values of the confidence interval of sigma.
     */
    public FredOutVal upperBound() {
        writeInputUpperBound();
        FredOutVal result = runCI();
        int sortPer = masterVariables.getSortPercentage();
        double[] percentages = value.getPercentages();
        double probThresh = percentages[sortPer] / masterVariables.CI_NUMBER;
        double[] perResult = result.getPercentages();
        if (perResult[sortPer] > probThresh && result.getSigma() > 97) {
            return null;
        }
        return result;
    }

    /**
     *  Finds the lower bound of the confidence interval for sigma.
     *
     *  @return FredOutVal containing the lower bound values of the confidence interval of sigma.
     */
    public FredOutVal lowerBound() {
        writeInputLowerBound();
        FredOutVal result = runCI();
        return result;
    }

    /**
     *  Writes in the input file for the upper bound of the Npop confidence interval.
     */
    private void writeInputUpperBound() {
        double[] sigmaRange = { value.getSigma(), upperBound };
        int[] xnumics = { 0, incUpper, 0, 0 };
        int sortPer = masterVariables.getSortPercentage();
        double[] percentages = value.getPercentages();
        double probThresh = percentages[sortPer] / masterVariables.CI_NUMBER;
        super.writeInput(input, sigmaRange, xnumics, probThresh);
        narr.println();
        narr.println("The input for upper bound sigma confidence interval: ");
        narr.writeInput(input);
    }

    /**
     *  Writes the input file, sigmaIn.dat for the lower bound confidence interval.
     */
    private void writeInputLowerBound() {
        double[] sigmaRange = { value.getSigma(), lowerBound };
        int[] xnumics = { 0, incLower, 0, 0 };
        int sortPer = masterVariables.getSortPercentage();
        double[] percentages = value.getPercentages();
        double probThresh = percentages[sortPer] / masterVariables.CI_NUMBER;
        super.writeInput(input, sigmaRange, xnumics, probThresh);
        narr.println();
        narr.println("The input for lower bound sigma confidence interval: ");
        narr.writeInput(input);
    }
}
