package com.icteam.fiji.command.admin.utente;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.util.CollectionUtils;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.UtenManager;
import com.icteam.fiji.model.*;

public class UpdateUtenCmd extends DefaultCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    private UtenManager utenManager = null;

    private Uten uten;

    private Set<UtenProf> profs;

    private Set<GrpUten> grps;

    private Boolean updatePwd;

    public Uten getUten() {
        return uten;
    }

    public void setUten(Uten uten) {
        this.uten = uten;
    }

    public Set<UtenProf> getProfs() {
        return profs;
    }

    public void setProfs(Set<UtenProf> profs) {
        this.profs = CollectionUtils.toSerialiazableSet(profs);
    }

    public Set<GrpUten> getGrps() {
        return grps;
    }

    public void setGrps(Set<GrpUten> grps) {
        this.grps = CollectionUtils.toSerialiazableSet(grps);
    }

    public Boolean getUpdatePwd() {
        return updatePwd;
    }

    public void setUpdatePwd(Boolean updatePwd) {
        this.updatePwd = updatePwd;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            utenManager = svcLocator.getService(UtenManager.class);
        } catch (ServiceLocationException ex) {
            throw new CommandException(ex);
        }
    }

    @Override
    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (uten == null) return commandValidationFailed(ErrorCode.NULL_OBJECT, "uten can't be null");
        return commandValidationSucceded();
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        Audt audt = createAudt(p_context, TipAudtEnum.AGGIORNAMENTO_UTENTE.getValue());
        audt.getAudtParms().add(createAudtParm("NLogin", uten.getNLogin()));
        setAudt(audt);
    }

    @Override
    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        uten.setUtenProfs(profs);
        uten.setUtenGrps(grps);
        try {
            res.setValue("uten", utenManager.setUten(uten, updatePwd));
        } catch (Exception e) {
            throw new CommandException(e);
        }
        return res;
    }
}
