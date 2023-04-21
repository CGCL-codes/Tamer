package playground.wrashid.sschieffer.V2G;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import playground.wrashid.sschieffer.DSC.DecentralizedSmartCharger;
import playground.wrashid.sschieffer.DecentralizedSmartCharger.TestSimulationSetUp;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.LoadDistributionInterval;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.ParkingInterval;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.Schedule;
import playground.wrashid.sschieffer.SetUp.IntervalScheduleClasses.TimeDataCollector;
import junit.framework.TestCase;
import lpsolve.LpSolveException;

/**
 * tests methods:
 * <li> checks if adding revenue from V2G works and statistics calc function works
 * <li> checks the most important V2G function reschedule at the example of one agent
 * 
 * @author Stella
 *
 */
public class V2GTestOnePlan extends TestCase {

    String configPath = "test/input/playground/wrashid/sschieffer/config_plans1.xml";

    final String outputPath = "D:\\ETH\\MasterThesis\\TestOutput\\";

    public Id agentOne = null;

    Controler controler;

    final double electrification = 1.0;

    final double ev = 0.0;

    final double bufferBatteryCharge = 0.0;

    final double standardChargingSlotLength = 15 * 60;

    double compensationPerKWHRegulationUp = 0.15;

    double compensationPerKWHRegulationDown = 0.15;

    double compensationPERKWHFeedInVehicle = 0.15;

    double xPercentNone = 0;

    double xPercentDown = 0;

    double xPercentDownUp = 1.0;

    public static DecentralizedSmartCharger myDecentralizedSmartCharger;

    public static void testMain(String[] args) throws MaxIterationsExceededException, OptimizationException, FunctionEvaluationException, IllegalArgumentException, LpSolveException, IOException {
    }

    /**
	*  
	 * @throws MaxIterationsExceededException
	 * @throws FunctionEvaluationException
	 * @throws IllegalArgumentException
	 * @throws LpSolveException
	 * @throws OptimizationException
	 * @throws IOException
	 */
    public void testV2GCheckVehicles() throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException, LpSolveException, OptimizationException, IOException {
        final TestSimulationSetUp mySimulation = new TestSimulationSetUp(configPath, electrification, ev);
        controler = mySimulation.getControler();
        controler.addControlerListener(new IterationEndsListener() {

            @Override
            public void notifyIterationEnds(IterationEndsEvent event) {
                try {
                    mySimulation.setUpStochasticLoadDistributions();
                    myDecentralizedSmartCharger = mySimulation.setUpSmartCharger(outputPath, bufferBatteryCharge, standardChargingSlotLength);
                    myDecentralizedSmartCharger.run();
                    HashMap<Integer, Schedule> stochasticLoad = mySimulation.getStochasticLoadSchedule();
                    HashMap<Id, Schedule> agentVehicleSourceMapping = mySimulation.getAgentStochasticLoadSources();
                    myDecentralizedSmartCharger.setStochasticSources(stochasticLoad, null, agentVehicleSourceMapping);
                    mySimulation.setUpAgentSchedules(myDecentralizedSmartCharger, compensationPerKWHRegulationUp, compensationPerKWHRegulationDown, compensationPERKWHFeedInVehicle, xPercentDown, xPercentDownUp);
                    myDecentralizedSmartCharger.setAgentContracts(mySimulation.getAgentContracts());
                    myDecentralizedSmartCharger.myV2G.initializeAgentStats();
                    myDecentralizedSmartCharger.setV2GRegUpAndDownStats(xPercentDown, xPercentDownUp);
                    for (Id id : mySimulation.getControler().getPopulation().getPersons().keySet()) {
                        agentOne = id;
                        break;
                    }
                    checkRevenueStats();
                    checkRescheduling();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        controler.run();
    }

    public void checkRescheduling() throws Exception {
        myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).printSchedule();
        double chargingTimeFirstInterval = ((ParkingInterval) myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).timesInSchedule.get(0)).getRequiredChargingDuration();
        double costChargingBefore = myDecentralizedSmartCharger.getChargingCostsForAgents().get(agentOne);
        Schedule answerScheduleAfterElectricSourceInterval = myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).cutScheduleAtTimeSecondHalf(21600, 0, agentOne);
        ((ParkingInterval) answerScheduleAfterElectricSourceInterval.timesInSchedule.get(1)).setRequiredChargingDuration(0);
        LoadDistributionInterval electricSourceInterval = new LoadDistributionInterval(0, 21600, 1);
        myDecentralizedSmartCharger.myV2G.reschedule(agentOne, answerScheduleAfterElectricSourceInterval, electricSourceInterval, 1, 0, 3500);
        double costChargingAfter = myDecentralizedSmartCharger.getChargingCostsForAgents().get(agentOne);
        assertEquals(costChargingBefore - 1, costChargingAfter);
        double newChargingTimeFirstInterval = ((ParkingInterval) myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).timesInSchedule.get(0)).getRequiredChargingDuration();
        assertEquals(newChargingTimeFirstInterval - chargingTimeFirstInterval, 1.0);
        double newChargingTimeThirdInterval = ((ParkingInterval) myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).timesInSchedule.get(2)).getRequiredChargingDuration();
        assertEquals(newChargingTimeThirdInterval, 0.0);
        Schedule newChargingSchedule = ((ParkingInterval) myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).timesInSchedule.get(0)).getChargingSchedule();
        assertEquals(newChargingTimeFirstInterval, newChargingSchedule.getTotalTimeOfIntervalsInSchedule());
        newChargingSchedule = ((ParkingInterval) myDecentralizedSmartCharger.getAllAgentParkingAndDrivingSchedules().get(agentOne).timesInSchedule.get(2)).getChargingSchedule();
        assertEquals(newChargingSchedule, null);
    }

    public void checkRevenueStats() {
        double revenue = myDecentralizedSmartCharger.getV2GRevenueForAgent(agentOne);
        myDecentralizedSmartCharger.myV2G.addRevenueToAgentFromV2G(100.0, agentOne);
        double revenueNew = myDecentralizedSmartCharger.getV2GRevenueForAgent(agentOne);
        assertEquals(revenue + 100.0, revenueNew);
        double up = myDecentralizedSmartCharger.myV2G.getTotalRegulationUp();
        myDecentralizedSmartCharger.myV2G.addJoulesUpDownToAgentStats(-1000.0, agentOne);
        myDecentralizedSmartCharger.myV2G.calcV2GVehicleStats();
        double newUp = myDecentralizedSmartCharger.myV2G.getTotalRegulationUp();
        assertEquals(-1000.0, newUp - up);
        double down = myDecentralizedSmartCharger.myV2G.getTotalRegulationDown();
        myDecentralizedSmartCharger.myV2G.addJoulesUpDownToAgentStats(1000.0, agentOne);
        myDecentralizedSmartCharger.myV2G.calcV2GVehicleStats();
        double newDown = myDecentralizedSmartCharger.myV2G.getTotalRegulationDown();
        assertEquals(1000.0, newDown - down);
        myDecentralizedSmartCharger.myV2G.calcV2GVehicleStats();
        assertEquals(myDecentralizedSmartCharger.myV2G.getAverageV2GRevenuePHEV(), myDecentralizedSmartCharger.myV2G.getAverageV2GRevenueAgent());
    }
}
