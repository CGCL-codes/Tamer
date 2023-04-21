package net.infopeers.restrant.engine;

import net.infopeers.restrant.engine.parser.PatternParser;

public interface ParserHolder {

    void addParser(PatternParser parser);
}
