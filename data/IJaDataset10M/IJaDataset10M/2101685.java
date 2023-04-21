package org.cleartk.classifier.liblinear;

import java.io.IOException;
import org.cleartk.classifier.DataWriter;
import org.cleartk.classifier.encoder.features.BooleanEncoder;
import org.cleartk.classifier.encoder.features.FeatureVectorFeaturesEncoder;
import org.cleartk.classifier.encoder.features.NumberEncoder;
import org.cleartk.classifier.encoder.features.StringEncoder;
import org.cleartk.classifier.encoder.features.normalizer.EuclidianNormalizer;
import org.cleartk.classifier.encoder.features.normalizer.NameNumberNormalizer;
import org.cleartk.classifier.encoder.outcome.BooleanToBooleanOutcomeEncoder;
import org.cleartk.classifier.jar.DataWriterFactory_ImplBase;
import org.cleartk.classifier.jar.DefaultDataWriterFactory;
import org.cleartk.classifier.util.featurevector.FeatureVector;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philipp Wetzler
 * @deprecated Use {@link DefaultDataWriterFactory} with {@link BinaryLIBLINEARDataWriter}.
 */
@Deprecated
public class DefaultBinaryLIBLINEARDataWriterFactory extends DataWriterFactory_ImplBase<FeatureVector, Boolean, Boolean> {

    public static final String PARAM_CUTOFF = ConfigurationParameterFactory.createConfigurationParameterName(DefaultBinaryLIBLINEARDataWriterFactory.class, "cutoff");

    @ConfigurationParameter(defaultValue = "5", description = "features that occur less than this number of times over the whole training set will not be encoded during testing")
    protected int cutoff = 5;

    public DataWriter<Boolean> createDataWriter() throws IOException {
        BinaryLIBLINEARDataWriter dataWriter = new BinaryLIBLINEARDataWriter(outputDirectory);
        if (!this.setEncodersFromFileSystem(dataWriter)) {
            NameNumberNormalizer normalizer = new EuclidianNormalizer();
            FeatureVectorFeaturesEncoder fe = new FeatureVectorFeaturesEncoder(cutoff, normalizer);
            fe.addEncoder(new NumberEncoder());
            fe.addEncoder(new BooleanEncoder());
            fe.addEncoder(new StringEncoder());
            dataWriter.setFeaturesEncoder(fe);
            dataWriter.setOutcomeEncoder(new BooleanToBooleanOutcomeEncoder());
        }
        return dataWriter;
    }
}
