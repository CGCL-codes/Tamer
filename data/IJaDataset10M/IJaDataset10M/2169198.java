package com.icteam.fiji.command.rels;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.EntManager;

public class SearchEntBsnsEntPrcsRelsCmd extends DefaultCommand {

    private EntManager m_entManager = null;

    private SearchEntPrcsEntBsnsRelsCriterium m_criterium;

    public SearchEntPrcsEntBsnsRelsCriterium getCriterium() {
        return m_criterium;
    }

    public void setCriterium(SearchEntPrcsEntBsnsRelsCriterium p_criterium) {
        m_criterium = p_criterium;
    }

    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            m_entManager = svcLocator.getService(EntManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (m_criterium == null) return commandValidationFailed(ErrorCode.INVALID_CRITERIUM);
        if (m_criterium.getCEntBsns() == null && m_criterium.getCEntPrcs() == null) return commandValidationFailed(ErrorCode.INVALID_OBJECT_ID, "Null object id");
        return commandValidationSucceded();
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        res.setValues("prcsRels", m_entManager.getEntBsnsEntPrcsRels(m_criterium));
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
