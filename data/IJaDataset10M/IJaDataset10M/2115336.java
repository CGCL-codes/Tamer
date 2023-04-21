package org.openemed.LQS;

/**
 * Translation Library
 */
interface TranslationLibrary {

    QualifiedCode str_to_qualified_code(String qualified_name_str) throws InvalidQualifiedName;

    String qualified_code_to_name_str(QualifiedCode qualified_code);
}
