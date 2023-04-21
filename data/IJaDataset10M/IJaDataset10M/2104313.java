package com.memoire.jedit;

import javax.swing.text.Segment;

/**
 * Patch/diff token marker.
 *
 * @author Slava Pestov
 * @version $Id: JEditPatchTokenMarker.java,v 1.2 2006-09-19 14:35:03 deniger Exp $
 */
public class JEditPatchTokenMarker extends JEditTokenMarker {

    public byte markTokensImpl(byte token, Segment line, int lineIndex) {
        if (line.count == 0) return JEditToken.NULL;
        switch(line.array[line.offset]) {
            case '+':
            case '>':
                addToken(line.count, JEditToken.KEYWORD1);
                break;
            case '-':
            case '<':
                addToken(line.count, JEditToken.KEYWORD2);
                break;
            case '@':
            case '*':
                addToken(line.count, JEditToken.KEYWORD3);
                break;
            default:
                addToken(line.count, JEditToken.NULL);
                break;
        }
        return JEditToken.NULL;
    }

    public boolean supportsMultilineTokens() {
        return false;
    }
}
