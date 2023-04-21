package org.mc4j.console.dashboard.components.dg;

import org.jgraph.JGraph;
import java.util.Properties;

/**
 * Represents an Algorithm that is applied to a graph.<br>
 * It is supposed to arrange the nodes in some usefull way.<br>
 *<br>
 *<br>
 * @author <a href="mailto:Sven.Luzar@web.de">Sven Luzar</a>
 * @since 1.2.2
 * @version 1.0 init
 */
public interface LayoutAlgorithm {

    /**
	 * Called when the Algorithm shall start its work.
	 */
    public abstract void perform(JGraph jgraph, boolean applyToAll, Properties configuration);
}
