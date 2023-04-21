package net.sf.buildbox.devmodel.fs.api;

import java.io.File;
import net.sf.buildbox.devmodel.VersionId;

public interface VersionLocator {

    File computeLocation(VersionId versionId);

    Iterable<String> findMatchingGroupIds(String groupId);

    Iterable<String> allArtifacts(String groupId);

    Iterable<VersionId> allVersions(String groupId, String artifactId);

    Iterable<VersionId> findMatchingVersionIds(VersionId versionTemplate);
}
