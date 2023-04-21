package org.tuba.spatschorke.diploma.repository.svn.ecore;

import java.io.File;
import org.tuba.plugins.IRepository;
import org.tuba.spatschorke.diploma.repository.svn.SVNExporter;
import org.tuba.spatschorke.diploma.representation.ecore.EcoreModelRepresentation;

public class Repository implements IRepository {

    @Override
    public EcoreModelRepresentation getArtefact(String artefactId) {
        if (artefactId == null) return null;
        File file = SVNExporter.export(artefactId);
        if (file == null) return null;
        file.deleteOnExit();
        return new EcoreModelRepresentation(file.getAbsolutePath());
    }
}
