package org.cleartk.classifier.baseline;

import java.io.File;
import java.io.FileNotFoundException;

public class MostFrequentStringDataWriter extends OutcomeOnlyDataWriter<MostFrequentStringClassifierBuilder, String> {

    public MostFrequentStringDataWriter(File outputDirectory) throws FileNotFoundException {
        super(outputDirectory);
    }

    @Override
    protected MostFrequentStringClassifierBuilder newClassifierBuilder() {
        return new MostFrequentStringClassifierBuilder();
    }
}
