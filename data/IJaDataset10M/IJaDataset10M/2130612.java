package poptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.swing.SwingUtilities;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.msg.PropertiesChanged;
import org.gjt.sp.jedit.msg.PropertiesChanging;
import org.gjt.sp.jedit.msg.ViewUpdate;
import projectviewer.ProjectManager;
import projectviewer.ProjectViewer;
import projectviewer.event.ProjectUpdate;
import projectviewer.event.ViewerUpdate;
import projectviewer.vpt.VPTProject;

/**
 *  The main plugin class.
 *
 *  @author     Marcelo Vanzin
 *  @version    $Id$
 *  @since      POP 0.1.0
 */
public class ProjectOptionsPlugin extends EBPlugin {

    private boolean ignoreChange;

    private String activeProjectName;

    private Map<String, String> savedProperties;

    public void start() {
        ignoreChange = false;
        VPTProject p = ProjectViewer.getActiveProject(jEdit.getActiveView());
        if (p != null) {
            setProjectOptions(p, false);
        }
    }

    public void stop() {
        restoreGlobalOptions(true);
    }

    /**
     *  When a PropertiesChanging message is sent on the EditBus,
     *  restore the global properties so that the user can edit
     *  them. When it's done, restore the active project's properties,
     *  if any.
     */
    public void handleMessage(EBMessage msg) {
        if (msg instanceof PropertiesChanging) {
            switch(((PropertiesChanging) msg).getState()) {
                case LOADING:
                    restoreGlobalOptions(false);
                    break;
                case CANCELED:
                    restoreProjectOptions();
                    break;
            }
        } else if (msg instanceof ViewUpdate && ((ViewUpdate) msg).getWhat() == ViewUpdate.ACTIVATED) {
            VPTProject proj = ProjectViewer.getActiveProject(((ViewUpdate) msg).getView());
            if (proj != null && !proj.getName().equals(activeProjectName)) {
                restoreGlobalOptions(false);
                setProjectOptions(proj, false);
            }
        } else if (!ignoreChange && msg instanceof PropertiesChanged) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    restoreProjectOptions();
                }
            });
        } else if (msg instanceof ProjectUpdate) {
            ProjectUpdate pup = (ProjectUpdate) msg;
            if (pup.getType() == ProjectUpdate.Type.PROPERTIES_CHANGED) {
                setProjectOptions(pup.getProject(), true);
            }
        } else if (msg instanceof ViewerUpdate) {
            ViewerUpdate vup = (ViewerUpdate) msg;
            VPTProject proj = ProjectViewer.getActiveProject(vup.getView());
            switch(vup.getType()) {
                case PROJECT_LOADED:
                    setProjectOptions(proj, false);
                    break;
                case GROUP_ACTIVATED:
                    setProjectOptions(null, false);
                    break;
            }
        }
    }

    protected void restoreGlobalOptions(boolean send) {
        if (savedProperties != null) {
            for (String key : savedProperties.keySet()) {
                jEdit.setProperty(key, savedProperties.get(key));
            }
            savedProperties = null;
        }
        if (send) {
            sendChangeMsg();
        }
    }

    protected void restoreProjectOptions() {
        if (activeProjectName != null) {
            VPTProject p = ProjectManager.getInstance().getProject(activeProjectName);
            if (p != null) {
                setProjectOptions(p, true);
            }
        }
    }

    protected void setProjectOptions(VPTProject p, boolean force) {
        boolean changed = false;
        boolean restore = true;
        if (savedProperties != null) {
            restoreGlobalOptions(false);
        }
        if (force) {
            activeProjectName = null;
        }
        if (p != null) {
            if (p.getName().equals(activeProjectName)) {
                return;
            }
            Properties popts = p.getProperties();
            boolean enabled = "true".equalsIgnoreCase(popts.getProperty("poptions.enabled"));
            if (enabled) {
                for (Object okey : popts.keySet()) {
                    String key = (String) okey;
                    if (key.startsWith("poptions.") && !key.equals("poptions.enabled")) {
                        String jkey = key.substring(9);
                        if (savedProperties == null) {
                            savedProperties = new HashMap<String, String>();
                        }
                        savedProperties.put(jkey, jEdit.getProperty(jkey));
                        jEdit.setProperty(jkey, popts.getProperty(key));
                        changed = true;
                    }
                }
                activeProjectName = p.getName();
                restore = false;
            }
        }
        if (restore) {
            restoreGlobalOptions(false);
            activeProjectName = null;
            changed = true;
        }
        if (changed) {
            sendChangeMsg();
        }
    }

    private void sendChangeMsg() {
        ignoreChange = true;
        jEdit.propertiesChanged();
        ignoreChange = false;
    }
}
