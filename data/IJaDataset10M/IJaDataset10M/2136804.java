package org.ctext.ite.project;

import java.io.File;

/**
 * Stores the directories used for the ITE project.
 * @author W. Fourie
 */
public class Directories {

    public String projectDir;

    public String outputDir;

    public String sourceDir;

    public String targetDir;

    public String omegatSource;

    public String omegatTarget;

    private String sep = File.separator;

    /**
     * Stores the directories
     * @param ProjectDirectory
     * @param OutputDirectory
     */
    public Directories(String ProjectDirectory, String OutputDirectory) {
        projectDir = ProjectDirectory;
        outputDir = OutputDirectory;
        sourceDir = projectDir + "Source" + sep;
        targetDir = projectDir + "Target" + sep;
        omegatSource = projectDir + "omegat" + sep + "source" + sep;
        omegatTarget = projectDir + "omegat" + sep + "target" + sep;
    }
}
