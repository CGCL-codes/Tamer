package ca.ucalgary.cpsc.ebe.fitClipse.core.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import ca.ucalgary.cpsc.ase.util.FileUtils;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipseProject;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.history.HTMLResultDoc;

public class AcceptanceTestLaunch {

    String testPath;

    ILaunch launch;

    public AcceptanceTestLaunch(String suitePath, ILaunch launch) {
        this.testPath = suitePath;
        this.launch = launch;
    }

    public boolean finished() {
        boolean ret = false;
        try {
            ret = launch.isTerminated();
        } catch (ConcurrentModificationException e) {
        }
        return ret;
    }

    public List<HTMLResultDoc> getResults() {
        String rootPath = FitClipseProject.getTestResultDir();
        File suite = expectedResultFile(rootPath + testPath);
        List<HTMLResultDoc> ret = new ArrayList<HTMLResultDoc>();
        for (File file : FileUtils.getAllFiles(suite)) {
            try {
                String id = HTMLResultDoc.generateID(new File(rootPath), file);
                HTMLResultDoc doc = new HTMLResultDoc(id, file);
                if (!id.endsWith("index.html")) {
                    ret.add(doc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private File expectedResultFile(String testPath2) {
        return new File(testPath2.replace(FitClipseProject.getTestStorageDir(), FitClipseProject.getTestResultDir()));
    }

    public IProject getProject() {
        IProject project;
        String p = null;
        try {
            p = launch.getLaunchConfiguration().getAttribute(AcceptanceTestLauncher.PROJECT_NAME, "");
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (p != null && !p.equals("")) {
            project = ResourcesPlugin.getWorkspace().getRoot().getProject(p);
        } else {
            project = null;
        }
        return project;
    }

    public Object getLaunch() {
        return launch;
    }
}
