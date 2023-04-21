package net.sf.openforge.optimize.loop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import net.sf.openforge.app.EngineThread;
import net.sf.openforge.app.GenericJob;
import net.sf.openforge.lim.Loop;
import net.sf.openforge.lim.Reg;
import net.sf.openforge.lim.util.LoopEmulator;
import net.sf.openforge.lim.util.UnEmulatableLoopException;
import net.sf.openforge.util.SizedInteger;

/**
 * @author gandhij
 *
 * This class is used to analyze/emulate candidate loops to find
 * out the sizes required for the loop feedback variables. 
 * 
 * Use as follows:
 * 
 * LoopVaiableAnalysis lva = new LoopVariableAnalysis(targetLoop);
 * HashMap myMap = lva.analyzeLoop();
 * 
 * myMap will contain a map of loop feedback variables to their 
 * maximum value taken (null if not known). myMap will be null if 
 * the loop cannot be emulated.
 * 
 */
public class LoopVariableAnalysis {

    /**
	 * Loop to be analyzed
	 */
    Loop loop = null;

    /**
	 * Max threshold of number of iterations that we emulate. We do 
	 * this since emulating large loops takes a lot of time ! 
	 * FIXME: This should probably be a parametersizable input to forge ! 
	 */
    private final int MAX_ITERATION_THRESHOLD = 100;

    /**
	 * LoopVariableAnalysis Constructor 
	 * 
	 * @param loop Loop to be analyzed for feedback 
	 * 		  variable sizes
	 */
    public LoopVariableAnalysis(Loop loop) {
        super();
        this.loop = loop;
    }

    /**
	 *  Find if this loop is candidate for full analysis.
	 *  Candidate loops must 
	 *  1. Be bounded
	 *  2. Not have very large number of iterations
	 *  3. Have more than 1 feedback variable
	 *  4. Must be iterative 
	 *  5. Must not be unrolled 
	 * 
	 *  The full analysis/emulation of a loop is time 
	 *  consuming. This check makes sure that the analysis
	 *  is done only if considerable gain is expected to 
	 *  be observed by doing reasonable amount of emulation.
	 *  
	 */
    private boolean isCandidateForAnalysis() {
        GenericJob gj = EngineThread.getGenericJob();
        if (!loop.isBounded()) {
            return false;
        }
        if (loop.getIterations() > MAX_ITERATION_THRESHOLD) {
            return false;
        }
        if (loop.getFeedbackPoints().size() == 1) {
            return false;
        }
        if (!loop.isIterative()) {
            return false;
        }
        if (loop.getIterations() < 1) {
            return false;
        }
        return true;
    }

    /**
	 * Analyzes the target loop to find maximum values for each of the feedback
	 * registers.
	 * 
	 * @return HashMap containing a map of (Reg feedbackReg, SizedInteger maxValue)
	 * 		   pairs. The maxValue is the highest absolute value that the feedback 
	 * 		   register will ever take during loop iterations. If maxValue is null,
	 *         the maximum value could not be found by static emulation. If the return
	 * 		   value is null, it means that the loop could not be emulated statically.
	 * 		   (it could also be the case that one of the operations in the path of 
	 *          the loop decision could not be emulated causing the loop unemulatable)
	 */
    public HashMap analyzeLoop() {
        HashMap feedbackVariableMap = null;
        if (isCandidateForAnalysis()) {
            LoopEmulator loopEmulator = new LoopEmulator(loop);
            HashMap inputMap = new HashMap();
            try {
                loopEmulator.emulate(inputMap);
                feedbackVariableMap = getFeedbackVariableMap(loopEmulator.getFeedbackRegisterValues());
            } catch (UnEmulatableLoopException ex) {
            }
        }
        return feedbackVariableMap;
    }

    /**
	 * Get a map of (Reg feedbackReg, SizedInteger maxValue) pairs from 
	 * a map of (Reg feedbackReg , LinkedList(iter1 value, iter2 value ...)) paris
	 * If the value in alteast one iteration for a feedback variable is null, that
	 * feedback variable will be removed from the output map.
	 *
	 * @param feedbackValuesMap  Map of (Reg feedbackReg , 
	 * 			LinkedList(iter1 value, iter2 value ...)) paris
	 * @return HashMap containing (Reg feedbackReg, SizedInteger maxValue) pairs
	 *         
	 */
    private HashMap getFeedbackVariableMap(Map feedbackValuesMap) {
        HashMap rangesMap = new HashMap();
        Iterator regIter = feedbackValuesMap.keySet().iterator();
        while (regIter.hasNext()) {
            Reg feedbackRegister = (Reg) regIter.next();
            LinkedList valueList = (LinkedList) feedbackValuesMap.get(feedbackRegister);
            Iterator valIter = valueList.iterator();
            rangesMap.put(feedbackRegister, null);
            while (valIter.hasNext()) {
                SizedInteger val = (SizedInteger) valIter.next();
                if (rangesMap.containsKey(feedbackRegister)) {
                    if (val != null) {
                        if (val.isNegative()) {
                            val = val.negate();
                        }
                    } else {
                        rangesMap.remove(feedbackRegister);
                    }
                    if (rangesMap.get(feedbackRegister) == null) {
                        rangesMap.put(feedbackRegister, val);
                    } else if (val.compareTo((SizedInteger) rangesMap.get(feedbackRegister)) > 0) {
                        rangesMap.put(feedbackRegister, val);
                    }
                }
            }
        }
        Iterator iter = rangesMap.keySet().iterator();
        ArrayList nullVals = new ArrayList();
        while (iter.hasNext()) {
            Reg register = (Reg) iter.next();
            if (rangesMap.get(register) == null) {
                nullVals.add(register);
            }
        }
        Iterator nullIter = nullVals.iterator();
        while (nullIter.hasNext()) {
            rangesMap.remove(nullIter.next());
        }
        return rangesMap;
    }
}
