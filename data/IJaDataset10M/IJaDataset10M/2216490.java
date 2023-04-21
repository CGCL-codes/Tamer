package org.cleartk.token.tokenizer.chunk;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.cleartk.token.TokenComponents;
import org.cleartk.token.pos.genia.GeniaPosGoldReader;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.CollectionReaderFactory;
import org.uimafit.pipeline.SimplePipeline;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Ogren
 * 
 */
@Deprecated
public class BuildTestTokenChunkModel {

    public static void main(String[] args) throws Exception {
        TypeSystemDescription typeSystemDescription = TokenComponents.TYPE_SYSTEM_DESCRIPTION;
        CollectionReader reader = CollectionReaderFactory.createCollectionReader(GeniaPosGoldReader.class, typeSystemDescription, GeniaPosGoldReader.PARAM_GENIA_CORPUS_FILE, "src/test/resources/token/genia/GENIAcorpus3.02.articleA.pos.xml", GeniaPosGoldReader.PARAM_LOAD_SENTENCES, true);
        AnalysisEngine subtokenizer = AnalysisEngineFactory.createPrimitive(TokenComponents.createSubtokenizer());
        AnalysisEngine chunkTokenizerDataWriter = AnalysisEngineFactory.createPrimitive(ChunkTokenizerFactory.createChunkTokenizerDataWriter("src/test/resources/token/chunk"));
        SimplePipeline.runPipeline(reader, subtokenizer, chunkTokenizerDataWriter);
        org.cleartk.classifier.jar.Train.main("src/test/resources/token/chunk");
    }
}
