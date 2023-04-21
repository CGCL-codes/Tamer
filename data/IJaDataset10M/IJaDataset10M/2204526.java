package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 * @author Simon Lessard (latest modification by $Author: slessard $)
 * @version $Revision: 696523 $ $Date: 2009-03-14 19:56:16 -0400 (mer., 17 sept. 2008) $
 * 
 * @since 2.0
 */
public class PostRestoreStateEvent extends ComponentSystemEvent {

    /**
     * @param component
     */
    public PostRestoreStateEvent(UIComponent component) {
        super(component);
    }

    public void setComponent(UIComponent newComponent) {
        super.source = newComponent;
    }
}
