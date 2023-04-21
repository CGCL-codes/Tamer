package com.icteam.fiji.command.admin.flus;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.model.Audt;
import com.icteam.fiji.model.TipAudtEnum;
import com.icteam.fiji.model.TipFntEst;
import com.icteam.fiji.command.*;
import com.icteam.fiji.manager.FlusManager;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 * @author m.traina
 */
public class SearchFlusCmd extends DefaultCommand implements Serializable {

    private FlusManager flusManager = null;

    private SearchFlusCriterium searchFlusCriterium = null;

    public SearchFlusCriterium getSearchFlusCriterium() {
        return searchFlusCriterium;
    }

    public void setSearchFlusCriterium(SearchFlusCriterium searchFlusCriterium) {
        this.searchFlusCriterium = searchFlusCriterium;
    }

    public FlusManager getFlusManager() {
        return flusManager;
    }

    public void setFlusManager(FlusManager flusManager) {
        this.flusManager = flusManager;
    }

    @Override
    protected void doInitialize(CommandContext p_context) throws CommandException {
        try {
            ServiceLocator svcLocator = p_context.getServiceLocator();
            flusManager = svcLocator.getService(FlusManager.class);
        } catch (ServiceLocationException e) {
            throw new CommandException(e);
        }
    }

    @Override
    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (searchFlusCriterium == null) {
            return commandValidationFailed(ErrorCode.NULL_OBJECT, "searchFlusCriterium can't be null");
        } else {
            return commandValidationSucceded();
        }
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        Audt audt = createAudt(p_context, TipAudtEnum.RICERCA_FLUSSI.getValue());
        if (searchFlusCriterium.getDIni() != null && !StringUtils.isBlank(searchFlusCriterium.getDIni().toString())) audt.getAudtParms().add(createAudtParm("DIni", getItaDateString(searchFlusCriterium.getDIni())));
        TipFntEst tfe = searchFlusCriterium.getTipFntEst();
        if (tfe != null && tfe.getCTipFntEst() != null && !tfe.getCTipFntEst().equals(-1)) audt.getAudtParms().add(createAudtParm("tipFntEst", tfe.getNTipFntEst()));
        setAudt(audt);
    }

    protected CommandResult doExecute(CommandContext p_context) throws CommandException {
        DefaultCommandResult res = new DefaultCommandResult();
        res.setValues("flusses", flusManager.getFlus(searchFlusCriterium));
        return res;
    }
}
