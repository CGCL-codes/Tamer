package twente.visico.org.GPSFilter;

import javax.swing.JFrame;

public class GPSFilter {

    public static void main(String[] args) {
        NEDGPSCSV csv = new NEDGPSCSV("/home/timo/RotterdamLane1Drierolwals.csv");
        chart = new GPSTimeSeriesChart("GPS Path Data");
        chart.pack();
        chart.updateChart(csv.getPoints());
        chart.setVisible(true);
        chart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static GPSTimeSeriesChart chart() {
        return chart;
    }

    public static GPSTimeSeriesChart chart;
}
