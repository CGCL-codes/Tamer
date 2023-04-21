package org.dllearner.examples.pdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.LearningProblemUnsupportedException;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class HelixRDFCreator {

    private static Logger _logger = Logger.getLogger(HelixRDFCreator.class);

    private static Logger _rootLogger = Logger.getRootLogger();

    private static String _dataDir = "../test/pdb/";

    private static File _dir = new File(_dataDir);

    /**
	 * @param args
	 * TODO: remove beginsAt, endsAt from model
	 */
    public static void main(String[] args) {
        Layout layout = new PatternLayout();
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        consoleAppender.setThreshold(Level.INFO);
        boolean htmlLog = false;
        Layout layout2 = null;
        FileAppender fileAppenderNormal = null;
        String fileName;
        if (htmlLog) {
            layout2 = new HTMLLayout();
            fileName = _dataDir + "log/log.html";
        } else {
            layout2 = new PatternLayout("%d [%t] %-5p %c : %m%n");
            fileName = _dataDir + "log/log.txt";
        }
        try {
            fileAppenderNormal = new FileAppender(layout2, fileName, false);
            fileAppenderNormal.setThreshold(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _rootLogger.removeAllAppenders();
        _rootLogger.addAppender(consoleAppender);
        _rootLogger.addAppender(fileAppenderNormal);
        _rootLogger.setLevel(Level.INFO);
        Boolean fasta = true;
        Boolean rdfConf = true;
        Boolean arff = true;
        Boolean dlLearn = false;
        Boolean wekaLearn = false;
        int dataSet = 5;
        PDBProtein testProtein = new PDBProtein("3A4R", "A");
        ProteinDataSet proteinSet;
        switch(dataSet) {
            case 1:
                proteinSet = ProteinDataSet.bt426();
                break;
            case 2:
                proteinSet = ProteinDataSet.plp273();
                break;
            case 3:
                proteinSet = ProteinDataSet.plp364();
                break;
            case 4:
                proteinSet = ProteinDataSet.plp399();
                break;
            default:
                proteinSet = new ProteinDataSet(testProtein);
                break;
        }
        PDBIdRdfModel trainmodel;
        for (int i = 0; i < proteinSet.getProteinset().size(); i++) {
            if (rdfConf || arff) {
                PDBProtein protein = proteinSet.getProteinset().get(i);
                _logger.info("Start with extracting data from: " + protein.getPdbID());
                String pdbDir = _dataDir + protein.getPdbID() + "/";
                File directory = new File(pdbDir);
                if (!directory.exists()) directory.mkdir();
                _logger.info("PDB ID: " + protein.getPdbID());
                _logger.info("Chain ID: " + protein.getChainID());
                trainmodel = new PDBIdRdfModel(protein);
                if (fasta) {
                    trainmodel.createFastaFile(pdbDir);
                }
                if (arff) {
                    ResIterator niter = trainmodel.getFirstAA();
                    createNumericArffFile(pdbDir, trainmodel, niter);
                    createNominalArffFile(pdbDir, trainmodel, niter);
                }
                Property beginsAt = ResourceFactory.createProperty("http://bio2rdf.org/pdb:", "beginsAt");
                trainmodel.removeStatementsWithPoperty(beginsAt);
                Property endsAt = ResourceFactory.createProperty("http://bio2rdf.org/pdb:", "endsAt");
                trainmodel.removeStatementsWithPoperty(endsAt);
                Resource residue = ResourceFactory.createResource("http://bio2rdf.org/pdb:Residue");
                trainmodel.removeStatementsWithObject(residue);
                Property isPartOf = ResourceFactory.createProperty("http://purl.org/dc/terms/", "isPartOf");
                trainmodel.removeStatementsWithPoperty(isPartOf);
                Property hasValue = ResourceFactory.createProperty("http://bio2rdf.org/pdb:", "hasValue");
                trainmodel.removeStatementsWithPoperty(hasValue);
                trainmodel.addDistanceInfo();
                if (rdfConf) {
                    String rdfFilePath = pdbDir + protein.getRdfFileName();
                    try {
                        createConfFile(pdbDir, trainmodel);
                        PrintStream out = new PrintStream(new File(rdfFilePath));
                        trainmodel.getModel().write(out, "RDF/XML");
                        out.close();
                    } catch (FileNotFoundException e) {
                        _logger.error("File " + rdfFilePath + " konnte nicht gefunden werden!");
                        e.printStackTrace();
                    }
                }
                if (protein.getSpecies() != "") {
                    File speciesProteins = new File(_dataDir + protein.getSpecies() + ".pos");
                    try {
                        String line = protein.getPdbID() + "." + protein.getChainID() + "." + protein.getSpecies() + "\n";
                        FileWriter out = new FileWriter(speciesProteins, true);
                        _logger.debug("Write " + line + " to file " + speciesProteins.getPath());
                        out.write(line);
                        out.close();
                    } catch (FileNotFoundException e) {
                        _logger.error("Could not find file " + speciesProteins.getPath() + speciesProteins.getName());
                        e.printStackTrace();
                    } catch (IOException e) {
                        _logger.error("Something went wrong while trying to write to " + speciesProteins.getPath() + speciesProteins.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        if (dlLearn) {
            startDlLearner();
        }
        if (wekaLearn) {
            startWekaLearner();
        }
    }

    private static void startDlLearner() {
        HashMap<String, File> pdbIDConfFile = loadConfFiles(_dir);
        for (String pdbID : pdbIDConfFile.keySet()) {
            try {
                new PDBDLLearner(pdbIDConfFile.get(pdbID));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ComponentInitException e) {
                e.printStackTrace();
            } catch (LearningProblemUnsupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void startWekaLearner() {
        HashMap<String, File> pdbIDArffFile = loadArffFiles(_dir);
        for (String pdbID : pdbIDArffFile.keySet()) {
            try {
                new PDBWekaLearner(pdbIDArffFile.get(pdbID));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static HashMap<String, File> loadConfFiles(File dir) {
        HashMap<String, File> confFiles = new HashMap<String, File>();
        _logger.info("Starting to load files in " + dir);
        File[] pdbDir = dir.listFiles(new DirectoryFileFilter());
        for (File activeDirectory : pdbDir) {
            File[] confFilesInActiveDirectory = activeDirectory.listFiles(new ConfFileFilter());
            _logger.info("Looking for Files in " + activeDirectory.getPath());
            for (File confFile : confFilesInActiveDirectory) {
                String confFileName = confFile.getName().substring(0, confFile.getName().indexOf(".conf"));
                confFiles.put(confFileName, confFile);
                _logger.info("Found .conf File " + confFile.getPath());
            }
        }
        return confFiles;
    }

    private static HashMap<String, File> loadArffFiles(File dir) {
        HashMap<String, File> arffFiles = new HashMap<String, File>();
        _logger.info("Starting to load files in " + dir);
        File[] pdbDir = dir.listFiles(new DirectoryFileFilter());
        for (File activeDirectory : pdbDir) {
            File[] arffFilesInActDir = activeDirectory.listFiles(new ArffFileFilter());
            _logger.info("Looking for .arff Files in " + activeDirectory.getPath());
            for (File arffFile : arffFilesInActDir) {
                String arffFileName = arffFile.getName().substring(0, arffFile.getName().indexOf(".arff"));
                arffFiles.put(arffFileName, arffFile);
                _logger.info("Found .arff File " + arffFile.getPath());
            }
        }
        return arffFiles;
    }

    private static void createConfFile(String pdbDir, PDBIdRdfModel model) {
        try {
            PDBProtein protein = model.getProtein();
            String confFilePath = pdbDir + protein.getConfFileName();
            PrintStream confFile = new PrintStream(new File(confFilePath));
            String ks = new String("// knowledge source definition\n" + "ks.type = \"OWL File\"\n" + "ks.fileName = \"AA_properties.owl\"\n" + "\n" + "ks.type = \"OWL File\"\n" + "ks.fileName = \"" + protein.getRdfFileName() + "\"\n");
            StringBuffer lp = new StringBuffer("// learning problem\n" + "lp.type = \"posNegStandard\"" + "\n" + "lp.positiveExamples = { ");
            HashMap<Resource, File> confFilePerResidue = AminoAcids.getAllConfFiles(pdbDir, protein.getConfFileName());
            HashMap<Resource, PrintStream> resprint = AminoAcids.getAminoAcidPrintStreamMap(confFilePerResidue);
            HashMap<Resource, StringBuffer> resourceStringBuffer = AminoAcids.getAminoAcidStringBufferMap(lp.toString());
            confFile.println(ks);
            Iterator<Resource> resources = resprint.keySet().iterator();
            while (resources.hasNext()) {
                resprint.get(resources.next()).println(ks);
            }
            ArrayList<Resource> positives = model.getPositives();
            Property type = ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "type");
            for (int i = 0; i < positives.size(); i++) {
                lp.append("\"" + positives.get(i).getURI() + "\", ");
                try {
                    Statement spo = model.getModel().getProperty(positives.get(i), type);
                    resourceStringBuffer.get(spo.getResource()).append("\"" + positives.get(i).getURI() + "\", ");
                } catch (NullPointerException e) {
                    _logger.error("Object probably not in our HashMap: " + model.getModel().getProperty(positives.get(i), type).getResource());
                    e.printStackTrace();
                }
            }
            if (lp.toString().contains(",")) lp.deleteCharAt(lp.lastIndexOf(","));
            lp.append("}\n" + "lp.negativeExamples = { ");
            resources = resourceStringBuffer.keySet().iterator();
            while (resources.hasNext()) {
                Resource residue = resources.next();
                if (resourceStringBuffer.get(residue).toString().contains(",")) resourceStringBuffer.get(residue).deleteCharAt(resourceStringBuffer.get(residue).lastIndexOf(","));
                resourceStringBuffer.get(residue).append("}\n" + "lp.negativeExamples = { ");
            }
            ArrayList<Resource> negatives = model.getNegatives();
            for (int i = 0; i < negatives.size(); i++) {
                lp.append("\"" + negatives.get(i).getURI() + "\", ");
                try {
                    _logger.info("Negative residue: " + negatives.get(i).getURI());
                    Statement spo = model.getModel().getProperty(negatives.get(i), type);
                    resourceStringBuffer.get(spo.getResource()).append("\"" + negatives.get(i).getURI() + "\", ");
                } catch (NullPointerException e) {
                    _logger.error("Object probably not in our HashMap: " + model.getModel().getProperty(negatives.get(i), type).getResource());
                    e.printStackTrace();
                }
            }
            if (lp.toString().contains(",")) lp.deleteCharAt(lp.lastIndexOf(","));
            lp.append("}\n");
            confFile.println(lp);
            resources = resourceStringBuffer.keySet().iterator();
            while (resources.hasNext()) {
                Resource residue = resources.next();
                if (resourceStringBuffer.get(residue).toString().contains(",")) resourceStringBuffer.get(residue).deleteCharAt(resourceStringBuffer.get(residue).lastIndexOf(","));
                resourceStringBuffer.get(residue).append("}\n");
                resprint.get(residue).println(resourceStringBuffer.get(residue));
            }
            confFile.close();
            Iterator<Resource> newkeys = resprint.keySet().iterator();
            while (newkeys.hasNext()) {
                resprint.get(newkeys.next()).close();
            }
        } catch (IOException e) {
            _logger.error("OutputStream konnte nicht geschlossen werden!");
        }
    }

    private static void createNumericArffFile(String pdbDir, PDBIdRdfModel model, ResIterator firstAAs) {
        try {
            PDBProtein protein = model.getProtein();
            String arffFilePath = pdbDir + protein.getArffFileName();
            arffFilePath = arffFilePath.replace(".arff", ".numeric.arff");
            PrintStream out = new PrintStream(arffFilePath);
            _logger.debug("Creating numeric ARFF file: " + arffFilePath);
            String relation = "@RELATION " + protein.getPdbID();
            out.println(relation);
            _logger.debug(relation);
            StringBuffer attributes = new StringBuffer("@ATTRIBUTE hydrophobicity NUMERIC\n" + "@ATTRIBUTE polarity NUMERIC\n" + "@ATTRIBUTE size NUMERIC\n");
            for (int i = -8; i <= 8; i++) {
                attributes.append("@ATTRIBUTE aa_position_" + i + " NUMERIC\n");
            }
            attributes.append("@ATTRIBUTE in_helix NUMERIC\n");
            _logger.debug(attributes);
            out.println(attributes);
            String data = "@DATA\n";
            _logger.debug(data);
            out.println(data);
            HashMap<String, String> resdata = AminoAcids.getAminoAcidNumericArffAttributeMap();
            HashMap<String, String> resnum = AminoAcids.getAminoAcidNumber();
            ArrayList<Resource> positives = model.getPositives();
            ArrayList<Resource> negatives = model.getNegatives();
            String sequence = protein.getSequence();
            HashMap<Integer, Resource> posRes = model.getPositionResource();
            for (int i = 0; i < sequence.length(); i++) {
                StringBuffer dataLine = new StringBuffer("");
                String key = Character.toString(sequence.charAt(i));
                if (resdata.containsKey(key)) {
                    dataLine.append(resdata.get(key) + ",");
                } else {
                    dataLine.append(resdata.get("X") + ",");
                }
                for (int j = (i - 8); j <= (i + 8); j++) {
                    try {
                        dataLine.append(resnum.get(Character.toString(protein.getSequence().charAt(j))) + ",");
                    } catch (IndexOutOfBoundsException e) {
                        dataLine.append("?,");
                    }
                }
                if (positives.contains(posRes.get(new Integer(i)))) {
                    dataLine.append("1");
                } else if (negatives.contains(posRes.get(new Integer(i)))) {
                    dataLine.append("0");
                } else {
                    dataLine.append("?");
                }
                _logger.debug(dataLine);
                out.println(dataLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createNominalArffFile(String pdbDir, PDBIdRdfModel model, ResIterator firstAAs) {
        try {
            PDBProtein protein = model.getProtein();
            String arffFilePath = pdbDir + protein.getArffFileName();
            arffFilePath = arffFilePath.replace(".arff", ".nominal.arff");
            PrintStream out = new PrintStream(arffFilePath);
            _logger.debug("Creating nominal ARFF file: " + arffFilePath);
            String relation = "@RELATION " + protein.getPdbID();
            out.println(relation);
            _logger.debug(relation);
            StringBuffer attributes = new StringBuffer("@ATTRIBUTE hydrophob {hydrophilic, hydrophobic, aromatic, aliphatic}\n" + "@ATTRIBUTE charge {unpolar, polar, positive, negative}\n" + "@ATTRIBUTE size {tiny, small, large}\n");
            for (int i = -8; i <= 8; i++) {
                attributes.append("@ATTRIBUTE aa_position_" + i + " {A,C,D,E,F,G,H,I,K,L,M,N,P,Q,R,S,T,V,W,Y}\n");
            }
            attributes.append("@ATTRIBUTE in_helix {Helix, Non_helix}\n");
            _logger.debug(attributes);
            out.println(attributes);
            String data = "@DATA\n";
            _logger.debug(data);
            out.println(data);
            HashMap<String, String> resdata = AminoAcids.getAminoAcidNominalArffAttributeMap();
            ArrayList<Resource> positives = model.getPositives();
            ArrayList<Resource> negatives = model.getNegatives();
            String sequence = protein.getSequence();
            HashMap<Integer, Resource> posRes = model.getPositionResource();
            for (int i = 0; i < sequence.length(); i++) {
                StringBuffer dataLine = new StringBuffer("");
                String key = Character.toString(sequence.charAt(i));
                if (resdata.containsKey(key)) {
                    dataLine.append(resdata.get(key) + ",");
                } else {
                    dataLine.append(resdata.get("X") + ",");
                }
                for (int j = (i - 8); j <= (i + 8); j++) {
                    try {
                        dataLine.append(protein.getSequence().charAt(j) + ",");
                    } catch (IndexOutOfBoundsException e) {
                        dataLine.append("?,");
                    }
                }
                if (positives.contains(posRes.get(new Integer(i)))) {
                    dataLine.append("Helix");
                } else if (negatives.contains(posRes.get(new Integer(i)))) {
                    dataLine.append("Non_helix");
                } else {
                    dataLine.append("?");
                }
                _logger.debug(dataLine);
                out.println(dataLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
