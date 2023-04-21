package extscripts;

import java.util.Vector;
import org.personalsmartspace.evaluation.scripts.DelayEvaluationFilter;
import org.personalsmartspace.evaluation.scripts.EvaluationFilter;
import org.personalsmartspace.evaluation.scripts.PerformanceStatistic;
import org.personalsmartspace.evaluation.scripts.QuantitativePerformanceResult;
import org.personalsmartspace.log.impl.PersistPerformanceMessage;

/**
 * This class provides the central entry point into the Performance Evaluation with a static
 * comfort method ReturnResultsFromLogFile(EvaluationFilter filter, String filename). The main()
 * method calls the actual entry pojt in which developers / testers should place their code:
 * EntryPointForPERSISTEvaluators().
 * 
 * @author Patrick Robertson, DLR
 *
 */
public class D55_Test20_CtxHistoryPerformanceTest {

    /**
	 * This is a static comfort method that returns a double array holding the numerical results of all the tests
	 * contained in the logfile with name filename that pass the filter.
	 * This method is the main one called by developers. Simple HOWTO: create an EvaluationFilter setting
	 * the pertinent filter fields, then call this method with the filter just created and filename of the
	 * logfile, then take the results and get their statistics with:
	 * 
	 *  statistic = new PerformanceStatistic(double[] values, int binSize); // binsize of the histogram
	 *  
	 * Then you can pretty print the results with 
	 * 
	 *  statistic.toString(filter2, true); // or use false if you do not want a histogram printout
	 * 
	 * 
	 * @param filter to apply to the logfile
	 * @param filename of the logfile
	 * @return array with double results
	 */
    public static double[] ReturnResultsFromLogFile(EvaluationFilter filter, String filename) {
        Vector<QuantitativePerformanceResult> res = filter.processLogFile(filename);
        double[] resultsDouble = new double[res.size()];
        for (int i = 0; i < res.size(); i++) {
            resultsDouble[i] = res.elementAt(i).getQuantResult();
        }
        return resultsDouble;
    }

    /**
	 * THIS IS THE CENTRAL ENTRY POINT FOR PERSIST DEVELOPERS AND TESTERS. YOU ONLY NEED TO CODE IN THIS
	 * METHOD OR ONE SIMILAR TO IT (E.G. IN A SUBCLASS).
	 * 
	 * The general pattern is to call this sequence:
	 * 
	 * EvaluationFilter filter = new DelayEvaluationFilter("DemoContext1", "org.personalsmartspace.demopackage.DemoClass",  
				"DemoOpFromContext", 11); // Your filter params go here, of course
	*			
	*
	*	String filename = "P:\\Persist-EUFP7-FS\\Persist-SVN\\DOCUMENTS\\WP5\\T53 Evaluation\\ExamplesForQuantitativeEvaluation\\LogTest1.txt";
	* .... or whereever your logfile might be ...
	* 
	* int numHistoBins = 10; // Set the number of histgramm bins you want (zero if you do not want any).
	* PerformanceStatistic statistic = new PerformanceStatistic(GenericLogParser.ReturnResultsFromLogFile(filter, filename), numHistBins);
    *
	* System.out.println(statistic.toString(filter2, filename, true)); // This will pretty print the statistics!
	* 
	*/
    public static void EntryPointForPERSISTEvaluators() {
        EvaluationFilter filter = new DelayEvaluationFilter("CtxHistoy.Tuple.Retrieval", "CtxHistoryMgmt", "CtxHistoryRetrievalTime.delay", 20);
        String filename = "C:\\PERSIST_SVN\\SOURCEFORGE_REPO\\open_source\\trunk\\PersistContainer\\PerformanceLog.log";
        int numHistoBins = 10;
        PerformanceStatistic statistic = new PerformanceStatistic(D55_Test20_CtxHistoryPerformanceTest.ReturnResultsFromLogFile(filter, filename), numHistoBins);
        System.out.println("Direct access to stats: ");
        System.out.println("Av: " + statistic.getMean());
        System.out.println("SD: " + statistic.getStandardDeviation());
        System.out.println("Histo: \n" + statistic.getHistogramStringRep());
        System.out.println(statistic.toString(filter, filename, true));
    }

    /**
	 * Calls EntryPointForPERSISTEvaluators
	 * @param args
	 */
    public static void main(String[] args) {
        D55_Test20_CtxHistoryPerformanceTest.EntryPointForPERSISTEvaluators();
    }

    private static void DoNotUse() {
        PersistPerformanceMessage m = new PersistPerformanceMessage("Component:org.personalsmartspace.demopackage.DemoClass,OperationType:DemoOpFromContext,PerformanceType:0,TestContext:DemoContext1,D55TestTableIndex:11,perfNameValue:Delay=1,ModifiedLast:1274861880995=Wed May 26 10:18:00 CEST 2010");
        System.out.println(m + " value: " + m.getValue());
    }
}
