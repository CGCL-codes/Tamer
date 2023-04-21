package org.wijiscommons.cdcl.rulesheet.compiler.syntacticanalyzer;

import java.util.List;
import org.wijiscommons.cdcl.rulesheet.compiler.RuleSheetSourceFile;
import org.wijiscommons.cdcl.rulesheet.language.Alias;
import org.wijiscommons.languageprocessors.CompilerException;

/**
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy
 *
 */
public interface AliasParser {

    public List<Alias> parseAliases(RuleSheetSourceFile ruleSheetSourceFile) throws CompilerException;
}
