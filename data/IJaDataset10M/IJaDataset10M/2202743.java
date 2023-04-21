package org.wijiscommons.cdcl.rulesheet.compiler;

import java.io.File;
import java.io.InputStream;
import org.wijiscommons.cdcl.rulesheet.language.FundamentalForm;
import org.wijiscommons.languageprocessors.CompilerDriver;
import org.wijiscommons.languageprocessors.CompilerException;

/**
 * 
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy
 */
public interface RulesAuthoringFormCompilerDriver extends CompilerDriver {

    public void validate(InputStream cdclRuleSheetInputStream) throws ValidationException;

    public void validate(File cdclRuleSheetSourceFile) throws ValidationException;

    public FundamentalForm compile(InputStream cdclRuleSheetInputStream) throws CompilerException;
}
