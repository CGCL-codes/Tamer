package net.stickycode.stile.cli;

import java.io.File;
import javax.inject.Inject;
import net.stickycode.resource.FileResource;
import net.stickycode.resource.Resource;
import net.stickycode.stereotype.Configured;
import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.ArtifactParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StileCli {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private ArtifactParser parser;

    @Configured
    File workingDirectory;

    public Artifact loadArtifact(String path) {
        Resource stile = new FileResource(new File(workingDirectory, path));
        log.info("loading artifact from", stile);
        if (!stile.canLoad()) throw new ArtifactResourceCannotBeFoundException(stile);
        return parser.parse(stile);
    }

    public void execute() {
        Workspace workspace = new Workspace();
    }
}
