package eu.haslgruebler.util.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import eu.haslgruebler.darsens.service.features.component.pojo.DataFeature;
import eu.haslgruebler.darsens.service.features.component.pojo.Energy;
import eu.haslgruebler.darsens.service.features.component.pojo.Fluctation;
import eu.haslgruebler.darsens.service.features.component.pojo.Mean;
import eu.haslgruebler.darsens.service.features.component.pojo.MeanCrossingRate;
import eu.haslgruebler.darsens.service.features.component.pojo.RootMeanSquare;
import eu.haslgruebler.darsens.service.features.component.pojo.StandardDeviation;
import eu.haslgruebler.darsens.service.features.component.pojo.Variance;
import eu.haslgruebler.darsens.service.features.component.pojo.ZeroCrossingRate;

/**
 * @author Michael Haslgrübler, uni-michael@haslgruebler.eu
 *
 */
public class CalcFeatures extends Thread {

    private ArrayList<String> sb;

    private String outFile;

    private String inFile;

    private DataFeature x;

    private DataFeature y;

    private DataFeature z;

    private Mean xMean;

    private Variance xVar;

    private StandardDeviation xStd;

    private ZeroCrossingRate xZcr;

    private MeanCrossingRate xMcr;

    private Mean yMean;

    private Variance yVar;

    private StandardDeviation yStd;

    private ZeroCrossingRate yZcr;

    private MeanCrossingRate yMcr;

    private Mean zMean;

    private Variance zVar;

    private StandardDeviation zStd;

    private ZeroCrossingRate zZcr;

    private MeanCrossingRate zMcr;

    private Energy xEng;

    private Energy yEng;

    private Energy zEng;

    private RootMeanSquare xRMS;

    private RootMeanSquare yRMS;

    private RootMeanSquare zRMS;

    private Fluctation xFluc;

    private Fluctation yFluc;

    private Fluctation zFluc;

    /**
	 * @return the outFile
	 */
    public String getOutFile() {
        return outFile;
    }

    /**
	 * @param outFile the outFile to set
	 */
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
	 * @return the inFile
	 */
    public String getInFile() {
        return inFile;
    }

    /**
	 * @param inFile the inFile to set
	 */
    public void setInFile(String inFile) {
        this.inFile = inFile;
    }

    /**
	 * 
	 */
    public CalcFeatures() {
        x = new DataFeature();
        y = new DataFeature();
        z = new DataFeature();
        xMean = new Mean(x);
        yMean = new Mean(y);
        zMean = new Mean(z);
        xRMS = new RootMeanSquare(x);
        yRMS = new RootMeanSquare(y);
        zRMS = new RootMeanSquare(z);
        xZcr = new ZeroCrossingRate(x);
        yZcr = new ZeroCrossingRate(y);
        zZcr = new ZeroCrossingRate(z);
        xMcr = new MeanCrossingRate(x, xMean);
        yMcr = new MeanCrossingRate(y, yMean);
        zMcr = new MeanCrossingRate(z, zMean);
        xVar = new Variance(x, xMean);
        yVar = new Variance(y, yMean);
        zVar = new Variance(z, zMean);
        xStd = new StandardDeviation(xVar);
        yStd = new StandardDeviation(yVar);
        zStd = new StandardDeviation(zVar);
        xEng = new Energy(x);
        yEng = new Energy(y);
        zEng = new Energy(z);
        xFluc = new Fluctation(xStd, xMean);
        yFluc = new Fluctation(yStd, yMean);
        zFluc = new Fluctation(zStd, zMean);
    }

    @Override
    public void run() {
        try {
            try {
                File f = new File(inFile);
                if (!f.exists()) return;
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile))));
                StringBuffer fl = new StringBuffer();
                String line = in.readLine();
                if (line == null || !line.startsWith("@relation")) return;
                while (!line.contains("@data")) {
                    fl.append(line + "\n");
                    line = in.readLine();
                    if (line.startsWith("@attribute class")) {
                        fl.append("@attribute x-zcr numeric\n");
                        fl.append("@attribute x-mean numeric\n");
                        fl.append("@attribute x-mcr numeric\n");
                        fl.append("@attribute x-std numeric\n");
                        fl.append("@attribute x-var numeric\n");
                        fl.append("@attribute x-egy numeric\n");
                        fl.append("@attribute x-rms numeric\n");
                        fl.append("@attribute x-fluc numeric\n");
                        fl.append("@attribute y-zcr numeric\n");
                        fl.append("@attribute y-mean numeric\n");
                        fl.append("@attribute y-mcr numeric\n");
                        fl.append("@attribute y-std numeric\n");
                        fl.append("@attribute y-var numeric\n");
                        fl.append("@attribute y-egy numeric\n");
                        fl.append("@attribute y-rms numeric\n");
                        fl.append("@attribute y-fluc numeric\n");
                        fl.append("@attribute z-zcr numeric\n");
                        fl.append("@attribute z-mean numeric\n");
                        fl.append("@attribute z-mcr numeric\n");
                        fl.append("@attribute z-std numeric\n");
                        fl.append("@attribute z-var numeric\n");
                        fl.append("@attribute z-egy numeric\n");
                        fl.append("@attribute z-rms numeric\n");
                        fl.append("@attribute z-fluc numeric\n");
                    }
                }
                fl.append(line + "\n");
                sb = new ArrayList<String>();
                sb.add(fl.toString());
                line = in.readLine();
                while (line != null) {
                    String[] split = line.split(",");
                    if (split.length >= 8) {
                        x.push(Double.parseDouble(split[1]));
                        y.push(Double.parseDouble(split[2]));
                        z.push(Double.parseDouble(split[3]));
                        sb.add(split[0] + ',' + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5] + "," + split[6] + ",");
                        sb.add(xZcr.getLast() + "," + xMean.getLast() + "," + xMcr.getLast() + "," + xStd.getLast() + "," + xVar.getLast() + "," + xEng.getLast() + "," + xRMS.getLast() + "," + xFluc.getLast() + ",");
                        sb.add(yZcr.getLast() + "," + yMean.getLast() + "," + yMcr.getLast() + "," + yStd.getLast() + "," + yVar.getLast() + "," + yEng.getLast() + "," + yRMS.getLast() + "," + yFluc.getLast() + ",");
                        sb.add(zZcr.getLast() + "," + zMean.getLast() + "," + zMcr.getLast() + "," + zStd.getLast() + "," + zVar.getLast() + "," + zEng.getLast() + "," + zRMS.getLast() + "," + zFluc.getLast() + ",");
                        sb.add(split[7] + "\n");
                    }
                    line = in.readLine();
                }
                in.close();
                in = null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("writing");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outFile))));
            for (String line : sb) {
                out.write(line);
            }
            out.flush();
            out.close();
            out = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CalcFeatures calc = new CalcFeatures();
        calc.setInFile(args[0]);
        calc.setOutFile(args[1]);
        calc.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
