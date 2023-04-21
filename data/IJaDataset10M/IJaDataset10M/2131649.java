package pathlossmodule;

import java.lang.Math.*;

/**
 *Class Okumura calculates the path loss based on Okumura Model.
 * This model gives path loss for suburban terrain. 
 * @author 05030037
 */
public class OkumuraModel {

    public double pt;

    public double gt;

    public double hte;

    public int fc;

    public float gArea;

    public int cellId;

    public double lambda;

    public double pl;

    public double pathLoss_FS;

    public double d;

    public int attn;

    public String areaType;

    public OkumuraModel() {
    }

    /**
  * This method calculates path loss in dB using Okumura Model
  * @param fc    the carrier frequency
  * @param d     the distance 
  * @param aType the terrain type  
  * @param hte   the height of the transmitter
  * @param gt    the gain of transmitted antenna
  * @param pt    the power transmitted
  * @return pl   the calculated path loss
  */
    public double calculateLoss(int fc, double d, String aType, double hte, double gt, double pt) {
        this.fc = fc;
        this.d = d;
        this.areaType = aType;
        this.attn = 0;
        this.hte = hte;
        this.pt = pt;
        this.gt = gt;
        this.d = this.d / 100;
        if (this.d <= 100) {
            double freeSpacePL = getFSPathLoss();
            int medianAttenuation = mAttentuation(this.fc, d);
            double gainHte = gainHte(hte);
            double gainHre = gainHre();
            double gainArea = gainArea(this.fc, areaType);
            pl = freeSpacePL + medianAttenuation - gainHte - gainHre - gainArea;
        } else {
            return 0.0;
        }
        return pl;
    }

    /**
 * This method calculated the value for lambda using the given carrier frequency
 * @return lambda 
 */
    public double getLambda() {
        lambda = 300000000 / (double) this.fc * Constants.MEGA;
        lambda = lambda * lambda;
        return lambda;
    }

    /**
 * This method calculates the free space path loss. The value for System Loss 
 * Factor is taken as 1.Also Receiver Gain (gr) is contant which is 1.7m
 * @return pathLoss_FS;
 */
    public double getFSPathLoss() {
        double distance = this.d * Constants.KILO;
        double lmd = getLambda();
        pathLoss_FS = (this.pt * this.gt * Constants.GAIN_RECEIVER * lmd) / ((Constants.PIE_FACTOR) * (Constants.SYSTEM_LOSS) * (distance * distance));
        pathLoss_FS = 10 * (java.lang.Math.log10(pathLoss_FS));
        return pathLoss_FS;
    }

    /**
 * This method calculates Transmitter Gain Factor
 * @param hte the height of transmitter in meters
 * @return valueHte
 */
    public double gainHte(double hte) {
        double valueHte = 20 * (java.lang.Math.log10((hte / 200)));
        return valueHte;
    }

    /**
 * This method calculates Receiver Gain Factor
 * 
 * @return valueHre
 */
    public double gainHre() {
        double valueHre = 10 * (java.lang.Math.log10((Constants.HEIGHT_RECEIVER / 3)));
        return valueHre;
    }

    /**
 * This method calculates Area Gain using the terrian type and carrier frequency
 * @param fc       the carrier frequency
 * @param areaType the terrain type
 * @return gArea   the area gain factor 
 */
    public double gainArea(int fc, String areaType) {
        char aType = 'x';
        if (areaType.equalsIgnoreCase("Open Area")) aType = 'o'; else if (areaType.equalsIgnoreCase("Quasi Open Area")) aType = 'q'; else if (areaType.equalsIgnoreCase("Suburban Area")) aType = 's';
        int freqAG = fc;
        switch(aType) {
            case 'o':
                if (freqAG >= 100 && freqAG < 150) gArea = 22; else if (freqAG >= 150 && freqAG < 200) gArea = 23; else if (freqAG >= 200 && freqAG < 250) gArea = 23; else if (freqAG >= 250 && freqAG <= 300) gArea = 24; else if (freqAG > 300 && freqAG <= 400) gArea = 25; else if (freqAG > 400 && freqAG < 500) gArea = 26; else if (freqAG >= 500 && freqAG < 600) gArea = 26; else if (freqAG >= 600 && freqAG <= 700) gArea = 27; else if (freqAG > 700 && freqAG <= 800) gArea = 27; else if (freqAG > 800 && freqAG <= 900) gArea = 28; else if (freqAG > 900 && freqAG <= 1000) gArea = 29; else if (freqAG > 1000 && freqAG <= 1200) gArea = 30; else if (freqAG > 1200 && freqAG <= 1400) gArea = 31; else if (freqAG > 1400 && freqAG <= 1600) gArea = 32; else if (freqAG > 1600 && freqAG <= 1800) gArea = 32; else if (freqAG > 1800 && freqAG <= 2000) gArea = 33; else if (freqAG > 2000 && freqAG <= 2250) gArea = 33; else if (freqAG > 2250 && freqAG <= 2750) gArea = 34; else if (freqAG > 2750 && freqAG <= 3000) gArea = 35;
                break;
            case 'q':
                if (freqAG >= 100 && freqAG < 150) gArea = 17; else if (freqAG >= 150 && freqAG < 200) gArea = 18; else if (freqAG >= 200 && freqAG < 250) gArea = 18; else if (freqAG >= 250 && freqAG <= 300) gArea = 19; else if (freqAG > 300 && freqAG <= 400) gArea = 19; else if (freqAG > 400 && freqAG < 500) gArea = 20; else if (freqAG >= 500 && freqAG < 600) gArea = 21; else if (freqAG >= 600 && freqAG <= 700) gArea = (float) 21.5; else if (freqAG > 700 && freqAG <= 800) gArea = 22; else if (freqAG > 800 && freqAG <= 900) gArea = (float) 23.5; else if (freqAG > 900 && freqAG <= 1000) gArea = (float) 23.5; else if (freqAG > 1000 && freqAG <= 1200) gArea = (float) 24.5; else if (freqAG > 1200 && freqAG <= 1400) gArea = 25; else if (freqAG > 1400 && freqAG <= 1600) gArea = 26; else if (freqAG > 1600 && freqAG <= 1800) gArea = 27; else if (freqAG > 1800 && freqAG <= 2000) gArea = (float) 27.5; else if (freqAG > 2000 && freqAG <= 2500) gArea = 29; else if (freqAG > 2500 && freqAG <= 3000) gArea = 30;
                break;
            case 's':
                if (freqAG >= 100 && freqAG < 150) gArea = 5; else if (freqAG >= 150 && freqAG < 200) gArea = 6; else if (freqAG >= 200 && freqAG < 250) gArea = 6; else if (freqAG >= 250 && freqAG <= 300) gArea = 7; else if (freqAG > 300 && freqAG <= 400) gArea = 7; else if (freqAG > 400 && freqAG < 500) gArea = (float) 7.5; else if (freqAG >= 500 && freqAG < 600) gArea = 8; else if (freqAG >= 600 && freqAG <= 700) gArea = (float) 8.5; else if (freqAG > 700 && freqAG <= 800) gArea = 9; else if (freqAG > 800 && freqAG <= 900) gArea = (float) 9.5; else if (freqAG > 900 && freqAG <= 1000) gArea = 10; else if (freqAG > 1000 && freqAG <= 1200) gArea = 10; else if (freqAG > 1200 && freqAG <= 1400) gArea = (float) 10.5; else if (freqAG > 1400 && freqAG <= 1600) gArea = 11; else if (freqAG > 1600 && freqAG <= 1800) gArea = 12; else if (freqAG > 1800 && freqAG <= 2000) gArea = (float) 12.5; else if (freqAG > 2000 && freqAG <= 2500) gArea = 13; else if (freqAG > 2500 && freqAG <= 3000) gArea = 14;
                break;
        }
        return gArea;
    }

    /**
 * This method calculates the median attenuation for given carier frequency and 
 * distance
 * @param fc the carrier frequency
 * @param d  the distance
 * @return attn the median attentuation
 */
    public int mAttentuation(int fc, double d) {
        int freq = this.fc;
        if (freq >= 100 && freq <= 200) {
            if (d == 1) attn = 15; else if (d >= 2 && d < 5) attn = 19; else if (d >= 5 && d < 10) attn = 22; else if (d >= 10 && d < 20) attn = 25; else if (d >= 20 && d < 30) attn = 28; else if (d >= 30 && d < 40) attn = 31; else if (d >= 40 && d < 50) attn = 35; else if (d >= 50 && d < 55) attn = 38; else if (d >= 55 && d < 60) attn = 40; else if (d >= 60 && d < 65) attn = 41; else if (d >= 65 && d < 70) attn = 42; else if (d >= 70 && d < 75) attn = 44; else if (d >= 75 && d <= 80) attn = 47; else if (d > 80 && d <= 90) attn = 50; else if (d > 90 && d <= 100) attn = 52;
        }
        if (freq > 200 && freq <= 300) {
            if (d == 1) attn = 15; else if (d >= 2 && d < 5) attn = 19; else if (d >= 5 && d < 8) attn = 22; else if (d >= 8 && d <= 10) attn = 25; else if (d > 10 && d <= 20) attn = 27; else if (d > 20 && d < 30) attn = 29; else if (d >= 30 && d < 40) attn = 32; else if (d >= 40 && d < 50) attn = 36; else if (d >= 50 && d <= 55) attn = 40; else if (d > 55 && d <= 60) attn = 41; else if (d > 60 && d <= 70) attn = 45; else if (d > 70 && d <= 75) attn = 47; else if (d > 75 && d <= 80) attn = 49; else if (d > 80 && d <= 85) attn = 50; else if (d > 85 && d <= 90) attn = 52; else if (d > 90 && d <= 100) attn = 55;
        }
        if (freq > 300 && freq <= 500) {
            if (d == 1) attn = 17; else if (d >= 2 && d < 5) attn = 20; else if (d >= 5 && d < 8) attn = 23; else if (d >= 8 && d <= 10) attn = 24; else if (d > 10 && d <= 20) attn = 27; else if (d > 20 && d <= 30) attn = 30; else if (d > 30 && d <= 40) attn = 34; else if (d >= 40 && d <= 50) attn = 37; else if (d > 50 && d <= 55) attn = 38; else if (d > 55 && d <= 60) attn = 41; else if (d > 60 && d <= 65) attn = 42; else if (d > 65 && d <= 70) attn = 44; else if (d > 70 && d <= 75) attn = 47; else if (d > 75 && d <= 80) attn = 49; else if (d > 80 && d <= 100) attn = 51;
        }
        if (freq > 500 && freq <= 700) {
            if (d >= 1 && d <= 2) attn = 18; else if (d > 2 && d <= 5) attn = 22; else if (d > 5 && d <= 10) attn = 24; else if (d > 10 && d <= 20) attn = 27; else if (d > 20 && d <= 25) attn = 29; else if (d > 25 && d <= 35) attn = 31; else if (d > 35 && d <= 40) attn = 34; else if (d > 40 && d <= 45) attn = 35; else if (d > 45 && d <= 50) attn = 38; else if (d > 50 && d <= 55) attn = 40; else if (d > 55 && d <= 60) attn = 41; else if (d > 60 && d <= 70) attn = 44; else if (d > 70 && d <= 80) attn = 47; else if (d > 80 && d <= 100) attn = 52;
        }
        if (freq > 700 && freq <= 1000) {
            if (d >= 1 && d <= 2) attn = 20; else if (d > 2 && d <= 5) attn = 22; else if (d > 5 && d <= 7) attn = 23; else if (d > 7 && d <= 10) attn = 24; else if (d > 10 && d <= 20) attn = 27; else if (d > 20 && d <= 25) attn = 29; else if (d > 25 && d <= 30) attn = 30; else if (d > 30 && d <= 35) attn = 32; else if (d > 35 && d <= 40) attn = 35; else if (d > 40 && d <= 45) attn = 37; else if (d > 45 && d < 50) attn = 39; else if (d >= 50 && d <= 55) attn = 40; else if (d > 55 && d <= 60) attn = 41; else if (d > 60 && d <= 65) attn = 42; else if (d > 65 && d <= 70) attn = 44; else if (d > 70 && d <= 80) attn = 46; else if (d > 80 && d <= 85) attn = 49; else if (d > 80 && d <= 100) attn = 52;
        }
        if (freq > 1000 && freq <= 1500) {
            if (d == 1) attn = 21; else if (d >= 2 && d < 5) attn = 24; else if (d >= 5 && d < 10) attn = 27; else if (d >= 10 && d < 20) attn = 31; else if (d >= 20 && d < 30) attn = 33; else if (d >= 30 && d < 40) attn = 37; else if (d >= 40 && d < 50) attn = 42; else if (d >= 50 && d < 60) attn = 46; else if (d >= 60 && d < 70) attn = 52; else if (d >= 70 && d < 80) attn = 55; else if (d >= 80 && d < 90) attn = 58; else if (d >= 90 && d <= 100) attn = 62;
        }
        if (freq > 1500 && freq <= 2000) {
            if (d == 1) attn = 22; else if (d >= 2 && d < 5) attn = 25; else if (d >= 5 && d < 10) attn = 29; else if (d >= 10 && d < 20) attn = 32; else if (d >= 20 && d < 30) attn = 35; else if (d >= 30 && d < 40) attn = 39; else if (d >= 40 && d < 50) attn = 43; else if (d >= 50 && d < 60) attn = 49; else if (d >= 60 && d < 70) attn = 54; else if (d >= 70 && d < 80) attn = 57; else if (d >= 80 && d < 90) attn = 63; else if (d >= 90 && d <= 100) attn = 66;
        }
        if (freq > 2000 && freq <= 3000) {
            if (d == 1) attn = 24; else if (d >= 2 && d < 5) attn = 28; else if (d >= 5 && d < 10) attn = 33; else if (d >= 10 && d < 20) attn = 35; else if (d >= 20 && d < 30) attn = 38; else if (d >= 30 && d < 40) attn = 43; else if (d >= 40 && d < 50) attn = 46; else if (d >= 50 && d < 60) attn = 51; else if (d >= 60 && d <= 65) attn = 55; else if (d > 65 && d < 70) attn = 59; else if (d > 70 && d <= 75) attn = 60; else if (d > 75 && d < 80) attn = 61; else if (d >= 80 && d < 90) attn = 63; else if (d >= 90 && d <= 100) attn = 66;
        }
        return attn;
    }
}
