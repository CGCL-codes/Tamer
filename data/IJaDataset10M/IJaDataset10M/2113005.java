package com.icteam.fiji.command.admin.profile;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.*;
import com.icteam.fiji.manager.UtenManager;
import java.io.Serializable;

/**
 * @author m.traina
 */
public class GetProfUtenLitesCmd extends DefaultCommand implements Serializable {

    private UtenManager utenManager = null;

    private SearchProfCriterium searchProfCriterium;

    public SearchProfCriterium getSearchProfCriterium() {
        return searchProfCriterium;
    }

    public void setSearchProfCriterium(SearchProfCriterium searchProfCriterium) {
        this.searchProfCriterium = searchProfCriterium;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            utenManager = svcLocator.getService(UtenManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    @Override
    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (searchProfCriterium == null) return commandValidationFailed(ErrorCode.NULL_OBJECT, "searchProfCriterium can't be null"); else return commandValidationSucceded();
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        res.setValues("utenProfs", utenManager.getUtenLitesByProf(searchProfCriterium));
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
