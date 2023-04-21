package uk.co.chamberlain.tagcloudmaker.domain;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import uk.co.chamberlain.tagcloudmaker.ExceptionHandler;
import uk.co.chamberlain.tagcloudmaker.Utils;

/**
 * Lists words to exclude from the cloud tag that are specified in a text file.
 * Each word should be on a separate line.
 *
 * @author Stephen Chamberlain
 */
class FileBackedWordList implements ExclusionWordListable {

    private List<String> wordList;

    /**
     * Constructor that will build an exclusion word list from the passed text
     * file reference.
     * 
     * @param file The text file to use to build this word lister.
     *
     * @throws IllegalArgumentException If the file passed is not a text file.
     */
    public FileBackedWordList(File file) {
        Utils.checkNotNull(file);
        if (!file.getName().endsWith(".txt")) {
            throw new IllegalArgumentException("Non-txt file given, cannot process this file: " + file.getAbsolutePath());
        }
        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(file);
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
        wordList = Arrays.asList(fileContent.split(System.getProperty("line.separator")));
    }

    /**
     * {@inheritDoc}
     * 
     * @return A list of words to exclude from the cloud based on the content of
     * the file given in the constructor.
     */
    @Override
    public List<String> getExclusionList() {
        return wordList;
    }
}
