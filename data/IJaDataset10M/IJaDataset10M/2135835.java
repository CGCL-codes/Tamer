package org.openejb.alt.assembler.classic;

/**
 * StatefulSessionContainerInfo is part of the OpenEjbConfiguration object structure that provides
 * the information about the configuration of OpenEJB and the container system.
 * 
 * <p>
 * The OpenEjbConfiguration itself is created by a OpenEjbConfigurationFactory and
 * is used by the org.openejb.alt.assembler.classic.Assembler to build a running unstance of
 * OpenEJB.</p>
 * 
 * The default OpenEjbConfigurationFactory is DomOpenEjbConfigurationFactory, which
 * creates an OpenEjbConfiguration object based on XML config files located on the
 * local system.</p>
 * 
 * <p>
 * Other OpenEjbConfigurationFactory implementations can be created that might populate
 * this object using a different approach.  Other usefull implementations might be:<br>
 * <UL>
 * <LI>Populating the OpenEjbConfiguration from values in a RDBMS.
 * <LI>Populating the OpenEjbConfiguration from values in a Properties file.
 * <LI>Retrieving the OpenEjbConfiguration from a ODBMS.
 * <LI>Creating the OpenEjbConfiguration using a JavaBeans enabled editing tool or wizard.
 * </UL>
 * 
 * <p>
 * If you are interested in creating alternate an OpenEjbConfigurationFactory to do
 * any of the above techniques or a new approach, email the
 * <a href="mailto:openejb-dev@exolab.org">OpenEJB Developer list</a> with a description
 * of the new OpenEjbConfigurationFactory implementation.
 * </p>
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:Richard@Monson-Haefel.com">Richard Monson-Haefel</a>
 * @see org.openejb.spi.Assembler
 * @see org.openejb.alt.assembler.classic.Assembler
 * @see org.openejb.alt.assembler.classic.OpenEjbConfiguration
 * @see org.openejb.alt.assembler.classic.OpenEjbConfigurationFactory
 * @see org.openejb.xmlconf.DomOpenEjbConfigurationFactory
 */
public class StatefulSessionContainerInfo extends ContainerInfo {

    public StatefulBeanInfo[] beans;

    public StatefulSessionContainerInfo() {
        containerType = STATEFUL_SESSION_CONTAINER;
    }
}
