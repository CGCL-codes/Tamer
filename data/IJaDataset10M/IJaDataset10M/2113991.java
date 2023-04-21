package com.threerings.jpkg.ant.dpkg;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;

public class MockDpkg extends Dpkg {

    public static final String PREFIX = "/usr/local";

    public static final String DISTRIBUTION = "unstable";

    public MockDpkg() throws IOException {
        this(true);
    }

    public MockDpkg(boolean withPackage) throws IOException {
        final Project project = new Project();
        project.init();
        setProject(project);
        _output = File.createTempFile("dpkgtest", "output");
        _output.delete();
        _output.mkdir();
        setOutput(_output.getAbsolutePath());
        setPrefix(PREFIX);
        setDistribution(DISTRIBUTION);
        if (withPackage) {
            final Package pkg = new MockPackage(MockPackage.FILENAME);
            addPackage(pkg);
        }
    }

    /**
     * Returns the output directory for this mock object.
     */
    public File getOutput() {
        return _output;
    }

    /**
     * Call to cleanup the output location for this Dpkg object.
     */
    public void deleteOutput() throws IOException {
        FileUtils.deleteDirectory(_output);
    }

    private final File _output;
}
