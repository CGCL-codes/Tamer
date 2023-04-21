package org.nakedobjects.nos.client.cli.controller;

import junit.framework.TestCase;
import org.nakedobjects.nos.client.cli.Command;

public class QuotedCommandTest extends TestCase {

    public void testCommandName() throws Exception {
        Command command = new QuotedCommand("  DO ");
        assertEquals("do", command.getName());
        command = new QuotedCommand("DO with param");
        assertEquals("do", command.getName());
    }

    public void testNoEntry() throws Exception {
        Command command = new QuotedCommand("  ");
        assertEquals("", command.getName());
        assertEquals(false, command.hasParameters());
        assertEquals(0, command.getNumberOfParameters());
    }

    public void testParamtersDelimitedBySpaces() throws Exception {
        Command command = new QuotedCommand("  DO WITH  3 parameters ");
        assertEquals("do", command.getName());
        assertEquals(3, command.getNumberOfParameters());
        assertEquals("WITH", command.getParameter(0));
        assertEquals("with", command.getParameterAsLowerCase(0));
        assertEquals("3", command.getParameter(1));
        assertEquals("parameters", command.getParameter(2));
    }

    public void testParamtersDelimitedByQuotes() throws Exception {
        Command command = new QuotedCommand("  DO \"WITH 3\"  \"parameters in\"  quotes");
        assertEquals("do", command.getName());
        assertEquals(3, command.getNumberOfParameters());
        assertEquals("WITH 3", command.getParameter(0));
        assertEquals("with 3", command.getParameterAsLowerCase(0));
        assertEquals("parameters in", command.getParameter(1));
        assertEquals("quotes", command.getParameter(2));
    }

    public void testParamtersDelimitedByQuotesPreservesSpaces() throws Exception {
        Command command = new QuotedCommand("  DO    \"one two  three   spaces\"");
        assertEquals("do", command.getName());
        assertEquals(1, command.getNumberOfParameters());
        assertEquals("one two  three   spaces", command.getParameter(0));
    }
}
