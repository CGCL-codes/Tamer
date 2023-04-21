package org.cipotato.entomology;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.cipotato.gis.raster.ArcAsciiReader;
import org.cipotato.gis.raster.Month2DayLinearInterpolatorTminTmax;

public class WorldRiskIndicesCalculator {

    public void run(String path, String dataPath, int randomSize, String outFile) {
        String file = dataPath + "alt.asc";
        String dbfile = dataPath + "minmax.txt";
        int stations = randomSize;
        Month2DayLinearInterpolatorTminTmax ip = new Month2DayLinearInterpolatorTminTmax();
        ip.readData(dbfile, stations);
        PhenologyCalculator pc = new PhenologyCalculator();
        pc.setPath(path);
        pc.readParameterTable(path + "data\\model_parameter.txt");
        RiskCalculator rc = new RiskCalculator();
        String line = "";
        double ai, ei, gi;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            out.write("id\tlat\tlon\talt\tai\tei\tgi\n");
            for (int sd = 0; sd < stations; sd++) {
                pc.setTdataDaysFirst(ip.getTminTmax(sd));
                pc.calculate();
                rc.setPc(pc);
                ai = rc.calculateActivityIndex();
                ei = rc.calculateEstablishmentIndex();
                gi = rc.calculateGenerationIndex();
                line = ip.getDataRecord(sd) + ai + "\t" + String.format("%.3f", ei) + "\t" + String.format("%.3f", gi);
                out.write(line + "\n");
                out.flush();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
