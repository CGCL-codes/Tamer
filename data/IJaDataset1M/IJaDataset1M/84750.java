package net.sf.buildbox.devmodel.api;

import java.util.List;
import java.util.Set;
import net.sf.buildbox.devmodel.ArtifactRef;
import net.sf.buildbox.devmodel.Version;
import net.sf.buildbox.devmodel.VersionId;

/**
 * access to libraries
 */
public interface LibraryDao {

    Version getLibrary(VersionId libraryId);

    List<Version> findLibraries(String groupId, String artifactId, String version);

    Set<String> listGroupIds(String groupIdPattern);

    List<ArtifactRef> findArtifacts(String groupId, String artifactId, String version, String classifier, String type);
}
