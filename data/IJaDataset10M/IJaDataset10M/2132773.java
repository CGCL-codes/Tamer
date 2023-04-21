package org.netbeans.modules.visual.action;

import org.netbeans.api.visual.action.CycleFocusProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.model.ObjectScene;

/**
 * @author David Kaspar
 */
public class CycleObjectSceneFocusProvider implements CycleFocusProvider {

    public boolean switchPreviousFocus(Widget widget) {
        Scene scene = widget.getScene();
        return scene instanceof ObjectScene && switchFocus((ObjectScene) scene, false);
    }

    public boolean switchNextFocus(Widget widget) {
        Scene scene = widget.getScene();
        return scene instanceof ObjectScene && switchFocus((ObjectScene) scene, true);
    }

    @SuppressWarnings("unchecked")
    private boolean switchFocus(ObjectScene scene, boolean forwardDirection) {
        Object object = scene.getFocusedObject();
        Comparable identityCode = scene.getIdentityCode(object);
        Object bestObject = null;
        Comparable bestIdentityCode = null;
        if (identityCode != null) {
            for (Object o : scene.getObjects()) {
                Comparable ic = scene.getIdentityCode(o);
                if (forwardDirection) {
                    if (identityCode.compareTo(ic) < 0) {
                        if (bestIdentityCode == null || bestIdentityCode.compareTo(ic) > 0) {
                            bestObject = o;
                            bestIdentityCode = ic;
                        }
                    }
                } else {
                    if (identityCode.compareTo(ic) > 0) {
                        if (bestIdentityCode == null || bestIdentityCode.compareTo(ic) < 0) {
                            bestObject = o;
                            bestIdentityCode = ic;
                        }
                    }
                }
            }
        }
        if (bestIdentityCode == null) {
            for (Object o : scene.getObjects()) {
                Comparable ic = scene.getIdentityCode(o);
                if (forwardDirection) {
                    if (bestIdentityCode == null || bestIdentityCode.compareTo(ic) > 0) {
                        bestObject = o;
                        bestIdentityCode = ic;
                    }
                } else {
                    if (bestIdentityCode == null || bestIdentityCode.compareTo(ic) < 0) {
                        bestObject = o;
                        bestIdentityCode = ic;
                    }
                }
            }
        }
        scene.setFocusedObject(bestObject);
        return true;
    }
}
