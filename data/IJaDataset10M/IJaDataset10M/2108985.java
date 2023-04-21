package uk.org.sgj.YAT.Tests;

import java.util.*;

public class VocabTestState {

    private VocabTestDefinition vtd;

    VocabTestState(VocabTestDefinition v) {
        vtd = v;
    }

    VocabTestSet getNextTestSet(Vector<ChapterIncludedInTest> chapters) {
        VocabTestSet testSet = includedChapters(chapters);
        if (vtd.getRandom()) {
            testSet.shuffle();
        }
        return (testSet);
    }

    private VocabTestSet includedChapters(Vector<ChapterIncludedInTest> chapters) {
        VocabTestSet testSet = new VocabTestSet();
        Iterator<ChapterIncludedInTest> chs = chapters.iterator();
        while (chs.hasNext()) {
            ChapterIncludedInTest ch = (ChapterIncludedInTest) chs.next();
            if (ch.isCurrentlyTesting()) {
                testSet.addIteratorWithFilter(ch.getChapter().getVocabAsIterator(), vtd.getFilterLevel(), vtd.getFrequencyLowerLimit(), vtd.getFrequencyUpperLimit(), vtd.isFrequencyIncludeBlanks(), vtd.isExcludeKnownWords());
            }
        }
        return (testSet);
    }
}
