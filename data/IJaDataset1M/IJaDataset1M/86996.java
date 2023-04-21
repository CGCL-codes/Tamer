package com.vitria.test.commandline.test;

import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.TestCase;
import com.vitria.test.commandline.CommandParser;
import com.vitria.test.commandline.CommandParserFactory;
import com.vitria.test.commandline.CommandResult;
import com.vitria.test.commandline.api.CommandException;
import com.vitria.test.commandline.api.CommandOption;
import com.vitria.test.commandline.api.CommandOptionValueType;
import com.vitria.test.commandline.api.InvalidOptionException;
import com.vitria.test.commandline.api.InvalidOptionValueException;
import com.vitria.test.commandline.api.OptionConflictException;
import com.vitria.test.commandline.api.OptionRequiredException;
import com.vitria.test.commandline.api.OptionValueRequiredException;

public class CommandParserTest extends TestCase {

    public void testBasicFunction() throws CommandException {
        ArrayList<CommandOption> cos = new ArrayList<CommandOption>();
        CommandOption co = CommandParserFactory.createCommandOption("project", true, "projectName", "project name e.g. /home/vtba/project");
        cos.add(co);
        co = CommandParserFactory.createCommandOption("sleep", false, "sleepSeconds", "sleep seconds");
        co.setValueType(CommandOptionValueType.INTEGER);
        cos.add(co);
        co = CommandParserFactory.createCommandOption("d", false, "subType", "durable");
        co.setCommandOptionNoValue("true");
        co.setValueType(CommandOptionValueType.BOOLEAN);
        cos.add(co);
        co = CommandParserFactory.createCommandOption("nd", false, "subType", "none durable");
        co.setCommandOptionNoValue("false");
        co.setValueType(CommandOptionValueType.BOOLEAN);
        cos.add(co);
        CommandParser cp = CommandParserFactory.createCommandParser("msc", cos);
        System.out.println(cp.getCommandHelp());
        CommandResult cr = null;
        try {
            cr = cp.parse(Arrays.asList("-project abcd -sleep 10".split(" ")));
            System.out.println(cr.getCommandOptions());
        } catch (Exception e) {
            fail();
        }
        try {
            cr = cp.parse(Arrays.asList("-project abcd".split(" ")));
            System.out.println(cr.getCommandOptions());
        } catch (Exception e) {
            fail();
        }
        try {
            cr = cp.parse(Arrays.asList("-nd -d -help".split(" ")));
            System.out.println(cr.getCommandOptions());
        } catch (Exception e) {
            fail();
        }
        try {
            cp.parse(Arrays.asList("-sss cdd".split(" ")));
            fail();
        } catch (InvalidOptionException ipv) {
            ipv.printStackTrace();
        } catch (Exception e) {
            fail();
        }
        try {
            cp.parse(Arrays.asList("-sleep cdd".split(" ")));
            fail();
        } catch (InvalidOptionValueException ipv) {
            ipv.printStackTrace();
        } catch (Exception e) {
            fail();
        }
        try {
            cp.parse(Arrays.asList("-d -nd".split(" ")));
            fail();
        } catch (OptionConflictException re) {
            re.printStackTrace();
        } catch (Exception e) {
            fail();
        }
        try {
            cp.parse(Arrays.asList("-sleep 10".split(" ")));
            fail();
        } catch (OptionRequiredException re) {
            re.printStackTrace();
        } catch (Exception e) {
            fail();
        }
        try {
            cp.parse(Arrays.asList("-sleep".split(" ")));
            fail();
        } catch (OptionValueRequiredException re) {
            re.printStackTrace();
        } catch (Exception e) {
            fail();
        }
    }
}
