package com.ixora.rms.agents.impl.jmx;

import java.io.IOException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import com.ixora.common.utils.Utils;
import com.ixora.rms.EntityId;
import com.ixora.rms.agents.impl.Entity;
import com.ixora.rms.agents.impl.RootEntity;

/**
 * @author Daniel Moraru
 */
public class JMXEntityRoot extends RootEntity {

    private static final long serialVersionUID = -2018813139889907164L;

    /**
	 * Constructor.
	 * @param c
	 */
    public JMXEntityRoot(JMXAgentExecutionContext c) {
        super(c);
    }

    /**
	 * @see com.ixora.rms.agents.impl.Entity#updateChildrenEntities(boolean)
	 */
    public void updateChildrenEntities(boolean recursive) throws Throwable {
        try {
            if (getChildrenCount() == 0) {
                String[] domains = retrieveDomains();
                if (!Utils.isEmptyArray(domains)) {
                    for (String domain : domains) {
                        if (getJMXContext().acceptDomain(domain)) {
                            Entity e = buildDomainEntity(getId(), getJMXContext(), domain);
                            if (e != null) {
                                addChildEntity(e);
                            }
                        }
                    }
                }
            }
            if (getJMXContext().sortEntities()) {
                sortChildren();
            }
            if (recursive) {
                super.updateChildrenEntities(recursive);
            }
        } catch (Throwable e) {
            getJMXContext().processException(e);
        }
    }

    /**
	 * @return the jmx execution context
	 */
    protected JMXAgentExecutionContext getJMXContext() {
        return (JMXAgentExecutionContext) fContext;
    }

    /**
	 * Subclasses can override this method to create their own domain entities. Invoked from
	 * <code>updateChildrenEntities(boolean)</code>.
	 * @param parent
	 * @param context
	 * @param domain
	 * @return return null to ignore the domain with this name
	 * @throws Throwable
	 * @throws IOException
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 * @throws IntrospectionException
	 */
    protected Entity buildDomainEntity(EntityId parent, JMXAgentExecutionContext context, String domain) throws IntrospectionException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException, Throwable {
        return new JMXEntityDomain(parent, context, domain);
    }

    /**
	 * Subclasses can override how the domains are retrieved; by default MBeanServer.getDomains() is used
	 * but this method was not available for earlier JMX specs.
	 * @return
	 * @throws Exception
	 */
    protected String[] retrieveDomains() throws Exception {
        return getJMXContext().getJMXConnection().getDomains();
    }
}
