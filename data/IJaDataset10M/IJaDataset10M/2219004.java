package com.icteam.fiji.command.corporate.company;

import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.SocManager;
import com.icteam.fiji.model.SocPersLite;
import java.io.Serializable;
import java.util.List;

public class GetSocPersLitesRolesCmd extends DefaultCommand implements Serializable {

    private SocManager socManager = null;

    private Long CEntBsnsSoc = null;

    public Long getCEntBsnsSoc() {
        return CEntBsnsSoc;
    }

    public void setCEntBsnsSoc(Long CEntBsnsSoc) {
        this.CEntBsnsSoc = CEntBsnsSoc;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            socManager = svcLocator.getService(SocManager.class);
        } catch (ServiceLocationException ex) {
            throw new CommandException(ex);
        }
    }

    @Override
    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        logger.trace("doExecute(): start...");
        List<SocPersLite> socPersLitesRoles;
        socPersLitesRoles = socManager.getSocPersLitesRoles(getCEntBsnsSoc());
        DefaultCommandResult res = new DefaultCommandResult();
        res.setValues("socPersLitesRoles", socPersLitesRoles);
        logger.trace("doExecute(): end!");
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
