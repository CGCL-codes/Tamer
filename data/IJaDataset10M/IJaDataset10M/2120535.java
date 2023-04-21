package org.openscience.cdk.applications.taverna.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.embl.ebi.escience.baclava.DataThing;
import org.openscience.cdk.applications.taverna.CDKTavernaConfig;
import org.openscience.cdk.applications.taverna.CMLChemFile;
import org.openscience.cdk.applications.taverna.basicutilities.CMLChemFileWrapper;
import org.openscience.cdk.applications.taverna.basicutilities.FileNameGenerator;
import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

public class CreatePDFWith2DStructures implements CDKLocalWorker {

    private String[] inputNames = new String[] { "inputStructures", "fileName" };

    private String[] outputNames = new String[] { "Comment" };

    public String[] inputNames() {
        return inputNames;
    }

    public String[] inputTypes() {
        return new String[] { CDKLocalWorker.CMLChemFileList, CDKLocalWorker.STRING_ARRAY };
    }

    public String[] outputNames() {
        return outputNames;
    }

    public String[] outputTypes() {
        return new String[] { CDKLocalWorker.STRING_ARRAY };
    }

    @SuppressWarnings("unchecked")
    public Map<String, DataThing> execute(Map<String, DataThing> inputs) throws TaskExecutionException {
        FileNameGenerator fileNameGenerator = new FileNameGenerator();
        boolean tab = true;
        boolean first = true;
        List<String> fileNames = null;
        String fileName = "";
        int numberOfMoleculesPerPDF = 500;
        List<IAtomContainer> giveList = new ArrayList<IAtomContainer>();
        String ofor = "pdf";
        List<CMLChemFile> inputList = null;
        List<String> comment = new ArrayList<String>();
        if (inputs.get(inputNames[0]) != null) {
            inputList = CMLChemFileWrapper.getListOfCMLChemfileFromDataThing(inputs.get(inputNames[0]));
        } else {
            return null;
        }
        if (inputs.get(inputNames[1]) != null) {
            fileNames = (List<String>) ((DataThing) (inputs.get(inputNames[1]))).getDataObject();
        }
        try {
            for (CMLChemFile file : inputList) {
                if (first) {
                    if (fileNames != null && fileNames.size() > 0) {
                        fileName = fileNames.get(0);
                    }
                    fileName = fileNameGenerator.generateFileNameAndPathFromList((List<String>) file.getProperty(FileNameGenerator.FILENAME), fileName);
                    first = false;
                }
                List<IAtomContainer> moleculeList = ChemFileManipulator.getAllAtomContainers(file);
                for (IAtomContainer atomContainer : moleculeList) {
                    if (file.getProperty(CDKTavernaConfig.DATABASEID) != null) {
                        atomContainer.setProperty(CDKTavernaConfig.DATABASEID, file.getProperty(CDKTavernaConfig.DATABASEID));
                    }
                    if (file.getProperty(CDKTavernaConfig.ORIGIN) != null) {
                        atomContainer.setProperty(CDKTavernaConfig.ORIGIN, file.getProperty(CDKTavernaConfig.ORIGIN));
                    }
                    if (file.getProperty(CDKTavernaConfig.ORIGINALID) != null) {
                        atomContainer.setProperty(CDKTavernaConfig.ORIGINALID, file.getProperty(CDKTavernaConfig.ORIGINALID));
                    }
                    giveList.add(atomContainer);
                }
            }
            try {
                if (giveList.size() > numberOfMoleculesPerPDF) {
                    int counter = 0;
                    while (giveList.size() > counter) {
                        Draw2DStructure.setWidth(300);
                        Draw2DStructure.setHeight(300);
                        if (counter + numberOfMoleculesPerPDF > giveList.size()) {
                            Draw2DStructure.draw(giveList.subList(counter, giveList.size()), tab, ofor, fileName);
                        } else {
                            Draw2DStructure.draw(giveList.subList(counter, counter + numberOfMoleculesPerPDF), tab, ofor, fileName);
                        }
                        counter += numberOfMoleculesPerPDF;
                    }
                } else {
                    Draw2DStructure.setWidth(300);
                    Draw2DStructure.setHeight(300);
                    Draw2DStructure.draw(giveList, tab, ofor, fileName);
                }
                comment.add("done");
            } catch (Exception e) {
                System.out.println("The creation of the pdf file caused an Error");
            }
        } catch (Exception exception) {
            throw new TaskExecutionException(exception);
        }
        Map<String, DataThing> outputs = new HashMap<String, DataThing>();
        outputs.put(outputNames[0], new DataThing(comment));
        return outputs;
    }
}
