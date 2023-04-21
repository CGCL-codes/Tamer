package it.southdown.avana.predict;

import it.southdown.avana.util.*;
import java.io.*;

public class PredictionDataSerializer implements FileTypeProcessor {

    public static final Class[] FILETYPE_CLASSES = { PredictionDataSerializer.class };

    public String getDescription() {
        return "Comma-separated peptide prediction";
    }

    public String[] getFileExtensions() {
        return new String[] { "txt", "csv" };
    }

    public static void serialize(File dbFile, PredictionData data) throws IOException, DataFileException {
        FileWriter fw = new FileWriter(dbFile);
        PrintWriter pw = new PrintWriter(fw);
        String[] peptides = data.getPeptides();
        String[] alleles = data.getAlleles();
        int peptidesCount = peptides.length;
        int alleleCount = alleles.length;
        pw.print(peptides.length);
        for (int i = 0; i < alleleCount; i++) {
            pw.print(',');
            pw.print(alleles[i]);
        }
        pw.println();
        for (int i = 0; i < peptidesCount; i++) {
            String peptide = peptides[i];
            pw.print(peptide);
            for (int j = 0; j < alleleCount; j++) {
                pw.print(',');
                String allele = alleles[j];
                float pred = PredictionData.NO_PREDICTION;
                try {
                    pred = data.getPrediction(peptide, allele);
                } catch (NoSuchAlleleException e) {
                    System.err.println("Could not get prediction: " + e);
                }
                pw.print(pred);
            }
            pw.println();
        }
        fw.close();
    }

    public static PredictionData deserialize(File dbFile) throws IOException, DataFileException {
        if ((dbFile == null) || (!dbFile.exists())) {
            return null;
        }
        FileInputStream fis = new FileInputStream(dbFile);
        CsvReader reader = new CsvReader(fis);
        String[] values = reader.getNextValidLine();
        int peptideCount = Integer.parseInt(values[0]);
        int initialCapacity = 5 * peptideCount / 4;
        int alleleCount = values.length - 1;
        String[] alleles = new String[alleleCount];
        for (int i = 0; i < alleleCount; i++) {
            alleles[i] = values[i + 1];
        }
        PredictionData result = new PredictionData(alleles, initialCapacity);
        while ((values = reader.getNextValidLine()) != null) {
            if (values.length != alleleCount + 1) {
                throw new DataFileException("Error at line " + reader.getLineNumber() + " of file " + dbFile.getCanonicalPath() + ": incorrect number of values, expected " + (alleleCount + 1) + ", found " + values.length);
            }
            String peptide = values[0];
            float[] predValues = new float[alleles.length];
            for (int i = 0; i < alleleCount; i++) {
                predValues[i] = PredictionData.NO_PREDICTION;
                String valueString = values[i + 1];
                if ((valueString != null) && (valueString.length() > 0)) {
                    try {
                        predValues[i] = Float.parseFloat(valueString);
                    } catch (Exception e) {
                        throw new DataFileException("Error at line " + reader.getLineNumber() + " of file " + dbFile.getCanonicalPath() + ": value for " + alleles[i] + " is not a valid number. Value found: " + valueString);
                    }
                }
            }
            result.setPredictions(peptide, predValues);
        }
        fis.close();
        return result;
    }
}
