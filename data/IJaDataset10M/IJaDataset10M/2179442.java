package ch.sahits.codegen.java.internal.wizards;

import ch.sahits.codegen.java.internal.wizards.GeneratedClassBuilder;

/**
 * Builder for a HeadlessGeneratedClassDelegateGenerator. No validation takes place.
 * @author Andi Hotz, Sahits GmbH
 * @since 3.0.0
 */
public class HeadlessGeneratedClassDelegateGeneratorBuilder extends GeneratedClassBuilder {

    /**
	 * @see ch.sahits.codegen.java.internal.wizards.GeneratedClassBuilder#build()
	 */
    @Override
    public HeadlessGeneratedClassDelegateGenerator build() {
        return new HeadlessGeneratedClassDelegateGenerator(this);
    }
}
