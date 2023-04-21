package net.code4j.dolon.service.indexer;

import java.io.InputStream;
import java.util.Set;

/**
 * @author xandro
 * 
 */
public class ArtifactAnalyser {

    private final InputStream artifactInputStrem;

    public ArtifactAnalyser(InputStream artifactInputStrem) {
        super();
        this.artifactInputStrem = artifactInputStrem;
    }

    public Set<String> getPackages() {
        return null;
    }

    public Set<String> getClasses() {
        return null;
    }
}
