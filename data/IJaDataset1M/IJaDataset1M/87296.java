package com.jmonkey.universal.open.jedit;

import javax.swing.text.Segment;

public class EiffelTokenMarker extends TokenMarker {

    private static KeywordMap eiffelKeywords;

    private boolean cpp;

    private KeywordMap keywords;

    private int lastOffset;

    private int lastKeyword;

    public EiffelTokenMarker() {
        keywords = getKeywords();
    }

    private boolean doKeyword(Segment line, int i, char c) {
        int i1 = i + 1;
        boolean klassname = false;
        int len = i - lastKeyword;
        byte id = keywords.lookup(line, lastKeyword, len);
        if (id == 0) {
            klassname = true;
            for (int at = lastKeyword; at < lastKeyword + len; at++) {
                char ch = line.array[at];
                if (ch == '_' || Character.isUpperCase(ch)) continue;
                klassname = false;
                break;
            }
            if (klassname) id = 8;
        }
        if (id != 0) {
            if (lastKeyword != lastOffset) addToken(lastKeyword - lastOffset, (byte) 0);
            addToken(len, id);
            lastOffset = i;
        }
        lastKeyword = i1;
        return false;
    }

    public static KeywordMap getKeywords() {
        if (eiffelKeywords == null) {
            eiffelKeywords = new KeywordMap(true);
            eiffelKeywords.add("alias", (byte) 6);
            eiffelKeywords.add("all", (byte) 6);
            eiffelKeywords.add("and", (byte) 6);
            eiffelKeywords.add("as", (byte) 6);
            eiffelKeywords.add("check", (byte) 6);
            eiffelKeywords.add("class", (byte) 6);
            eiffelKeywords.add("creation", (byte) 6);
            eiffelKeywords.add("debug", (byte) 6);
            eiffelKeywords.add("deferred", (byte) 6);
            eiffelKeywords.add("do", (byte) 6);
            eiffelKeywords.add("else", (byte) 6);
            eiffelKeywords.add("elseif", (byte) 6);
            eiffelKeywords.add("end", (byte) 6);
            eiffelKeywords.add("ensure", (byte) 6);
            eiffelKeywords.add("expanded", (byte) 6);
            eiffelKeywords.add("export", (byte) 6);
            eiffelKeywords.add("external", (byte) 6);
            eiffelKeywords.add("feature", (byte) 6);
            eiffelKeywords.add("from", (byte) 6);
            eiffelKeywords.add("frozen", (byte) 6);
            eiffelKeywords.add("if", (byte) 6);
            eiffelKeywords.add("implies", (byte) 6);
            eiffelKeywords.add("indexing", (byte) 6);
            eiffelKeywords.add("infix", (byte) 6);
            eiffelKeywords.add("inherit", (byte) 6);
            eiffelKeywords.add("inspect", (byte) 6);
            eiffelKeywords.add("invariant", (byte) 6);
            eiffelKeywords.add("is", (byte) 6);
            eiffelKeywords.add("like", (byte) 6);
            eiffelKeywords.add("local", (byte) 6);
            eiffelKeywords.add("loop", (byte) 6);
            eiffelKeywords.add("not", (byte) 6);
            eiffelKeywords.add("obsolete", (byte) 6);
            eiffelKeywords.add("old", (byte) 6);
            eiffelKeywords.add("once", (byte) 6);
            eiffelKeywords.add("or", (byte) 6);
            eiffelKeywords.add("prefix", (byte) 6);
            eiffelKeywords.add("redefine", (byte) 6);
            eiffelKeywords.add("rename", (byte) 6);
            eiffelKeywords.add("require", (byte) 6);
            eiffelKeywords.add("rescue", (byte) 6);
            eiffelKeywords.add("retry", (byte) 6);
            eiffelKeywords.add("select", (byte) 6);
            eiffelKeywords.add("separate", (byte) 6);
            eiffelKeywords.add("then", (byte) 6);
            eiffelKeywords.add("undefine", (byte) 6);
            eiffelKeywords.add("until", (byte) 6);
            eiffelKeywords.add("variant", (byte) 6);
            eiffelKeywords.add("when", (byte) 6);
            eiffelKeywords.add("xor", (byte) 6);
            eiffelKeywords.add("current", (byte) 4);
            eiffelKeywords.add("false", (byte) 4);
            eiffelKeywords.add("precursor", (byte) 4);
            eiffelKeywords.add("result", (byte) 4);
            eiffelKeywords.add("strip", (byte) 4);
            eiffelKeywords.add("true", (byte) 4);
            eiffelKeywords.add("unique", (byte) 4);
            eiffelKeywords.add("void", (byte) 4);
        }
        return eiffelKeywords;
    }

    public byte markTokensImpl(byte token, Segment line, int lineIndex) {
        char array[] = line.array;
        int offset = line.offset;
        lastOffset = offset;
        lastKeyword = offset;
        int length = line.count + offset;
        boolean backslash = false;
        label0: for (int i = offset; i < length; i++) {
            int i1 = i + 1;
            char c = array[i];
            if (c == '%') {
                backslash ^= true;
                continue;
            }
            label1: switch(token) {
                case 0:
                    switch(c) {
                        case 34:
                            doKeyword(line, i, c);
                            if (backslash) {
                                backslash = false;
                            } else {
                                addToken(i - lastOffset, token);
                                token = 3;
                                lastOffset = lastKeyword = i;
                            }
                            break label1;
                        case 39:
                            doKeyword(line, i, c);
                            if (backslash) {
                                backslash = false;
                            } else {
                                addToken(i - lastOffset, token);
                                token = 4;
                                lastOffset = lastKeyword = i;
                            }
                            break label1;
                        case 58:
                            if (lastKeyword == offset) {
                                if (!doKeyword(line, i, c)) {
                                    backslash = false;
                                    addToken(i1 - lastOffset, (byte) 5);
                                    lastOffset = lastKeyword = i1;
                                }
                            } else if (!doKeyword(line, i, c)) ;
                            break label1;
                        case 45:
                            backslash = false;
                            doKeyword(line, i, c);
                            if (length - i <= 1) break label1;
                            switch(array[i1]) {
                                default:
                                    break label1;
                                case 45:
                                    addToken(i - lastOffset, token);
                                    addToken(length - i, (byte) 1);
                                    lastOffset = lastKeyword = length;
                                    break;
                            }
                            break;
                        default:
                            backslash = false;
                            if (!Character.isLetterOrDigit(c) && c != '_') doKeyword(line, i, c);
                            break label1;
                    }
                    break label0;
                case 1:
                case 2:
                    throw new RuntimeException("Wrong eiffel parser state");
                case 3:
                    if (backslash) {
                        backslash = false;
                        break;
                    }
                    if (c == '"') {
                        addToken(i1 - lastOffset, token);
                        token = 0;
                        lastOffset = lastKeyword = i1;
                    }
                    break;
                case 4:
                    if (backslash) {
                        backslash = false;
                        break;
                    }
                    if (c == '\'') {
                        addToken(i1 - lastOffset, (byte) 3);
                        token = 0;
                        lastOffset = lastKeyword = i1;
                    }
                    break;
                default:
                    throw new InternalError("Invalid state: " + token);
            }
        }
        if (token == 0) doKeyword(line, length, '\0');
        switch(token) {
            case 3:
            case 4:
                addToken(length - lastOffset, (byte) 10);
                token = 0;
                break;
            case 7:
                addToken(length - lastOffset, token);
                if (!backslash) token = 0;
            case 5:
            case 6:
            default:
                addToken(length - lastOffset, token);
                break;
        }
        return token;
    }
}
