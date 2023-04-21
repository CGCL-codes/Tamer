package org.databene.benerator.csv;

import java.util.List;
import org.databene.benerator.distribution.Distribution;
import org.databene.benerator.distribution.Sequence;
import org.databene.benerator.sample.SampleGeneratorUtil;
import org.databene.benerator.sample.SampleGenerator;
import org.databene.benerator.wrapper.GeneratorProxy;
import org.databene.commons.Context;
import org.databene.commons.Converter;
import org.databene.script.ScriptConverterForStrings;
import org.databene.script.WeightedSample;

/**
 * Generates values from a dataset based on a {@link Sequence}.<br/><br/>
 * Created: 17.02.2010 23:22:52
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class SequencedDatasetCSVGenerator<E> extends GeneratorProxy<E> {

    @SuppressWarnings("unchecked")
    public SequencedDatasetCSVGenerator(String filenamePattern, char separator, String datasetName, String nesting, Distribution distribution, String encoding, Context context) {
        this(filenamePattern, separator, datasetName, nesting, distribution, encoding, (Converter<String, E>) new ScriptConverterForStrings(context));
    }

    @SuppressWarnings("unchecked")
    public SequencedDatasetCSVGenerator(String filenamePattern, char separator, String datasetName, String nesting, Distribution distribution, String encoding, Converter<String, E> preprocessor) {
        super((Class<E>) Object.class);
        List<E> samples = parseFiles(datasetName, separator, nesting, filenamePattern, encoding, preprocessor);
        setSource(new SampleGenerator<E>((Class<E>) samples.get(0).getClass(), distribution, false, samples));
    }

    private List<E> parseFiles(String datasetName, char separator, String nesting, String filenamePattern, String encoding, Converter<String, E> preprocessor) {
        List<WeightedSample<E>> weightedSamples = CSVGeneratorUtil.parseDatasetFiles(datasetName, separator, nesting, filenamePattern, encoding, preprocessor);
        return SampleGeneratorUtil.extractValues(weightedSamples);
    }
}
