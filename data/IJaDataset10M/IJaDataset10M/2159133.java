package org.dllearner.examples.pdb;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.apache.log4j.Logger;

public class ProteinDataSet {

    private static Logger _logger = Logger.getLogger(HelixRDFCreator.class);

    private static String _dataDir = "../test/pdb/";

    private static File bt426List = new File(_dataDir + "bt426.list");

    public static ProteinDataSet bt426() {
        return new ProteinDataSet(bt426List);
    }

    private static File plp273List = new File(_dataDir + "plp273.list");

    public static ProteinDataSet plp273() {
        return new ProteinDataSet(plp273List);
    }

    private static File plp364List = new File(_dataDir + "plp364.list");

    public static ProteinDataSet plp364() {
        return new ProteinDataSet(plp364List);
    }

    private static File plp399List = new File(_dataDir + "plp399.list");

    public static ProteinDataSet plp399() {
        return new ProteinDataSet(plp399List);
    }

    private ArrayList<PDBProtein> _proteinSet;

    public ArrayList<PDBProtein> getProteinset() {
        return _proteinSet;
    }

    public ProteinDataSet(PDBProtein protein) {
        this._proteinSet = new ArrayList<PDBProtein>(1);
        this._proteinSet.add(protein);
    }

    public ProteinDataSet(PDBProtein[] proteins) {
        this._proteinSet = new ArrayList<PDBProtein>(proteins.length);
        for (int i = 0; i < proteins.length; i++) {
            this._proteinSet.add(proteins[i]);
        }
    }

    public ProteinDataSet(File pdbIDlist) {
        try {
            LineNumberReader pdbproteins = new LineNumberReader(new FileReader(pdbIDlist));
            ArrayList<String> lines = this.readInFile(pdbproteins);
            pdbproteins.close();
            int linenr = lines.size();
            _logger.info("File " + pdbIDlist.getCanonicalPath() + " has " + linenr + " lines.");
            this._proteinSet = new ArrayList<PDBProtein>(linenr);
            for (int i = 0; i < linenr; i++) {
                _logger.info("LINES element " + i + " contains " + lines.get(i));
                this._proteinSet.add(new PDBProtein(this.getPdbID(i, lines), this.getChainID(i, lines), this.getSpecies(i, lines)));
            }
        } catch (IOException e) {
            _logger.error("File " + pdbIDlist.getAbsolutePath() + " could not be read in!");
            e.printStackTrace();
        }
    }

    private ArrayList<String> readInFile(LineNumberReader lnr) {
        ArrayList<String> arraylist = new ArrayList<String>();
        try {
            String line;
            while ((line = lnr.readLine()) != null) {
                arraylist.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arraylist;
    }

    private String getPdbID(int lineNumber, ArrayList<String> lines) {
        String line = (String) lines.get(lineNumber);
        String pdbID;
        if (line.length() >= 4) {
            pdbID = line.substring(0, line.indexOf("."));
        } else {
            pdbID = "";
        }
        return pdbID;
    }

    private String getChainID(int lineNumber, ArrayList<String> lines) {
        String line = (String) lines.get(lineNumber);
        String chainID;
        if (line.contains(".")) {
            chainID = line.substring(line.indexOf(".") + 1, line.lastIndexOf("."));
        } else {
            chainID = "";
        }
        return chainID;
    }

    private String getSpecies(int lineNumber, ArrayList<String> lines) {
        String line = (String) lines.get(lineNumber);
        String species;
        if (line.length() > 6) {
            species = line.substring(line.lastIndexOf("."));
        } else {
            species = "";
        }
        return species;
    }
}
