package avrora.actions;

import avrora.Main;
import avrora.arch.AbstractArchitecture;
import avrora.arch.AbstractDisassembler;
import avrora.syntax.elf.ELFParser;
import cck.util.Util;

/**
 * @author Ben L. Titzer
 */
public class NewSimAction extends Action {

    public static String HELP = "This action is an experimental testbed for developing and refining the " + "infrastructure surrounding the new simulation framework that allows multiple different " + "instruction architectures to be simulated.";

    public NewSimAction() {
        super(HELP);
    }

    public void run(String[] args) throws Exception {
        if (args.length != 1) Util.userError("no simulation file specified");
        String fn = args[0];
        Main.checkFileExists(fn);
        ELFParser loader = new ELFParser();
        loader.options.process(options);
        AbstractArchitecture arch = loader.getArchitecture();
        AbstractDisassembler d = arch.getDisassembler();
    }
}
