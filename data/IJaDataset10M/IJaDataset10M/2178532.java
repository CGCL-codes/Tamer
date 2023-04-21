package org.sonify.vm.hop;

import java.util.HashMap;
import org.sonify.vm.CodeMatcher;
import org.sonify.vm.MatchKeyword;

/**
 * Matches code blocks through the parser to make it easier to query
 * what aspect of Hop matches up with other elements of the source code.
 * 
 * @author Andreas Stefik
 */
public class HopCodeMatcher extends CodeMatcher {

    private HashMap<String, FileMatcher> map;

    private FileMatcher currentFileMatcher = null;

    private boolean isRun = true;

    public HopCodeMatcher() {
        map = new HashMap<String, FileMatcher>();
    }

    public void addMatch(MatchKeyword prefix, MatchKeyword suffix) {
        if (currentFileMatcher != null) {
            if (suffix != null && !isRun) {
                currentFileMatcher.add(prefix);
            }
            if (prefix != null && !isRun) {
                currentFileMatcher.add(suffix);
            }
        } else {
            throw new RuntimeException("Compiler Bug: Tried to match strings in a non-existent file.");
        }
    }

    public void setFile(String key) {
        if (!map.containsKey(key)) {
            FileMatcher matcher = new FileMatcher();
            matcher.setFileKey(key);
            currentFileMatcher = matcher;
            map.put(key, matcher);
        } else {
            currentFileMatcher = map.get(key);
        }
        if (currentFileMatcher == null || !currentFileMatcher.getFileKey().equals(key)) {
            currentFileMatcher = new FileMatcher(key);
            map.put(key, currentFileMatcher);
        }
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

    @Override
    public MatchKeyword findOrigin(String fileKey, int searchOffset) {
        FileMatcher get = map.get(fileKey);
        if (get != null) {
            return get.findOrigin(searchOffset);
        }
        return null;
    }

    @Override
    public MatchKeyword findMatch(String fileKey, int matchID, boolean backward) {
        FileMatcher get = map.get(fileKey);
        if (get != null) {
            return get.findMatch(matchID, backward);
        }
        return null;
    }

    /**
     * Empties the files matcher
     */
    @Override
    public void clear() {
        if (currentFileMatcher != null) {
            String temp = currentFileMatcher.getFileKey();
            currentFileMatcher.clear();
            map.put(temp, currentFileMatcher);
        }
        this.setIsRun(true);
    }

    /**
     * Empties the files matcher
     */
    @Override
    public void clear(String fileKey) {
    }
}
