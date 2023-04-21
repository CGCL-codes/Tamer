package editor.plugins;

import editor.view.EditorViewBase;

/**
 * 
 *@author hao sun
 *@version $Revision: 1.1 $
 *@since version 1.1
 **/
public abstract class EditorPlugin {

    protected EditorViewBase view;

    public EditorPlugin(EditorViewBase view) {
        this.view = view;
    }

    private boolean isActivated = true;

    /**
     * Main part for excuting
     *
     */
    protected abstract void performAction();

    /**
     * Activate current plugin,it can be modified by developer
     *
     */
    public void initialPlugin() {
        performAction();
    }

    public void setView(EditorViewBase view) {
        this.view = view;
    }

    /**
     * @return the isActivated
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * @param isActivated the isActivated to set
     */
    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
}
