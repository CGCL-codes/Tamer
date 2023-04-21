package br.com.caelum.testslicer.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.resources.FileResource;
import br.com.caelum.testslicer.registry.DefaultRegistryData;
import br.com.caelum.testslicer.registry.RegistryData;

public class PickTests extends Task {

    private DirSet dirs;

    private FileSet fullTests;

    private File output;

    private boolean runFullBuildOnNoChangesDetected = false;

    private int runFullBuildOnEvery = 5;

    public void setRunFullBuildOnEvery(int runFullBuildOnEvery) {
        this.runFullBuildOnEvery = runFullBuildOnEvery;
    }

    public void setRunFullBuildOnNoChangesDetected(boolean runFullBuildOnNoChangesDetected) {
        this.runFullBuildOnNoChangesDetected = runFullBuildOnNoChangesDetected;
    }

    public void add(DirSet dir) {
        this.dirs = dir;
    }

    public void add(FileSet fullTests) {
        this.fullTests = fullTests;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void execute() {
        List<FileTimeStampChangedFilesSelector> list = new ArrayList<FileTimeStampChangedFilesSelector>();
        RegistryData data = new DefaultRegistryData(new File(".testslicer"));
        for (Iterator it = dirs.iterator(); it.hasNext(); ) {
            FileResource fr = (FileResource) it.next();
            File f = fr.getFile();
            list.add(new FileTimeStampChangedFilesSelector(f, data));
        }
        data.increaseBuilds();
        if (data.shouldFullBuild(runFullBuildOnEvery)) {
            log("Will run everything because it is the " + runFullBuildOnEvery + " build with testslicer.");
            empty();
            return;
        }
        ChangedFilesSelector selector = new MultipleChangedFilesSelector(list.toArray(new ChangedFilesSelector[0]));
        List<String> changes = selector.discoverChangedFiles();
        if (changes.isEmpty()) {
            if (runFullBuildOnNoChangesDetected) {
                log("Will run everything because no changes were detected!!!");
                empty();
            } else {
                log("Will not run anything because no changes were detected!!!");
                write("**/NOTESTS.java");
            }
            return;
        }
        log("Detected " + changes.size() + " changes.");
        JUnitTestsSelector junitSelector = new JUnitTestsSelector(data);
        Set<String> tests = junitSelector.getTestsFor(changes);
        if (tests.isEmpty()) {
            log("Will run everything because no tests were affected!!!");
            empty();
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (String t : tests) {
            sb.append(t.replace('.', '/') + ".class\n");
        }
        write(sb.toString());
        log("Loaded list to run contains " + tests.size() + " test files");
    }

    private void empty() {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = fullTests.iterator(); it.hasNext(); ) {
            FileResource resource = (FileResource) it.next();
            sb.append(resource.getName() + "\n");
        }
        write(sb.toString());
    }

    private void write(String str) {
        try {
            FileWriter fw = new FileWriter(output);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
