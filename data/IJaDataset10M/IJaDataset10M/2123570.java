package net.sf.ts2.pm.artifact.repository.layout;

import java.util.List;
import net.sf.ts2.pm.artifact.repository.layout.scanner.RepositoryScanner;
import net.sf.ts2.pm.model.Artifact;

/**
 * @author chalumeau
 */
public interface ArtifactRepositoryLayout {

    RepositoryScanner getRepositoryScanner();

    void setRepositoryScanner(RepositoryScanner repositoryScanner);

    String pathOf(Artifact artifact) throws ArtifactPathFormatException;

    List scanRepository();
}
