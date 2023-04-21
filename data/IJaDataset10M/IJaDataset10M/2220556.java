package tomcatdeployer.handlers;

import java.util.Iterator;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import tomcatdeployer.handlers.methods.UndeployOperation;
import tomcatdeployer.util.Util;

public class UndeployHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        List projects = GlobalHandler.getProjectList(event);
        if (projects.size() < 1) {
            Util.info("Please select a project", "The selected operation requires at least one project be selected.");
            return null;
        }
        UndeployOperation ud = new UndeployOperation();
        for (Iterator iter = projects.iterator(); iter.hasNext(); ) {
            IProject proj = (IProject) iter.next();
            try {
                ud.run(proj);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return null;
    }
}
