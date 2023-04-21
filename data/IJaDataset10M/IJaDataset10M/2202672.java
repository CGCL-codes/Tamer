package org.datanucleus.enhancer.jdo.bcel;

import org.datanucleus.JDOClassLoaderResolver;
import org.datanucleus.enhancer.ClassEnhancer;
import org.datanucleus.enhancer.bcel.BCELClassEnhancer;
import org.datanucleus.enhancer.bcel.metadata.BCELMetaDataFactory;
import org.datanucleus.metadata.ClassMetaData;
import org.datanucleus.metadata.MetaDataFactory;
import org.datanucleus.metadata.MetaDataManager;

/**
 * 
 * @version $Revision: 1.1 $
 */
public class TestA18_5 extends org.datanucleus.enhancer.jdo.TestA18_5 {

    /**
     * Accessor for a MetaDataFactory to use when enhancing.
     * @param mgr MetaDataManager
     * @return The MetaData factory
     */
    public MetaDataFactory getMetaDataFactory(MetaDataManager mgr) {
        return new BCELMetaDataFactory(mgr);
    }

    /**
     * Accessor for a ClassEnhancer to use in enhancing.
     * @param cmd ClassMetaData.
     * @return The ClassEnhancer for this class
     */
    public ClassEnhancer getClassEnhancer(ClassMetaData cmd) {
        return new BCELClassEnhancer(cmd, new JDOClassLoaderResolver());
    }
}
