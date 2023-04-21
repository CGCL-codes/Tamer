package org.nakedobjects.system.install.distribution;

import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.reflect.ReflectionPeerFactory;
import org.nakedobjects.object.transaction.TransactionPeerFactory;
import org.nakedobjects.reflector.java.reflect.JavaSpecificationLoader;
import org.nakedobjects.system.install.InstallSpecificationLoader;
import org.nakedobjects.utility.NakedObjectConfiguration;
import org.apache.log4j.Logger;

public class ProxySpecificationLoader implements InstallSpecificationLoader {

    private static final Logger LOG = Logger.getLogger(ProxySpecificationLoader.class);

    public NakedObjectSpecificationLoader installSpecificationLoader(final NakedObjectConfiguration configuration) {
        LOG.info("installing " + this.getClass().getName());
        ReflectionPeerFactory[] factories = new ReflectionPeerFactory[] { new TransactionPeerFactory() };
        JavaSpecificationLoader specificationLoader = new JavaSpecificationLoader();
        specificationLoader.setReflectionPeerFactories(factories);
        return specificationLoader;
    }
}
