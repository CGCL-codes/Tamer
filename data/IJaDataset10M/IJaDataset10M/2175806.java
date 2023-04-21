package org.tuba.spatschorke.diploma.repository.mock;

import java.io.File;
import org.tuba.plugins.IArtefactRepresentation;
import org.tuba.plugins.IRepository;
import org.tuba.spatschorke.diploma.representation.ecoretoolsdiagram.EcoreToolsGMFDiagramURI;

public class Repository implements IRepository {

    @Override
    public IArtefactRepresentation getArtefact(String artefactID) {
        File model = ModelController.getInstance().getModelMap().get(artefactID);
        if (model == null) return null;
        String fileName = model.getAbsolutePath() + ModelController.DIAGRAM_EXTENSION;
        return new EcoreToolsGMFDiagramURI(fileName);
    }
}
