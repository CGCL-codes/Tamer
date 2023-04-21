package foldTools;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.buffer.FoldHandler;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.syntax.DefaultTokenHandler;
import org.gjt.sp.jedit.syntax.Token;

public class CommentFoldHandler extends FoldHandler {

    public CommentFoldHandler() {
        super("comment");
    }

    private boolean isCommentLine(JEditBuffer buffer, int line) {
        DefaultTokenHandler tokenHandler = new DefaultTokenHandler();
        buffer.markTokens(line, tokenHandler);
        Token token = tokenHandler.getTokens();
        int lineStart = buffer.getLineStartOffset(line);
        int start = lineStart;
        boolean tokenExists = false;
        while (token.id != Token.END) {
            int next = start + token.length;
            switch(token.id) {
                case Token.NULL:
                case Token.COMMENT1:
                case Token.COMMENT2:
                case Token.COMMENT3:
                case Token.COMMENT4:
                    tokenExists = true;
                    break;
                default:
                    return false;
            }
            start = next;
            token = token.next;
        }
        return tokenExists;
    }

    @Override
    public int getFoldLevel(JEditBuffer buffer, int lineIndex, Segment seg) {
        if ((lineIndex == 0) || (!isCommentLine(buffer, lineIndex)) || (!isCommentLine(buffer, lineIndex - 1))) return 0;
        return 1;
    }
}
