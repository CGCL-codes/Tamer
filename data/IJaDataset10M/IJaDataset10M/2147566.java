package org.junithelper.core.generator;

import java.util.List;
import org.junithelper.core.config.Configuration;
import org.junithelper.core.meta.ClassMeta;
import org.junithelper.core.meta.ConstructorMeta;

public interface ConstructorGenerator {

    List<String> getAllInstantiationSourceCodeList(Configuration config, ClassMeta classMeta);

    String getFirstInstantiationSourceCode(Configuration config, ClassMeta classMeta);

    String getInstantiationSourceCode(Configuration config, ClassMeta classMeta, ConstructorMeta constructorMeta);
}
