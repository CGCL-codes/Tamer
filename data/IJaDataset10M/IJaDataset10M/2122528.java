package org.yarl.db;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.yarl.util.Log4jConfigurator;
import junit.framework.TestCase;

public class StatisticTest extends TestCase {

    private static Logger log4j = Logger.getLogger(StatisticTest.class);

    private Person person;

    private Statistic statistic;

    private WorkoutType workoutType;

    static YarlDatabase db;

    public StatisticTest() {
        Log4jConfigurator.configure();
        if (db == null) {
            db = YarlDatabase.getInstance("yarldb");
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        person = PersonDelegate.findDefaultPerson();
        log4j.info("person: " + person.toString());
        statistic = new Statistic();
        workoutType = WorkoutTypeDelegate.findDefaultWorkoutType();
        log4j.info("workoutType: " + workoutType.toString());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetWorkoutType() {
        statistic.setWorkoutType(workoutType);
        assertEquals(workoutType, statistic.getWorkoutType());
    }

    public void testSetNumberOfWorkouts() {
        statistic.setNumberOfWorkouts(12345);
        assertEquals(statistic.getNumberOfWorkouts(), 12345);
        statistic.setNumberOfWorkouts(123);
        assertEquals(statistic.getNumberOfWorkouts(), 123);
    }

    public void testSetTotalDistance() {
        statistic.setTotalDistance(123.45);
        assertEquals(statistic.getTotalDistance(), 123.45);
    }

    public void testSetTotalTime() {
        statistic.setTotalTime(123456);
        assertEquals(statistic.getTotalTime(), 123456);
        log4j.info("displayTotalTime: " + statistic.getDisplayTotalTime());
        assertEquals(statistic.getDisplayTotalTime(), "00:02:03.456");
    }

    public void testSetAveragePace() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, 6, 35);
        log4j.info(calendar.getTime().toString());
        statistic.setAveragePace(((6 * 60) + 35) * 1000);
        assertEquals(statistic.getAveragePace(), 395000);
    }

    public void testSetAverageSpeed() {
        double averageSpeed = 10.01;
        statistic.setAverageSpeed(averageSpeed);
        assertEquals(statistic.getAverageSpeed(), averageSpeed);
        averageSpeed = 0;
        statistic.setAverageSpeed(averageSpeed);
        assertEquals(statistic.getAverageSpeed(), averageSpeed);
        averageSpeed = 3.1415926;
        statistic.setAverageSpeed(averageSpeed);
        assertEquals(statistic.getAverageSpeed(), averageSpeed);
    }

    public void testSetAverageWeight() {
        double averageWeight = 134.5;
        statistic.setAverageWeight(averageWeight);
        assertEquals(statistic.getAverageWeight(), averageWeight);
        averageWeight = 156.5;
        statistic.setAverageWeight(averageWeight);
        assertEquals(statistic.getAverageWeight(), averageWeight);
        averageWeight = 124.75;
        statistic.setAverageWeight(averageWeight);
        assertEquals(statistic.getAverageWeight(), averageWeight);
    }

    public void testUpdateStats() {
        Calendar starting = Calendar.getInstance();
        starting.add(Calendar.DAY_OF_YEAR, -365);
        Calendar ending = Calendar.getInstance();
        ending.add(Calendar.DAY_OF_YEAR, -335);
        Collection<Workout> collectionOfWorkouts = WorkoutDelegate.findWorkouts(person, starting.getTime(), ending.getTime());
        Iterator<Workout> iter = collectionOfWorkouts.iterator();
        double totalDistance = 0.00;
        int numberOfWorkouts = 0;
        long totalTime = 0;
        while (iter.hasNext()) {
            Workout workout = (Workout) iter.next();
            totalDistance += workout.getTotalDistance();
            totalTime += workout.getTotalTime();
            numberOfWorkouts++;
            log4j.info("total distance is now: " + totalDistance);
            log4j.info("total time is now    : " + totalTime);
            statistic.updateStats(workout);
        }
        log4j.info("totalDistance: " + totalDistance);
        log4j.info("stat totalDistance: " + statistic.getTotalDistance());
        assertEquals(statistic.getTotalDistance(), totalDistance);
        log4j.info("totalTime: " + totalTime);
        log4j.info("stat totalTime: " + statistic.getTotalTime());
        assertEquals(statistic.getTotalTime(), totalTime);
        assertEquals(statistic.getNumberOfWorkouts(), numberOfWorkouts);
    }

    public void testSetNumberOfDays() {
        int numberOfDays = 1;
        statistic.setNumberOfDays(numberOfDays);
        assertEquals(statistic.getNumberOfDays(), numberOfDays);
        numberOfDays = 0;
        statistic.setNumberOfDays(numberOfDays);
        assertEquals(statistic.getNumberOfDays(), numberOfDays);
        numberOfDays = 1234;
        statistic.setNumberOfDays(numberOfDays);
        assertEquals(statistic.getNumberOfDays(), numberOfDays);
    }
}
