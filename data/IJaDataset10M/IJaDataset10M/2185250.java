package geovista.colorbrewer;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CIELabToSRGB
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * 
 */
public class CIELabToSRGB {

    protected static final Logger logger = Logger.getLogger(CIELabToSRGB.class.getName());

    public static final double b00 = 3.240479;

    public static final double b01 = -1.537150;

    public static final double b02 = -0.498535;

    public static final double b10 = -0.969256;

    public static final double b11 = 1.875992;

    public static final double b12 = 0.041556;

    public static final double b20 = 0.055648;

    public static final double b21 = -0.204043;

    public static final double b22 = 1.057311;

    double SR;

    double SG;

    double SB;

    double R709;

    double G709;

    double B709;

    double R255;

    double G255;

    double B255;

    double X;

    double Y;

    double Z;

    double x;

    double y;

    double z;

    double L;

    double a;

    double b;

    public static final double Xn = 95.0456;

    public static final double Yn = 100.0000;

    public static final double Zn = 108.8754;

    public static final double keyratio = 0.008856;

    public static final double power = 0.333333;

    public CIELabToSRGB(double L, double a, double b) {
        this.L = L;
        this.a = a;
        this.b = b;
        X = Xn * Math.pow(((L + 16) / 116 + a / 500), 3);
        Y = Yn * Math.pow((L + 16) / 116, 3);
        Z = Zn * Math.pow(((L + 16) / 116 - b / 200), 3);
        x = X / (X + Y + Z);
        y = Y / (X + Y + Z);
        z = Z / (X + Y + Z);
        R709 = (b00 * X + b01 * Y + b02 * Z) / 100;
        G709 = (b10 * X + b11 * Y + b12 * Z) / 100;
        B709 = (b20 * X + b21 * Y + b22 * Z) / 100;
        if (R709 >= 1.0) {
            R709 = 1.0;
        }
        if (R709 <= 0.0) {
            R709 = 0.0;
        }
        if (B709 >= 1.0) {
            B709 = 1.0;
        }
        if (B709 <= 0.0) {
            B709 = 0.0;
        }
        if (G709 >= 1.0) {
            G709 = 1.0;
        }
        if (R709 <= 0.0) {
            R709 = 0.0;
        }
        if (R709 <= 0.0031308) {
            SR = 12.92 * R709;
        } else {
            SR = 1.055 * Math.pow(R709, 1.0 / 2.4) - 0.055;
        }
        if (G709 <= 0.0031308) {
            SG = 12.92 * G709;
        } else {
            SG = 1.055 * Math.pow(G709, 1.0 / 2.4) - 0.055;
        }
        if (B709 <= 0.0031308) {
            SB = 12.92 * B709;
        } else {
            SB = 1.055 * Math.pow(B709, 1.0 / 2.4) - 0.055;
        }
        R255 = Math.ceil(255 * SR);
        G255 = Math.ceil(255 * SG);
        B255 = Math.ceil(255 * SB);
        if (R255 >= 255) {
            R255 = 255;
        }
        if (R255 <= 0) {
            R255 = 0;
        }
        if (G255 >= 255) {
            G255 = 255;
        }
        if (G255 <= 0) {
            G255 = 0;
        }
        if (B255 >= 255) {
            B255 = 255;
        }
        if (B255 <= 0) {
            B255 = 0;
        }
        logger.finest("L= " + L + '\n' + "a= " + a + '\n' + "b= " + b + '\n');
        logger.finest("X=  " + X + '\n' + "Y=  " + Y + '\n' + "Z=  " + Z + '\n');
        logger.finest("R709= " + R709 + '\n' + "G709= " + G709 + '\n' + "B709= " + B709 + '\n');
        logger.finest("x= " + x + '\n' + "y= " + y + '\n' + "z= " + z + '\n');
        logger.finest("SR= " + SR + '\n' + "SG= " + SG + '\n' + "SB= " + SB + '\n');
        logger.finest("R255= " + R255 + '\n' + "G255= " + G255 + '\n' + "B255= " + B255 + '\n');
    }

    public CIELabToSRGB() {
        this.X = 50;
        this.Y = 40;
        this.Z = 30;
        R709 = (b00 * X + b01 * Y + b02 * Z);
        G709 = (b10 * X + b11 * Y + b12 * Z);
        B709 = (b20 * X + b21 * Y + b22 * Z);
        if (R709 <= 0.0031308) {
            SR = 12.92 * R709;
        } else {
            SR = 1.055 * Math.pow(R709, 1.0 / 2.4) - 0.055;
        }
        if (G709 <= 0.0031308) {
            SG = 12.92 * G709;
        } else {
            SG = 1.055 * Math.pow(G709, 1.0 / 2.4) - 0.055;
        }
        if (B709 <= 0.0031308) {
            SB = 12.92 * B709;
        } else {
            SB = 1.055 * Math.pow(B709, 1.0 / 2.4) - 0.055;
        }
        R255 = Math.ceil(255 * SR);
        G255 = Math.ceil(255 * SG);
        B255 = Math.ceil(255 * SB);
        if (R255 >= 255) {
            R255 = 255;
        }
        if (R255 <= 0) {
            R255 = 0;
        }
        if (G255 >= 255) {
            G255 = 255;
        }
        if (G255 <= 0) {
            G255 = 0;
        }
        if (B255 >= 255) {
            B255 = 255;
        }
        if (B255 <= 0) {
            B255 = 0;
        }
        logger.finest("L= " + L + '\n' + "a= " + a + '\n' + "b= " + b + '\n');
        logger.finest("X=  " + X + '\n' + "Y=  " + Y + '\n' + "Z=  " + Z + '\n');
        logger.finest("R709= " + R709 + '\n' + "G709= " + G709 + '\n' + "B709= " + B709 + '\n');
        logger.finest("x= " + x + '\n' + "y= " + y + '\n' + "z= " + z + '\n');
        logger.finest("SR= " + SR + '\n' + "SG= " + SG + '\n' + "SB= " + SB + '\n');
        logger.finest("R255= " + R255 + '\n' + "G255= " + G255 + '\n' + "B255= " + B255 + '\n');
    }

    public static void main(String[] args) throws IOException {
        CIELabToSRGB cIELabToSRGB1 = new CIELabToSRGB(50, 20, 70);
        DataOutputStream out = new DataOutputStream(new FileOutputStream("D:/data1.txt"));
        Double dou1 = new Double(cIELabToSRGB1.R255);
        out.writeChars(dou1.toString());
        out.writeChar('\t');
        Double dou2 = new Double(cIELabToSRGB1.G255);
        out.writeChars(dou2.toString());
        out.writeChar('\t');
        Double dou3 = new Double(cIELabToSRGB1.B255);
        out.writeChars(dou3.toString());
        out.writeChar('\t');
        out.close();
    }
}
