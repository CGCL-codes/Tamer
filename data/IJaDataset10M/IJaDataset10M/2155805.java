package net.sf.magicmap.core.model.node;

import net.sf.magicmap.client.model.node.INode;
import net.sf.magicmap.client.model.node.NodeModelConstants;

/**
 * <p>
 * Class NodeFilter ZUSAMMENFASSUNG
 * </p>
 * <p>
 * DETAILS
 * </p>
 *
 * @author Jan Friderici
 *         Date: 31.12.2007
 *         Time: 15:45:51
 */
public interface NodeFilter {

    /**
     * 
     * @param node
     * @return
     */
    boolean accept(INode node);

    static class Defaults {

        private static final NodeFilter macNodeFilter = new NodeFilter() {

            public boolean accept(INode node) {
                int type = node.getType();
                return type == NodeModelConstants.NODETYPE_ACCESSPOINT || type == NodeModelConstants.NODETYPE_CLIENT;
            }
        };

        /**
         *
         * @return
         */
        public static NodeFilter getMacNodeFilter() {
            return macNodeFilter;
        }
    }
}
