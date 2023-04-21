package analysis;

import data.Chapter;
import data.Paragraph;
import data.WordsGroupList;
import data.Definition;
import preparation.Stemmer;
import preparation.StopWordsFilter;
import preparation.DefinitionExtractor;
import util.DocumentParser;
import util.ReportHelper;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import org.openoffice.odf.dom.element.OdfElement;

/**
 * Author:
 */
public class TextAnalyser {

    private static WordsGroupList count(List<List<WordsGroup>> words, WordsGroupList previousResult) {
        WordsGroupList result = previousResult == null ? new WordsGroupList() : previousResult;
        for (List<WordsGroup> sequencesWords : words) {
            result.addAll(sequencesWords);
        }
        return result;
    }

    public static WordsGroupList analyse(List<List<String>> words, List<List<WordsGroup>> wordsSentensecList, WordsGroupList previousResult, Paragraph paragraph) {
        List<List<WordsGroup>> currentWords = new ArrayList<List<WordsGroup>>();
        for (List<String> sentence : words) {
            ArrayList<WordsGroup> wordsList = new ArrayList<WordsGroup>();
            for (String word : sentence) {
                if (word.length() > 2) {
                    wordsList.add(new WordsGroup(word, 1, paragraph));
                }
            }
            wordsSentensecList.add(wordsList);
            currentWords.add(wordsList);
        }
        return count(currentWords, previousResult);
    }

    private static void countAndJoin(WordsGroupList frequencies, List<List<WordsGroup>> sentencesWords, int groupsLength, GroupExtractionRule rule) {
        WordsGroupList doubleGroups = new WordsGroupList();
        for (List<WordsGroup> words : sentencesWords) {
            for (int i = 0; i < words.size() - 1; ++i) {
                if (frequencies.contains(words.get(i)) && frequencies.contains(words.get(i + 1)) && (groupsLength == 1 || words.get(i).getWords().size() == groupsLength || words.get(i + 1).getWords().size() == groupsLength)) {
                    doubleGroups.add(new WordsGroup(1, words.get(i).getParagraph(), words.get(i), words.get(i + 1)));
                }
            }
        }
        ReportHelper.reportWriter.addSheet("Word Groups' Join");
        ReportHelper.ROWS = 0;
        ReportHelper.reportWriter.setCell(++ReportHelper.ROWS, 0, "Word Group");
        ReportHelper.reportWriter.setCell(ReportHelper.ROWS, 1, "Frequency");
        Collections.sort(doubleGroups);
        for (; doubleGroups.size() > 0; ) {
            WordsGroup doubleGroup = doubleGroups.get(0);
            doubleGroups.remove(doubleGroup);
            int fregFirstIndex = frequencies.indexOf(doubleGroup.getWords().get(0));
            int fregSecondIndex = frequencies.indexOf(doubleGroup.getWords().get(1));
            if (fregFirstIndex < 0 || fregSecondIndex < 0) {
                continue;
            }
            WordsGroup frequencyFirst = frequencies.get(fregFirstIndex);
            WordsGroup frequencySecond = frequencies.get(fregSecondIndex);
            if (rule.isGroup(doubleGroup, frequencyFirst, frequencySecond, frequencies.wordsSize())) {
                ReportHelper.reportWGJoin(doubleGroup, frequencyFirst, frequencySecond);
                joinInList(sentencesWords, doubleGroup, doubleGroups);
                joinInFrequencies(frequencies, doubleGroup, frequencyFirst, frequencySecond);
            }
        }
    }

    private static void joinInFrequencies(WordsGroupList frequencies, WordsGroup doubleGroup, WordsGroup frequencyFirst, WordsGroup frequencySecond) {
        updateFrequencyForWordGroup(frequencies, doubleGroup, frequencyFirst);
        updateFrequencyForWordGroup(frequencies, doubleGroup, frequencySecond);
        frequencies.add(doubleGroup);
    }

    private static void updateFrequencyForWordGroup(WordsGroupList frequencies, WordsGroup doubleGroup, WordsGroup frequencyFirst) {
        if (frequencyFirst.getFrequency() - doubleGroup.getFrequency() > 0) {
            frequencyFirst.setFrequency(frequencyFirst.getFrequency() - doubleGroup.getFrequency());
        } else {
            frequencies.remove(frequencyFirst);
        }
    }

    private static void joinInList(List<List<WordsGroup>> sentencesWords, WordsGroup doubleGroup, WordsGroupList frequencies) {
        WordsGroup first = doubleGroup.getWords().get(0);
        WordsGroup second = doubleGroup.getWords().get(1);
        for (List<WordsGroup> words : sentencesWords) {
            joinInSentence(doubleGroup, frequencies, first, second, words);
        }
    }

    private static void joinInSentence(WordsGroup doubleGroup, WordsGroupList frequencies, WordsGroup first, WordsGroup second, List<WordsGroup> words) {
        for (int i = 0; i < words.size() - 2; ++i) {
            if (first.equals(words.get(i)) && second.equals(words.get(i + 1))) {
                if (i > 0) {
                    WordsGroup group = new WordsGroup(1, null, words.get(i - 1), words.get(i));
                    frequencies.remove(group);
                }
                if (i < words.size() - 1) {
                    WordsGroup group = new WordsGroup(1, null, words.get(i + 1), words.get(i + 2));
                    frequencies.remove(group);
                }
                words.remove(i);
                words.remove(i);
                words.add(i, doubleGroup);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        analyseText("test/���������������� �����2.odt");
    }

    public static WordsGroupList analyseText(String fileName) throws Exception {
        OdfElement body = DocumentParser.getTextBody(fileName);
        Chapter allDocument = DocumentParser.parseDocument(body);
        WordsGroupList result = new WordsGroupList();
        List<List<WordsGroup>> wordsSentensecList = new ArrayList<List<WordsGroup>>();
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        result = analyseChapter(result, wordsSentensecList, allDocument, definitions);
        WordsGroupList frequencies = result;
        ReportHelper.ROWS = 0;
        ReportHelper.reportWriter.addSheet("Summary");
        ReportHelper.makeTableRow("�����", "�������");
        Collections.sort(frequencies);
        for (WordsGroup wordsGroup : frequencies) {
            ReportHelper.makeTableRow(wordsGroup.toString(), wordsGroup.getFrequency());
        }
        int groupsLength = 2;
        frequencies = SortingHelper.choiceByRangs(frequencies, 0, 30);
        for (int i = 1; i < groupsLength; ++i) {
            countAndJoin(frequencies, wordsSentensecList, i, new MutualInformationRule());
        }
        Collections.sort(frequencies);
        frequencies = SortingHelper.choiceByRangsInDependenceOfLength(frequencies, 10);
        ConnectionBuilder.buildConnections(definitions, frequencies);
        reportJoinResults(frequencies);
        String fullContent = DocumentParser.fullContent(body);
        ReportHelper.reportWriter.addSheet("Destemming");
        ReportHelper.ROWS = 0;
        ReportHelper.makeTableRow("Words Group", "Destemmed Form");
        fullContent = fullContent.toLowerCase();
        for (WordsGroup wordsGroup : frequencies) {
            String wg = wordsGroup.toString();
            String realForm = Stemmer.findRealWordForm(fullContent, wordsGroup.toString());
            ReportHelper.makeTableRow(wg, realForm);
        }
        for (WordsGroup wordsGroup : frequencies) {
            System.out.println("Frequency " + wordsGroup.getFrequency());
            System.out.println("Words: " + wordsGroup.toString());
        }
        ReportHelper.reportWriter.close();
        frequencies.toXML();
        return frequencies;
    }

    private static void reportJoinResults(WordsGroupList frequencies) throws IOException {
        ReportHelper.makeHeader("Result");
        ReportHelper.makeTableRow("��������������", "�������");
        for (WordsGroup wordsGroup : frequencies) {
            ReportHelper.makeTableRow(wordsGroup.toString(), wordsGroup.getFrequency());
        }
    }

    public static boolean containsSomeForm(String wg, String text) {
        try {
            Pattern pattern = Pattern.compile(wg);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                return true;
            }
        } catch (PatternSyntaxException e) {
            int i = 0;
        }
        return false;
    }

    private static WordsGroupList analyseChapter(WordsGroupList result, List<List<WordsGroup>> wordsSentensecList, Chapter chapter, List<Definition> definitions) throws IOException {
        String absoluteId = ReportHelper.chapterAbsoluteId(chapter);
        ReportHelper.reportWriter.addSheet(absoluteId);
        ReportHelper.ROWS = 0;
        WordsGroupList newResult = new WordsGroupList();
        newResult = analiseItem(newResult, wordsSentensecList, chapter.getValue());
        analiseItem(result, wordsSentensecList, chapter.getValue());
        for (Paragraph paragraph : chapter.getParagraphs()) {
            List<Definition> currentDefinitions = DefinitionExtractor.findDefinitions(paragraph.getContent());
            paragraph.setDefinitions(currentDefinitions);
            definitions.addAll(currentDefinitions);
            analiseItem(result, wordsSentensecList, paragraph.getContent());
        }
        result.addAllSummarization(newResult);
        for (Chapter subsection : chapter.getChapters()) {
            analyseChapter(result, wordsSentensecList, subsection, definitions);
        }
        result.getFrequenciesInParts().put(absoluteId, newResult);
        return result;
    }

    private static WordsGroupList analiseItem(WordsGroupList result, List<List<WordsGroup>> wordsSentensecList, String item) throws IOException {
        ReportHelper.reportChapter("Input Value", item);
        List<List<String>> separatedStemms = new ArrayList<List<String>>();
        List<List<String>> sentences = StopWordsFilter.filter(item);
        if (sentences.size() > 0) {
            ReportHelper.makeTableRow("Filtered Inseparability Sentence", "Stemmed Sentence");
        }
        for (List<String> sentence : sentences) {
            List<String> stems = Stemmer.stemAll(sentence);
            separatedStemms.add(stems);
            ReportHelper.makeTableRow(sentence, stems);
        }
        WordsGroupList newResult = analyse(separatedStemms, wordsSentensecList, null, null);
        WordsGroupList frequencies = newResult;
        ReportHelper.reportChapter("Stemmed Sentence", frequencies);
        result.addAll(newResult);
        return result;
    }
}
