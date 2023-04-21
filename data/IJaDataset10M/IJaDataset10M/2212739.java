package org.sodbeans.libraries.hop.io;

import org.openide.util.Lookup;
import org.sodbeans.io.CommandLine;
import org.sodbeans.libraries.LibraryCall;
import org.sodbeans.libraries.LibraryReturn;
import org.sodbeans.libraries.SodbeansLibraryAction;

/**
 * A function native to java for handling command line output.
 * 
 * @author Andreas Stefik
 */
public class CommandLineOutput extends SodbeansLibraryAction {

    private CommandLine commandLine;

    public CommandLineOutput() {
        commandLine = Lookup.getDefault().lookup(CommandLine.class);
    }

    @Override
    public LibraryReturn call(LibraryCall lcall) {
        LibraryReturn ret = new LibraryReturn();
        ret.setIsCalled(true);
        commandLine.post(lcall.argumentString);
        return ret;
    }

    public LibraryReturn uncall(LibraryCall lcall) {
        LibraryReturn ret = new LibraryReturn();
        ret.setIsCalled(true);
        commandLine.unpost();
        return ret;
    }

    @Override
    public String getName() {
        return "org.sodbeans.libraries.hop.io.CommandLineOutput";
    }
}
