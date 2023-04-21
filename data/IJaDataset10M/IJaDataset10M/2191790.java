package com.informatics.polymer.server.DescriptorCalculation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.APolDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.AromaticAtomsCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.AromaticBondsCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.AtomCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.BondCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.FragmentComplexityDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondAcceptorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.HBondDonorCountDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.KappaShapeIndicesDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.RuleOfFiveDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.TPSADescriptor;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * A class to calculate Chemistry Development Kit (CDK) descriptors for
 * newly added repeat units.
 * @author ed
 * @version 1.0
 */
public class MolecularDescriptorCalculation {

    /**
	 * Instantiates a MolecularDescriptorCalculation object.
	 */
    public MolecularDescriptorCalculation() {
    }

    /**
	 * Method to calculate descriptors for a repeat unit.
	 * @param - smileString, the molecule representation to have descriptors generated.
	 * @return HashMap of calculated descriptor values.
	 * @throws CDKException 
	 */
    public HashMap<String, Double> calculateAllDescriptors(final String smileString) throws CDKException {
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule m = sp.parseSmiles(smileString);
        IMolecularDescriptor aLOGPDescriptor = new ALOGPDescriptor();
        DescriptorValue vals = aLOGPDescriptor.calculate(m);
        String[] calculatedDescriptors = vals.getValue().toString().split(",");
        HashMap<String, Double> descriptorValues = new HashMap<String, Double>();
        int counter = 0;
        for (String s : calculatedDescriptors) {
            Double data = Double.parseDouble(s);
            if (counter == 0) {
                descriptorValues.put("ALOGP", data);
            } else if (counter == 1) {
                descriptorValues.put("ALogP2", data);
            } else if (counter == 2) {
                descriptorValues.put("AMR", data);
            }
            counter++;
        }
        IMolecularDescriptor aPol = new APolDescriptor();
        IDescriptorResult aPolValue = aPol.calculate(m).getValue();
        descriptorValues.put("APol", Double.parseDouble(aPolValue.toString()));
        IMolecularDescriptor aromaticAtomCount = new AromaticAtomsCountDescriptor();
        IDescriptorResult aromaticAtomCountValue = aromaticAtomCount.calculate(m).getValue();
        descriptorValues.put("AromaticAtomCount", Double.parseDouble(aromaticAtomCountValue.toString()));
        IMolecularDescriptor aromaticBondCount = new AromaticBondsCountDescriptor();
        descriptorValues.put("AromaticBondCount", Double.parseDouble(aromaticBondCount.calculate(m).getValue().toString()));
        IMolecularDescriptor atomCount = new AtomCountDescriptor();
        descriptorValues.put("AtomCount", Double.parseDouble(atomCount.calculate(m).getValue().toString()));
        IMolecularDescriptor bondCount = new BondCountDescriptor();
        descriptorValues.put("BondCount", Double.parseDouble(bondCount.calculate(m).getValue().toString()));
        IMolecularDescriptor fragmentComplexityDescriptor = new FragmentComplexityDescriptor();
        descriptorValues.put("FragmentComplexity", Double.parseDouble(fragmentComplexityDescriptor.calculate(m).getValue().toString()));
        IMolecularDescriptor hBondAcceptorCount = new HBondAcceptorCountDescriptor();
        descriptorValues.put("HBACount", Double.parseDouble(hBondAcceptorCount.calculate(m).getValue().toString()));
        IMolecularDescriptor hBondDonorCount = new HBondDonorCountDescriptor();
        descriptorValues.put("HBDCount", Double.parseDouble(hBondDonorCount.calculate(m).getValue().toString()));
        IMolecularDescriptor kappaShapes = new KappaShapeIndicesDescriptor();
        String[] separateKappaIndices = kappaShapes.calculate(m).getValue().toString().split(",");
        descriptorValues.put("Kappa1", Double.parseDouble(separateKappaIndices[0]));
        descriptorValues.put("Kappa2", Double.parseDouble(separateKappaIndices[1]));
        descriptorValues.put("Kappa3", Double.parseDouble(separateKappaIndices[2]));
        IMolecularDescriptor ruleOfFive = new RuleOfFiveDescriptor();
        descriptorValues.put("RuleOf5", Double.parseDouble(ruleOfFive.calculate(m).getValue().toString()));
        IMolecularDescriptor tPSA = new TPSADescriptor();
        descriptorValues.put("TPSA", Double.parseDouble(tPSA.calculate(m).getValue().toString()));
        return descriptorValues;
    }
}
