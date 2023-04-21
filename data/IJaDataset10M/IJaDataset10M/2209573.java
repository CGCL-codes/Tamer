package com.clanwts.bncs.server.commands;

import org.apache.commons.lang.BooleanUtils;
import com.clanwts.bncs.server.BattleNetChatServerContext;
import com.clanwts.lang.InvalidCommandContextException;
import com.clanwts.nbdf.kernel.Command;
import com.clanwts.nbdf.kernel.KernelCommandContext;

public class chname implements Command {

    @Override
    public int run(KernelCommandContext ctx, String[] args) throws Exception {
        Object cc = ctx.getSessionContext();
        if (!(cc instanceof BattleNetChatServerContext)) {
            throw new InvalidCommandContextException();
        }
        BattleNetChatServerContext bncc = (BattleNetChatServerContext) cc;
        if (args.length == 1) {
            String name = args[0];
            bncc.setName(name);
            ctx.out().println("You have changed your name to \"" + name + "\".  The change will take effect on your next login.");
        } else {
            throw new IllegalArgumentException();
        }
        return 0;
    }
}
