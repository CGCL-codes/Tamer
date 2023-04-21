package org.jecars.client.local;

import java.util.logging.Level;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.jecars.client.JC_DefaultNode;
import org.jecars.client.JC_Exception;
import org.jecars.client.JC_Propertyable;

/**
 *
 * @author weert
 */
public class JC_DirectNode extends JC_DefaultNode {

    private final Node mNode;

    public JC_DirectNode(final Node pNode) {
        mNode = pNode;
        try {
            setNodeType(pNode.getPrimaryNodeType().getName());
        } catch (RepositoryException re) {
            gLog.log(Level.WARNING, re.getMessage(), re);
        }
        return;
    }

    @Override
    public boolean hasProperty(final String pName) throws JC_Exception {
        try {
            return mNode.hasProperty(pName);
        } catch (RepositoryException re) {
            throw new JC_Exception(re);
        }
    }

    @Override
    public JC_Propertyable getProperty(String pName) throws JC_Exception {
        JC_Propertyable prop = super.getProperty(pName);
        if (prop == null) {
            try {
                if (mNode.hasProperty(pName)) {
                    JC_DirectProperty dprop = new JC_DirectProperty(mNode.getProperty(pName));
                    getOrCreateProperties().add(dprop);
                    prop = dprop;
                }
            } catch (RepositoryException re) {
                throw new JC_Exception(re);
            }
        }
        return prop;
    }
}
