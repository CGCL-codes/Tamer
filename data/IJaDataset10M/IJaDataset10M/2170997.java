package com.jogrammah.jgradebook;

import java.util.Calendar;

/**
 *
 * @author joe
 */
public class Grade implements java.io.Serializable {

    /**
     * The Adler32 hash of "Grade" in decimal form. Ensures compatibility with future versions of JGradeBook.
     */
    public static final long serialVersionUID = 92275172L;

    private Calendar date;

    private int right;

    private int total;

    private String name;

    private double percent;

    private String letterGrade;

    public Calendar getCal() {
        return date;
    }

    public int getRight() {
        return right;
    }

    public int getTotal() {
        return total;
    }

    public String getName() {
        return name;
    }

    public double getPercent() {
        return percent;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public Grade(String n, int r, int tot, Calendar d) {
        name = n;
        right = r;
        total = tot;
        percent = calcPercent(r, tot);
        letterGrade = calcLetterGrade(percent);
        date = d;
    }

    public static String calcLetterGrade(double dperc) {
        int percent = (int) dperc;
        String toReturn = "ERROR";
        if (percent >= 97) {
            toReturn = "A+";
        } else if (percent >= 93 && percent <= 96) {
            toReturn = "A";
        } else if (percent >= 90 && percent <= 92) {
            toReturn = "A-";
        } else if (percent >= 87 && percent <= 89) {
            toReturn = "B+";
        } else if (percent >= 83 && percent <= 86) {
            toReturn = "B";
        } else if (percent >= 80 && percent <= 82) {
            toReturn = "B-";
        } else if (percent >= 77 && percent <= 79) {
            toReturn = "C+";
        } else if (percent >= 73 && percent <= 76) {
            toReturn = "C";
        } else if (percent >= 70 && percent <= 72) {
            toReturn = "C-";
        } else if (percent >= 67 && percent <= 69) {
            toReturn = "D+";
        } else if (percent >= 63 && percent <= 66) {
            toReturn = "D";
        } else if (percent >= 60 && percent <= 62) {
            toReturn = "D-";
        } else if (percent < 60) {
            toReturn = "F";
        }
        return toReturn;
    }

    public static double calcPercent(int right, int total) {
        double dr = (double) right;
        double dt = (double) total;
        double doubleRounded = round(dr / dt, 4);
        StringBuilder rounded = new StringBuilder(String.valueOf(doubleRounded));
        if (String.valueOf(rounded.charAt(0)).equals("1")) {
            rounded.delete(1, 2);
            try {
                rounded.charAt(3);
            } catch (Exception e) {
                rounded.insert(1, "0");
            }
            rounded.insert(3, ".");
        } else {
            rounded.delete(0, 2);
            if (rounded.length() == 1) {
                rounded.insert(1, "0");
            } else {
                rounded.insert(2, ".");
            }
        }
        return Double.valueOf(rounded.toString());
    }

    public static double round(double toRound, int places) {
        boolean noRound = false;
        String stRound = String.valueOf(toRound);
        String[] beforeandafter = stRound.split("\\.");
        String before = beforeandafter[0];
        StringBuilder decimals = new StringBuilder(beforeandafter[1]);
        int roundFactor;
        int lastDec;
        StringBuilder extra = new StringBuilder("");
        try {
            roundFactor = Integer.valueOf(String.valueOf(decimals.charAt(places)));
            lastDec = Integer.valueOf(String.valueOf(decimals.charAt(places - 1)));
        } catch (StringIndexOutOfBoundsException sioobe) {
            noRound = true;
            int lastPlace = places;
            for (int i = 0; i < places - lastPlace; i++) {
                extra.append("0");
            }
            roundFactor = 0;
            lastDec = 0;
        }
        String wholeThing;
        if (!noRound) {
            int lastDecRounded;
            if (roundFactor >= 5) {
                lastDecRounded = lastDec + 1;
            } else {
                lastDecRounded = lastDec;
            }
            wholeThing = before + "." + decimals.substring(0, places - 1) + lastDecRounded;
        } else {
            wholeThing = before + "." + decimals.toString() + extra;
        }
        return Double.valueOf(wholeThing);
    }
}
