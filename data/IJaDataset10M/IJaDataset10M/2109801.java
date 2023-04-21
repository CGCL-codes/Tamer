package de.beas.explicanto.client.rcp.projects.commands;

import java.util.List;
import org.eclipse.ui.IWorkbenchPage;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;

public class LoadLessonsCommand extends GenericCommand {

    private CCourse course;

    public LoadLessonsCommand(CCourse course, IWorkbenchPage page) {
        super(page);
        this.course = course;
    }

    public Object execute() {
        try {
            List list = VidyaDataTree.getDefault().loadLessonTree(course.getUid());
            if (list.size() > 0) {
                course.addLessonList(list);
                return list;
            } else {
                ExplicantoMessageDialog.openInformation(getWindow().getShell(), translate("actions.messages.noLessons"));
                return null;
            }
        } catch (Exception e) {
            ExplicantoClientPlugin.handleException(e, course);
            return null;
        }
    }
}
