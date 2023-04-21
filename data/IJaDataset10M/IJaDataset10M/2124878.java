package net.sf.picomapping.meta;

import net.sf.picomapping.PicomappingException;

public interface MetaClassFactory {

    public MetaClass getMetaClass(String className) throws PicomappingException;
}
