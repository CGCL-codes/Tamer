package net.sourceforge.javautil.database.jpa;

import net.sourceforge.javautil.common.classloader.ClassLoaderResource;
import net.sourceforge.javautil.common.classloader.IResourceObjectFactory;
import net.sourceforge.javautil.common.xml.XMLDocument;
import net.sourceforge.javautil.database.jpa.descriptor.impl.PersistenceXML;
import net.sourceforge.javautil.datasource.IDataSourceFactory;

/**
 * This will attempt to generate {@link PersistenceSetup} instances based off found {@link ClassLoaderResource}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class PersistenceSetupResourceFactory implements IResourceObjectFactory<PersistenceSetup> {

    protected final IDataSourceFactory factory;

    public PersistenceSetupResourceFactory(IDataSourceFactory factory) {
        this.factory = factory;
    }

    public PersistenceSetup generate(ClassLoaderResource resource) {
        return JPAUtil.load(XMLDocument.read(resource.getFile(), PersistenceXML.class), resource.getArchive(), factory);
    }
}
