package ideah.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ideah.lexer.HaskellLexer;
import ideah.lexer.HaskellTokenTypes;
import ideah.parser.HaskellFile;

public final class HaskellIndexPatternBuilder implements IndexPatternBuilder {

    public Lexer getIndexingLexer(PsiFile file) {
        if (file instanceof HaskellFile) {
            return new HaskellLexer();
        }
        return null;
    }

    public TokenSet getCommentTokenSet(PsiFile file) {
        return HaskellTokenTypes.COMMENTS;
    }

    public int getCommentStartDelta(IElementType tokenType) {
        return 2;
    }

    public int getCommentEndDelta(IElementType tokenType) {
        return tokenType == HaskellTokenTypes.COMMENT ? 0 : 2;
    }
}
