package org.dbmaintain.script.parser.impl;

import java.util.Properties;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class InformixScriptParserFactory extends DefaultScriptParserFactory {

    public InformixScriptParserFactory(boolean backSlashEscapingEnabled, Properties scriptParameters) {
        super(backSlashEscapingEnabled, scriptParameters);
    }

    @Override
    protected boolean isCurlyBraceBlockCommentSupported() {
        return true;
    }
}
