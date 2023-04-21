package com.gregor.rrd.graph.parser.jrobin;

public class Command {

    private String m_startsWithMatch;

    private CommandProcessor m_processor;

    public Command(String startsWithMatch, CommandProcessor processor) {
        m_startsWithMatch = startsWithMatch;
        m_processor = processor;
    }

    public String getStartsWithMatch() {
        return m_startsWithMatch;
    }

    public CommandProcessor getProcessor() {
        return m_processor;
    }
}
