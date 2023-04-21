package org.openscience.cdk.xws.services;

import java.io.ByteArrayInputStream;
import net.bioclipse.xws.JavaDOMTools;
import net.bioclipse.xws.component.adhoc.function.FunctionInformation;
import net.bioclipse.xws.component.adhoc.function.IFunction;
import net.bioclipse.xws.component.xmpp.process.IProcessStatus;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.modeling.builder3d.ModelBuilder3D;
import org.openscience.cdk.nonotify.NNChemFile;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.xws.schema.CML;
import org.w3c.dom.Element;
import org.xmlcml.cml.element.CMLMolecule;

public class SomeCDKFunctionTwo implements IFunction {

    private static final String FUNCTION_NAME = "generate3Dcoordinates";

    private static final String FUNCTION_DESCRIPTION = "Generates a 3D model for the input molecule.";

    private static final String FUNCTION_DETAILS = "A full-connected molecular structure in CML.";

    private static ModelBuilder3D builder;

    private static Convertor convertor = new Convertor(true, null);

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
                if (builder == null) {
                    builder = ModelBuilder3D.getInstance();
                }
                IMolecule mol = file.getBuilder().newMolecule(ChemFileManipulator.getAllAtomContainers(file).get(0));
                builder.generate3DCoordinates(mol, false);
                for (IBond bond : mol.bonds()) {
                    bond.getProperties().clear();
                }
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
