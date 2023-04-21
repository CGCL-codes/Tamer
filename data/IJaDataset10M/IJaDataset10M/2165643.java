package org.cishell.remoting.service.framework;

import java.util.Vector;
import org.osgi.service.metatype.MetaTypeProvider;

public interface MetaTypeProviderRegistry {

    public static String SERVICE_NAME = "MetaTypeProviderRegistry";

    public Vector getLocales(String providerID);

    public String getObjectClassDefinition(String providerID, String id, String locale);

    public void unregisterMetaTypeProvider(String providerID);

    public String registerMetaTypeProvider(MetaTypeProvider provider);

    public MetaTypeProvider getMetaTypeProvider(String providerID);
}
