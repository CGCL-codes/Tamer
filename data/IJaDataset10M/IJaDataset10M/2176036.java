package org.tuba.spatschorke.diploma.operation.ecoretools.generatediagram;

import java.io.File;
import org.junit.Assert;
import org.junit.Test;
import org.tuba.plugins.IArtefactRepresentation;
import org.tuba.spatschorke.diploma.operation.ecoretools.generatediagram.Operation;
import org.tuba.spatschorke.diploma.representation.ecore.EcoreModelRepresentation;
import org.tuba.spatschorke.diploma.representation.ecoretoolsdiagram.EcoreToolsGMFDiagramURI;

public class GenerationTest {

    @Test
    public void generationTest() {
        String curDir = System.getProperty("user.dir");
        String modelFile = curDir + "/test/CompositePattern.ecore";
        EcoreModelRepresentation model = new EcoreModelRepresentation(modelFile);
        Operation operation = new Operation();
        IArtefactRepresentation artefact = operation.process(model, null);
        Assert.assertNotNull(artefact);
        EcoreToolsGMFDiagramURI diagram = (EcoreToolsGMFDiagramURI) artefact;
        String fileName = diagram.getDiagramURI().toFileString();
        File file = new File(fileName);
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.getTotalSpace() > 0);
        file.delete();
    }
}
