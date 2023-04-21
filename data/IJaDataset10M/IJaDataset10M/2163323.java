package repast.simphony.ui.probe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import repast.simphony.ui.RSGui;
import repast.simphony.util.ClassUtilities;
import repast.simphony.visualization.ProbeEvent;
import repast.simphony.visualization.ProbeListener;
import saf.core.ui.dock.DockableFrame;
import saf.core.ui.event.DockableFrameAdapter;
import saf.core.ui.event.DockableFrameEvent;

/**
 * Manages the creation, destruction and updating of probes.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbeManager extends DockableFrameAdapter implements ProbeListener {

    private static final String PROBED_OBJ_KEY = "repast.simphony.ui.probe.PROBED_OBJ_KEY";

    private static int probeCount = 0;

    private Map<Object, DockableFrame> probeViewMap = new HashMap<Object, DockableFrame>();

    private Map<Object, Probe> probeMap = new HashMap<Object, Probe>();

    private RSGui gui;

    private List probesToRemove = new ArrayList();

    private Map<Object, Probe> probesToAdd = new HashMap<Object, Probe>();

    /**
   * Creates a ProbeManager and associates it with the specified gui.
   *
   * @param gui the gui for this app
   */
    public ProbeManager(RSGui gui) {
        this.gui = gui;
        gui.addViewListener(this);
    }

    public void reset() {
        probesToRemove.clear();
        probesToAdd.clear();
        probeMap.clear();
        probeViewMap.clear();
    }

    /**
   * Update all the probes to show the current values of their probed objects.
   */
    public void update() {
        addAddedProbes();
        for (Probe probe : probeMap.values()) probe.update();
        removeRemovedProbes();
        probesToRemove.clear();
        probesToAdd.clear();
    }

    private void removeRemovedProbes() {
        for (Object obj : probesToRemove) {
            probeMap.remove(obj);
        }
    }

    private void addAddedProbes() {
        for (Object obj : probesToAdd.keySet()) {
            probeMap.put(obj, probesToAdd.get(obj));
        }
    }

    private void probeObject(Object obj) {
        DockableFrame view = probeViewMap.get(obj);
        if (view != null) {
            gui.setActiveView(view);
        } else {
            String title = createTitle(obj);
            ProbePanelCreator creator = new ProbePanelCreator(obj);
            Probe probe = creator.getProbe(title, true);
            probesToAdd.put(obj, probe);
            view = gui.addProbeView("probe_" + probeCount++, title, probe.getPanel());
            view.putClientProperty(PROBED_OBJ_KEY, obj);
            probeViewMap.put(obj, view);
            gui.setActiveView(view);
            probe.addPropertyChangeListener(gui);
            gui.setActiveView(view);
            probe.update();
        }
    }

    public static String createTitle(Object obj) {
        Method[] methods = ClassUtilities.findMethods(obj.getClass(), ProbeID.class);
        if (methods.length > 0) {
            Method method = methods[0];
            if (!method.getReturnType().equals(void.class) && method.getParameterTypes().length == 0) {
                try {
                    return method.invoke(obj).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        String title = obj.toString();
        if (title.lastIndexOf('.') > 0) {
            title = title.substring(title.lastIndexOf(".") + 1, title.length());
        }
        return title;
    }

    /**
   * Removes the probe in the closed view from this ProbeManager.
   *
   * @param evt the details of the close event
   */
    public void dockableClosed(DockableFrameEvent evt) {
        DockableFrame view = evt.getDockable();
        Object obj = view.getClientProperty(PROBED_OBJ_KEY);
        if (obj != null) {
            probeViewMap.remove(obj);
            Probe probe = probeMap.get(obj);
            probesToRemove.add(obj);
            if (probe != null) {
                probe.removePropertyChangeListener(gui);
            }
        }
    }

    /**
   * Creates and display a probe for the objects in the ProbeEvent.
   *
   * @param evt the details of the probe
   */
    public void objectProbed(ProbeEvent evt) {
        List<?> objs = evt.getProbedObjects();
        for (Object obj : objs) {
            probeObject(obj);
        }
    }
}
