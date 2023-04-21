package com.google.gwt.core.ext.linker;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.Linker;

/**
 * A resource created by a {@link Generator} invoking
 * {@link com.google.gwt.core.ext.GeneratorContext#tryCreateResource(com.google.gwt.core.ext.TreeLogger, String)}
 * during the compilation process.
 */
public abstract class GeneratedResource extends EmittedArtifact {

    private final String generatorTypeName;

    private transient Class<? extends Generator> generatorType;

    protected GeneratedResource(Class<? extends Linker> linkerType, Class<? extends Generator> generatorType, String partialPath) {
        super(linkerType, partialPath);
        this.generatorTypeName = generatorType.getName();
        this.generatorType = generatorType;
    }

    /**
   * The type of Generator that created the resource.
   */
    public final Class<? extends Generator> getGenerator() {
        if (generatorType == null) {
            try {
                Class<?> clazz = Class.forName(generatorTypeName, false, Thread.currentThread().getContextClassLoader());
                generatorType = clazz.asSubclass(Generator.class);
            } catch (ClassNotFoundException e) {
                generatorType = Generator.class;
            }
        }
        return generatorType;
    }
}
