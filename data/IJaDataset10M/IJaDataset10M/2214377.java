package org.ncsa.foodlog.calculations.hibernate;

import org.ncsa.foodlog.data.Daily;
import org.ncsa.foodlog.repository.hibernate.Queries;

public class HWaterWeight {

    private Summary sum;

    private int dailyMaintenanceCalories = 0;

    private int foodCalorieDeficit = 0;

    private int totalCalorieDeficit = 0;

    private double predictedScaleLoss = 0.0;

    private int actualWeight = 0;

    private Queries queries;

    public HWaterWeight(Queries queries) {
        this.queries = queries;
        sum = new Summary(queries);
    }

    public void calculate() {
        searchForWeight();
        if (actualWeight == 0) return;
        dailyMaintenanceCalories = 11 * actualWeight;
        int actualFoodCalories = sum.getMealCalories();
        int actualPACalories = sum.getPACalories();
        foodCalorieDeficit = (dailyMaintenanceCalories * 7) - actualFoodCalories;
        totalCalorieDeficit = foodCalorieDeficit + actualPACalories;
        predictedScaleLoss = (double) totalCalorieDeficit / 3500.0;
    }

    private void searchForWeight() {
        Daily latest = queries.latestWeight();
        if (latest == null) {
            actualWeight = 0;
            return;
        }
        actualWeight = latest.getWeight();
    }

    public Summary getSum() {
        return sum;
    }

    public void setSum(Summary accum) {
        this.sum = accum;
    }

    public int getDailyMaintenanceCalories() {
        return dailyMaintenanceCalories;
    }

    public int getFoodCalorieDeficit() {
        return foodCalorieDeficit;
    }

    public double getPredictedScaleLoss() {
        return predictedScaleLoss;
    }

    public int getTotalCalorieDeficit() {
        return totalCalorieDeficit;
    }

    public String dump() {
        String result = "Water Weight Percentages:\n";
        result += "\tActual Weight= " + actualWeight + "\n";
        int actualFoodCalories = sum.getMealCalories();
        int actualPACalories = sum.getPACalories();
        result += "\tActual Food Calories= " + actualFoodCalories + "\n";
        result += "\tActual PA Calories= " + actualPACalories + "\n";
        result += "\tDaily Maintenance Calories=" + dailyMaintenanceCalories + "\n";
        result += "\tFood Calorie Deficit= " + foodCalorieDeficit + "\n";
        result += "\tTotal Calorie Deficit = " + totalCalorieDeficit + "\n";
        result += "\tPredicted Scale Loss = " + predictedScaleLoss + "\n";
        return result;
    }
}
