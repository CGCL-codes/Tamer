package org.tuba.spatschorke.diploma.repository.svn.sdedit;

import java.io.File;
import org.tuba.plugins.IRepository;
import org.tuba.spatschorke.diploma.repository.svn.SVNExporter;
import org.tuba.spatschorke.diploma.representation.sdedit.SDEditFile;

public class Repository implements IRepository {

    @Override
    public SDEditFile getArtefact(String artefactId) {
        if (artefactId == null) return null;
        File file = SVNExporter.export(artefactId);
        if (file == null) return null;
        file.deleteOnExit();
        return new SDEditFile(file);
    }
}
