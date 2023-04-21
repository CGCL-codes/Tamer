package org.xteam.cs.generator.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.xteam.cs.model.ErrorMark;
import org.xteam.cs.model.ErrorMarkConsoleDiagnostic;
import org.xteam.cs.model.FileResource;
import org.xteam.cs.model.IProgressMonitor;
import org.xteam.cs.model.Project;
import org.xteam.cs.model.ProjectManager;
import org.xteam.cs.model.ProjectResource;

public class ProjectManagerGeneratorTest {

    public static void main(String[] args) throws IOException {
        ProjectManager manager = new ProjectManager();
        Project project = new Project(manager);
        project.open(new File("examples/cxx/cxx.cpj"));
        manager.buildProject(project, new ProgressMonitor());
        boolean hasError = false;
        for (ProjectResource resource : project.getResources()) {
            List<ErrorMark> marks = resource.getMarks(ErrorMark.class);
            hasError |= marks.size() > 0;
            ErrorMarkConsoleDiagnostic diag = new ErrorMarkConsoleDiagnostic(System.out);
            diag.printDiagnostic(new FileReader(((FileResource) resource).getFile()), marks);
        }
        if (!hasError) {
        }
    }

    private static class ProgressMonitor implements IProgressMonitor {

        @Override
        public void beginTask(String msg, int amountOfWork) {
            System.out.println("=== " + msg);
        }

        @Override
        public void done() {
            System.out.println("=== done");
        }

        @Override
        public void subTask(String name) {
            System.out.println("*** " + name);
        }

        @Override
        public void worked(int amount) {
            System.out.println("+(" + amount + ")");
        }

        @Override
        public void internalWorked(double amount) {
            System.out.println("+(" + amount + ")");
        }
    }
}
