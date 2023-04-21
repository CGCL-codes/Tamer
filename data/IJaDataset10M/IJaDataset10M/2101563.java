package org.openscience.cdk.xws.services;

import java.io.ByteArrayInputStream;
import net.bioclipse.xws.JavaDOMTools;
import net.bioclipse.xws.component.adhoc.function.FunctionInformation;
import net.bioclipse.xws.component.adhoc.function.IFunction;
import net.bioclipse.xws.component.xmpp.process.IProcessStatus;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.nonotify.NNChemFile;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.xws.schema.CML;
import org.w3c.dom.Element;
import org.xmlcml.cml.element.CMLMolecule;

public class AddExplicitHydrogens implements IFunction {

    private static final String FUNCTION_NAME = "addExplicitHydrogens";

    private static final String FUNCTION_DESCRIPTION = "Adds explicit hydrogens to recognized atom types in the input molecule.";

    private static final String FUNCTION_DETAILS = "A full-connected molecular structure in CML.";

    private static Convertor convertor = new Convertor(true, null);

    private static CDKAtomTypeMatcher atMatcher = CDKAtomTypeMatcher.getInstance(NoNotificationChemObjectBuilder.getInstance());

    private static CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(NoNotificationChemObjectBuilder.getInstance());

    private FunctionInformation info = null;

    public FunctionInformation getFunctionInformation() {
        if (info == null) {
            info = new FunctionInformation(FUNCTION_NAME, FUNCTION_DESCRIPTION, FUNCTION_DETAILS, CML.SCHEMATA, CML.SCHEMATA, true);
        }
        return info;
    }

    public void run(IProcessStatus ps, Element input) {
        Element output = null;
        if ("http://www.xml-cml.org/schema".equals(input.getNamespaceURI()) && "molecule".equals(input.getLocalName())) {
            String cmlString = JavaDOMTools.w3cElementToString(input);
            System.out.println("CML: " + cmlString);
            CMLReader reader = new CMLReader(new ByteArrayInputStream(cmlString.getBytes()));
            try {
                IChemFile file = (IChemFile) reader.read(new NNChemFile());
                IMolecule mol = file.getBuilder().newMolecule(ChemFileManipulator.getAllAtomContainers(file).get(0));
                IAtomType[] types = atMatcher.findMatchingAtomType(mol);
                for (int i = 0; i < mol.getAtomCount(); i++) {
                    if (types[i] != null) {
                        mol.getAtom(i).setAtomTypeName(types[i].getAtomTypeName());
                        hAdder.addImplicitHydrogens(mol, mol.getAtom(i));
                    }
                }
                AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
                CMLMolecule cmlMol = convertor.cdkAtomContainerToCMLMolecule(mol);
                String outputString = cmlMol.toXML();
                output = JavaDOMTools.string2Element(outputString);
            } catch (Exception e) {
                e.printStackTrace();
                ps.setError(e.getMessage());
            }
        } else {
            System.out.println("Expected CML but got: " + input.getNamespaceURI());
            ps.setError("Expected CML input.");
        }
        ps.setResult(output, "Done");
    }
}
