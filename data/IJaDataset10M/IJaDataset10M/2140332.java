package org.zkoss.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

/**
 * An image map.
 *
 * <p>There are two ways to use Imagemap:</p>
 *
 * <ol>
 * <li>Listen to the onClick event, which is an instance of
 * {@link org.zkoss.zk.ui.event.MouseEvent}. Then, you could call
 * getX() and getY() to retrieve where user has clicked.</li>
 * <li>Assign one or multiple of {@link Area} as its children.
 * Then, listen to the onClick event, and use
 * {@link org.zkoss.zk.ui.event.MouseEvent#getArea} to retrieve
 * which area is clicked.</li>
 * </ol>
 *
 * <p>Note: IE 5.5/6 (not 7) has a bug that failed to render PNG with
 * alpha transparency. See http://homepage.ntlworld.com/bobosola/index.htm for details.
 * Thus, if you want to display such image, you have to use the alphafix mold.
 * <code>&lt;imagemap mold="alphafix"/&gt;</code>
 *
 * @author tomyeh
 */
public class Imagemap extends Image implements org.zkoss.zul.api.Imagemap {

    public String getOuterAttrs() {
        final String attrs = super.getOuterAttrs();
        final String attrnm = " z.lfclk=";
        final int j = attrs.indexOf(attrnm);
        if (j < 0) return attrs;
        int k = attrs.indexOf('"', j + attrnm.length());
        assert k > 0 : attrs;
        k = attrs.indexOf('"', k + 1);
        assert k > 0 : attrs;
        return attrs.substring(0, j) + attrs.substring(k + 1);
    }

    /** Default: childable.
	 */
    public boolean isChildable() {
        return true;
    }

    public void beforeChildAdded(Component newChild, Component refChild) {
        if (!(newChild instanceof Area)) throw new UiException("Unsupported child for imagemap: " + newChild);
        super.beforeChildAdded(newChild, refChild);
    }

    public void onChildAdded(Component child) {
        super.onChildAdded(child);
        if (getChildren().size() == 1) smartUpdate("ckchd", true);
    }

    public void onChildRemoved(Component child) {
        super.onChildRemoved(child);
        if (getChildren().isEmpty()) smartUpdate("ckchd", true);
    }
}
