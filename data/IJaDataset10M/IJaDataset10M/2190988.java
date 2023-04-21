package com.googlecode.mjorm.spring;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.googlecode.mjorm.annotations.AnnotationsDescriptorObjectMapper;
import com.googlecode.jot.TypeTranslator;

/**
 * {@link FactoryBean} for created {@link AnnotationsDescriptorObjectMapper}s.
 */
public class AnnotationsDescriptorObjectMapperFactoryBean extends AbstractFactoryBean<AnnotationsDescriptorObjectMapper> {

    private Class<?>[] annotatedClasses = new Class<?>[0];

    private List<TypeTranslator<?, ?>> typeTranslators = new ArrayList<TypeTranslator<?, ?>>();

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected AnnotationsDescriptorObjectMapper createInstance() throws Exception {
        AnnotationsDescriptorObjectMapper mapper = new AnnotationsDescriptorObjectMapper();
        for (TypeTranslator<?, ?> converter : typeTranslators) {
            mapper.registerConverter(converter);
        }
        for (Class<?> clazz : annotatedClasses) {
            mapper.addClass(clazz);
        }
        return mapper;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Class<?> getObjectType() {
        return AnnotationsDescriptorObjectMapper.class;
    }

    /**
	 * @param typeTranslators the typeTranslators to set
	 */
    public void setTypeTranslators(List<TypeTranslator<?, ?>> typeTranslators) {
        this.typeTranslators = typeTranslators;
    }
}
