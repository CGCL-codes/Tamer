package diagram.tool;

import diagram.Diagram;
import util.WeakList;

/**
 * @class Tool
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 */
public abstract class AbstractTool implements Tool {

    private WeakList listeners = new WeakList();

    /**
   * Add a new listener to this tool.
   *
   * @param ToolListener
   */
    public void addToolListener(ToolListener l) {
        listeners.add(l);
    }

    /**
   * Remove a listener to this tool.
   *
   * @param ToolListener
   */
    public void removeToolListener(ToolListener l) {
        listeners.remove(l);
    }

    /**
   * Install support for something in the given Diagram
   *
   * @param Diagram
   */
    public abstract void install(Diagram diagram);

    /**
   * Remove support for something that was previously installed.
   *
   * @param Diagram
   */
    public abstract void uninstall(Diagram diagram);

    /**
   * Fire the tool started event
   */
    protected void fireToolStarted() {
        try {
            for (int i = 0; i < listeners.size(); i++) {
                ToolListener l = (ToolListener) listeners.get(i);
                if (l != null) l.toolStarted(this);
            }
        } catch (Throwable t) {
            handleError(t);
        }
    }

    /**
   * Fire the tool finished event
   */
    protected void fireToolFinished() {
        try {
            for (int i = 0; i < listeners.size(); i++) {
                ToolListener l = (ToolListener) listeners.get(i);
                if (l != null) l.toolFinished(this);
            }
        } catch (Throwable t) {
            handleError(t);
        }
    }

    protected void handleError(Throwable t) {
        System.err.println("Error dispatching event");
        t.printStackTrace();
        System.exit(0);
    }
}
