package us.wthr.jdem846.gis.datetime;

public class SolarPosition {

    private double azimuth;

    private double elevation;

    private double equationOfTime;

    private double solarDeclination;

    private double zenithAngle;

    private EarthDateTime solarNoon;

    private EarthDateTime apparentSunriseUtc;

    private EarthDateTime apprentSunsetUtc;

    private EarthDateTime apparentSunrise;

    private EarthDateTime apprentSunset;

    public SolarPosition() {
    }

    public double getZenithAngle() {
        return zenithAngle;
    }

    public void setZenithAngle(double zenithAngle) {
        this.zenithAngle = zenithAngle;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getEquationOfTime() {
        return equationOfTime;
    }

    public void setEquationOfTime(double equationOfTime) {
        this.equationOfTime = equationOfTime;
    }

    public double getSolarDeclination() {
        return solarDeclination;
    }

    public void setSolarDeclination(double solarDeclination) {
        this.solarDeclination = solarDeclination;
    }

    public EarthDateTime getSolarNoon() {
        return solarNoon;
    }

    public void setSolarNoon(EarthDateTime solarNoon) {
        this.solarNoon = solarNoon;
    }

    public EarthDateTime getApparentSunriseUtc() {
        return apparentSunriseUtc;
    }

    public void setApparentSunriseUtc(EarthDateTime apparentSunriseUtc) {
        this.apparentSunriseUtc = apparentSunriseUtc;
    }

    public EarthDateTime getApprentSunsetUtc() {
        return apprentSunsetUtc;
    }

    public void setApprentSunsetUtc(EarthDateTime apprentSunsetUtc) {
        this.apprentSunsetUtc = apprentSunsetUtc;
    }

    public EarthDateTime getApparentSunrise() {
        return apparentSunrise;
    }

    public void setApparentSunrise(EarthDateTime apparentSunrise) {
        this.apparentSunrise = apparentSunrise;
    }

    public EarthDateTime getApprentSunset() {
        return apprentSunset;
    }

    public void setApprentSunset(EarthDateTime apprentSunset) {
        this.apprentSunset = apprentSunset;
    }
}
