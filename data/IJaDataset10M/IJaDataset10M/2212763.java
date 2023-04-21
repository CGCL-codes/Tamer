package com.google.gdata.wireformats.output;

import com.google.gdata.model.Element;
import java.io.IOException;
import java.io.Writer;

/**
 * A bridge between old and new data models for output generators.
 * 
 * 
 * 
 * @param <T> expected source object type
 */
public abstract class DualModeGenerator<T> extends WireFormatOutputGenerator<T> {

    private final CharacterGenerator<T> oldGen;

    protected DualModeGenerator(CharacterGenerator<T> oldGen) {
        this.oldGen = oldGen;
    }

    @Override
    public void generate(Writer contentWriter, OutputProperties outProps, T source) throws IOException {
        if (isNewModel(source)) {
            super.generate(contentWriter, outProps, source);
        } else {
            oldGen.generate(contentWriter, outProps, source);
        }
    }

    /**
   * Returns true if the response contains data in the new data model.
   */
    private boolean isNewModel(T source) {
        return source instanceof Element;
    }
}
