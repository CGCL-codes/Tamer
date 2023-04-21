package org.openscience.cdk.test;

import org.openscience.cdk.*;
import org.openscience.cdk.io.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.geometry.*;
import java.util.*;
import java.io.*;

public class TableTest {

    public TableTest(String inFile) {
        try {
            ChemObjectReader reader;
            System.out.println("Loading: " + inFile);
            if (inFile.endsWith(".xyz")) {
                reader = new XYZReader(new FileReader(inFile));
                System.out.println("Expecting XYZ format...");
            } else if (inFile.endsWith(".cml")) {
                reader = new CMLReader(new FileReader(inFile));
                System.out.println("Expecting CML format...");
            } else {
                reader = new MDLReader(new FileInputStream(inFile));
                System.out.println("Expecting MDL MolFile format...");
            }
            ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
            ChemSequence chemSequence;
            ChemModel chemModel;
            SetOfMolecules setOfMolecules;
            for (int sequence = 0; sequence < chemFile.getChemSequenceCount(); sequence++) {
                chemSequence = chemFile.getChemSequence(sequence);
                for (int model = 0; model < chemSequence.getChemModelCount(); model++) {
                    chemModel = chemSequence.getChemModel(model);
                    setOfMolecules = chemModel.getSetOfMolecules();
                    MoleculesTable mt = new MoleculesTable(setOfMolecules);
                    mt.display();
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String filename = args[0];
            if (new File(filename).canRead()) {
                new TableTest(filename);
            } else {
                System.out.println("File " + filename + " does not exist!");
            }
        } else {
            System.out.println("Syntax: TableTest <inputfile>");
        }
    }
}
