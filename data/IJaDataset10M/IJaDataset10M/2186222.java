package applicationWorkbench;

import java.io.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import cards.CardConstants;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    private ApplicationActionBarAdvisor actionBarAdvisor;

    private IWorkbenchWindowConfigurer configurer;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        actionBarAdvisor = new ApplicationActionBarAdvisor(configurer);
        return actionBarAdvisor;
    }

    public void preWindowOpen() {
        configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowMenuBar(true);
        configurer.setTitle(CardConstants.APPLICATIONNAME);
    }

    public void dispose() {
    }

    public void postWindowOpen() {
        IWorkbenchPage page = configurer.getWindow().getActivePage();
        IEditorInput input = createEditorInput(new File("C:/AgilePlanner.xml"));
        String editorId = "RallyDemoGEF.Editor";
        try {
            page.openEditor(input, editorId);
        } catch (PartInitException e) {
            util.Logger.singleton().error(e);
        }
    }

    private IEditorInput createEditorInput(File file) {
        IPath path = new Path(file.getAbsolutePath());
        PathEditorInput input = new PathEditorInput(path);
        return input;
    }
}
