package dataretriever.gns;

import java.util.TimerTask;
import javax.swing.JProgressBar;

public class ProgressMeter extends TimerTask {

    private GNSDataRetriever retriever;

    private JProgressBar progressBar;

    private long lastReadedAmount;

    private long lastReadedTime;

    private double[] speeds;

    private int averagePos;

    public ProgressMeter(GNSDataRetriever retriever, JProgressBar progressBar) {
        this.retriever = retriever;
        this.progressBar = progressBar;
        this.lastReadedAmount = 0;
        this.lastReadedTime = 0;
        this.speeds = new double[20];
        this.averagePos = 0;
    }

    public void run() {
        long readed = retriever.getReadedAmount();
        long total = retriever.getGeoFileSize();
        double ratio = ((double) readed / (double) total);
        double speed = (((double) (readed - lastReadedAmount)) / (double) (System.currentTimeMillis() - lastReadedTime)) * 1000;
        speeds[averagePos] = speed;
        averagePos = (averagePos + 1) % speeds.length;
        double averageSpeed = getAverageSpeed();
        String string = String.format("%1$dmb", readed / (1024 * 1024)) + " / " + String.format("%1$dmb", total / (1024 * 1024)) + " (" + Math.round(ratio * 100) + "%)";
        string += " at " + String.format("%1$2.3f", averageSpeed / (1024 * 1024)) + "mb/s";
        progressBar.setValue((int) (ratio * progressBar.getMaximum()));
        progressBar.setString(string);
        lastReadedAmount = readed;
        lastReadedTime = System.currentTimeMillis();
    }

    private double getAverageSpeed() {
        double average = 0;
        for (double speed : speeds) average += speed / (double) speeds.length;
        return average;
    }
}
