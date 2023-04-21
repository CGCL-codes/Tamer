package org.xmlsh.builtin;

import java.util.List;
import org.xmlsh.core.BuiltinCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.Shell;
import org.xmlsh.sh.shell.ShellThread;

public class wait extends BuiltinCommand {

    public int run(Shell shell, String cmd, List<XValue> args) throws Exception {
        if (args.size() > 0) {
            for (XValue arg : args) {
                if (!arg.isAtomic()) {
                    shell.printErr("Arg is not a job ID");
                    continue;
                }
                long id = arg.toLong();
                if (id < 0) {
                    shell.printErr("Arg is not a job ID");
                    continue;
                }
                for (ShellThread thread : shell.getChildren()) if (thread.getId() == id) {
                    thread.join();
                    break;
                }
            }
        } else for (ShellThread thread : shell.getChildren()) {
            thread.join();
        }
        return 0;
    }
}
