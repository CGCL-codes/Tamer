package org.cleartk.srl.propbank;

import java.io.IOException;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.cleartk.srl.SrlComponents;
import org.cleartk.syntax.constituent.TreebankGoldAnnotator;
import org.cleartk.util.ViewURIFileNamer;
import org.uimafit.component.xwriter.XWriter;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.CollectionReaderFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.pipeline.SimplePipeline;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public class ParseProbankExample {

    public static void main(String[] args) throws UIMAException, IOException {
        String propbankFileName = args[0];
        String penntreebankDirectoryName = args[1];
        String wsjSections = args[2];
        String outputDirectory = args[3];
        CollectionReader reader = CollectionReaderFactory.createCollectionReader(PropbankGoldReader.class, TypeSystemDescriptionFactory.createTypeSystemDescription("org.cleartk.srl.TypeSystem"), PropbankGoldReader.PARAM_PROPBANK_FILE_NAME, propbankFileName, PropbankGoldReader.PARAM_PENNTREEBANK_DIRECTORY_NAME, penntreebankDirectoryName, PropbankGoldReader.PARAM_WSJ_SECTIONS, wsjSections);
        AnalysisEngine treebankEngine = AnalysisEngineFactory.createPrimitive(TreebankGoldAnnotator.class, SrlComponents.TYPE_SYSTEM_DESCRIPTION);
        AnalysisEngine propbankEngine = AnalysisEngineFactory.createPrimitive(PropbankGoldAnnotator.class, SrlComponents.TYPE_SYSTEM_DESCRIPTION);
        AnalysisEngine xWriter = AnalysisEngineFactory.createPrimitive(XWriter.class, SrlComponents.TYPE_SYSTEM_DESCRIPTION, XWriter.PARAM_OUTPUT_DIRECTORY_NAME, outputDirectory, XWriter.PARAM_FILE_NAMER_CLASS_NAME, ViewURIFileNamer.class.getName());
        SimplePipeline.runPipeline(reader, treebankEngine, propbankEngine, xWriter);
    }
}
