package org.gwtoolbox.ioc.core.rebind;

import java.lang.annotation.Annotation;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.typeinfo.JClassType;
import org.gwtoolbox.ioc.core.rebind.config.MutableComponentContainerOracle;

/**
 * @author Uri Boness
 */
public interface ComponentContainerProcessor<T extends Annotation> {

    public static final int DEFAULT_ORDER = Integer.MIN_VALUE;

    int getOrder();

    void init(T t, JClassType containerType, GeneratorContext context) throws ProcessorInitializationException;

    void process(MutableComponentContainerOracle oracle, GeneratorContext context) throws Exception;
}
