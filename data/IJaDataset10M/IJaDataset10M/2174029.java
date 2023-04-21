package at.ac.tuwien.law.yaplaf.plugin.output.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import at.ac.tuwien.law.yaplaf.entities.Equality;
import at.ac.tuwien.law.yaplaf.entities.Evaluation;
import at.ac.tuwien.law.yaplaf.entities.Paper;
import at.ac.tuwien.law.yaplaf.exceptions.MissingPropertyException;

/**
 * CSVOutput
 * @author mat
 * 
 * CSVOutput gibt die Daten des LVCSVektors im alten yaplaf Format aus.
 */
public class CsvOutput implements at.ac.tuwien.law.yaplaf.interfaces.Output {

    Logger logger = Logger.getLogger(CsvOutput.class);

    private Properties p;

    @Override
    public void execute(List<Evaluation> l) throws MissingPropertyException {
        String s = this.getProperties().getProperty("csv-seperator");
        if (s == null) s = ";";
        String matrikel1, vorname1, nachname1, laenge1, lcs1;
        Date abgabezeit1;
        String matrikel2, vorname2, nachname2, laenge2, lcs2;
        Date abgabezeit2;
        String lcs, anz;
        if (p.getProperty("csv-outfile") == null) {
            p.setProperty("csv-outfile", "testout.csv");
        }
        File out = new File(p.getProperty("csv-outfile"));
        FileWriter outstream = null;
        try {
            outstream = new FileWriter(out);
            outstream.write("Matrikelnr1" + s + "Vorname1" + s + "Nachname1" + s + "L�nge1" + s + "Abgabezeit1" + s + "Matrikelnr2" + s + "Vorname2" + s + "Nachname2" + s + "L�nge2" + s + "Abgabezeit2" + s + "LCS" + s + "Anzahl" + "\n");
        } catch (IOException e2) {
            this.logger.error("Failed to open File " + out);
            e2.printStackTrace();
        }
        for (Evaluation item : l) {
            Paper e = item.getPaper();
            matrikel1 = e.getMatriculationNumber();
            if (matrikel1 == null) matrikel1 = "";
            vorname1 = e.getVorname();
            if (vorname1 == null) vorname1 = "";
            nachname1 = e.getNachname();
            if (nachname1 == null) nachname1 = "";
            abgabezeit1 = e.getAbgabezeit();
            if (abgabezeit1 == null) abgabezeit1 = new Date();
            laenge1 = (new Integer(e.getPaperText().length())).toString();
            for (Equality eq : item.getAllEqualities()) {
                Paper comp = eq.getPaper();
                matrikel2 = comp.getMatriculationNumber();
                if (matrikel2 == null) matrikel2 = "";
                vorname2 = comp.getVorname();
                if (vorname2 == null) vorname2 = "";
                nachname2 = comp.getNachname();
                if (nachname2 == null) nachname2 = "";
                abgabezeit2 = comp.getAbgabezeit();
                if (abgabezeit2 == null) abgabezeit2 = new Date();
                laenge2 = (new Integer(comp.getPaperText().length())).toString();
                lcs = eq.getProperties().getProperty("lcs");
                if (lcs == null) lcs = "";
                anz = eq.getProperties().getProperty("count");
                if (anz == null) anz = "";
                try {
                    outstream.write(matrikel1 + s + vorname1 + s + nachname1 + s + laenge1 + s + abgabezeit1 + s + matrikel2 + s + vorname2 + s + nachname2 + s + laenge2 + s + abgabezeit2 + s + lcs + s + anz + "\n");
                } catch (IOException e1) {
                    this.logger.error("Failed to Write to File");
                    e1.printStackTrace();
                }
                this.logger.info(matrikel1 + s + laenge1 + s + matrikel2 + s + laenge2 + s + lcs + s + anz);
            }
        }
        try {
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Properties getProperties() {
        return this.p;
    }

    @Override
    public Map<String, String> getPropertiesDescription() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("csv-outfile", "CSV Output file");
        m.put("csv-seperator", "Seperator for the CSV file, default is the colon (,)");
        return m;
    }

    @Override
    public void setProperties(Properties props) throws MissingPropertyException {
        this.p = props;
    }
}
