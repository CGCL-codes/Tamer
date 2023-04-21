package main;

import junit.framework.Assert;
import org.junit.Test;

public class CommandLineParserTests {

    @Test
    public void testHelp() {
        final CommandLineParser commandLineParser = new CommandLineParser();
        commandLineParser.parse(new String[] { "-help" });
        Assert.assertTrue(commandLineParser.isSetted(CommandLineParser.HELP));
        commandLineParser.parse(new String[] { "-Help" });
        Assert.assertTrue(commandLineParser.isSetted(CommandLineParser.HELP));
        commandLineParser.parse(new String[] { "help" });
        Assert.assertTrue(commandLineParser.isSetted(CommandLineParser.HELP));
        commandLineParser.parse(new String[] { "--help" });
        Assert.assertTrue(commandLineParser.isSetted(CommandLineParser.HELP));
        commandLineParser.parse(new String[] { "topAi" });
        Assert.assertFalse(commandLineParser.isSetted(CommandLineParser.HELP));
    }
}
