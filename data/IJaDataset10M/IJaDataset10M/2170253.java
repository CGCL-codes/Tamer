package org.cleartk.timeml.eval;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cleartk.syntax.opennlp.ParserAnnotator;
import org.cleartk.syntax.opennlp.PosTaggerAnnotator;
import org.cleartk.timeml.corpus.TempEval2010GoldAnnotator;
import org.cleartk.timeml.corpus.TempEval2010Writer;
import org.cleartk.timeml.tlink.TemporalLinkEventToSameSentenceTimeAnnotator;
import org.cleartk.timeml.type.TemporalLink;
import org.cleartk.token.stem.snowball.DefaultSnowballStemmer;

/**
 * TempEval 2010 task C: event to same sentence time.
 * 
 * Best reported precision in TempEval 2010: 0.65
 * 
 * <br>
 * Copyright (c) 2011, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public class TempEval2010TaskC extends TempEval2010Main {

    public static void main(String[] args) throws Exception {
        new TempEval2010TaskC().runMain(args);
    }

    @Override
    protected TempEval2010Evaluation getEvaluation(File trainDir, File testDir, File outputDir) throws Exception {
        List<ModelInfo<TemporalLink>> infos = new ArrayList<ModelInfo<TemporalLink>>();
        infos.add(new TemporalLinkModelInfo(TemporalLinkEventToSameSentenceTimeAnnotator.FACTORY));
        return new TempEval2010Evaluation(trainDir, testDir, outputDir, Arrays.asList(TempEval2010GoldAnnotator.PARAM_TEXT_VIEWS, TempEval2010GoldAnnotator.PARAM_TIME_EXTENT_VIEWS, TempEval2010GoldAnnotator.PARAM_TIME_ATTRIBUTE_VIEWS, TempEval2010GoldAnnotator.PARAM_EVENT_EXTENT_VIEWS, TempEval2010GoldAnnotator.PARAM_EVENT_ATTRIBUTE_VIEWS), TempEval2010GoldAnnotator.PARAM_TEMPORAL_LINK_EVENT_TO_SAME_SENTENCE_TIME_VIEWS, TempEval2010Writer.PARAM_TEMPORAL_LINK_EVENT_TO_SAME_SENTENCE_TIME_VIEW, Arrays.asList(DefaultSnowballStemmer.getDescription("English"), PosTaggerAnnotator.getDescription(), ParserAnnotator.getDescription()), infos);
    }
}
