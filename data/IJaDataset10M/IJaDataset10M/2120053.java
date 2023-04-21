package ishima.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GnuplotGenerator {

    private String title;

    private String outFileName;

    private String dataFile;

    private String xAxisText;

    private String yAxisText;

    public void setTitel(String title) {
        this.title = title;
    }

    public void setOutFile(String outFileName) {
        this.outFileName = outFileName;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public void generate(String scriptFileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(scriptFileName));
            String newLine = "\r\n";
            out.write("set terminal png" + newLine);
            out.write("unset key" + newLine);
            out.write("set title \"" + title + "\"" + newLine);
            out.write("set xlabel \"" + xAxisText + "\"" + newLine);
            out.write("set ylabel \"" + yAxisText + "\"" + newLine);
            outFileName = outFileName.replace("\\", "/");
            out.write("set output \"" + outFileName + "\"" + newLine);
            dataFile = dataFile.replace("\\", "/");
            out.write("plot \"" + dataFile + "\" with points pt 1 ps 0.3" + newLine);
            out.close();
        } catch (IOException e) {
        }
    }

    public void setAxis(String xAxis, String yAxis) {
        this.xAxisText = xAxis;
        this.yAxisText = yAxis;
    }
}
