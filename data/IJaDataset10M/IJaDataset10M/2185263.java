package com.icteam.fiji.command.corporate.company;

import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.OrgManager;
import com.icteam.fiji.model.OrgLite;
import java.io.Serializable;
import java.util.List;

public class GetOrgLiteAutocompleteCmd extends DefaultCommand implements Serializable {

    private String NDnmn = null;

    private Boolean onlyCompany = false;

    private OrgManager orgManager = null;

    public String getNDnmn() {
        return NDnmn;
    }

    public void setNDnmn(String NDnmn) {
        this.NDnmn = NDnmn;
    }

    public void setOnlyCompany(Boolean onlyCompany) {
        this.onlyCompany = onlyCompany;
    }

    public Boolean getOnlyCompany() {
        return onlyCompany;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            orgManager = svcLocator.getService(OrgManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    @Override
    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        List<OrgLite> lites = orgManager.getOrgLitesAutocomplete(getNDnmn(), getOnlyCompany());
        DefaultCommandResult res = new DefaultCommandResult();
        res.setValues("orgLites", lites);
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
