package net.sourceforge.fluxion.runcible.graph.view.utils;

import net.sourceforge.fluxion.graph.Node;
import net.sourceforge.fluxion.graph.Graph;
import java.util.regex.Pattern;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 09-Mar-2009
 */
public class FlexElementUtils {

    public static Node lookupNodeByFlexNodeName(String flexName, Graph graph) {
        for (Node node : graph.getAllNodes()) {
            Pattern p1 = Pattern.compile(Pattern.quote(flexName));
            if (p1.matcher(node.getLabel()).matches()) {
                return node;
            }
        }
        return null;
    }
}
