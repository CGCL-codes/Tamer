package glaceo.gui.client.util;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import glaceo.gui.client.vc.GContentController;

/**
 * Special version of the GXT {@link TabItem} class that adds support for lazy loading of
 * content.
 *
 * @version $Id$
 * @author jjanke
 */
public class GTabItem extends TabItem {

    private GContentController d_controller;

    private boolean d_fContentReady = false;

    /**
   * @see TabItem#TabItem()
   */
    public GTabItem(GContentController controller) {
        super();
        setLayout(new BorderLayout());
        d_controller = controller;
    }

    /**
   * @see TabItem#TabItem(String)
   */
    public GTabItem(GContentController controller, String strText) {
        super(strText);
        setLayout(new BorderLayout());
        d_controller = controller;
    }

    /**
   * <p>
   * Builds the content of this tab panel. Any content that should only be rendered the
   * first time when this tab panel is actually shown, should be implemented in this
   * method.
   * </p>
   *
   * <p>
   * This method should be overridden if lazy content building is required. Implementors
   * should only build the content if {@link #isContentReady()} returns <code>false</code>
   * and make sure to correctly set the content as being built by calling this super
   * method at the end of their implementation.
   * </p>
   */
    public void buildContent() {
        setContentReady(true);
        layout();
    }

    /**
   * Indicates if the tab item's content has already been built or not.
   */
    protected final boolean isContentReady() {
        return d_fContentReady;
    }

    /**
   * Sets whether the tab item's content has been built or not.
   *
   * @param flag <code>true</code> to avoid that the item's content is rebuilt the next
   *          time the tab item is selected, otherwise <code>false</code>
   */
    protected final void setContentReady(boolean flag) {
        d_fContentReady = flag;
    }

    /**
   * Returns the associated controller.
   */
    protected final GContentController getController() {
        return d_controller;
    }
}
