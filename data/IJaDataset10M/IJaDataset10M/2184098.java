package org.cleartk.examples.documentclassification;

import java.io.File;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.classifier.CleartkAnnotator;
import org.cleartk.classifier.Instance;
import org.cleartk.classifier.feature.extractor.simple.BagExtractor;
import org.cleartk.classifier.feature.extractor.simple.CountsExtractor;
import org.cleartk.classifier.feature.extractor.simple.SimpleFeatureExtractor;
import org.cleartk.classifier.feature.extractor.simple.TypePathExtractor;
import org.cleartk.classifier.jar.GenericJarClassifierFactory;
import org.cleartk.examples.ExampleComponents;
import org.cleartk.token.type.Token;
import org.cleartk.util.ViewURIUtil;
import org.uimafit.factory.AnalysisEngineFactory;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philipp G. Wetzler
 * 
 */
@Deprecated
public class DocumentClassificationAnnotator extends CleartkAnnotator<String> {

    public static final String PREDICTION_VIEW_NAME = "ExampleDocumentClassificationPredictionView";

    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        SimpleFeatureExtractor subExtractor = new TypePathExtractor(Token.class, "stem", false, false, true);
        extractor = new CountsExtractor(new BagExtractor(Token.class, subExtractor));
    }

    public void process(JCas jCas) throws AnalysisEngineProcessException {
        DocumentAnnotation doc = (DocumentAnnotation) jCas.getDocumentAnnotationFs();
        Instance<String> instance = new Instance<String>();
        instance.addAll(extractor.extract(jCas, doc));
        try {
            if (isTraining()) {
                JCas goldView = jCas.getView(GoldAnnotator.GOLD_VIEW_NAME);
                instance.setOutcome(goldView.getSofaDataString());
                this.dataWriter.write(instance);
            } else {
                String result = this.classifier.classify(instance.getFeatures());
                JCas predictionView = jCas.createView(PREDICTION_VIEW_NAME);
                predictionView.setSofaDataString(result, "text/plain");
                System.out.println("classified " + ViewURIUtil.getURI(jCas) + " as " + result + ".");
            }
        } catch (CASException e) {
            throw new AnalysisEngineProcessException(e);
        }
    }

    private CountsExtractor extractor;

    public static AnalysisEngineDescription getClassifierDescription(File classifierJarFile) throws ResourceInitializationException {
        return AnalysisEngineFactory.createPrimitiveDescription(DocumentClassificationAnnotator.class, ExampleComponents.TYPE_SYSTEM_DESCRIPTION, GenericJarClassifierFactory.PARAM_CLASSIFIER_JAR_PATH, classifierJarFile.toString());
    }
}
