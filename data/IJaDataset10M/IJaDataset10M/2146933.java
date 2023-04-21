package ru.adv.repository.shelladmin;

public class PrepareCommand extends Command {

    public PrepareCommand(ShellAdmin shellAdmin) {
        super(shellAdmin);
    }

    public String getName() {
        return "PREPARE";
    }

    public String getOldName() {
        return "\\p";
    }

    public boolean run(String parameters) throws Exception {
        if (getShellAdmin().isDatabaseSelected()) {
            getShellAdmin().prepare(new Boolean(false));
        }
        return true;
    }
}
