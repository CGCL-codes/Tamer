package org.armedbear.j;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import gnu.regexp.UncheckedRE;

public final class LispShellFormatter extends Formatter {

    private static final byte FORMAT_TEXT = 0;

    private static final byte FORMAT_COMMENT = 1;

    private static final byte FORMAT_PROMPT = 2;

    private static final byte FORMAT_INPUT = 3;

    private final RE defaultPromptRE = new UncheckedRE("^[^>\\*\\]]*[>\\*\\]] *");

    public LispShellFormatter(Buffer buffer) {
        this.buffer = buffer;
    }

    public LineSegmentList formatLine(Line line) {
        clearSegmentList();
        if (line == null) {
            addSegment("", FORMAT_TEXT);
            return segmentList;
        }
        final String text = getDetabbedText(line);
        Annotation a = line.getAnnotation();
        if (a != null) {
            int index = a.getIntegerValue();
            if (index > 0 && index <= text.length()) {
                addSegment(text, 0, index, FORMAT_PROMPT);
                addSegment(text, index, FORMAT_INPUT);
                return segmentList;
            }
        }
        if (line.flags() == 0) {
            int promptEnd = getPromptEndIndex(text);
            if (promptEnd > 0) {
                line.setAnnotation(new Annotation(promptEnd));
                line.setFlags(STATE_PROMPT);
                addSegment(text, 0, promptEnd, FORMAT_PROMPT);
                int commentStart = text.indexOf(';', promptEnd);
                if (commentStart >= promptEnd) {
                    addSegment(text, promptEnd, commentStart, FORMAT_INPUT);
                    addSegment(text, commentStart, FORMAT_COMMENT);
                } else addSegment(text, promptEnd, FORMAT_INPUT);
                return segmentList;
            }
            line.setFlags(STATE_OUTPUT);
        }
        if (text.indexOf(';') == 0) addSegment(text, FORMAT_COMMENT); else {
            int format = (line.flags() == STATE_INPUT) ? FORMAT_INPUT : FORMAT_TEXT;
            int index = text.indexOf(" ;");
            if (index >= 0) {
                addSegment(text, 0, index + 1, format);
                addSegment(text, index + 1, FORMAT_COMMENT);
            } else addSegment(text, format);
        }
        return segmentList;
    }

    private int getPromptEndIndex(String text) {
        if (text.length() == 0) return 0;
        String trim = text.trim();
        if (trim.length() == 0) return 0;
        if (trim.charAt(0) == '(') return 0;
        if (text.startsWith("> ")) return 2;
        if (text.startsWith("* ")) return 2;
        final RE promptRE;
        if (buffer instanceof CommandInterpreter) promptRE = ((CommandInterpreter) buffer).getPromptRE(); else promptRE = defaultPromptRE;
        REMatch match = promptRE.getMatch(text);
        if (match != null) return match.getEndIndex();
        return 0;
    }

    public FormatTable getFormatTable() {
        if (formatTable == null) {
            formatTable = new FormatTable("LispShellMode");
            formatTable.addEntryFromPrefs(FORMAT_TEXT, "text");
            formatTable.addEntryFromPrefs(FORMAT_COMMENT, "comment");
            formatTable.addEntryFromPrefs(FORMAT_PROMPT, "prompt");
            formatTable.addEntryFromPrefs(FORMAT_INPUT, "input");
        }
        return formatTable;
    }
}
