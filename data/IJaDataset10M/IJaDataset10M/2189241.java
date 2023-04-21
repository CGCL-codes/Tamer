package uima.taes.interestingness;

import uima.types.CharacterBasedFeature;
import uima.types.LexicalRichness;
import uima.types.StructuralFeature;
import uima.types.SyntacticFeature;
import uima.types.WordBasedFeature;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

public class MultipleCategoryAuthorStyle extends MultpleCategoryBayesianLearnedFeature {

    protected void train() {
    }

    protected double[] getFeatureVector(int docID, JCas jcas) {
        double[] feature = new double[featureSize];
        int offset = 0;
        FSIndex index = jcas.getJFSIndexRepository().getAnnotationIndex(CharacterBasedFeature.type);
        CharacterBasedFeature cb = (CharacterBasedFeature) index.iterator().next();
        feature[offset + 0] = cb.getNumberOfWhiteSpaceCharacters();
        feature[offset + 1] = cb.getNumberOfUppercaseCharacters();
        feature[offset + 2] = cb.getNumberOfDigitCharacters();
        feature[offset + 3] = cb.getNumberOfCharacters();
        feature[offset + 4] = cb.getNumberOfAlphabeticCharacters();
        offset += 5;
        for (int i = 0; i < 21; i++) {
            feature[offset + i] = cb.getFrequencyOfSpecialCharacters(i);
        }
        offset += 21;
        for (int i = 0; i < 26; i++) {
            feature[offset + i] = cb.getFrequencyOfLetters(i);
        }
        offset += 26;
        index = jcas.getJFSIndexRepository().getAnnotationIndex(LexicalRichness.type);
        LexicalRichness lr = (LexicalRichness) index.iterator().next();
        feature[offset + 0] = lr.getHapaxDislegomena();
        feature[offset + 1] = lr.getHapaxLegomena();
        feature[offset + 2] = lr.getHerdansV();
        feature[offset + 3] = lr.getHonoroesR();
        feature[offset + 4] = lr.getSichelsS();
        feature[offset + 5] = lr.getSimpsonsD();
        feature[offset + 6] = lr.getYulesK();
        feature[offset + 7] = lr.getBrunetsW();
        offset += 8;
        index = jcas.getJFSIndexRepository().getAnnotationIndex(StructuralFeature.type);
        StructuralFeature sf = (StructuralFeature) index.iterator().next();
        feature[offset + 0] = sf.getAverageNumberOfCharactersPerParagraph();
        feature[offset + 1] = sf.getAverageNumberofSentencesPerParagraph();
        feature[offset + 2] = sf.getAverageNumberOfWordsPerParagraph();
        feature[offset + 3] = sf.getHasQuotedContent();
        feature[offset + 4] = sf.getNumberOfLines();
        feature[offset + 5] = sf.getNumberOfParagraphs();
        feature[offset + 6] = sf.getNumberOfSentences();
        offset += 7;
        index = jcas.getJFSIndexRepository().getAnnotationIndex(SyntacticFeature.type);
        SyntacticFeature syf = (SyntacticFeature) index.iterator().next();
        for (int i = 0; i < 8; i++) feature[offset + i] = syf.getPunctuationFrequency(i);
        offset += 8;
        for (int i = 0; i < 150; i++) feature[offset + i] = syf.getFunctionalWordFrequency(i);
        offset += 150;
        index = jcas.getJFSIndexRepository().getAnnotationIndex(WordBasedFeature.type);
        WordBasedFeature wf = (WordBasedFeature) index.iterator().next();
        feature[offset + 0] = wf.getAverageSentenceLengthInCharacters();
        feature[offset + 1] = wf.getAverageSetenceLengthInWords();
        feature[offset + 2] = wf.getAverageWordLength();
        feature[offset + 3] = wf.getNumberOfShortWords();
        feature[offset + 4] = wf.getNumberOfWords();
        feature[offset + 5] = wf.getTotalCharactersInWord();
        feature[offset + 6] = wf.getTotalDifferentWords();
        offset += 7;
        for (int i = 0; i < 20; i++) feature[offset + i] = wf.getWordLengthFrequency(i);
        return feature;
    }

    protected void initializeFeatureSize() {
        featureSize = 5 + 26 + 21;
        featureSize += 8;
        featureSize += 7;
        featureSize += 8 + 150;
        featureSize += 7 + 20;
    }

    @Override
    public String getFeature() {
        return "uima.taes.interestingness.AuthorStyle";
    }

    @Override
    protected void initializeTables() {
    }
}
