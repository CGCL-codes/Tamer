package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;

/**
 * 
 * @author Jeff Butler
 */
public abstract class AbstractJavaProviderMethodGenerator extends AbstractGenerator {

    public AbstractJavaProviderMethodGenerator() {
        super();
    }

    public abstract void addClassElements(TopLevelClass topLevelClass);
}
