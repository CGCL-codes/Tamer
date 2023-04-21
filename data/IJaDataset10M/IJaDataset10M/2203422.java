package com.icteam.fiji.command.admin.locazstage;

import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.CommandContext;
import com.icteam.fiji.command.CommandException;
import com.icteam.fiji.command.CommandResult;
import com.icteam.fiji.command.DefaultCommand;
import com.icteam.fiji.command.DefaultCommandResult;
import com.icteam.fiji.manager.LocazManager;
import com.icteam.fiji.model.LocazStage;
import com.icteam.fiji.model.Audt;
import com.icteam.fiji.model.TipAudtEnum;
import com.icteam.fiji.model.TipStaCrcm;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class GetLocazStagesCmd extends DefaultCommand implements Serializable {

    private LocazManager locazManager = null;

    private SearchLocazStageCriterium searchLSCriterium = null;

    public SearchLocazStageCriterium getSearchLSCriterium() {
        return searchLSCriterium;
    }

    public void setSearchLSCriterium(SearchLocazStageCriterium searchLSCriterium) {
        this.searchLSCriterium = searchLSCriterium;
    }

    @Override
    protected void doInitialize(CommandContext context) throws CommandException {
        try {
            ServiceLocator svcLocator = context.getServiceLocator();
            locazManager = svcLocator.getService(LocazManager.class);
        } catch (ServiceLocationException ex) {
            throw new CommandException(ex);
        }
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        Audt audt = createAudt(p_context, TipAudtEnum.RICERCA_LOCAZ_STAGE.getValue());
        if (searchLSCriterium.getCEntBsnsSoc() > 0) audt.getAudtParms().add(createAudtParm("CEntBsnsSoc", searchLSCriterium.getCEntBsnsSoc().toString()));
        TipStaCrcm tipStaCrcm = searchLSCriterium.getTipStaCrcm();
        if (tipStaCrcm != null && tipStaCrcm.getCTipStaCrcm() != null) audt.getAudtParms().add(createAudtParm("NTipStaCrcm", tipStaCrcm.getNTipStaCrcm()));
        if (!StringUtils.isBlank(searchLSCriterium.getCLocazStageGrp())) audt.getAudtParms().add(createAudtParm("CLocazStageGrp", searchLSCriterium.getCLocazStageGrp()));
        audt.getAudtParms().add(createAudtParm("flagGeoRef", searchLSCriterium.getFlagGeoRef().toString()));
        audt.getAudtParms().add(createAudtParm("flagFIJILocazStage", searchLSCriterium.getFlagFIJILocazStage().toString()));
        if (!StringUtils.isBlank(searchLSCriterium.getCCodEstDivn())) {
            audt.getAudtParms().add(createAudtParm("CCodEstDivn", searchLSCriterium.getCCodEstDivn()));
        }
        if (!StringUtils.isBlank(searchLSCriterium.getCCodEstNaz())) {
            audt.getAudtParms().add(createAudtParm("CCodEstNaz", searchLSCriterium.getCCodEstNaz()));
        }
        if (!StringUtils.isBlank(searchLSCriterium.getCCodEstCom())) {
            audt.getAudtParms().add(createAudtParm("CCodEstCom", searchLSCriterium.getCCodEstCom()));
        }
        setAudt(audt);
    }

    @Override
    protected CommandResult doExecute(CommandContext context) throws CommandException {
        logger.trace("doExecute(): start...");
        DefaultCommandResult res = new DefaultCommandResult();
        List<LocazStage> stages = locazManager.getLocazStageByCEntBsnsSoc(getSearchLSCriterium());
        res.setValues("locazStages", stages);
        logger.trace("doExecute(): end!");
        return res;
    }
}
