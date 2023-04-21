package org.webcastellum;

import java.io.CharArrayWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class AbstractRelaxingHtmlParserWriter extends FilterWriter implements RelaxingHtmlParser {

    private final boolean useTunedBlockParser;

    private final int[] LAST_FEW = new int[4];

    private final CharArrayWriter currentTag = new CharArrayWriter();

    private boolean isWithinTag = false;

    private boolean isWithinComment = false;

    protected AbstractRelaxingHtmlParserWriter(final Writer delegate, final boolean useTunedBlockParser) {
        super(delegate);
        this.useTunedBlockParser = useTunedBlockParser;
    }

    public final void writeToUnderlyingSink(final String string) throws IOException {
        out.write(string);
    }

    public final void writeToUnderlyingSink(final char[] chars, final int start, final int count) throws IOException {
        out.write(chars, start, count);
    }

    public final void writeToUnderlyingSink(final int character) throws IOException {
        out.write(character);
    }

    public void handleTag(final String tag) throws IOException {
        writeToUnderlyingSink(tag);
    }

    public void handleTagClose(final String tag) throws IOException {
        writeToUnderlyingSink(tag);
    }

    public void handlePseudoTagRestart(final char[] stuff) throws IOException {
        writeToUnderlyingSink(stuff, 0, stuff.length);
    }

    public void handleText(final int character) throws IOException {
        writeToUnderlyingSink(character);
    }

    public void handleText(final String text) throws IOException {
        writeToUnderlyingSink(text);
    }

    public final void write(final int original) throws IOException {
        boolean tagEnd = false;
        boolean writeChar = false;
        if (!this.isWithinComment) {
            if (original == TAG_START) {
                if (this.currentTag.size() > 0) {
                    handlePseudoTagRestart(this.currentTag.toCharArray());
                }
                this.currentTag.reset();
                LAST_FEW[0] = 0;
                LAST_FEW[1] = 0;
                LAST_FEW[2] = 0;
                LAST_FEW[3] = 0;
                this.isWithinTag = true;
            } else if (original == TAG_END) {
                tagEnd = true;
            }
        }
        if (this.isWithinTag) {
            LAST_FEW[0] = LAST_FEW[1];
            LAST_FEW[1] = LAST_FEW[2];
            LAST_FEW[2] = LAST_FEW[3];
            LAST_FEW[3] = original;
            this.currentTag.write(original);
            if (this.isWithinComment) {
                if (original == TAG_END) {
                    if (LAST_FEW[1] == '-' && LAST_FEW[2] == '-' && LAST_FEW[3] == '>') {
                        this.isWithinComment = false;
                        this.isWithinTag = false;
                        tagEnd = true;
                    }
                }
            } else if (LAST_FEW[1] == '!' && LAST_FEW[2] == '-' && LAST_FEW[3] == '-' && LAST_FEW[0] == '<') {
                this.isWithinComment = true;
            }
        } else writeChar = true;
        if (tagEnd) {
            final String tag = this.currentTag.toString().trim();
            final boolean closingTag = tag.length() > 1 && tag.charAt(1) == SLASH;
            if (closingTag) handleTagClose(tag); else if (tag.length() > 0) handleTag(tag);
            this.isWithinTag = false;
            this.currentTag.reset();
            LAST_FEW[0] = 0;
            LAST_FEW[1] = 0;
            LAST_FEW[2] = 0;
            LAST_FEW[3] = 0;
        }
        if (writeChar) handleText(original);
    }

    private final void handleNonTagRelevantContentChunk(final char[] charsWithoutAnyTagRelevantChars, final int startPosInclusive, final int endPosExclusive) throws IOException {
        final int count = endPosExclusive - startPosInclusive;
        if (this.isWithinTag) {
            if (count >= 4) {
                LAST_FEW[0] = charsWithoutAnyTagRelevantChars[endPosExclusive - 4];
                LAST_FEW[1] = charsWithoutAnyTagRelevantChars[endPosExclusive - 3];
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars[endPosExclusive - 2];
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars[endPosExclusive - 1];
            } else if (count == 3) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = charsWithoutAnyTagRelevantChars[endPosExclusive - 3];
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars[endPosExclusive - 2];
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars[endPosExclusive - 1];
            } else if (count == 2) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = LAST_FEW[2];
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars[endPosExclusive - 2];
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars[endPosExclusive - 1];
            } else if (count == 1) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = LAST_FEW[2];
                LAST_FEW[2] = LAST_FEW[3];
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars[endPosExclusive - 1];
            }
            this.currentTag.write(charsWithoutAnyTagRelevantChars, startPosInclusive, count);
        } else handleText(new String(charsWithoutAnyTagRelevantChars, startPosInclusive, count));
    }

    private final void handleNonTagRelevantContentChunk(final String charsWithoutAnyTagRelevantChars, final int startPosInclusive, final int endPosExclusive) throws IOException {
        final int count = endPosExclusive - startPosInclusive;
        if (this.isWithinTag) {
            if (count >= 4) {
                LAST_FEW[0] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 4);
                LAST_FEW[1] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 3);
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 2);
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 1);
            } else if (count == 3) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 3);
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 2);
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 1);
            } else if (count == 2) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = LAST_FEW[2];
                LAST_FEW[2] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 2);
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 1);
            } else if (count == 1) {
                LAST_FEW[0] = LAST_FEW[1];
                LAST_FEW[1] = LAST_FEW[2];
                LAST_FEW[2] = LAST_FEW[3];
                LAST_FEW[3] = charsWithoutAnyTagRelevantChars.charAt(endPosExclusive - 1);
            }
            this.currentTag.write(charsWithoutAnyTagRelevantChars, startPosInclusive, count);
        } else handleText(charsWithoutAnyTagRelevantChars.substring(startPosInclusive, endPosExclusive));
    }

    public final void write(char[] cbuf, int off, int len) throws IOException {
        final int end = off + len;
        if (this.useTunedBlockParser) {
            int pos = off;
            for (int i = off; i < end; i++) {
                if (cbuf[i] == '<' || cbuf[i] == '>' || cbuf[i] == '-') {
                    if (i > pos) handleNonTagRelevantContentChunk(cbuf, pos, i);
                    write(cbuf[i]);
                    pos = i + 1;
                }
            }
            if (pos < end) {
                handleNonTagRelevantContentChunk(cbuf, pos, end);
            }
        } else {
            for (int i = off; i < end; i++) {
                write(cbuf[i]);
            }
        }
    }

    public final void write(String str, int off, int len) throws IOException {
        final int end = off + len;
        if (this.useTunedBlockParser) {
            int pos = off;
            for (int i = off; i < end; i++) {
                if (str.charAt(i) == '<' || str.charAt(i) == '>' || str.charAt(i) == '-') {
                    if (i > pos) handleNonTagRelevantContentChunk(str, pos, i);
                    write(str.charAt(i));
                    pos = i + 1;
                }
            }
            if (pos < end) {
                handleNonTagRelevantContentChunk(str, pos, end);
            }
        } else {
            for (int i = off; i < end; i++) {
                write(str.charAt(i));
            }
        }
    }
}
