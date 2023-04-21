package com.bluemarsh.jswat.command.commands;

import com.bluemarsh.jswat.command.AbstractCommand;
import com.bluemarsh.jswat.command.CommandArguments;
import com.bluemarsh.jswat.command.CommandContext;
import com.bluemarsh.jswat.command.CommandException;
import com.bluemarsh.jswat.command.MissingArgumentsException;
import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.util.Strings;
import com.sun.jdi.PathSearchingVirtualMachine;
import com.sun.jdi.VirtualMachine;
import java.io.PrintWriter;
import org.openide.util.NbBundle;

/**
 * Displays information regarding the connected debuggee.
 *
 * @author Nathan Fiedler
 */
public class DebuggeeInfoCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "vminfo";
    }

    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        StringBuilder sb = new StringBuilder();
        sb.append(NbBundle.getMessage(DebuggeeInfoCommand.class, "CTL_vminfo_version"));
        sb.append(' ');
        sb.append(vm.version());
        sb.append("\n\n");
        if (vm instanceof PathSearchingVirtualMachine) {
            PathSearchingVirtualMachine psvm = (PathSearchingVirtualMachine) vm;
            sb.append(NbBundle.getMessage(DebuggeeInfoCommand.class, "CTL_vminfo_basedir"));
            sb.append('\n');
            sb.append(psvm.baseDirectory());
            sb.append("\n\n");
            sb.append(NbBundle.getMessage(DebuggeeInfoCommand.class, "CTL_vminfo_cpath"));
            sb.append('\n');
            sb.append(Strings.listToString(psvm.classPath(), "\n"));
            sb.append("\n\n");
            sb.append(NbBundle.getMessage(DebuggeeInfoCommand.class, "CTL_vminfo_bcpath"));
            sb.append('\n');
            sb.append(Strings.listToString(psvm.bootClassPath(), "\n"));
            sb.append('\n');
        }
        sb.append('\n');
        sb.append(NbBundle.getMessage(DebuggeeInfoCommand.class, "CTL_vminfo_stratum"));
        sb.append(' ');
        sb.append(vm.getDefaultStratum());
        writer.println(sb.toString());
    }

    @Override
    public boolean requiresDebuggee() {
        return true;
    }
}
