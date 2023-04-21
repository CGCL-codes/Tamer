package buseylab.gwtgrid;

import static java.lang.Math.*;

class ImgMod31 {

    public static void main(String[] args) {
        int switchCase = 2;
        int displayType = 1;
        if (args.length == 1) {
            switchCase = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            switchCase = Integer.parseInt(args[0]);
            displayType = Integer.parseInt(args[1]);
        } else {
            System.out.println("Usage: java ImgMod31 " + "CaseNumber DisplayType");
            System.out.println("CaseNumber from 0 to 13 inclusive.");
            System.out.println("DisplayType from 0 to 2 inclusive.");
            System.out.println("Running case " + switchCase + " by default.");
            System.out.println("Running DisplayType " + displayType + " by default.");
        }
        int rows = 41;
        int cols = 41;
        double[][] spatialData = getSpatialData(switchCase, rows, cols);
        new ImgMod29(spatialData, 3, false, displayType);
        double[][] realSpect = new double[rows][cols];
        double[][] imagSpect = new double[rows][cols];
        double[][] amplitudeSpect = new double[rows][cols];
        ImgMod30.xform2D(spatialData, realSpect, imagSpect, amplitudeSpect);
        new ImgMod29(amplitudeSpect, 3, true, displayType);
        double[][] shiftedRealSpect = ImgMod30.shiftOrigin(realSpect);
        new ImgMod29(shiftedRealSpect, 3, true, displayType);
        double[][] shiftedImagSpect = ImgMod30.shiftOrigin(imagSpect);
        new ImgMod29(shiftedImagSpect, 3, true, displayType);
        double[][] shiftedAmplitudeSpect = ImgMod30.shiftOrigin(amplitudeSpect);
        new ImgMod29(shiftedAmplitudeSpect, 3, true, displayType);
        double[][] recoveredSpatialData = new double[rows][cols];
        ImgMod30.inverseXform2D(realSpect, imagSpect, recoveredSpatialData);
        new ImgMod29(recoveredSpatialData, 3, false, displayType);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.println(spatialData[row][col] + " " + recoveredSpatialData[row][col] + " ");
            }
        }
    }

    private static double[][] getSpatialData(int switchCase, int rows, int cols) {
        double[][] spatialData = new double[rows][cols];
        switch(switchCase) {
            case 0:
                spatialData[0][0] = 1;
                break;
            case 1:
                spatialData[2][2] = 1;
                break;
            case 2:
                spatialData[3][3] = 1;
                spatialData[3][4] = 1;
                spatialData[3][5] = 1;
                spatialData[4][3] = 1;
                spatialData[4][4] = 1;
                spatialData[4][5] = 1;
                spatialData[5][3] = 1;
                spatialData[5][4] = 1;
                spatialData[5][5] = 1;
                break;
            case 3:
                spatialData[0][3] = 1;
                spatialData[0][4] = 1;
                spatialData[0][5] = 1;
                spatialData[1][3] = 1;
                spatialData[1][4] = 1;
                spatialData[1][5] = 1;
                spatialData[2][3] = 1;
                spatialData[2][4] = 1;
                spatialData[2][5] = 1;
                break;
            case 4:
                spatialData[0][0] = 1;
                spatialData[1][1] = 1;
                spatialData[2][2] = 1;
                spatialData[3][3] = 1;
                spatialData[4][4] = 1;
                spatialData[5][5] = 1;
                spatialData[6][6] = 1;
                spatialData[7][7] = 1;
                break;
            case 5:
                spatialData[0][3] = 1;
                spatialData[1][2] = 1;
                spatialData[2][1] = 1;
                spatialData[3][0] = 1;
                break;
            case 6:
                spatialData[0][0] = -1;
                spatialData[1][1] = 1;
                spatialData[2][2] = -1;
                spatialData[3][3] = 0;
                spatialData[4][4] = -1;
                spatialData[5][5] = 1;
                spatialData[6][6] = -1;
                spatialData[6][0] = -1;
                spatialData[5][1] = 1;
                spatialData[4][2] = -1;
                spatialData[3][3] = 0;
                spatialData[2][4] = -1;
                spatialData[1][5] = 1;
                spatialData[0][6] = -1;
                spatialData[3][0] = 1;
                spatialData[3][1] = -1;
                spatialData[3][2] = 1;
                spatialData[3][3] = 0;
                spatialData[3][4] = 1;
                spatialData[3][5] = -1;
                spatialData[3][6] = 1;
                spatialData[0][3] = 1;
                spatialData[1][3] = -1;
                spatialData[2][3] = 1;
                spatialData[3][3] = 0;
                spatialData[4][3] = 1;
                spatialData[5][3] = -1;
                spatialData[6][3] = 1;
                break;
            case 7:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = 1.0;
                    }
                }
                break;
            case 8:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2 * PI * col / 1);
                    }
                }
                break;
            case 9:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2 * PI * col / 2);
                    }
                }
                break;
            case 10:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2 * PI * row / 2);
                    }
                }
                break;
            case 11:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2 * PI * col / 8);
                    }
                }
                break;
            case 12:
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2 * PI * row / 8) + cos(2 * PI * col / 3);
                    }
                }
                break;
            case 13:
                double phase = 0;
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        spatialData[row][col] = cos(2.0 * PI * col / 8 - phase);
                    }
                    phase += .8;
                }
                break;
            default:
                System.out.println("Case must be " + "between 0 and 13 inclusive.");
                System.out.println("Terminating program.");
                System.exit(0);
        }
        return spatialData;
    }
}
