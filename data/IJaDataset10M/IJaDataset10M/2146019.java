package ch.unibe.a3ubAdmin.view.terminal;

import java.util.List;

public class CmdNoOp extends Command {

    public CmdNoOp() {
        this.name = "noop";
        this.help = "Tut nichts.";
        this.usage = "noop";
    }

    public void run(List<String> commandLine, Console console) {
        console.println("Sucessfully done nothing!");
    }
}
