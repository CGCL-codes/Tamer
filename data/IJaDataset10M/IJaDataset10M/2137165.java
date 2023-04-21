package jp.locky.research.gmm;

public class Particle {

    private String imgName;

    private double locX;

    private double locY;

    private double weight;

    private double likelihood;

    public Particle() {
        this.likelihood = 1;
    }

    public Particle(double locX, double locY) {
        this.locX = locX;
        this.locY = locY;
        this.likelihood = 1;
    }

    public Particle(double locX, double locY, double weight) {
        this.locX = locX;
        this.locY = locY;
        this.weight = weight;
        this.likelihood = 1;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public double getLocX() {
        return locX;
    }

    public void setLocX(double locX) {
        this.locX = locX;
    }

    public double getLocY() {
        return locY;
    }

    public void setLocY(double locY) {
        this.locY = locY;
    }

    public void setLoc(double locX, double locY) {
        this.setLocX(locX);
        this.setLocY(locY);
    }

    public void addLocX(double locX) {
        this.locX += locX;
    }

    public void addLocY(double locY) {
        this.locY += locY;
    }

    public void addLoc(double locX, double locY) {
        this.addLocX(locX);
        this.addLocY(locY);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }
}
