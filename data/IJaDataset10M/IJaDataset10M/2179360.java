package playground.benjamin.income;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.matsim.core.utils.io.IOUtils;

/**
 * @author dgrether
 *
 */
public class ChartDataWriter {

    private ChartData data;

    public ChartDataWriter(ChartData data) {
        this.data = data;
    }

    public void writeFile(String filename) {
        try {
            BufferedWriter writer = IOUtils.getBufferedWriter(filename);
            writer.write("Data for chart: " + this.data.getChartName() + " xAxisLabel: " + this.data.getXLabel() + " yAxisLabel: " + this.data.getYLabel());
            writer.newLine();
            writer.newLine();
            double[] values;
            for (String seriesName : this.data.getSeries().keySet()) {
                writer.write(seriesName);
                writer.newLine();
                writer.write("x values: \t");
                writer.newLine();
                values = this.data.getSeries().get(seriesName).getFirst();
                for (int i = 0; i < values.length; i++) {
                    writer.write(Double.toString(values[i]) + "\t");
                }
                writer.newLine();
                writer.write("y values: \t");
                writer.newLine();
                values = this.data.getSeries().get(seriesName).getSecond();
                for (int i = 0; i < values.length; i++) {
                    writer.write(Double.toString(values[i]) + "\t");
                }
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
