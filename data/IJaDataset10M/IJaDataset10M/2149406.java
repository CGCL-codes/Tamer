package com.sumitbirla.s3shell;

public class CommandFactory {

    public static Command getCommand(String[] params, Environment env) {
        String cmd = params[0];
        if (cmd.equals("lsbucket")) return new LsBucket(env); else if (cmd.equals("ls")) return new Ls(env); else if (cmd.equals("use")) return new Use(env); else if (cmd.equals("acct")) return new Acct(env);
        return new Command(env);
    }
}
