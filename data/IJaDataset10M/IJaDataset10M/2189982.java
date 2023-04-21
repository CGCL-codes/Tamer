package net.beeger.osmorc.manifest.lang;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import net.beeger.osmorc.manifest.lang.psi.ManifestClause;
import net.beeger.osmorc.manifest.lang.psi.ManifestDirective;
import net.beeger.osmorc.manifest.lang.psi.ManifestFile;
import net.beeger.osmorc.manifest.lang.psi.ManifestHeader;
import net.beeger.osmorc.manifest.lang.psi.ManifestHeaderName;
import net.beeger.osmorc.manifest.lang.psi.ManifestHeaderValueImpl;
import net.beeger.osmorc.manifest.lang.psi.ManifestAttribute;
import org.jetbrains.annotations.NotNull;

class ManifestParserDefinition implements ParserDefinition {

    @NotNull
    public Lexer createLexer(Project project) {
        return new ManifestLexer();
    }

    public PsiParser createParser(Project project) {
        return new ManifestParser();
    }

    public IFileElementType getFileNodeType() {
        return ManifestElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(TokenType.WHITE_SPACE);
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        final IElementType type = node.getElementType();
        if (type == ManifestElementTypes.HEADER) {
            return new ManifestHeader(node);
        } else if (type == ManifestElementTypes.HEADER_NAME) {
            return new ManifestHeaderName(node);
        } else if (type == ManifestElementTypes.HEADER_VALUE) {
            return new ManifestHeaderValueImpl(node);
        } else if (type == ManifestElementTypes.CLAUSE) {
            return new ManifestClause(node);
        } else if (type == ManifestElementTypes.DIRECTIVE) {
            return new ManifestDirective(node);
        } else if (type == ManifestElementTypes.ATTRIBUTE) {
            return new ManifestAttribute(node);
        }
        return new ASTWrapperPsiElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ManifestFile(viewProvider);
    }

    @SuppressWarnings({ "MethodNameWithMistakes" })
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        Lexer lexer = createLexer(left.getPsi().getProject());
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer, 0);
    }
}
