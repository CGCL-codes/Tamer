package org.datanucleus.enhancer.jdo.asm;

import org.datanucleus.JDOClassLoaderResolver;
import org.datanucleus.enhancer.ClassEnhancer;
import org.datanucleus.enhancer.asm.ASMClassEnhancer;
import org.datanucleus.metadata.ClassMetaData;
import org.datanucleus.metadata.MetaDataManager;

/**
 * 
 */
public class TestA21_16 extends org.datanucleus.enhancer.jdo.TestA21_16 {

    /**
     * Accessor for a ClassEnhancer to use in enhancing.
     * @param cmd ClassMetaData.
     * @return The ClassEnhancer for this class
     */
    public ClassEnhancer getClassEnhancer(ClassMetaData cmd, MetaDataManager mmgr) {
        return new ASMClassEnhancer(cmd, new JDOClassLoaderResolver(), mmgr);
    }
}
