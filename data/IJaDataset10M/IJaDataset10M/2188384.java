package org.deft.representation.tempfile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.util.FileUtil;

public class TempFileRepresentation implements ArtifactRepresentation {

    private File tempDir;

    private String path;

    public static final String TYPE = "org.deft.representation.tempfile";

    public TempFileRepresentation(String tempDir) {
        this.tempDir = new File(tempDir);
    }

    public TempFileRepresentation(File tempDir) {
        this.tempDir = tempDir;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /**
	 * Stores a given {@link Artifact} and internally referred {@link Artifact}s
	 * at the given directory.
	 * 
	 * @param artifact
	 *            The {@link Artifact} to be stored.
	 * @param directoryPath
	 *            The directory where the {@link Artifact} shall be stored.
	 * @throws IOException
	 */
    public void writeArtifactAndRelatedArtifacts(Artifact artifact) throws IOException {
        String directoryPath = tempDir.getAbsolutePath();
        Set<Artifact> dependingArtifacts = getAllBasicArtifacts(artifact);
        String rootDirectory = computeRootDirectory(dependingArtifacts);
        for (Artifact a : dependingArtifacts) {
            byte[] content = a.getArtifactContent();
            String originalLocation = a.getOriginalLocation();
            String pathWithoutRootDirPart = originalLocation.substring(rootDirectory.length());
            String completeDestFilePath = directoryPath + pathWithoutRootDirPart;
            File destFile = new File(completeDestFilePath);
            File destFileDir = destFile.getParentFile();
            if (destFileDir != null) {
                destFileDir.mkdirs();
            }
            FileUtil.saveToFile(content, destFile, true);
            destFile.deleteOnExit();
            if (a.equals(artifact)) {
                path = destFile.getAbsolutePath();
            }
        }
    }

    private Set<Artifact> getAllBasicArtifacts(Artifact artifact) {
        Set<Artifact> result = new HashSet<Artifact>();
        result.add(artifact);
        List<Artifact> basicArtifacts = artifact.getBasicArtifacts();
        for (Artifact a : basicArtifacts) {
            result.addAll(getAllBasicArtifacts(a));
        }
        return result;
    }

    private String computeRootDirectory(Set<Artifact> artifacts) {
        List<String> paths = new LinkedList<String>();
        for (Artifact artifact : artifacts) {
            paths.add(artifact.getOriginalLocation());
        }
        String root = FileUtil.getCommonRootDirectory(paths);
        return root;
    }
}
