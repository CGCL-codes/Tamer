package TaskProfile;

import java.awt.Color;
import System.ConvertDoubleToTimeString;

public class ProfilePointView {

    public ProfilePointView(String name) {
        profileID = name;
        color = getNextColor();
    }

    public void registerCall(double time) {
        if (minTime > time) {
            minTime = time;
        }
        if (maxTime < time) {
            maxTime = time;
        }
        meanTime = (nbCall * meanTime + time) / (nbCall + 1);
        nbCall++;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileId) {
        profileID = profileId;
    }

    public long getNbCall() {
        return nbCall;
    }

    public void setNbCall(long nbCall) {
        this.nbCall = nbCall;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public double getMinTime() {
        return minTime;
    }

    public void setMinTime(double minTime) {
        this.minTime = minTime;
    }

    public double getMeanTime() {
        return meanTime;
    }

    public void setMeanTime(double meanTime) {
        this.meanTime = meanTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString() {
        String ret = "Name : " + profileID + "\nNumber of calls : " + nbCall + "\nMaximum time : " + ConvertDoubleToTimeString.convert(maxTime) + "\nMinimum time : " + ConvertDoubleToTimeString.convert(minTime) + "\nMean time : " + ConvertDoubleToTimeString.convert(meanTime);
        return ret;
    }

    private static Color getNextColor() {
        CurrentColorPtr++;
        if (CurrentColorPtr > (colorTable.length - 1)) {
            CurrentColorPtr = 0;
        }
        return colorTable[CurrentColorPtr];
    }

    private String profileID = "";

    private long nbCall = 0;

    private double maxTime = 0;

    private double minTime = 10000;

    private double meanTime = 0;

    private Color color;

    private static Color colorTable[] = { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.PINK, Color.YELLOW, Color.CYAN, Color.GRAY, Color.ORANGE, Color.RED, Color.LIGHT_GRAY };

    private static int CurrentColorPtr = 0;
}
