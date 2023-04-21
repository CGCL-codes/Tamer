package ch.unizh.ini.jaer.projects.facetracker;

public class Descriptive {

    public Descriptive() {
    }

    public float Median(float[] list) {
        float median = list.length;
        short index = 0;
        if (median % 2 == 0) {
            median = (float) list[(list.length / 2) - 1];
            median = (float) list[(list.length / 2)] + median;
            median = median / 2;
        } else {
            index = (short) ((list.length + 1) / 2 - 1);
            median = (short) list[index];
        }
        return (float) median;
    }
}
