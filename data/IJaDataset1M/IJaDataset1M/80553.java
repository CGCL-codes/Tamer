package org.dbmaintain.script.parser;

import java.io.Reader;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public interface ScriptParserFactory {

    ScriptParser createScriptParser(Reader scriptReader);
}
