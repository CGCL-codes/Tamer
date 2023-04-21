package pl.edu.mimuw.mas.bezier;

import pl.edu.mimuw.mas.chart.Area;
import pl.edu.mimuw.mas.chart.Chart;

public class ChartModeller {

    private ISurface modeller;

    private Chart chart;

    public ChartModeller(Chart chart, ISurface modeller) {
        this.chart = chart;
        this.modeller = modeller;
    }

    public void model(Area a) {
        modeller.recalculateCachedData();
        int[] area = a.getArea();
        float x, y, h;
        for (int i = area[Area.X0]; i <= area[Area.X1]; i++) {
            for (int j = area[Area.Y0]; j <= area[Area.Y1]; j++) {
                x = (float) (i - area[Area.X0]) / (area[Area.X1] - area[Area.X0]);
                y = (float) (j - area[Area.Y0]) / (area[Area.Y1] - area[Area.Y0]);
                h = modeller.value(x, y);
                chart.setHeight(i, j, h);
            }
        }
    }
}
