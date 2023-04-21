package utilities;

import kernel.Process;
import kernel.ProcessManager;
import shell.Shell;

public class shell extends Process {

    Shell shellClass;

    @Override
    public String getInfo() {
        return "Executes command-line shell";
    }

    @Override
    public void main() {
        try {
            final Process p = ProcessManager.getInstance().runProcess("shell.Shell", "", input, output, this);
            p.join();
        } catch (final Throwable e) {
        }
    }
}
