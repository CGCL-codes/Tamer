package net.sf.parser4j.kernelgenerator.entity.grammarnode.impl;

import net.sf.parser4j.parser.entity.data.NonTerminalMap;

/**
 * property setter for spring bean grammar node
 * 
 * @author luc peuvrier
 * 
 */
public interface IGrammarNodeSpringBean {

    /**
	 * 
	 * @param nonTerminalMap
	 *            npn terminal map between name and identifier
	 */
    void setNonTerminalMap(NonTerminalMap nonTerminalMap);

    /**
	 * 
	 * @param nonTerminal
	 *            non terminal name for this spring bean grammar node
	 */
    void setNonTerminalName(String nonTerminalName);
}
