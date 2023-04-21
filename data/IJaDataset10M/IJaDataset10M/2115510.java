package org.omg.TerminologyServices;

/**
 * Interface definition: TranslationLibrary.
 * 
 * @author OpenORB Compiler
 */
public interface TranslationLibraryOperations {

    /**
     * Operation str_to_qualified_code
     */
    public org.omg.TerminologyServices.QualifiedCode str_to_qualified_code(String qualified_name_str) throws org.omg.TerminologyServices.TranslationLibraryPackage.InvalidQualifiedName;

    /**
     * Operation qualified_code_to_name_str
     */
    public String qualified_code_to_name_str(org.omg.TerminologyServices.QualifiedCode qualified_code);
}
