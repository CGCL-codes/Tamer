package playground.ikaddoura.busCorridor.busCorridorWelfareAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.utils.misc.Time;

/**
 * @author Ihab
 *
 */
public class ExternalControler {

    private static final Logger log = Logger.getLogger(ExternalControler.class);

    static String networkFile = "../../shared-svn/studies/ihab/busCorridor/input/network80links.xml";

    static String configFile = "../../shared-svn/studies/ihab/busCorridor/input/config_busline.xml";

    static String populationFile = "../../shared-svn/studies/ihab/busCorridor/input/output_plans_withTimeChoice_9min.xml";

    static String outputExternalIterationDirPath = "../../shared-svn/studies/ihab/busCorridor/output/test";

    static int lastExternalIteration = 0;

    static int lastInternalIteration = 0;

    TimePeriod p1 = new TimePeriod(1, "DAY", 8, 4 * 3600, 24 * 3600);

    private final double MONEY_UTILS = 0.14026;

    private double fare = -2.0;

    private int capacity = 100;

    private int extItNr;

    private String directoryExtIt;

    private int maxNumberOfBuses;

    private Map<Integer, TimePeriod> day = new HashMap<Integer, TimePeriod>();

    PtLegHandler ptLegHandler = new PtLegHandler();

    private SortedMap<Integer, Double> iteration2operatorProfit = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2operatorCosts = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2operatorRevenue = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2numberOfBuses = new TreeMap<Integer, Double>();

    private SortedMap<Integer, String> iteration2day = new TreeMap<Integer, String>();

    private SortedMap<Integer, Double> iteration2userScore = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2userScoreSum = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2totalScore = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Integer> iteration2numberOfCarLegs = new TreeMap<Integer, Integer>();

    private SortedMap<Integer, Integer> iteration2numberOfPtLegs = new TreeMap<Integer, Integer>();

    private SortedMap<Integer, Integer> iteration2numberOfWalkLegs = new TreeMap<Integer, Integer>();

    private SortedMap<Integer, Double> iteration2fare = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Double> iteration2capacity = new TreeMap<Integer, Double>();

    private SortedMap<Integer, Map<Id, Double>> iteration2personId2waitTime = new TreeMap<Integer, Map<Id, Double>>();

    public static void main(final String[] args) throws IOException {
        ExternalControler simulation = new ExternalControler();
        simulation.externalIteration();
    }

    private void externalIteration() throws IOException {
        day.put(p1.getOrderId(), p1);
        ChartFileWriter chartWriter = new ChartFileWriter();
        TextFileWriter stats = new TextFileWriter();
        for (int extIt = 0; extIt <= lastExternalIteration; extIt++) {
            log.info("************* EXTERNAL ITERATION " + extIt + " BEGINS *************");
            this.setExtItNr(extIt);
            this.setDirectoryExtIt(outputExternalIterationDirPath + "/extITERS/extIt." + extIt);
            File directory = new File(this.getDirectoryExtIt());
            directory.mkdirs();
            VehicleScheduleWriter transitWriter = new VehicleScheduleWriter(this.day, this.getCapacity(), networkFile, this.getDirectoryExtIt());
            transitWriter.writeTransit();
            this.setDay(transitWriter.getNewDay());
            this.setMaxNumberOfBuses(this.day);
            InternalControler internalControler = new InternalControler(configFile, this.extItNr, this.getDirectoryExtIt(), lastInternalIteration, populationFile, outputExternalIterationDirPath, this.getMaxNumberOfBuses(), networkFile, fare, MONEY_UTILS, ptLegHandler);
            internalControler.run();
            Operator operator = new Operator(this.getMaxNumberOfBuses(), this.getCapacity());
            Users users = new Users(this.getDirectoryExtIt(), networkFile, MONEY_UTILS);
            OperatorUserAnalysis analysis = new OperatorUserAnalysis(this.directoryExtIt, lastInternalIteration, networkFile);
            analysis.readEvents(operator, users, this.day);
            users.calculateScore();
            operator.calculateScore();
            this.iteration2operatorProfit.put(this.getExtItNr(), operator.getProfit());
            this.iteration2operatorCosts.put(this.getExtItNr(), operator.getCosts());
            this.iteration2operatorRevenue.put(this.getExtItNr(), operator.getRevenue());
            this.iteration2numberOfBuses.put(this.getExtItNr(), (double) this.getMaxNumberOfBuses());
            this.iteration2day.put(this.getExtItNr(), this.day.toString());
            this.iteration2userScoreSum.put(this.getExtItNr(), users.getLogSum());
            this.iteration2userScore.put(this.getExtItNr(), users.getAvgExecScore());
            this.iteration2totalScore.put(this.getExtItNr(), (users.getLogSum() + operator.getProfit()));
            this.iteration2numberOfCarLegs.put(this.getExtItNr(), users.getNumberOfCarLegs());
            this.iteration2numberOfPtLegs.put(this.getExtItNr(), users.getNumberOfPtLegs());
            this.iteration2numberOfWalkLegs.put(this.getExtItNr(), users.getNumberOfWalkLegs());
            this.iteration2fare.put(this.getExtItNr(), this.getFare());
            this.iteration2capacity.put(this.getExtItNr(), (double) this.getCapacity());
            this.iteration2personId2waitTime.put(this.getExtItNr(), ptLegHandler.getPersonId2WaitingTime());
            stats.writeFile(outputExternalIterationDirPath, this.iteration2numberOfBuses, this.iteration2day, this.iteration2fare, this.iteration2capacity, this.iteration2operatorCosts, this.iteration2operatorRevenue, this.iteration2operatorProfit, this.iteration2userScore, this.iteration2userScoreSum, this.iteration2totalScore, this.iteration2numberOfCarLegs, this.iteration2numberOfPtLegs, this.iteration2numberOfWalkLegs);
            stats.writeWaitingTimes(outputExternalIterationDirPath, this.extItNr, this.iteration2personId2waitTime.get(this.extItNr));
            chartWriter.writeChart_Parameters(outputExternalIterationDirPath, this.iteration2numberOfBuses, "Number of buses per iteration", "NumberOfBuses");
            chartWriter.writeChart_Parameters(outputExternalIterationDirPath, this.iteration2capacity, "Vehicle capacity per iteration", "Capacity");
            chartWriter.writeChart_Parameters(outputExternalIterationDirPath, this.iteration2fare, "Bus fare per iteration", "Fare");
            chartWriter.writeChart_LegModes(outputExternalIterationDirPath, this.iteration2numberOfCarLegs, this.iteration2numberOfPtLegs);
            chartWriter.writeChart_UserScores(outputExternalIterationDirPath, this.iteration2userScore);
            chartWriter.writeChart_UserScoresSum(outputExternalIterationDirPath, this.iteration2userScoreSum);
            chartWriter.writeChart_TotalScore(outputExternalIterationDirPath, this.iteration2totalScore);
            chartWriter.writeChart_OperatorScores(outputExternalIterationDirPath, this.iteration2operatorProfit, this.iteration2operatorCosts, this.iteration2operatorRevenue);
            if (this.getExtItNr() < lastExternalIteration) {
                this.setDay(increaseNumberOfBusesAllTimePeriods(1));
            }
            log.info("************* EXTERNAL ITERATION " + extIt + " ENDS *************");
        }
    }

    private Map<Integer, TimePeriod> increaseBuses(String periodId, int increase) {
        Map<Integer, TimePeriod> dayMod = this.getDay();
        int period = 0;
        for (TimePeriod tt : dayMod.values()) {
            if (tt.getId().equals(periodId)) {
                period = tt.getOrderId();
            }
        }
        if (dayMod.containsKey(period)) {
            dayMod.get(period).increaseNumberOfBuses(increase);
        }
        return dayMod;
    }

    private Map<Integer, TimePeriod> extend(String periodId, double time) {
        Map<Integer, TimePeriod> dayNextExtIt = this.getDay();
        int period = 0;
        for (TimePeriod timePeriod : dayNextExtIt.values()) {
            if (timePeriod.getId().equals(periodId)) {
                period = timePeriod.getOrderId();
            }
        }
        if (dayNextExtIt.containsKey(period)) {
            dayNextExtIt.get(period).changeFromTime(-time / 2);
            dayNextExtIt.get(period).changeToTime(time / 2);
            if (dayNextExtIt.containsKey(period - 1)) {
                dayNextExtIt.get(period - 1).changeToTime(-time / 2);
            }
            if (dayNextExtIt.containsKey(period + 1)) {
                dayNextExtIt.get(period + 1).changeFromTime(time / 2);
            }
        }
        return dayNextExtIt;
    }

    private Map<Integer, TimePeriod> increaseNumberOfBusesAllTimePeriods(int i) {
        Map<Integer, TimePeriod> dayNextExtIt = new HashMap<Integer, TimePeriod>();
        for (TimePeriod t : this.getDay().values()) {
            TimePeriod t2 = t;
            t2.increaseNumberOfBuses(i);
            dayNextExtIt.put(t.getOrderId(), t2);
        }
        return dayNextExtIt;
    }

    public int getMaxNumberOfBuses() {
        return maxNumberOfBuses;
    }

    public void setMaxNumberOfBuses(Map<Integer, TimePeriod> day) {
        int maxBusNumber = 0;
        for (TimePeriod t : day.values()) {
            if (t.getNumberOfBuses() > maxBusNumber) {
                maxBusNumber = t.getNumberOfBuses();
            }
        }
        log.info("Total number of Vehicles: " + maxBusNumber);
        this.maxNumberOfBuses = maxBusNumber;
    }

    /**
	 * @return the extItNr
	 */
    public int getExtItNr() {
        return extItNr;
    }

    /**
	 * @param extItNr the extItNr to set
	 */
    public void setExtItNr(int extItNr) {
        this.extItNr = extItNr;
    }

    /**
	 * @return the directoryExtIt
	 */
    public String getDirectoryExtIt() {
        return directoryExtIt;
    }

    /**
	 * @param directoryExtIt the directoryExtIt to set
	 */
    public void setDirectoryExtIt(String directoryExtIt) {
        this.directoryExtIt = directoryExtIt;
    }

    /**
	 * @return the fare
	 */
    public double getFare() {
        return fare;
    }

    /**
	 * @param fare the fare to set
	 */
    public void setFare(double fare) {
        this.fare = fare;
    }

    /**
	 * @return the capacity
	 */
    public int getCapacity() {
        return capacity;
    }

    /**
	 * @param capacity the capacity to set
	 */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
	 * @return the day
	 */
    public Map<Integer, TimePeriod> getDay() {
        return day;
    }

    /**
	 * @param day the day to set
	 */
    public void setDay(Map<Integer, TimePeriod> day) {
        this.day = day;
    }
}
