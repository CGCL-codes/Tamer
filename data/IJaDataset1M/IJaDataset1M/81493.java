package shipsim;

public class Utils {

    /**
     * Extracts sign of argument
     * (1=+; 0=0; -1=-)
     * 
     * @version
     * @date 07.04.28
     */
    public static int getSign(float number) {
        int sign = 0;
        if (number < 0.0) {
            sign = -1;
        } else if (number > 0.0) {
            sign = 1;
        }
        return sign;
    }

    /**
     * Converts wind speed (mph)
     * to Beaufort-force scale 
     * 
     * @version 
     * @date 03.02.22
     */
    public static int convertBeaufort(float fWind) {
        if (fWind < 1.0) return 0;
        if (fWind < 4.0) return 1;
        if (fWind < 7.0) return 2;
        if (fWind < 11.0) return 3;
        if (fWind < 17.0) return 4;
        if (fWind < 22.0) return 5;
        if (fWind < 28.0) return 6;
        if (fWind < 34.0) return 7;
        if (fWind < 41.0) return 8;
        if (fWind < 48.0) return 9;
        if (fWind < 56.0) return 10;
        if (fWind < 65.0) return 11;
        return 12;
    }

    public static void exitApp() {
        System.exit(0);
    }
}
