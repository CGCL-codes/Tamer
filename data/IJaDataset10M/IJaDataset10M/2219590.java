package com.icteam.fiji.command.corporate.organization;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.OrgManager;
import com.icteam.fiji.model.LocazLite;

public class GetOrgLocationsCmd extends DefaultCommand {

    private static final Log logger = LogFactory.getLog(GetOrgLocationsCmd.class);

    private static final long serialVersionUID = 1L;

    private OrgManager orgManager = null;

    private Long m_CEntBsnsOrg;

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            orgManager = svcLocator.getService(OrgManager.class);
        } catch (ServiceLocationException ex) {
            throw new CommandException(ex);
        }
    }

    @Override
    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (m_CEntBsnsOrg != null) {
            return commandValidationSucceded();
        } else {
            return commandValidationFailed(ErrorCode.NULL_OBJECT, "CEntBsnsOrg can't be null");
        }
    }

    @Override
    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        logger.trace("doExecute(): start...");
        DefaultCommandResult res = new DefaultCommandResult();
        List<LocazLite> locazs = orgManager.getOrgLocations(m_CEntBsnsOrg);
        res.setValues("orgLocations", locazs);
        logger.trace("doExecute(): end!");
        return res;
    }

    public Long getCEntBsnsOrg() {
        return m_CEntBsnsOrg;
    }

    public void setCEntBsnsOrg(Long p_entBsnsOrg) {
        m_CEntBsnsOrg = p_entBsnsOrg;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
