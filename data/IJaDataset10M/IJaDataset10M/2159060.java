package org.wijiscommons.cdcl.rules;

import java.io.InputStream;

/**
 * TODO: Add Java Doc
 *
 * @author Pattabi Doraiswamy (http://pattabidoraiswamy.com)
 * @since Jan 19, 2009
 */
public interface RuleSheet {

    public InputStream getRuleSheet();

    public RuleSheetMetaData gtRuleSheetMetaData();
}
