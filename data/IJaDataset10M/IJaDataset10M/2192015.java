package playground.wrashid.sschieffer.DecentralizedSmartCharger.ClassTests;

import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.controler.Controler;
import playground.wrashid.PSF.data.HubLinkMapping;
import playground.wrashid.sschieffer.DSC.DecentralizedSmartCharger;
import playground.wrashid.sschieffer.DecentralizedSmartCharger.TestSimulationSetUp;
import playground.wrashid.sschieffer.SetUp.ElectricitySourceDefinition.HubLoadDistributionReader;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.LoadDistributionInterval;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.Schedule;
import playground.wrashid.sschieffer.SetUp.VehicleDefinition.GasType;
import junit.framework.TestCase;
import lpsolve.LpSolveException;

/**
 * HubLoadDistributionReader
 * checks if PHEV deterministic load is calculated correctly
 * for constant, linear electricity price functions
 * 
 * @author Stella
 *
 */
public class HubLoadDistributionReaderTestOnePlan_new extends TestCase {

    String configPath = "test/input/playground/wrashid/sschieffer/config_plans1.xml";

    final String outputPath = "D:\\ETH\\MasterThesis\\TestOutput\\";

    final double electrification = 1.0;

    final double ev = 1.0;

    private TestSimulationSetUp mySimulation;

    private Controler controler;

    final double bufferBatteryCharge = 0.0;

    final double MINCHARGINGLENGTH = 5 * 60;

    public static DecentralizedSmartCharger myDecentralizedSmartCharger;

    static final double optimalPrice = 0.00001;

    static final double suboptimalPrice = optimalPrice * 3;

    public static void testMain(String[] args) throws MaxIterationsExceededException, OptimizationException, FunctionEvaluationException, IllegalArgumentException, LpSolveException, IOException {
    }

    public Controler setControler() throws IOException, OptimizationException, InterruptedException {
        mySimulation = new TestSimulationSetUp(configPath, electrification, ev);
        mySimulation.setUpSmartCharger(outputPath, bufferBatteryCharge, 15 * 60);
        return mySimulation.getControler();
    }

    /**
	*  Schedule:  part 1  / part 2 </br>
	*  deterministic load: 10  /  -10 </br>
	*  deterministic pricing 2.4*E-4/ 
	*  pricing   belowGas / aboveGas  </br>
	*  
	 * @throws MaxIterationsExceededException
	 * @throws FunctionEvaluationException
	 * @throws IllegalArgumentException
	 * @throws LpSolveException
	 * @throws OptimizationException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
    public void testHubLoadDistributionReaderConstant() throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException, LpSolveException, OptimizationException, IOException, InterruptedException {
        controler = setControler();
        HubLoadDistributionReader hubReader = new HubLoadDistributionReader(controler, mapHubsTest(), readHubsTest(), readHubsPricingTest(optimalPrice, suboptimalPrice), mySimulation.getVehicleTypeCollector(), outputPath, optimalPrice, suboptimalPrice);
        System.out.println("Deterministic load");
        Schedule deterministicLoad = hubReader.getDeterministicHubLoadDistribution(1);
        deterministicLoad.printSchedule();
        assertEquals(deterministicLoad.getNumberOfEntries(), 2);
        LoadDistributionInterval t1 = (LoadDistributionInterval) deterministicLoad.timesInSchedule.get(0);
        LoadDistributionInterval t2 = (LoadDistributionInterval) deterministicLoad.timesInSchedule.get(1);
        double[] coeffs = t1.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], 10.0);
        coeffs = t2.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], -10.0);
        System.out.println("Deterministic PHEV load");
        Schedule deterministicLoadPHEV = hubReader.getDeterministicHubLoadDistributionPHEVAdjusted(1);
        deterministicLoadPHEV.printSchedule();
        assertEquals(deterministicLoadPHEV.getNumberOfEntries(), 2);
        LoadDistributionInterval tPHEV1 = (LoadDistributionInterval) deterministicLoadPHEV.timesInSchedule.get(0);
        LoadDistributionInterval tPHEV2 = (LoadDistributionInterval) deterministicLoadPHEV.timesInSchedule.get(1);
        coeffs = tPHEV1.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], 10.0);
        coeffs = tPHEV2.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], -10.0);
    }

    /**
	*  Schedule:part 1/part 2
	*  deterministic load 10/-10
	*  pricing linearCuttingGasPrice/aboveGas
	*  
	 * @throws MaxIterationsExceededException
	 * @throws FunctionEvaluationException
	 * @throws IllegalArgumentException
	 * @throws LpSolveException
	 * @throws OptimizationException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
    public void testHubLoadDistributionReaderLinear() throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException, LpSolveException, OptimizationException, IOException, InterruptedException {
        controler = setControler();
        HubLoadDistributionReader hubReader = new HubLoadDistributionReader(controler, mapHubsTest(), readHubsTest(), readHubsPricingTestLinear(), mySimulation.getVehicleTypeCollector(), outputPath, 0, 0);
        System.out.println("Deterministic load");
        Schedule deterministicLoad = hubReader.getDeterministicHubLoadDistribution(1);
        deterministicLoad.printSchedule();
        assertEquals(deterministicLoad.getNumberOfEntries(), 2);
        LoadDistributionInterval t1 = (LoadDistributionInterval) deterministicLoad.timesInSchedule.get(0);
        LoadDistributionInterval t2 = (LoadDistributionInterval) deterministicLoad.timesInSchedule.get(1);
        double[] coeffs = t1.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], 10.0);
        coeffs = t2.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], -10.0);
        System.out.println("Deterministic PHEV load");
        Schedule deterministicLoadPHEV = hubReader.getDeterministicHubLoadDistributionPHEVAdjusted(1);
        deterministicLoadPHEV.printSchedule();
        double gasPriceInCostPerSecond = DecentralizedSmartCharger.STANDARDCONNECTIONSWATT * 1 / 43.0 * 1000000.0 * 0.25;
        assertEquals(deterministicLoadPHEV.getNumberOfEntries(), 3);
        LoadDistributionInterval tPHEV1 = (LoadDistributionInterval) deterministicLoadPHEV.timesInSchedule.get(0);
        LoadDistributionInterval tPHEV2 = (LoadDistributionInterval) deterministicLoadPHEV.timesInSchedule.get(1);
        LoadDistributionInterval tPHEV3 = (LoadDistributionInterval) deterministicLoadPHEV.timesInSchedule.get(2);
        double intersectAtX = 62490.0 / 2;
        coeffs = tPHEV1.getPolynomialFunction().getCoefficients();
        assertEquals(tPHEV1.getEndTime(), intersectAtX);
        assertEquals(coeffs[0], 10.0);
        assertEquals(tPHEV1.getStartTime(), 0.0);
        coeffs = tPHEV2.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], -10.0);
        coeffs = tPHEV3.getPolynomialFunction().getCoefficients();
        assertEquals(coeffs[0], -10.0);
    }

    public static HashMap<Integer, Schedule> readHubsTest() throws IOException {
        HashMap<Integer, Schedule> hubLoadDistribution1 = new HashMap<Integer, Schedule>();
        hubLoadDistribution1.put(1, makeBullshitScheduleTest());
        return hubLoadDistribution1;
    }

    public static HashMap<Integer, Schedule> readHubsPricingTest(double optimal, double suboptimal) throws IOException {
        HashMap<Integer, Schedule> hubLoadDistribution1 = new HashMap<Integer, Schedule>();
        hubLoadDistribution1.put(1, makeBullshitPricingScheduleTest(optimal, suboptimal));
        return hubLoadDistribution1;
    }

    public static HashMap<Integer, Schedule> readHubsPricingTestLinear() throws IOException {
        HashMap<Integer, Schedule> hubLoadDistribution1 = new HashMap<Integer, Schedule>();
        hubLoadDistribution1.put(1, makeBullshitScheduleTestLinear());
        return hubLoadDistribution1;
    }

    public static Schedule makeBullshitScheduleTest() throws IOException {
        Schedule bullShitSchedule = new Schedule();
        double[] bullshitCoeffs = new double[] { 10 };
        double[] bullshitCoeffs2 = new double[] { -10 };
        PolynomialFunction bullShitFunc = new PolynomialFunction(bullshitCoeffs);
        PolynomialFunction bullShitFunc2 = new PolynomialFunction(bullshitCoeffs2);
        LoadDistributionInterval l1 = new LoadDistributionInterval(0.0, 62490.0, bullShitFunc, true);
        bullShitSchedule.addTimeInterval(l1);
        LoadDistributionInterval l2 = new LoadDistributionInterval(62490.0, DecentralizedSmartCharger.SECONDSPERDAY, bullShitFunc2, false);
        bullShitSchedule.addTimeInterval(l2);
        return bullShitSchedule;
    }

    public HubLinkMapping mapHubsTest() {
        HubLinkMapping hubLinkMapping = new HubLinkMapping(1);
        for (Link link : controler.getNetwork().getLinks().values()) {
            hubLinkMapping.addMapping(link.getId().toString(), 1);
        }
        return hubLinkMapping;
    }

    public static Schedule makeBullshitPricingScheduleTest(double optimal, double suboptimal) throws IOException {
        Schedule bullShitSchedule = new Schedule();
        PolynomialFunction pOpt = new PolynomialFunction(new double[] { optimal });
        PolynomialFunction pSubopt = new PolynomialFunction(new double[] { suboptimal });
        LoadDistributionInterval l1 = new LoadDistributionInterval(0.0, 62490.0, pOpt, true);
        bullShitSchedule.addTimeInterval(l1);
        LoadDistributionInterval l2 = new LoadDistributionInterval(62490.0, DecentralizedSmartCharger.SECONDSPERDAY, pSubopt, false);
        bullShitSchedule.addTimeInterval(l2);
        return bullShitSchedule;
    }

    public Schedule makeBullshitScheduleTestConstant() throws IOException {
        Schedule bullShitSchedule = new Schedule();
        double[] bullshitCoeffs = new double[] { 1.0 };
        double[] bullshitCoeffs2 = new double[] { 3.0 };
        PolynomialFunction bullShitFunc = new PolynomialFunction(bullshitCoeffs);
        PolynomialFunction bullShitFunc2 = new PolynomialFunction(bullshitCoeffs2);
        LoadDistributionInterval l1 = new LoadDistributionInterval(0.0, 62490.0, bullShitFunc, true);
        bullShitSchedule.addTimeInterval(l1);
        LoadDistributionInterval l2 = new LoadDistributionInterval(62490.0, DecentralizedSmartCharger.SECONDSPERDAY, bullShitFunc2, false);
        bullShitSchedule.addTimeInterval(l2);
        return bullShitSchedule;
    }

    public static Schedule makeBullshitScheduleTestLinear() throws IOException {
        Schedule bullShitSchedule = new Schedule();
        double gasPriceInCostPerSecond = DecentralizedSmartCharger.STANDARDCONNECTIONSWATT * 1 / (43.0 * 1000000.0) * 0.25;
        double[] bullshitCoeffs = new double[] { 0, 2 * gasPriceInCostPerSecond / 62490.0 };
        double[] bullshitCoeffs2 = new double[] { suboptimalPrice };
        PolynomialFunction bullShitFunc = new PolynomialFunction(bullshitCoeffs);
        PolynomialFunction bullShitFunc2 = new PolynomialFunction(bullshitCoeffs2);
        LoadDistributionInterval l1 = new LoadDistributionInterval(0.0, 62490.0, bullShitFunc, false);
        bullShitSchedule.addTimeInterval(l1);
        LoadDistributionInterval l2 = new LoadDistributionInterval(62490.0, DecentralizedSmartCharger.SECONDSPERDAY, bullShitFunc2, false);
        bullShitSchedule.addTimeInterval(l2);
        return bullShitSchedule;
    }
}
