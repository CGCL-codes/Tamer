package org.atricore.idbus.kernel.main.mediation;

/**
 * @org.apache.xbean.XBean element="josso11-binding-factory"
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public abstract class MediationBindingFactory {

    public abstract MediationBinding createBinding(String binding, Channel channel);
}
