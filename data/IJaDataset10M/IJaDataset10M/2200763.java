package com.icteam.fiji.command.admin.utente;

import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.UtenManager;
import com.icteam.fiji.model.UtenLite;
import java.io.Serializable;

public class GetUtenLiteCmd extends DefaultCommand implements Serializable {

    private UtenManager m_utenManager = null;

    private String m_NLogin;

    public String getNLogin() {
        return m_NLogin;
    }

    public void setNLogin(String p_NLogin) {
        m_NLogin = p_NLogin;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            m_utenManager = svcLocator.getService(UtenManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        final UtenLite creator = m_utenManager.getUtenLite(getNLogin());
        res.setValue("uten", creator);
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
