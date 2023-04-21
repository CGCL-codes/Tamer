package newsatort.ui.view;

import java.util.HashMap;
import java.util.Map;
import newsatort.exception.ViewException;
import newsatort.manager.AbstractManager;
import newsatort.util.CollectionUtils;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ViewManager extends AbstractManager implements IViewManager {

    private final Map<String, Map<String, Object>> mapParameters;

    private final Map<String, Integer> mapNumInstances;

    private IWorkbenchWindow window = null;

    public ViewManager() {
        super();
        mapParameters = new HashMap<String, Map<String, Object>>();
        mapNumInstances = new HashMap<String, Integer>();
    }

    public void setWindow(IWorkbenchWindow window) {
        this.window = window;
    }

    public String getNewSecondaryId(String viewId) {
        String secondaryId = null;
        if (PlatformUI.getWorkbench().getViewRegistry().find(viewId).getAllowMultiple()) secondaryId = Integer.toString(CollectionUtils.increaseIntegerInMap(mapNumInstances, viewId));
        return secondaryId;
    }

    public void createView(String viewId, String secondaryId) throws ViewException {
        if (window == null) throw new ViewException("Il faut utiliser la m�thode 'setWindow(...)' avant d'utiliser 'createView(...)'");
        try {
            window.getActivePage().showView(viewId, secondaryId, IWorkbenchPage.VIEW_ACTIVATE);
        } catch (PartInitException exception) {
            throw new ViewException("Erreur � la cr�ation d'une nouvelle vue d'id '" + viewId + "'", exception);
        }
    }

    public void createView(String viewId) throws ViewException {
        createView(viewId, getNewSecondaryId(viewId));
    }

    public void addParameter(String viewId, String secondaryId, String paramaterName, Object parameterValue) {
        String id = getId(viewId, secondaryId);
        if (!mapParameters.containsKey(id)) mapParameters.put(id, new HashMap<String, Object>());
        mapParameters.get(id).put(paramaterName, parameterValue);
    }

    public <CLASS> CLASS getParameter(String viewId, String secondaryId, String paramaterName, Class<CLASS> clazz, CLASS parameterDefaultValue) {
        CLASS object = parameterDefaultValue;
        String id = getId(viewId, secondaryId);
        if (mapParameters.containsKey(id)) object = clazz.cast(mapParameters.get(id).get(paramaterName));
        return object;
    }

    public void clearParameter(String viewId) {
        mapParameters.remove(viewId);
    }

    private String getId(String viewId, String secondaryId) {
        return viewId + "@" + secondaryId;
    }
}
