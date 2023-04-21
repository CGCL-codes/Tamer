package org.esfinge.aom.model.rolemapper.metadata.reader.interfaces;

import org.esfinge.aom.exceptions.EsfingeAOMRoleMapperException;

public interface IAOMMetadataReader {

    public Object getDescriptor(Class<?> c) throws EsfingeAOMRoleMapperException;

    public boolean isReaderApplicable(Class<?> c);
}
