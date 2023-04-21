package de.esoco.ewt.component;

import de.esoco.ewt.component.List.ListView;
import de.esoco.ewt.component.ScrollPanel.Viewport;
import de.esoco.ewt.impl.j2me.AbstractContainer;
import de.esoco.ewt.impl.j2me.MenuPanel;
import de.esoco.ewt.layout.EdgeLayout;
import de.esoco.ewt.layout.GenericLayout;
import de.esoco.ewt.style.StyleFlag;

/********************************************************************
 * A panel is a simple container implementation without additional
 * functionality.
 *
 * <p>Supported style flags:</p>
 *
 * <ul>
 *   <li>All border styles (like {@link StyleFlag#ETCHED_IN}); support depends
 *     on the skin used.</li>
 * </ul>
 *
 * @author eso
 */
public class Panel extends AbstractContainer {

    /** Style flag: list view panel ({@link ListView} */
    public static final int LIST_VIEW_STYLE = 0x010000;

    /** Style flag: menu panel ({@link MenuPanel}) */
    public static final int MENU_PANEL_STYLE = 0x020000;

    /** Style flag: scroll viewport ({@link Viewport} */
    public static final int SCROLL_VIEWPORT_STYLE = 0x040000;

    /***************************************
	 * Creates a new instance with the default no gap edge layout {@link
	 * EdgeLayout#NO_GAP_LAYOUT}.
	 */
    public Panel() {
    }

    /***************************************
	 * Creates a new instance with a specific layout.
	 *
	 * @param rLayout The layout
	 */
    public Panel(GenericLayout rLayout) {
        setLayout(rLayout);
    }

    /***************************************
	 * Subclass constructor to create a new instance with a specific style flag.
	 * This constructor is meant to be used by microEWT subclasses that need to
	 * be rendered differently by skins.
	 *
	 * @param nStyle The panel style flag to set
	 */
    protected Panel(int nStyle) {
        changeStyle(nStyle, true);
    }
}
