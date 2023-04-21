package edu.jhu.joshua.sa.decoder.feature_function.translation_model;

import java.util.List;
import edu.jhu.sa.util.suffix_array.AlignmentArray;
import edu.jhu.sa.util.suffix_array.CorpusArray;
import edu.jhu.sa.util.suffix_array.PrefixTree;
import edu.jhu.sa.util.suffix_array.SuffixArray;

public class SAGrammar implements TMGrammarInterface<Integer> {

    private final SuffixArray sourceSuffixArray;

    private final CorpusArray targetCorpus;

    private final AlignmentArray alignments;

    private final int maxPhraseSpan;

    private final int maxPhraseLength;

    private final int maxNonterminals;

    public SAGrammar(SuffixArray sourceSuffixArray, CorpusArray targetCorpus, AlignmentArray alignments, int maxPhraseSpan, int maxPhraseLength, int maxNonterminals) {
        this.sourceSuffixArray = sourceSuffixArray;
        this.targetCorpus = targetCorpus;
        this.alignments = alignments;
        this.maxPhraseSpan = maxPhraseSpan;
        this.maxPhraseLength = maxPhraseLength;
        this.maxNonterminals = maxNonterminals;
    }

    public Trie<Integer, RuleInterface> getGrammarForSentence(List<Integer> sentence) {
        int[] words = new int[sentence.size()];
        for (int i = 0; i < words.length; i++) {
            words[i] = sentence.get(i);
        }
        PrefixTree prefixTree = new PrefixTree(sourceSuffixArray, targetCorpus, alignments, words, maxPhraseSpan, maxPhraseLength, maxNonterminals);
        return prefixTree.getRoot();
    }
}
