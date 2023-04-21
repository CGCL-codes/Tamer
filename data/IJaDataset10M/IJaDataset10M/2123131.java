package at.ssw.coco.lib.model.scanners;

/**
 * Defines partitions for the atg file.
 *
 * @author Andreas Woess <andwoe@users.sf.net>
 * @author Andreas Greilinger <Andreas.Greilinger@gmx.net>
 * @author Konstantin Bina <Konstantin.Bina@gmx.at>
 */
public class ATGPartitions {

    /** The identifier of the partitioning. */
    public static final String PARTITIONING = "__coco_partitioning";

    /** The identifier of the default partition content type. */
    public static final String DEFAULT = "__coco_default";

    /** The identifier of the single-line comment partition content type. */
    public static final String SINGLE_LINE_COMMENT = "__coco_singleline_comment";

    /** The identifier of the multi-line comment partition content type. */
    public static final String MULTI_LINE_COMMENT = "__coco_multiline_comment";

    /** The identifier of the string partition content type. */
    public static final String STRING = "__coco_string";

    /** The identifier of the character partition content type. */
    public static final String CHARACTER = "__coco_character";

    public static final String IMPORTS = "__coco_imports";

    public static final String PARSER_CODE = "__coco_parser_code";

    public static final String INLINE_CODE = "__coco_inline_code";

    public static final String COMPILER_KEYWORD = "__coco_compiler_keyword";

    public static final String COMPILER_IDENT = "__coco_compiler_ident";

    public static final String SINGLE_LINE_COMMENT_IMPORTS = "__coco_singleline_comment_imports";

    public static final String MULTI_LINE_COMMENT_IMPORTS = "__coco_multiline_comment_imports";

    public static final String STRING_IMPORTS = "__coco_string_imports";

    public static final String CHARACTER_IMPORTS = "__coco_character_imports";

    public static final String SINGLE_LINE_COMMENT_IGNORECASE_SEGMENT = "__coco_singleline_comment_ignorecase_segment";

    public static final String MULTI_LINE_COMMENT_IGNORECASE_SEGMENT = "__coco_multiline_comment_ignorecase_segment";

    public static final String STRING_IGNORECASE_SEGMENT = "__coco_string_ignorecase_segment";

    public static final String CHARACTER_IGNORECASE_SEGMENT = "__coco_character_ignorecase_segment";

    public static final String SINGLE_LINE_COMMENT_CHARACTERS_SEGMENT = "__coco_singleline_comment_characters_segment";

    public static final String MULTI_LINE_COMMENT_CHARACTERS_SEGMENT = "__coco_multiline_comment_characters_segment";

    public static final String STRING_CHARACTERS_SEGMENT = "__coco_string_characters_segment";

    public static final String CHARACTER_CHARACTERS_SEGMENT = "__coco_character_characters_segment";

    public static final String SINGLE_LINE_COMMENT_TOKENS_SEGMENT = "__coco_singleline_comment_tokens_segment";

    public static final String MULTI_LINE_COMMENT_TOKENS_SEGMENT = "__coco_multiline_comment_tokens_segment";

    public static final String STRING_TOKENS_SEGMENT = "__coco_string_tokens_segment";

    public static final String CHARACTER_TOKENS_SEGMENT = "__coco_character_tokens_segment";

    public static final String SINGLE_LINE_COMMENT_PRAGMAS_SEGMENT = "__coco_singleline_comment_pragmas_comment";

    public static final String MULTI_LINE_COMMENT_PRAGMAS_SEGMENT = "__coco_multiline_comment_pragmas_comment";

    public static final String STRING_PRAGMAS_SEGMENT = "__coco_string_pragmas_comment";

    public static final String CHARACTER_PRAGMAS_SEGMENT = "__coco_character_pragmas_comment";

    public static final String SINGLE_LINE_COMMENT_COMMENTS_SEGMENT = "__coco_singleline_comment_comments_segment";

    public static final String MULTI_LINE_COMMENT_COMMENTS_SEGMENT = "__coco_multiline_comment_comments_segment";

    public static final String STRING_COMMENTS_SEGMENT = "__coco_string_comments_segment";

    public static final String CHARACTER_COMMENTS_SEGMENT = "__coco_character_comments_segment";

    public static final String SINGLE_LINE_COMMENT_IGNORE_SEGMENT = "__coco_singleline_comment_ignore_segment";

    public static final String MULTI_LINE_COMMENT_IGNORE_SEGMENT = "__coco_multiline_comment_ignore_segment";

    public static final String STRING_IGNORE_SEGMENT = "__coco_string_ignore_segment";

    public static final String CHARACTER_IGNORE_SEGMENT = "__coco_character_ignore_segment";

    public static final String SINGLE_LINE_COMMENT_PRODUCTIONS_SEGMENT = "__coco_singleline_comment_productions_segment";

    public static final String MULTI_LINE_COMMENT_PRODUCTIONS_SEGMENT = "__coco_multiline_comment_productions_segment";

    public static final String STRING_PRODUCTIONS_SEGMENT = "__coco_string_productions_segment";

    public static final String CHARACTER_PRODUCTIONS_SEGMENT = "__coco_character_productions_segment";

    public static final String SINGLE_LINE_COMMENT_PARSER_CODE = "__coco_singleline_comment_parser_code";

    public static final String MULTI_LINE_COMMENT_PARSER_CODE = "__coco_multiline_comment_parser_code";

    public static final String STRING_PARSER_CODE = "__coco_string_parser_code";

    public static final String CHARACTER_PARSER_CODE = "__coco_character_parser_code";

    public static final String SINGLE_LINE_COMMENT_INLINE_CODE = "__coco_singleline_comment_inline_code";

    public static final String MULTI_LINE_COMMENT_INLINE_CODE = "__coco_multiline_comment_inline_code";

    public static final String STRING_INLINE_CODE = "__coco_string_inline_code";

    public static final String CHARACTER_INLINE_CODE = "__coco_character_inline_code";

    public static final String SINGLE_LINE_COMMENT_PRAGMAS_INLINE_CODE = "__coco_singleline_comment_pragmas_inline_code";

    public static final String MULTI_LINE_COMMENT_PRAGMAS_INLINE_CODE = "__coco_multiline_comment_pragmas_inline_code";

    public static final String STRING_PRAGMAS_INLINE_CODE = "__coco_string_pragmas_inline_code";

    public static final String CHARACTER_PRAGMAS_INLINE_CODE = "__coco_character_pragmas_inline_code";

    public static final String SINGLE_LINE_COMMENT_PRODUCTIONS_INLINE_CODE = "__coco_singleline_comment_productions_inline_code";

    public static final String MULTI_LINE_COMMENT_PRODUCTIONS_INLINE_CODE = "__coco_multiline_comment_productions_inline_code";

    public static final String STRING_PRODUCTIONS_INLINE_CODE = "__coco_string_productions_inline_code";

    public static final String CHARACTER_PRODUCTIONS_INLINE_CODE = "__coco_character_productions_inline_code";

    public static final String SINGLE_LINE_COMMENT_COMPILER_IDENT = "__coco_singleline_comment_compiler_ident";

    public static final String MULTI_LINE_COMMENT_COMPILER_IDENT = "__coco_multiline_comment_compiler_ident";

    public static final String IGNORECASE_SEGMENT = "__coco_ignorecase_segment";

    public static final String CHARACTERS_SEGMENT = "__coco_characters_segment";

    public static final String TOKENS_SEGMENT = "__coco_tokens_segment";

    public static final String PRAGMAS_SEGMENT = "__coco_pragmas_segment";

    public static final String COMMENTS_SEGMENT = "__coco_comments_segment";

    public static final String IGNORE_SEGMENT = "__coco_ignore_segment";

    public static final String PRODUCTIONS_SEGMENT = "__coco_productions_segment";

    public static final String PRAGMAS_INLINE_CODE = "__coco_pragmas_inline_code";

    public static final String PRODUCTIONS_INLINE_CODE = "__coco_productions_inline_code";

    public static final String INLINE_CODE_START = "__coco_inline_code_start";

    public static final String INLINE_CODE_END = "__coco_inline_code_end";

    public static String[] getLegalContentTypes() {
        return LEGAL_CONTENT_TYPES;
    }

    private static String[] LEGAL_CONTENT_TYPES = { DEFAULT, SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, STRING, CHARACTER, IMPORTS, PARSER_CODE, INLINE_CODE, COMPILER_KEYWORD, COMPILER_IDENT, IGNORECASE_SEGMENT, CHARACTERS_SEGMENT, TOKENS_SEGMENT, PRAGMAS_SEGMENT, COMMENTS_SEGMENT, IGNORE_SEGMENT, PRODUCTIONS_SEGMENT, SINGLE_LINE_COMMENT_IMPORTS, MULTI_LINE_COMMENT_IMPORTS, STRING_IMPORTS, CHARACTER_IMPORTS, SINGLE_LINE_COMMENT_IGNORECASE_SEGMENT, MULTI_LINE_COMMENT_IGNORECASE_SEGMENT, STRING_IGNORECASE_SEGMENT, CHARACTER_IGNORECASE_SEGMENT, SINGLE_LINE_COMMENT_CHARACTERS_SEGMENT, MULTI_LINE_COMMENT_CHARACTERS_SEGMENT, STRING_CHARACTERS_SEGMENT, CHARACTER_CHARACTERS_SEGMENT, SINGLE_LINE_COMMENT_TOKENS_SEGMENT, MULTI_LINE_COMMENT_TOKENS_SEGMENT, STRING_TOKENS_SEGMENT, CHARACTER_TOKENS_SEGMENT, SINGLE_LINE_COMMENT_PRAGMAS_SEGMENT, MULTI_LINE_COMMENT_PRAGMAS_SEGMENT, STRING_PRAGMAS_SEGMENT, CHARACTER_PRAGMAS_SEGMENT, SINGLE_LINE_COMMENT_COMMENTS_SEGMENT, MULTI_LINE_COMMENT_COMMENTS_SEGMENT, STRING_COMMENTS_SEGMENT, CHARACTER_COMMENTS_SEGMENT, SINGLE_LINE_COMMENT_IGNORE_SEGMENT, MULTI_LINE_COMMENT_IGNORE_SEGMENT, STRING_IGNORE_SEGMENT, CHARACTER_IGNORE_SEGMENT, SINGLE_LINE_COMMENT_PRODUCTIONS_SEGMENT, MULTI_LINE_COMMENT_PRODUCTIONS_SEGMENT, STRING_PRODUCTIONS_SEGMENT, CHARACTER_PRODUCTIONS_SEGMENT, SINGLE_LINE_COMMENT_PARSER_CODE, MULTI_LINE_COMMENT_PARSER_CODE, STRING_PARSER_CODE, CHARACTER_PARSER_CODE, SINGLE_LINE_COMMENT_INLINE_CODE, MULTI_LINE_COMMENT_INLINE_CODE, STRING_INLINE_CODE, CHARACTER_INLINE_CODE, SINGLE_LINE_COMMENT_PRAGMAS_INLINE_CODE, MULTI_LINE_COMMENT_PRAGMAS_INLINE_CODE, STRING_PRAGMAS_INLINE_CODE, CHARACTER_PRAGMAS_INLINE_CODE, SINGLE_LINE_COMMENT_PRODUCTIONS_INLINE_CODE, MULTI_LINE_COMMENT_PRODUCTIONS_INLINE_CODE, STRING_PRODUCTIONS_INLINE_CODE, CHARACTER_PRODUCTIONS_INLINE_CODE, SINGLE_LINE_COMMENT_COMPILER_IDENT, MULTI_LINE_COMMENT_COMPILER_IDENT, PRAGMAS_INLINE_CODE, PRODUCTIONS_INLINE_CODE, INLINE_CODE_START, INLINE_CODE_END };

    public static boolean isLegalContentType(String contentType) {
        for (String entry : LEGAL_CONTENT_TYPES) {
            if (entry.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
