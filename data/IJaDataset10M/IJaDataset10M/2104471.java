package kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.MorphAnalyzer;

import kr.ac.kaist.swrc.jhannanum.comm.PlainSentence;
import kr.ac.kaist.swrc.jhannanum.comm.SetOfSentences;
import kr.ac.kaist.swrc.jhannanum.plugin.Plugin;

/**
 * The plug-in interface is for morphological analysis
 * 
 * - Phase: The Second Phase
 * - Type: Major Plug-in
 * 
 * @author Sangwon Park (hudoni@world.kaist.ac.kr), CILab, SWRC, KAIST
 */
public interface MorphAnalyzer extends Plugin {

    /**
	 * It performs morphological analysis on the specified plain sentence, and returns the all analysis result where
	 * each plain eojeol has more than one morphologically analyzed eojeol.
	 * @param ps - the plain sentence to be morphologically analyzed
	 * @return - the set of eojeols where each eojeol has at least one morphological analysis result
	 */
    public abstract SetOfSentences morphAnalyze(PlainSentence ps);
}
