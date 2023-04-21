package simpleorm.simplets.componentframework;

/** A renderer for a component.  Essentially a singleton, no per request state here. <p>
 * 
 * Components can be rendered directly by overriding onRenderBegin|End, or via a renderer.
 * The advantage of a renderer is that a completely different style can be applied to an application.
 */
public class HRenderer {

    public void onRenderBegin(HComponent comp) throws Exception {
    }

    public void onRenderEnd(HComponent comp) throws Exception {
    }
}
