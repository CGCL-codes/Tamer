package com.icteam.fiji.command.admin.utente;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.*;
import com.icteam.fiji.manager.UtenManager;
import com.icteam.fiji.manager.exception.CreateException;
import com.icteam.fiji.manager.exception.UpdateException;
import com.icteam.fiji.model.UtenCfgnLite;
import java.io.Serializable;

public class SaveUtenCfgnCmd extends DefaultCommand implements Serializable {

    private UtenManager m_utenMgr = null;

    private UtenCfgnLite cfgnLite;

    public UtenCfgnLite getCfgnLite() {
        return cfgnLite;
    }

    public void setCfgnLite(UtenCfgnLite cfgnLite) {
        this.cfgnLite = cfgnLite;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            m_utenMgr = svcLocator.getService(UtenManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        try {
            DefaultCommandResult res = new DefaultCommandResult();
            m_utenMgr.saveUtenCfgn(cfgnLite);
            return res;
        } catch (UpdateException e) {
            throw new CommandException(ErrorCode.GENERIC_ERROR, "Update failed");
        } catch (CreateException e) {
            throw new CommandException(ErrorCode.GENERIC_ERROR, "Creation failed");
        }
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
