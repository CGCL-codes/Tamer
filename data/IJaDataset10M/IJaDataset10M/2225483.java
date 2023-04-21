package Mypackage.Lexer;

import com.google.common.collect.Maps;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

/**
 * @author: traff
 */
public class MySyntaxHighlighter extends SyntaxHighlighterBase implements TestTokenTypes {

    @NonNls
    static final String COMMENT_ID = "BUILDOUT_COMMENT";

    @NonNls
    static final String TEXT_ID = "BUILDOUT_TEXT";

    @NonNls
    static final String BRACKETS_ID = "BUILDOUT_BRACKETS";

    public static final TextAttributesKey BUILDOUT_SECTION_NAME = TextAttributesKey.createTextAttributesKey("BUILDOUT.SECTION_NAME", SyntaxHighlighterColors.NUMBER.getDefaultAttributes().clone());

    public static final TextAttributesKey BUILDOUT_KEY = TextAttributesKey.createTextAttributesKey("BUILDOUT.KEY", SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());

    public static final TextAttributesKey BUILDOUT_VALUE = TextAttributesKey.createTextAttributesKey("BUILDOUT.VALUE", SyntaxHighlighterColors.STRING.getDefaultAttributes());

    public static final TextAttributesKey BUILDOUT_COMMENT = TextAttributesKey.createTextAttributesKey("BUILDOUT.LINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes());

    public static final TextAttributesKey BUILDOUT_KEY_VALUE_SEPARATOR = TextAttributesKey.createTextAttributesKey("BUILDOUT.KEY_VALUE_SEPARATOR", SyntaxHighlighterColors.OPERATION_SIGN.getDefaultAttributes());

    public static final TextAttributesKey BUILDOUT_VALID_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("BUILDOUT.VALID_STRING_ESCAPE", SyntaxHighlighterColors.VALID_STRING_ESCAPE.getDefaultAttributes());

    public static final TextAttributesKey BUILDOUT_INVALID_STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("BUILDOUT.INVALID_STRING_ESCAPE", SyntaxHighlighterColors.INVALID_STRING_ESCAPE.getDefaultAttributes());

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = Maps.newHashMap();

    static {
        ATTRIBUTES.put(COMMENT, BUILDOUT_COMMENT);
        ATTRIBUTES.put(SECTION_NAME, BUILDOUT_SECTION_NAME);
        ATTRIBUTES.put(KEY_VALUE_SEPARATOR, BUILDOUT_KEY_VALUE_SEPARATOR);
        ATTRIBUTES.put(KEY_CHARACTERS, BUILDOUT_KEY);
        ATTRIBUTES.put(KEY_CHARACTERS, BUILDOUT_KEY);
        ATTRIBUTES.put(VALUE_CHARACTERS, BUILDOUT_VALUE);
        ATTRIBUTES.put(FOR, BUILDOUT_KEY);
        ATTRIBUTES.put(BLABLABLA, BUILDOUT_KEY);
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return SyntaxHighlighterBase.pack(ATTRIBUTES.get(tokenType));
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new LexerWrapper();
    }
}
