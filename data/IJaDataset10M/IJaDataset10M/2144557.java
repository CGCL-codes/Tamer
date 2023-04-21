package org.eclipse.jdt.internal.compiler.parser;

import org.eclipse.jdt.core.compiler.CharOperation;

/**
 * Javadoc tag constants.
 *
 * @since 3.2
 */
public interface JavadocTagConstants {

    public static final char[] TAG_DEPRECATED = "deprecated".toCharArray();

    public static final char[] TAG_PARAM = "param".toCharArray();

    public static final char[] TAG_RETURN = "return".toCharArray();

    public static final char[] TAG_THROWS = "throws".toCharArray();

    public static final char[] TAG_EXCEPTION = "exception".toCharArray();

    public static final char[] TAG_SEE = "see".toCharArray();

    public static final char[] TAG_LINK = "link".toCharArray();

    public static final char[] TAG_LINKPLAIN = "linkplain".toCharArray();

    public static final char[] TAG_INHERITDOC = "inheritDoc".toCharArray();

    public static final char[] TAG_VALUE = "value".toCharArray();

    public static final char[] TAG_AUTHOR = "author".toCharArray();

    public static final char[] TAG_CODE = "code".toCharArray();

    public static final char[] TAG_DOC_ROOT = "docRoot".toCharArray();

    public static final char[] TAG_LITERAL = "literal".toCharArray();

    public static final char[] TAG_SERIAL = "serial".toCharArray();

    public static final char[] TAG_SERIAL_DATA = "serialData".toCharArray();

    public static final char[] TAG_SERIAL_FIELD = "serialField".toCharArray();

    public static final char[] TAG_SINCE = "since".toCharArray();

    public static final char[] TAG_VERSION = "version".toCharArray();

    public static final char[] TAG_CATEGORY = "category".toCharArray();

    public static final int TAG_DEPRECATED_LENGTH = TAG_DEPRECATED.length;

    public static final int TAG_PARAM_LENGTH = TAG_PARAM.length;

    public static final int TAG_RETURN_LENGTH = TAG_RETURN.length;

    public static final int TAG_THROWS_LENGTH = TAG_THROWS.length;

    public static final int TAG_EXCEPTION_LENGTH = TAG_EXCEPTION.length;

    public static final int TAG_SEE_LENGTH = TAG_SEE.length;

    public static final int TAG_LINK_LENGTH = TAG_LINK.length;

    public static final int TAG_LINKPLAIN_LENGTH = TAG_LINKPLAIN.length;

    public static final int TAG_INHERITDOC_LENGTH = TAG_INHERITDOC.length;

    public static final int TAG_VALUE_LENGTH = TAG_VALUE.length;

    public static final int TAG_CATEGORY_LENGTH = TAG_CATEGORY.length;

    public static final int TAG_AUTHOR_LENGTH = TAG_AUTHOR.length;

    public static final int TAG_SERIAL_LENGTH = TAG_SERIAL.length;

    public static final int TAG_SERIAL_DATA_LENGTH = TAG_SERIAL_DATA.length;

    public static final int TAG_SERIAL_FIELD_LENGTH = TAG_SERIAL_FIELD.length;

    public static final int TAG_SINCE_LENGTH = TAG_SINCE.length;

    public static final int TAG_VERSION_LENGTH = TAG_VERSION.length;

    public static final int TAG_CODE_LENGTH = TAG_CODE.length;

    public static final int TAG_LITERAL_LENGTH = TAG_LITERAL.length;

    public static final int TAG_DOC_ROOT_LENGTH = TAG_DOC_ROOT.length;

    public static final int NO_TAG_VALUE = 0;

    public static final int TAG_DEPRECATED_VALUE = 1;

    public static final int TAG_PARAM_VALUE = 2;

    public static final int TAG_RETURN_VALUE = 3;

    public static final int TAG_THROWS_VALUE = 4;

    public static final int TAG_EXCEPTION_VALUE = 5;

    public static final int TAG_SEE_VALUE = 6;

    public static final int TAG_LINK_VALUE = 7;

    public static final int TAG_LINKPLAIN_VALUE = 8;

    public static final int TAG_INHERITDOC_VALUE = 9;

    public static final int TAG_VALUE_VALUE = 10;

    public static final int TAG_CATEGORY_VALUE = 11;

    public static final int TAG_AUTHOR_VALUE = 12;

    public static final int TAG_SERIAL_VALUE = 13;

    public static final int TAG_SERIAL_DATA_VALUE = 14;

    public static final int TAG_SERIAL_FIELD_VALUE = 15;

    public static final int TAG_SINCE_VALUE = 16;

    public static final int TAG_VERSION_VALUE = 17;

    public static final int TAG_CODE_VALUE = 18;

    public static final int TAG_LITERAL_VALUE = 19;

    public static final int TAG_DOC_ROOT_VALUE = 20;

    public static final int TAG_OTHERS_VALUE = 100;

    public static final char[][] TAG_NAMES = { CharOperation.NO_CHAR, TAG_DEPRECATED, TAG_PARAM, TAG_RETURN, TAG_THROWS, TAG_EXCEPTION, TAG_SEE, TAG_LINK, TAG_LINKPLAIN, TAG_INHERITDOC, TAG_VALUE, TAG_CATEGORY, TAG_AUTHOR, TAG_SERIAL, TAG_SERIAL_DATA, TAG_SERIAL_FIELD, TAG_SINCE, TAG_VERSION, TAG_CODE, TAG_LITERAL, TAG_DOC_ROOT };

    public static final int ORDERED_TAGS_NUMBER = 3;

    public static final int PARAM_TAG_EXPECTED_ORDER = 0;

    public static final int THROWS_TAG_EXPECTED_ORDER = 1;

    public static final int SEE_TAG_EXPECTED_ORDER = 2;

    public static final int BLOCK_IDX = 0;

    public static final int INLINE_IDX = 1;

    public static final char[] HREF_TAG = { 'h', 'r', 'e', 'f' };

    public static final char[][][] BLOCK_TAGS = { { TAG_AUTHOR, TAG_DEPRECATED, TAG_EXCEPTION, TAG_PARAM, TAG_RETURN, TAG_SEE, TAG_VERSION, TAG_CATEGORY }, { TAG_SINCE }, { TAG_SERIAL, TAG_SERIAL_DATA, TAG_SERIAL_FIELD, TAG_THROWS }, {}, {}, {}, {}, {} };

    public static final char[][][] INLINE_TAGS = { {}, {}, { TAG_LINK }, { TAG_DOC_ROOT }, { TAG_INHERITDOC, TAG_LINKPLAIN, TAG_VALUE }, { TAG_CODE, TAG_LITERAL }, {}, {} };

    public static final int INLINE_TAGS_LENGTH = INLINE_TAGS.length;

    public static final int BLOCK_TAGS_LENGTH = BLOCK_TAGS.length;

    public static final int ALL_TAGS_LENGTH = BLOCK_TAGS_LENGTH + INLINE_TAGS_LENGTH;

    public static final short TAG_TYPE_NONE = 0;

    public static final short TAG_TYPE_INLINE = 1;

    public static final short TAG_TYPE_BLOCK = 2;

    public static final short[] JAVADOC_TAG_TYPE = { TAG_TYPE_NONE, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_INLINE, TAG_TYPE_INLINE, TAG_TYPE_INLINE, TAG_TYPE_INLINE, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_BLOCK, TAG_TYPE_INLINE, TAG_TYPE_INLINE, TAG_TYPE_INLINE };

    public static final char[][] PACKAGE_TAGS = { TAG_SEE, TAG_SINCE, TAG_SERIAL, TAG_AUTHOR, TAG_VERSION, TAG_CATEGORY, TAG_LINK, TAG_LINKPLAIN, TAG_DOC_ROOT, TAG_VALUE };

    public static final char[][] COMPILATION_UNIT_TAGS = {};

    public static final char[][] CLASS_TAGS = { TAG_SEE, TAG_SINCE, TAG_DEPRECATED, TAG_SERIAL, TAG_AUTHOR, TAG_VERSION, TAG_PARAM, TAG_CATEGORY, TAG_LINK, TAG_LINKPLAIN, TAG_DOC_ROOT, TAG_VALUE, TAG_CODE, TAG_LITERAL };

    public static final char[][] FIELD_TAGS = { TAG_SEE, TAG_SINCE, TAG_DEPRECATED, TAG_SERIAL, TAG_SERIAL_FIELD, TAG_CATEGORY, TAG_LINK, TAG_LINKPLAIN, TAG_DOC_ROOT, TAG_VALUE, TAG_CODE, TAG_LITERAL };

    public static final char[][] METHOD_TAGS = { TAG_SEE, TAG_SINCE, TAG_DEPRECATED, TAG_PARAM, TAG_RETURN, TAG_THROWS, TAG_EXCEPTION, TAG_SERIAL_DATA, TAG_CATEGORY, TAG_LINK, TAG_LINKPLAIN, TAG_INHERITDOC, TAG_DOC_ROOT, TAG_VALUE, TAG_CODE, TAG_LITERAL };
}
