package org.databene.benerator.wrapper;

import org.databene.benerator.Generator;
import org.databene.benerator.IllegalGeneratorStateException;
import org.databene.benerator.InvalidGeneratorSetupException;

/**
 * Wraps another Generator of same product type.<br/>
 * <br/>
 * Created: 17.08.2007 19:05:42
 * @author Volker Bergmann
 */
public abstract class GeneratorProxy<E> extends GeneratorWrapper<E, E> {

    protected Class<E> generatedType;

    public GeneratorProxy(Class<E> generatedType) {
        super(null);
        this.generatedType = generatedType;
    }

    public GeneratorProxy(Generator<E> source) {
        super(source);
        if (source == null) throw new InvalidGeneratorSetupException("source is null");
    }

    public Class<E> getGeneratedType() {
        if (getSource() != null) return getSource().getGeneratedType(); else if (generatedType != null) return generatedType; else throw new IllegalGeneratorStateException("Generator not initialized correctly: " + this);
    }

    public ProductWrapper<E> generate(ProductWrapper<E> wrapper) {
        assertInitialized();
        return getSource().generate(wrapper);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + getSource() + ']';
    }
}
