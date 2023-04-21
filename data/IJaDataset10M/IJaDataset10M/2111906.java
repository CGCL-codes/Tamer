package net.sf.buildbox.releasator.spi;

import java.io.IOException;
import net.sf.buildbox.releasator.BuildResult;
import net.sf.buildbox.releasator.DistroType;

public interface CodeBuilder {

    public String getGroupId();

    public String getArtifactId();

    public String getVersion();

    public void requestDistro(DistroType distroType);

    public BuildResult build() throws IOException, InterruptedException;
}
