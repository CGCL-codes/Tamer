package org.umldebugger;

import java.awt.event.ActionEvent;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.BooleanStateAction;

public final class StartUMLDebugger extends BooleanStateAction {

    @Override
    public void actionPerformed(ActionEvent event) {
        Project[] openProjects = org.netbeans.api.project.ui.OpenProjects.getDefault().getOpenProjects();
        for (Project openProject : openProjects) {
            Sources sources = openProject.getLookup().lookup(Sources.class);
            SourceGroup[] sourceGroups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
            for (SourceGroup sourceGroup : sourceGroups) {
                System.out.println(sourceGroup.getRootFolder().getPath());
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(StartUMLDebugger.class, "CTL_StartUMLDebugger");
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
