package net.sourceforge.processdash.tool.diff;

public class SQLFilter extends AbstractLanguageFilter {

    private static final String[] COMMENT_STARTERS = { "--", "/*" };

    protected String[] getCommentStarters() {
        return COMMENT_STARTERS;
    }

    private static final String[] COMMENT_ENDERS = { "\n", "*/" };

    protected String[] getCommentEnders() {
        return COMMENT_ENDERS;
    }

    private static final String[] STRING_STARTERS = { "\"", "'" };

    protected String[] getStringStarters() {
        return STRING_STARTERS;
    }

    private static final char[] STRING_ESCAPES = { '\\', '\\' };

    protected char[] getStringEscapes() {
        return STRING_ESCAPES;
    }

    private static final String[] STRING_EMBEDS = { "\"\"", "''" };

    protected String[] getStringEmbeds() {
        return STRING_EMBEDS;
    }

    private static final String[] STRING_ENDERS = { "\"", "'" };

    protected String[] getStringEnders() {
        return STRING_ENDERS;
    }

    private static final String[] FILENAME_ENDINGS = { ".sql", ".ada", ".adb", ".ads" };

    protected String[] getDefaultFilenameEndings() {
        return FILENAME_ENDINGS;
    }
}
