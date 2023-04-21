package org.rrd4j.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

class RrdCmdScanner {

    private Logger log = Logger.getLogger(super.getClass());

    private LinkedList<String> words = new LinkedList<String>();

    private StringBuilder buff;

    RrdCmdScanner(String command) {
        String cmd = command.trim();
        char activeQuote = 0;
        for (int i = 0; i < cmd.length(); i++) {
            char c = cmd.charAt(i);
            if ((c == '"' || c == '\'') && activeQuote == 0) {
                initWord();
                activeQuote = c;
                continue;
            }
            if (c == activeQuote) {
                activeQuote = 0;
                continue;
            }
            if (isSeparator(c) && activeQuote == 0) {
                finishWord();
                continue;
            }
            if (c == '\\' && activeQuote == '"' && i + 1 < cmd.length()) {
                char c2 = cmd.charAt(i + 1);
                if (c2 == '\\' || c2 == '"') {
                    appendWord(c2);
                    i++;
                    continue;
                }
            }
            appendWord(c);
        }
        if (activeQuote != 0) {
            throw new IllegalArgumentException("End of command reached but " + activeQuote + " expected");
        }
        finishWord();
    }

    String getCmdType() {
        if (words.size() > 0) {
            return words.get(0);
        } else {
            return null;
        }
    }

    private void appendWord(char c) {
        if (buff == null) {
            buff = new StringBuilder("");
        }
        buff.append(c);
    }

    private void finishWord() {
        if (buff != null) {
            words.add(buff.toString());
            buff = null;
        }
    }

    private void initWord() {
        if (buff == null) {
            buff = new StringBuilder("");
        }
    }

    void dump() {
        for (String word : words) {
            log.debug(word);
        }
    }

    String getOptionValue(String shortForm, String longForm, String defaultValue) {
        String value = null;
        if (shortForm != null) {
            value = getOptionValue("-" + shortForm);
        }
        if (value == null && longForm != null) {
            value = getOptionValue("--" + longForm);
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    String getOptionValue(String shortForm, String longForm) {
        return getOptionValue(shortForm, longForm, null);
    }

    private String getOptionValue(String fullForm) {
        Iterator<String> iter = words.listIterator();
        while (iter.hasNext()) {
            String word = iter.next();
            if (word.equals(fullForm)) {
                if (iter.hasNext()) {
                    iter.remove();
                    String value = iter.next();
                    iter.remove();
                    return value;
                } else {
                    throw new IllegalArgumentException("Value for option " + fullForm + " expected but not found");
                }
            }
            if (word.startsWith(fullForm)) {
                int pos = fullForm.length();
                if (word.charAt(pos) == '=') {
                    pos++;
                }
                iter.remove();
                return word.substring(pos);
            }
        }
        return null;
    }

    boolean getBooleanOption(String shortForm, String longForm) {
        Iterator<String> iter = words.listIterator();
        while (iter.hasNext()) {
            String word = iter.next();
            if ((shortForm != null && word.equals("-" + shortForm)) || (longForm != null && word.equals("--" + longForm))) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    String[] getMultipleOptions(String shortForm, String longForm) {
        List<String> values = new ArrayList<String>();
        for (; ; ) {
            String value = getOptionValue(shortForm, longForm, null);
            if (value == null) {
                break;
            }
            values.add(value);
        }
        return values.toArray(new String[values.size()]);
    }

    String[] getRemainingWords() {
        return words.toArray(new String[words.size()]);
    }

    boolean isSeparator(char c) {
        return Character.isWhitespace(c);
    }
}
