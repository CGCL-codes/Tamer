package org.deved.antlride.stringtemplate.internal.ui.text.partitions;

import org.deved.antlride.stringtemplate.core.StringTemplateConstants;
import org.eclipse.jface.text.IDocument;

public interface StringTemplatePartitions extends StringTemplateConstants {

    /**
	 * Partitions
	 */
    String PARTITION_ID = "__string_template_partitioning__";

    String COMMENT = PARTITION_ID + "comment";

    String STG_MULTI_LINE_COMMENT = PARTITION_ID + "stg_multi_line_comment";

    String STG_SINGLE_LINE_COMMENT = PARTITION_ID + "stg_single_line_comment";

    String STRING = PARTITION_ID + "string";

    String BUILD_IN_STRING = PARTITION_ID + "build_in_string";

    String MAP = PARTITION_ID + "map";

    String TEMPLATE_BODY = PARTITION_ID + "template_body";

    String TEMPLATE = PARTITION_ID + "template";

    String DOUBLE_ANGLE_BRACKETS = PARTITION_ID + "double_angle_brackets";

    String TEMPLATE_REGION = PARTITION_ID + "template_region";

    String ESPECIAL_CHARACTERS = PARTITION_ID + "especial_characters";

    String[] PARTITION_TYPES = { TEMPLATE, ESPECIAL_CHARACTERS, MAP, TEMPLATE_REGION, DOUBLE_ANGLE_BRACKETS, TEMPLATE_BODY, BUILD_IN_STRING, STRING, STG_SINGLE_LINE_COMMENT, STG_MULTI_LINE_COMMENT, COMMENT, IDocument.DEFAULT_CONTENT_TYPE };

    String[] LEGAL_CONTENT_TYPES = { TEMPLATE, ESPECIAL_CHARACTERS, MAP, TEMPLATE_REGION, DOUBLE_ANGLE_BRACKETS, TEMPLATE_BODY, BUILD_IN_STRING, STRING, STG_SINGLE_LINE_COMMENT, STG_MULTI_LINE_COMMENT, COMMENT };
}
