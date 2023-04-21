package org.deft.repository.ast.decoration;

import org.deft.repository.ast.annotation.Ident;
import org.deft.repository.ast.annotation.TemplatesBasic;

/**
 * Gives access to the identifier of all standard templates.
 */
public class Templates extends TemplatesBasic {

    public static final Ident BLOCK = new Ident("block", "blockTemplate");

    public static final Ident CSFORMAT = new Ident("csformat", "csFormatTemplate");

    public static final Ident LINE = new Ident("line", "lineTemplate");

    public static final Ident NESTEDBLOCK = new Ident("nestedblock", "nestedBlockTemplate");

    public static final Ident SELECTED = new Ident("selected", "selectedTemplate2");

    public static final Ident SIMPLEBLOCK = new Ident("simpleblock", "simpleBlockTemplate");

    public static final Ident SOURCECODEROOT = new Ident("sourcecoderoot", "sourceCodeRootTemplate");

    public static final Ident TOKENTYPE = new Ident("tokentype", "tokenTypeTemplate");

    public static final Ident REPLACE = new Ident("replace", "replaceTemplate");
}
