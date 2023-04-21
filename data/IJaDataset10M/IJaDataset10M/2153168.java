package org.peertrust.modeler.policysystem;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    public static String rdfFileName;

    private static final String PERSPECTIVE_ID = "org.peertrust.modeler.policysystem.perspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    public void preStartup() {
        super.preStartup();
        PolicySystemRDFModel.getInstance();
    }

    public static String askForRDFFileName(Shell parent) {
        if (parent == null) {
            parent = new Shell();
        }
        FileDialog fileDlg = new FileDialog(parent);
        fileDlg.setText("choose rdf file");
        fileDlg.setFilterNames(new String[] { "*.rdf" });
        String fileName = fileDlg.open();
        System.out.println("dir=" + fileName);
        return fileName;
    }
}
