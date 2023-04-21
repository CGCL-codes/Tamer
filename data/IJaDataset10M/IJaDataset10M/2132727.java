package org.regilo.content.cck;

import org.eclipse.core.runtime.IAdapterFactory;
import org.regilo.content.cck.model.CCKNode;
import org.regilo.content.cck.model.CCKNodeManager;
import org.regilo.content.model.Node;

public class NodeAdapterFactory implements IAdapterFactory {

    private static Class<?>[] SUPPORTED_TYPES = new Class[] { CCKNode.class };

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (CCKNode.class.equals(adapterType)) {
            if (adaptableObject instanceof Node) {
                return CCKNodeManager.getCCKNode((Node) adaptableObject);
            }
        }
        return null;
    }

    public Class[] getAdapterList() {
        return SUPPORTED_TYPES;
    }
}
