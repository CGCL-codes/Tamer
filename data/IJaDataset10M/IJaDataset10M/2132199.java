package org.armedbear.j.mail;

import gnu.regexp.RE;
import gnu.regexp.UncheckedRE;
import org.armedbear.j.Buffer;
import org.armedbear.j.Debug;
import org.armedbear.j.DiffFormatter;
import org.armedbear.j.Editor;
import org.armedbear.j.FormatTable;
import org.armedbear.j.Formatter;
import org.armedbear.j.Line;
import org.armedbear.j.LineSegmentList;
import org.armedbear.j.Utilities;

public final class MessageFormatter extends Formatter {

    private static final int MESSAGE_FORMAT_FIRST = DiffFormatter.DIFF_FORMAT_LAST + 1;

    private static final byte MESSAGE_FORMAT_TEXT = MESSAGE_FORMAT_FIRST;

    private static final byte MESSAGE_FORMAT_COMMENT = MESSAGE_FORMAT_FIRST + 1;

    private static final byte MESSAGE_FORMAT_HEADER_NAME = MESSAGE_FORMAT_FIRST + 2;

    private static final byte MESSAGE_FORMAT_HEADER_VALUE = MESSAGE_FORMAT_FIRST + 3;

    private static final byte MESSAGE_FORMAT_QUOTE = MESSAGE_FORMAT_FIRST + 4;

    private static final byte MESSAGE_FORMAT_SIGNATURE = MESSAGE_FORMAT_FIRST + 5;

    private static final byte MESSAGE_FORMAT_DIFF = MESSAGE_FORMAT_FIRST + 6;

    private static final RE quoteRE = new UncheckedRE("^[a-zA-Z]*>");

    private static final RE headerRE = new UncheckedRE("^ *[a-zA-Z\\-/]+:");

    private Line startOfBody;

    private final DiffFormatter diffFormatter;

    public MessageFormatter(Buffer buffer) {
        this.buffer = buffer;
        diffFormatter = new DiffFormatter(buffer);
    }

    public synchronized LineSegmentList formatLine(Line line) {
        if (line.flags() == MESSAGE_FORMAT_DIFF) return diffFormatter.formatLine(line);
        final String text = getDetabbedText(line);
        clearSegmentList();
        if (line.flags() == MESSAGE_FORMAT_SIGNATURE) {
            addSegment(text, MESSAGE_FORMAT_SIGNATURE);
            return segmentList;
        }
        String trim = text.trim();
        if (trim.length() == 0) {
            addSegment(text, MESSAGE_FORMAT_TEXT);
            return segmentList;
        }
        if (startOfBody == null || line.lineNumber() < startOfBody.lineNumber()) {
            if (text.length() > 0) {
                int i = text.indexOf(':');
                if (i >= 0 && headerRE.getMatch(text) != null) {
                    String headerName = text.substring(0, i).trim();
                    if (isKeyword(headerName)) {
                        addSegment(text, 0, i + 1, MESSAGE_FORMAT_HEADER_NAME);
                        addSegment(text, i + 1, MESSAGE_FORMAT_HEADER_VALUE);
                        return segmentList;
                    }
                }
            }
            if (line.flags() == MESSAGE_FORMAT_HEADER_VALUE) addSegment(text, MESSAGE_FORMAT_HEADER_VALUE); else addSegment(text, MESSAGE_FORMAT_COMMENT);
            return segmentList;
        }
        char c = trim.charAt(0);
        if (c == '>') {
            if (trim.startsWith(">>>>> ")) {
                addSegment(text, MESSAGE_FORMAT_TEXT);
            } else if (trim.startsWith(">From ")) {
                addSegment(text, MESSAGE_FORMAT_TEXT);
            } else {
                addSegment(text, MESSAGE_FORMAT_QUOTE);
            }
            return segmentList;
        }
        if (quoteRE.getMatch(text) != null) {
            addSegment(text, MESSAGE_FORMAT_QUOTE);
            return segmentList;
        }
        addSegment(text, MESSAGE_FORMAT_TEXT);
        return segmentList;
    }

    public synchronized boolean parseBuffer() {
        startOfBody = null;
        boolean inDiff = false;
        boolean inSig = false;
        if (buffer.needsRenumbering()) buffer.renumber();
        for (Line line = buffer.getFirstLine(); line != null; line = line.next()) {
            if (buffer instanceof MessageBuffer) {
                if (line.lineNumber() == ((MessageBuffer) buffer).getHeaderLineCount()) {
                    if (startOfBody != null) Debug.bug();
                    startOfBody = line;
                }
            }
            String text = line.getText();
            if (text == null) continue;
            if (startOfBody == null) {
                if (buffer instanceof SendMail && text.equals(SendMail.getHeaderSeparator())) startOfBody = line.next(); else {
                    int i = text.indexOf(':');
                    if (i >= 0 && headerRE.getMatch(text) != null) {
                        String headerName = text.substring(0, i).trim();
                        if (isKeyword(headerName)) line.setFlags(MESSAGE_FORMAT_HEADER_VALUE); else line.setFlags(0);
                    } else if (line.previous() != null) line.setFlags(line.previous().flags());
                }
            } else {
                if (text.equals("-- ")) {
                    inSig = true;
                    inDiff = false;
                    line.setFlags(MESSAGE_FORMAT_SIGNATURE);
                    continue;
                }
                if (inDiff) {
                    if (isDiffContinuation(line)) {
                        line.setFlags(MESSAGE_FORMAT_DIFF);
                        continue;
                    }
                    inDiff = false;
                } else if (isDiffStart(line)) {
                    inDiff = true;
                    line.setFlags(MESSAGE_FORMAT_DIFF);
                    continue;
                }
                if (inDiff) Debug.bug();
                if (inSig) {
                    line.setFlags(MESSAGE_FORMAT_SIGNATURE);
                    continue;
                }
                line.setFlags(0);
                if (text.length() == 0) continue;
                final char c = text.charAt(0);
                if (buffer.getLineCount() - line.lineNumber() < 16) {
                    if (c == '_' || c == '-') {
                        text = text.trim();
                        boolean all = true;
                        for (int i = text.length(); i-- > 0; ) {
                            if (text.charAt(i) != c) {
                                all = false;
                                break;
                            }
                        }
                        if (all) {
                            inSig = true;
                            line.setFlags(MESSAGE_FORMAT_SIGNATURE);
                            continue;
                        }
                    }
                }
            }
        }
        buffer.setNeedsParsing(false);
        return true;
    }

    public static boolean isDiffStart(Line line) {
        String text = line.trim();
        if (text.startsWith("+++ ")) return true;
        if (text.startsWith("--- ")) return true;
        if (text.startsWith("@@ ")) return true;
        if (text.startsWith("@") && text.endsWith("@")) return true;
        if (text.startsWith("diff ")) {
            Line nextLine = line.next();
            if (nextLine != null && nextLine.trim().startsWith("---")) return true;
        } else if (text.startsWith("Index: ")) {
            Line nextLine = line.next();
            if (nextLine != null) {
                String s = nextLine.trim();
                if (s.startsWith("diff ") || s.startsWith("========")) return true;
            }
        }
        return false;
    }

    public static boolean isDiffContinuation(Line line) {
        if (line.length() == 0) return true;
        final char c = line.charAt(0);
        if (c == ' ') return true;
        if (c == '+') return true;
        if (c == '-') {
            Line nextLine = line.next();
            return (nextLine != null && isDiffContinuation(nextLine));
        }
        String text = line.getText();
        if (text.startsWith("diff ")) return true;
        if (text.startsWith("Index: ")) return true;
        if (text.startsWith("========")) return true;
        if (text.startsWith("RCS file: ")) return true;
        if (text.startsWith("retrieving ")) return true;
        if (text.startsWith("@@")) return true;
        if (text.startsWith("@") && text.endsWith("@")) return true;
        return false;
    }

    public FormatTable getFormatTable() {
        if (formatTable == null) {
            formatTable = diffFormatter.getFormatTable();
            formatTable.setModeName("MessageMode");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_TEXT, "text");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_COMMENT, "comment");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_HEADER_NAME, "headerName", "keyword");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_HEADER_VALUE, "headerValue", "operator");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_QUOTE, "string");
            formatTable.addEntryFromPrefs(MESSAGE_FORMAT_SIGNATURE, "signature", "comment");
        }
        return formatTable;
    }

    public void reset() {
        diffFormatter.reset();
        super.reset();
    }
}
