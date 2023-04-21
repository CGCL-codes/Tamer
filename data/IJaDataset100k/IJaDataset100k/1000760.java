package org.cleanbuild.gatherer.providerproxy;

import org.cleanbuild.projectmetadata.spi.ArchiveMetadataProvider;

public class ModuleSetArchive implements ArchiveMetadataProvider {

    private final String path;

    public ModuleSetArchive(String path) {
        this.path = path;
    }

    public String getPathToArchive() {
        return path;
    }
}
