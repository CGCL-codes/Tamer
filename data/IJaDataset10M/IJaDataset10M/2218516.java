package playground.scnadine.postprocessChoiceSets;

import java.util.GregorianCalendar;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;

public class CarModelChoiceSetStage extends CarChoiceSetStage {

    private double avNumberOfTripsPerDay = 0;

    private double avNumberOfStagesPerDay = 0;

    private double avNumberOfWalkStagesPerDay = 0;

    private double avNumberOfBikeStagesPerDay = 0;

    private double avNumberOfCarStagesPerDay = 0;

    private double avNumberOfUPuTStagesPerDay = 0;

    private double avNumberOfRailStagesPerDay = 0;

    private double dailyTotalMileage = 0;

    private double dailyWalkMileage = 0;

    private double dailyBikeMileage = 0;

    private double dailyCarMileage = 0;

    private double dailyUPuTMileage = 0;

    private double dailyRailMileage = 0;

    private double avTravelSpeedTotal = 0;

    private double avTravelSpeedWalk = 0;

    private double avTravelSpeedBike = 0;

    private double avTravelSpeedCar = 0;

    private double avTravelSpeedUPuT = 0;

    private double avTravelSpeedRail = 0;

    private int townOfResidence = 0;

    public CarModelChoiceSetStage(Id personId, Id tripId, Id stageId, GregorianCalendar startTime, Node startNode, Node endNode, Id chosenRouteId, Network network, String calculationBase) {
        super(personId, tripId, stageId, startTime, startNode, endNode, chosenRouteId);
        this.choiceSetRoutes = new CarModelChoiceSetRoutes(network, calculationBase);
    }

    public void setAvNumberOfBikeStagesPerDay(double avNumberOfBikeStagesPerDay) {
        this.avNumberOfBikeStagesPerDay = avNumberOfBikeStagesPerDay;
    }

    public void setAvNumberOfCarStagesPerDay(double avNumberOfCarStagesPerDay) {
        this.avNumberOfCarStagesPerDay = avNumberOfCarStagesPerDay;
    }

    public void setAvNumberOfRailStagesPerDay(double avNumberOfRailStagesPerDay) {
        this.avNumberOfRailStagesPerDay = avNumberOfRailStagesPerDay;
    }

    public void setAvNumberOfStagesPerDay(double avNumberOfStagesPerDay) {
        this.avNumberOfStagesPerDay = avNumberOfStagesPerDay;
    }

    public void setAvNumberOfTripsPerDay(double avNumberOfTripsPerDay) {
        this.avNumberOfTripsPerDay = avNumberOfTripsPerDay;
    }

    public void setAvNumberOfUPuTStagesPerDay(double avNumberOfUPuTStagesPerDay) {
        this.avNumberOfUPuTStagesPerDay = avNumberOfUPuTStagesPerDay;
    }

    public void setAvNumberOfWalkStagesPerDay(double avNumberOfWalkStagesPerDay) {
        this.avNumberOfWalkStagesPerDay = avNumberOfWalkStagesPerDay;
    }

    public void setAvTravelSpeedBike(double avTravelSpeedBike) {
        this.avTravelSpeedBike = avTravelSpeedBike;
    }

    public void setAvTravelSpeedCar(double avTravelSpeedCar) {
        this.avTravelSpeedCar = avTravelSpeedCar;
    }

    public void setAvTravelSpeedRail(double avTravelSpeedRail) {
        this.avTravelSpeedRail = avTravelSpeedRail;
    }

    public void setAvTravelSpeedTotal(double avTravelSpeedTotal) {
        this.avTravelSpeedTotal = avTravelSpeedTotal;
    }

    public void setAvTravelSpeedUPuT(double avTravelSpeedUPuT) {
        this.avTravelSpeedUPuT = avTravelSpeedUPuT;
    }

    public void setAvTravelSpeedWalk(double avTravelSpeedWalk) {
        this.avTravelSpeedWalk = avTravelSpeedWalk;
    }

    public void setDailyBikeMileage(double dailyBikeMileage) {
        this.dailyBikeMileage = dailyBikeMileage;
    }

    public void setDailyCarMileage(double dailyCarMileage) {
        this.dailyCarMileage = dailyCarMileage;
    }

    public void setDailyRailMileage(double dailyRailMileage) {
        this.dailyRailMileage = dailyRailMileage;
    }

    public void setDailyTotalMileage(double dailyTotalMileage) {
        this.dailyTotalMileage = dailyTotalMileage;
    }

    public void setDailyUPuTMileage(double dailyUPuTMileage) {
        this.dailyUPuTMileage = dailyUPuTMileage;
    }

    public void setDailyWalkMileage(double dailyWalkMileage) {
        this.dailyWalkMileage = dailyWalkMileage;
    }

    public void setTownOfResidence(int townOfResidence) {
        this.townOfResidence = townOfResidence;
    }

    public double getAvNumberOfBikeStagesPerDay() {
        return this.avNumberOfBikeStagesPerDay;
    }

    public double getAvNumberOfCarStagesPerDay() {
        return this.avNumberOfCarStagesPerDay;
    }

    public double getAvNumberOfRailStagesPerDay() {
        return this.avNumberOfRailStagesPerDay;
    }

    public double getAvNumberOfStagesPerDay() {
        return this.avNumberOfStagesPerDay;
    }

    public double getAvNumberOfTripsPerDay() {
        return this.avNumberOfTripsPerDay;
    }

    public double getAvNumberOfUPuTStagesPerDay() {
        return this.avNumberOfUPuTStagesPerDay;
    }

    public double getAvNumberOfWalkStagesPerDay() {
        return this.avNumberOfWalkStagesPerDay;
    }

    public double getAvTravelSpeedBike() {
        return this.avTravelSpeedBike;
    }

    public double getAvTravelSpeedCar() {
        return this.avTravelSpeedCar;
    }

    public double getAvTravelSpeedRail() {
        return this.avTravelSpeedRail;
    }

    public double getAvTravelSpeedTotal() {
        return this.avTravelSpeedTotal;
    }

    public double getAvTravelSpeedUPuT() {
        return this.avTravelSpeedUPuT;
    }

    public double getAvTravelSpeedWalk() {
        return this.avTravelSpeedWalk;
    }

    public double getDailyBikeMileage() {
        return this.dailyBikeMileage;
    }

    public double getDailyCarMileage() {
        return this.dailyCarMileage;
    }

    public double getDailyRailMileage() {
        return this.dailyRailMileage;
    }

    public double getDailyTotalMileage() {
        return this.dailyTotalMileage;
    }

    public double getDailyUPuTMileage() {
        return this.dailyUPuTMileage;
    }

    public double getDailyWalkMileage() {
        return this.dailyWalkMileage;
    }

    public int getTownOfResidence() {
        return this.townOfResidence;
    }
}
