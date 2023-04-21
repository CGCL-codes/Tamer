package org.systemsbiology.apps.utils.targetanalysis;

import org.systemsbiology.apmlparser.v2.datatype.Coordinate;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.EnumMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mi-Youn Brusniak
 * @version version 1.0
 * @since Apr 10, 2008
 */
public class SpectraSTParser extends Parser {

    private String primaryKey;

    private String secondaryKey;

    private int initialCapacity;

    private static final int PEPTIDE_LOC = 0;

    private static final int CHARGE_LOC = 1;

    private static final String HEADER = new String("m/z\trt\tcharge\tpeptide\tPepXMLData from Comment");

    public SpectraSTParser(File targetf, String mzKey, String rtKey, int capacity) {
        file = targetf;
        this.primaryKey = mzKey;
        this.secondaryKey = rtKey;
        this.initialCapacity = capacity;
    }

    public void parse() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String line;
        String[] lineTokens;
        String peptide;
        int index = 0;
        ArrayList<ArrayList<AnnotatedFeature>> dataByCharge = new ArrayList<ArrayList<AnnotatedFeature>>();
        ArrayList<AnnotatedFeature> dataBySameCharge;
        for (int i = 0; i < ChargeState.SIZE; ++i) {
            dataBySameCharge = new ArrayList<AnnotatedFeature>();
            dataBySameCharge.ensureCapacity(this.initialCapacity);
            dataByCharge.add(dataBySameCharge);
        }
        int charge;
        double rt;
        double mz;
        Coordinate coord;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("Name")) {
                lineTokens = line.split("/");
                peptide = lineTokens[PEPTIDE_LOC];
                charge = Integer.parseInt(lineTokens[CHARGE_LOC]);
                assert (charge < ChargeState.SIZE);
                dataBySameCharge = dataByCharge.get(charge);
                while ((line = br.readLine()) != null && !line.startsWith(this.primaryKey)) ;
                assert (line != null);
                lineTokens = line.split(":");
                mz = Double.parseDouble(lineTokens[1]);
                while ((line = br.readLine()) != null && !line.startsWith("Comment:")) ;
                assert (line != null);
                lineTokens = line.split("[' ',]");
                int ith = 0;
                for (String itoken : lineTokens) {
                    if (itoken.startsWith(this.secondaryKey)) break; else ith++;
                }
                if (this.convert2minutes) {
                    rt = Double.parseDouble(lineTokens[ith + 1]) / 60.0;
                } else {
                    rt = Double.parseDouble(lineTokens[ith + 1]);
                }
                coord = new Coordinate();
                coord.setMz((float) mz);
                coord.setRt((float) rt);
                coord.setCharge(charge);
                dataBySameCharge.add(new AnnotatedFeature(index, coord, String.valueOf(mz) + "\t" + String.valueOf(rt) + "\t" + String.valueOf(charge) + "\t" + peptide + "\t" + line));
                index++;
            }
        }
        for (int i = 0; i < ChargeState.SIZE; ++i) {
            data.put(ChargeState.getEnum(i), dataByCharge.get(i));
        }
        br.close();
    }

    public String getHeaders() {
        return SpectraSTParser.HEADER;
    }

    public int getIPI(File outputf, EnumMap<ChargeState, ArrayList<AnnotatedFeature>> data) throws IOException {
        int ipicount = 0;
        Iterator<ChargeState> it = data.keySet().iterator();
        ArrayList<AnnotatedFeature> dataBySameCharge;
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputf, true));
        String[] lineTokens;
        String tempLine;
        while (it.hasNext()) {
            dataBySameCharge = data.get(it.next());
            for (AnnotatedFeature d : dataBySameCharge) {
                tempLine = d.toString();
                lineTokens = tempLine.split("[/' ']");
                for (String itoken : lineTokens) {
                    if (itoken.startsWith(IPI_STRING) || itoken.startsWith(YEAST_PROTEIN_STRING)) {
                        bw.write(itoken);
                        bw.newLine();
                        ipicount++;
                    }
                }
            }
        }
        bw.close();
        return ipicount;
    }
}
