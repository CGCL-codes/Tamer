package net.mitnet.tools.pdf.book.ui.cli;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * Command Line Helper.
 * 
 * This class provides a simple decorator for the Command Line class.
 *  
 * @author Tim Telcik <telcik@gmail.com>
 * 
 * @see CommandLine
 */
public class CommandLineHelper {

    private CommandLine commandLine = null;

    public CommandLineHelper(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    /**
	 * Returns the Option value.
	 */
    public String getOptionValue(Option option) {
        return commandLine.getOptionValue(option.getOpt());
    }

    /**
	 * Returns true if an option has been set.
	 */
    public boolean hasOption(Option option) {
        return commandLine.hasOption(option.getOpt());
    }

    /**
	 * Returns the Option value as a File.
	 */
    public File getOptionValueAsFile(Option option) {
        return getOptionValueAsFile(option.getOpt());
    }

    /**
	 * Returns the Option value as a File.
	 */
    public File getOptionValueAsFile(String opt) {
        String fileName = this.commandLine.getOptionValue(opt);
        File file = new File(fileName);
        return file;
    }

    /**
	 * Returns the Option value as an int.
	 */
    public int getOptionValueAsInt(Option option) {
        return getOptionValueAsInt(option.getOpt());
    }

    /**
	 * Returns the Option value as an int.
	 */
    public int getOptionValueAsInt(String opt) {
        String intString = commandLine.getOptionValue(opt);
        int resultInt = Integer.parseInt(intString);
        return resultInt;
    }

    /**
	 * Returns the Option value as an Integer.
	 */
    public Integer getOptionValueAsInteger(Option option) {
        return getOptionValueAsInteger(option.getOpt());
    }

    /**
	 * Returns the Option value as an Integer.
	 */
    public Integer getOptionValueAsInteger(String opt) {
        String integerString = commandLine.getOptionValue(opt);
        int resultInteger = Integer.valueOf(integerString);
        return resultInteger;
    }

    /**
	 * Returns the Options as a List.
	 * 
	 * NOTE: The Options are stored in CommandLine as a List, 
	 *       but only exposed by an Iterator or Option[] array. 
	 * 
	 * @return Options List 
	 */
    public List<Option> getOptionsAsList() {
        Option[] options = commandLine.getOptions();
        List<Option> optionsList = Arrays.asList(options);
        return optionsList;
    }
}
