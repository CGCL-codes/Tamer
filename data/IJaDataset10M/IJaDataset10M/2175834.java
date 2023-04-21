package com.icteam.fiji.command.prcs;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.EntManager;
import java.util.List;

public class GetVisiblePrcsRelsCmd extends DefaultCommand {

    private EntManager m_entManager = null;

    private Long m_id = null;

    private boolean m_subject = true;

    private boolean m_entPrcs = true;

    public boolean isEntPrcs() {
        return m_entPrcs;
    }

    public void setEntPrcs(boolean p_entPrcs) {
        m_entPrcs = p_entPrcs;
    }

    public boolean isSubject() {
        return m_subject;
    }

    public void setSubject(boolean p_subject) {
        m_subject = p_subject;
    }

    public void setId(Long p_id) {
        m_id = p_id;
    }

    public Long getId() {
        return m_id;
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
        if (getId() == null || getId().toString().trim().length() == 0) return commandValidationFailed(ErrorCode.INVALID_OBJECT_ID, "Null object id"); else {
            try {
                Long.parseLong(getId().toString());
                return commandValidationSucceded();
            } catch (NumberFormatException e) {
                return commandValidationFailed(ErrorCode.INVALID_OBJECT_ID, "Invalid object id");
            }
        }
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        List<Long> prcsRels;
        if (isEntPrcs()) {
            prcsRels = m_entManager.getVisibleEntPrcsPrcsRels(p_context.getCaller().getName(), getId(), isSubject());
        } else {
            prcsRels = m_entManager.getVisibleEntBsnsPrcsRels(p_context.getCaller().getName(), getId());
        }
        res.setValues("prcsRels", prcsRels);
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        setAudt(null);
    }
}
