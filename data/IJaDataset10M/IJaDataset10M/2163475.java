package playground.benjamin.analysis;

public class Row {

    private Double score1;

    private Double score2;

    private double scoreDiff;

    private double personalIncome;

    private double homeX;

    private double homeY;

    private boolean isCarAvail;

    private boolean isSelectedPlanCar;

    public Row() {
        this.setScore1(0.0);
        this.setScore2(0.0);
        this.setPersonalIncome(0.0);
        this.setHomeX(0.0);
        this.setHomeY(0.0);
        this.setCarAvail(null != null);
        this.setSelectedPlanCar(null != null);
    }

    public void setScore1(Double score1) {
        this.score1 = score1;
    }

    public Double getScore1() {
        return score1;
    }

    public void setScore2(Double score2) {
        this.score2 = score2;
    }

    public Double getScore2() {
        return score2;
    }

    public void setPersonalIncome(double personalIncome) {
        this.personalIncome = personalIncome;
    }

    public double getPersonalIncome() {
        return personalIncome;
    }

    public void setHomeX(double homeX) {
        this.homeX = homeX;
    }

    public double getHomeX() {
        return homeX;
    }

    public void setHomeY(double homeY) {
        this.homeY = homeY;
    }

    public double getHomeY() {
        return homeY;
    }

    public void setCarAvail(boolean isCarAvail) {
        this.isCarAvail = isCarAvail;
    }

    public boolean isCarAvail() {
        return isCarAvail;
    }

    public void setSelectedPlanCar(boolean isSelectedPlanCar) {
        this.isSelectedPlanCar = isSelectedPlanCar;
    }

    public boolean isSelectedPlanCar() {
        return isSelectedPlanCar;
    }

    public double getScoreDiff() {
        this.scoreDiff = this.score2 - this.score1;
        return this.scoreDiff;
    }
}
