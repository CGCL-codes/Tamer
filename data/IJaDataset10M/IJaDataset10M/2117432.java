package tapestryassistant;

import org.eclipse.core.resources.IProject;
import tapestryassistant.core.DataMapping;
import tapestryassistant.core.DataMappingManager;

public class DataMappingLoadHandler implements Runnable {

    private IProject proj;

    public DataMappingLoadHandler(IProject project) {
        proj = project;
    }

    @Override
    public void run() {
        if (proj == null) return;
        DataMapping mapping = Util.loadDataMapping(proj);
        if (mapping != null) {
            DataMappingManager.instance().updateMapping(proj, mapping);
        }
    }
}
