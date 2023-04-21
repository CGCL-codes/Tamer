package de.jmulti.proc;

import com.jstatcom.engine.gauss.GaussLoadTypes;
import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.model.JSCTypeDef;
import com.jstatcom.model.JSCTypes;
import com.jstatcom.ts.TSDate;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.util.FArg;
import com.jstatcom.util.UData;
import com.jstatcom.util.UMatrix;
import com.jstatcom.util.UStringArray;

/**
 * This GAUSS command object computes the Johansen Trace Test for cointegration
 * together with the p-values and critical values. It allows for up to 2 breaks
 * in the levels.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig </a>
 */
public final class JohCointTestCall extends GaussPCall {

    public static final JSCTypeDef JOH_RESULT = new JSCTypeDef("JOH_RESULT", JSCTypes.NARRAY, "Johansen test: LR statistic~critical values (1%,5%,10%)~p-values");

    private int testState = 1;

    private int lagLength = 0;

    private TSDateRange range = null;

    private JSCNArray dummies = null;

    private JSCNArray endog = null;

    private JSCSArray dummyNames = null;

    private JSCSArray endogNames = null;

    private boolean isSeasDum = false;

    private int[] ecSelection = null;

    private JSCNArray joh_Result = null;

    private TSDate[] breakDates = new TSDate[0];

    private boolean isTrendBreak = false;

    /**
     * <code>JohCointTestCall</code> constructor takes the arguments for the
     * procedure call.
     * 
     * @param lagLength
     *            levels lags (VAR)
     */
    public JohCointTestCall(JSCNArray endog, JSCSArray endogNames, JSCNArray dummies, JSCSArray dummyNames, boolean isSeasDum, int lagLength, int testState, TSDateRange range, int[] ecSelection, TSDate[] breakDates, boolean isTrendBreak) {
        super();
        setName("Johansen Trace Test");
        this.testState = testState;
        this.lagLength = lagLength;
        this.range = range;
        this.dummies = dummies;
        this.endog = endog;
        this.dummyNames = dummyNames;
        this.endogNames = endogNames;
        this.ecSelection = ecSelection;
        if (range.lowerBound().subPeriodicity() != 1) this.isSeasDum = isSeasDum;
        if (breakDates != null) this.breakDates = breakDates;
        this.isTrendBreak = isTrendBreak;
    }

    /**
     * Writes the text output to a string that can be referenced via
     * <code>getOutput</code>.
     */
    protected void finalCode() {
        TSDateRange newRange = range.addPeriodsToStart(lagLength);
        if (testState == 3 && breakDates.length > 0) output.append("Breaks are ignored for test version with trend orthogonal to EC term.\n");
        String[] namArray = new String[dummyNames.rows()];
        for (int i = 0; i < namArray.length; i++) namArray[i] = dummyNames.stringAt(i, 0);
        String resNames = UData.stringForArray(new JSCSArray("tmp", UStringArray.selRowsIf(namArray, ecSelection)));
        String unresNames = UData.stringForArray(new JSCSArray("tmp", UStringArray.delRowsIf(namArray, ecSelection)));
        boolean pValueComputed = true;
        if (Double.doubleToLongBits(joh_Result.doubleAt(0, 4)) == Double.doubleToLongBits(Double.NaN)) pValueComputed = false;
        output.append(FArg.sprintf("%-25s %s \n", new FArg("Johansen Trace Test for:").add(UData.stringForArray(endogNames))));
        if (namArray.length > 0) {
            output.append(FArg.sprintf("%-25s %s \n", new FArg("unrestricted dummies:").add(unresNames)));
            output.append(FArg.sprintf("%-25s %s \n", new FArg("restricted dummies:").add(resNames)));
        }
        output.append(newRange.format("sample range:", 26) + "\n");
        output.append(FArg.sprintf("%-25s %i \n", new FArg("included lags (levels):").add(lagLength)));
        output.append(FArg.sprintf("%-25s %i \n", new FArg("dimension of the process:").add(endogNames.rows())));
        String ref = "response surface computed:\n";
        if (testState == 1) {
            output.append("intercept included\n");
        }
        if (testState == 2) {
            output.append("trend and intercept included\n");
        }
        if (testState == 3) {
            output.append("trend orthogonal to cointegration relation\n");
        }
        if (!pValueComputed) ref = "critical values not available for this model\n";
        if (isSeasDum) output.append("seasonal dummies included\n");
        output.append(ref);
        output.append("-----------------------------------------------\n");
        output.append(FArg.sprintf(" %-3s %-8s %-8s %-8s %-8s %-8s\n", new FArg("r0").add("LR").add("pval").add("90%").add("95%").add("99%")));
        output.append("-----------------------------------------------\n");
        if (pValueComputed) for (int i = 0; i < joh_Result.rows(); i++) {
            output.append(FArg.sprintf("%- 3i %- 8.2f %- 8.4f %- 8.2f %- 8.2f %- 8.2f", new FArg(i).add(joh_Result.doubleAt(i, 0)).add(joh_Result.doubleAt(i, 4)).add(joh_Result.doubleAt(i, 1)).add(joh_Result.doubleAt(i, 2)).add(joh_Result.doubleAt(i, 3))));
            output.append("\n");
        } else for (int i = 0; i < joh_Result.rows(); i++) {
            output.append(FArg.sprintf("%- 3i %- 8.2f %-8s %-8s %-8s %-8s", new FArg(i).add(joh_Result.doubleAt(i, 0)).add(" " + joh_Result.doubleAt(i, 4)).add(" " + joh_Result.doubleAt(i, 1)).add(" " + joh_Result.doubleAt(i, 2)).add(" " + joh_Result.doubleAt(i, 3))));
            output.append("\n");
        }
        output.append("\n");
    }

    /**
     * @see GaussPCall
     */
    protected void runCode() {
        if (getSymbolTable() != null) getSymbolTable().get(JOH_RESULT).clear();
        JSCNArray seasDum = new JSCNArray("SeasonalDummies");
        if (isSeasDum) seasDum = new JSCNArray("SeasonalDummies", range.createSeasDum(true, false));
        JSCNArray unresDum = new JSCNArray("unresDum");
        JSCNArray resDum = new JSCNArray("resDum");
        int[] bIndices = new int[breakDates.length];
        for (int i = 0; i < bIndices.length; i++) bIndices[i] = range.indexForDate(breakDates[bIndices.length - 1 - i]) + 1;
        JSCNArray breakIndex = new JSCNArray("breakIndex", bIndices);
        if (!dummies.isEmpty()) {
            unresDum = new JSCNArray("unresDum", UMatrix.delColsIf(dummies.doubleArray(), ecSelection));
            resDum = new JSCNArray("resDum", UMatrix.selColsIf(dummies.doubleArray(), ecSelection));
        }
        joh_Result = (JSCNArray) JOH_RESULT.getInstance();
        engine().load("coi", GaussLoadTypes.LIB);
        engine().load("fil", GaussLoadTypes.LIB);
        engine().load("diag", GaussLoadTypes.LIB);
        engine().call("johtest_cointjoh", new JSCData[] { endog, unresDum, seasDum, resDum, new JSCInt("lags", lagLength), new JSCInt("testState", testState), breakIndex, new JSCInt("isTrendBreak", isTrendBreak) }, new JSCData[] { joh_Result });
        if (getSymbolTable() != null) getSymbolTable().set(joh_Result);
    }

    /**
     * Computes the impulse dummies used for taking out the 1st p (number of
     * lags) observations of each break period.
     * 
     * @param breakDates
     *            up to two break dates, latest break must be first element
     * @param origRange
     *            overall range
     * @param lags
     *            levels lags (VAR)
     * @return array with dummies that must be included to model (unrestricted)
     *         if breaks are used in the test
     */
    public static JSCNArray createCondDummies(TSDate[] breakDates, TSDateRange origRange, int lags) {
        JSCNArray condDummies = new JSCNArray("condDummies");
        for (TSDate date : breakDates) {
            double[][] dum = new double[origRange.numOfObs()][lags];
            int breakIndex = origRange.indexForDate(date);
            for (int i = 0; i < lags; i++) dum[breakIndex + i][i] = 1;
            condDummies.appendCols(new JSCNArray("dum", dum));
        }
        return condDummies;
    }

    /**
     * Automatically assembles the EC selection if break dates are specified.
     * The resulting indicator vector is used to tell the test routine which
     * deterministic variables should be restricted to the EC term
     * (corresponding element 1). For example, for the case of a break in levels
     * and trend in a model with constant+trend, the trend dummies need to be
     * restricted to the EC term, whereas the shift dummies not. This method
     * generates these restrictions automatically according to the breaks, the
     * type of breaks, and the model state.
     * <p>
     * This routine assumes that the deterministics was assembled in the
     * following way: user specified dummies ~ conditioning dummies ~ 1st shift ~
     * 1st trend shift ~ 2nd shift ~ 2nd trend shift
     * 
     * @param ecSel
     *            selection for user specified dummies (additional impulse
     *            dummies)
     * @param breakDates
     *            up to 2 break dates, latest break must be first element
     * @param testState
     *            can be 1 (const), 2 (const+trend), 3 (orth trend)
     * @param isTrendBreak
     *            whether break should be in trend + level or level only, only
     *            relevant if testState == 2
     * @param levelsLags
     *            number of levels in VAR
     * @return the indicator for EC restrictions
     */
    public static int[] getECSelection(int[] ecSel, TSDate[] breakDates, int testState, boolean isTrendBreak, int levelsLags) {
        int userDum = ecSel.length;
        int numOfDetWithBreaks = userDum + breakDates.length * levelsLags + breakDates.length * ((isTrendBreak && testState == 2) ? 2 : 1);
        int[] ecSelection = new int[numOfDetWithBreaks];
        int k = 0;
        for (int val : ecSel) ecSelection[k++] = val;
        if (breakDates.length > 0) {
            if (testState == 1) {
                for (int i = 0; i < breakDates.length; i++) ecSelection[ecSelection.length - i - 1] = 1;
            } else if (testState == 2) {
                if (!isTrendBreak) for (int i = 0; i < breakDates.length; i++) ecSelection[ecSelection.length - i - 1] = 1; else for (int i = 0; i < breakDates.length; i++) {
                    ecSelection[ecSelection.length - 2 * i - 1] = 1;
                    ecSelection[ecSelection.length - 2 * i - 2] = 0;
                }
            }
        }
        return ecSelection;
    }

    /**
     * Gets the names of the deterministic variables automatically included when
     * breaks are specified.
     * 
     * @param breakDates
     *            up to 2 break dates, latest break must be first element
     * @param testState
     *            can be 1 (const), 2 (const+trend), 3 (orth trend)
     * @param isTrendBreak
     *            whether break should be in trend + level or level only, only
     *            relevant if testState == 2
     * @param levelsLags
     *            number of levels in VAR
     * @return a string vector with the names of the conditioning dummies
     *         followed by the names of the level/trend shifts
     */
    public static String[] getBreakDumNames(TSDate[] breakDates, int testState, boolean isTrendBreak, int levelsLags) {
        String[] breakdumNames = new String[levelsLags * breakDates.length + breakDates.length * ((isTrendBreak && testState == 2) ? 2 : 1)];
        for (int i = 0, ii = 0; i < breakDates.length; i++) for (int j = 0; j < levelsLags; j++) breakdumNames[ii++] = "D[" + breakDates[breakDates.length - i - 1].addPeriods(j) + "]";
        if (testState == 1) {
            for (int i = 0; i < breakDates.length; i++) breakdumNames[breakdumNames.length - 1 - i] = "S[" + breakDates[i] + "]";
        } else if (testState == 2) {
            if (!isTrendBreak) for (int i = 0; i < breakDates.length; i++) breakdumNames[breakdumNames.length - 1 - i] = "S[" + breakDates[i] + "]"; else for (int i = 0; i < breakDates.length; i++) {
                breakdumNames[breakdumNames.length - 1 - 2 * i] = "t[" + breakDates[i] + "]";
                breakdumNames[breakdumNames.length - 2 - 2 * i] = "S[" + breakDates[i] + "]";
            }
        }
        return breakdumNames;
    }

    /**
     * Computes break dummies to be included in the test if level only or
     * level+trend breaks are used.
     * 
     * @param breakDates
     *            up to two break dates, latest break must be first element
     * @param testState
     *            can be 1 (const), 2 (const+trend), 3 (orth trend)
     * @param isTrendBreak
     *            whether break should be in trend + level or level only, only
     *            relevant if testState == 2
     * @return matrix with break dummies that must be appropriately added to the
     *         model, requires using ecSelection to add the breaks correctly to
     *         EC or unrestricted
     *  
     */
    public static JSCNArray createBreaks(TSDate[] breakDates, TSDateRange range, int testState, boolean isTrendBreak) {
        JSCNArray breaks = new JSCNArray("breaks");
        if (testState == 1) {
            if (breakDates.length == 1) {
                double[] break1 = range.createShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                JSCNArray dataBreak1 = new JSCNArray("break1", break1);
                breaks.appendCols(dataBreak1);
            } else if (breakDates.length == 2) {
                double[] break1 = range.createShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                JSCNArray dataBreak1 = new JSCNArray("break1", break1);
                breaks.appendCols(dataBreak1);
                double[] break2 = range.createShiftDum(new TSDateRange(breakDates[1], breakDates[0].addPeriods(-1)));
                JSCNArray dataBreak2 = new JSCNArray("break1", break2);
                breaks.appendCols(dataBreak2);
            }
        } else if (testState == 2) {
            if (breakDates.length == 1) {
                double[] break1 = range.createShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                JSCNArray dataBreak1 = new JSCNArray("break1", break1);
                breaks.appendCols(dataBreak1);
                if (isTrendBreak) {
                    double[] trendBreak1 = range.createTrendShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                    JSCNArray dataTrendBreak1 = new JSCNArray("trendbreak1", trendBreak1);
                    breaks.appendCols(dataTrendBreak1);
                }
            } else if (breakDates.length == 2) {
                double[] break1 = range.createShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                JSCNArray dataBreak1 = new JSCNArray("break1", break1);
                breaks.appendCols(dataBreak1);
                if (isTrendBreak) {
                    double[] trendBreak1 = range.createTrendShiftDum(new TSDateRange(breakDates[0], range.upperBound()));
                    JSCNArray dataTrendBreak1 = new JSCNArray("trendbreak1", trendBreak1);
                    breaks.appendCols(dataTrendBreak1);
                }
                double[] break2 = range.createShiftDum(new TSDateRange(breakDates[1], breakDates[0].addPeriods(-1)));
                JSCNArray dataBreak2 = new JSCNArray("break1", break2);
                breaks.appendCols(dataBreak2);
                if (isTrendBreak) {
                    double[] trendBreak2 = range.createTrendShiftDum(new TSDateRange(breakDates[1], breakDates[0].addPeriods(-1)));
                    JSCNArray dataTrendBreak2 = new JSCNArray("trendbreak2", trendBreak2);
                    breaks.appendCols(dataTrendBreak2);
                }
            }
        }
        return breaks;
    }
}
