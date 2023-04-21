package javax.swing.plaf;

import javax.swing.ActionMap;

/**
 * An <code>ActionMap</code> that implements the {@link UIResource}
 * interface to indicate that it belongs to a pluggable
 * LookAndFeel.
 *
 * @see javax.swing.ActionMap
 *
 * @author Andrew Selkirk
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
public class ActionMapUIResource extends ActionMap implements UIResource {

    /**
   * Constructs a new ActionMapUIResource.
   */
    public ActionMapUIResource() {
    }
}
