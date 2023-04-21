package com.icteam.fiji.command.admin.utente;

import java.io.Serializable;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.UtenManager;
import com.icteam.fiji.manager.exception.ManagerException;

public class UpdateUtenStatusCmd extends DefaultCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    private UtenManager m_utenMgr = null;

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            m_utenMgr = svcLocator.getService(UtenManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    @Override
    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        try {
            DefaultCommandResult res = new DefaultCommandResult();
            m_utenMgr.updateAllUtenStatus();
            return res;
        } catch (ManagerException e) {
            throw new CommandException(e);
        }
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
