package com.icteam.fiji.command.corporate.company;

import com.icteam.fiji.ErrorCode;
import com.icteam.fiji.ServiceLocationException;
import com.icteam.fiji.ServiceLocator;
import com.icteam.fiji.command.*;
import com.icteam.fiji.manager.CodEstEntBsnsManager;
import com.icteam.fiji.manager.LocazManager;
import com.icteam.fiji.manager.SistCfgnManager;
import com.icteam.fiji.manager.SocManager;
import com.icteam.fiji.manager.exception.ConcurrentAccessException;
import com.icteam.fiji.manager.exception.CreateException;
import com.icteam.fiji.manager.exception.UpdateException;
import com.icteam.fiji.model.*;
import static com.icteam.fiji.persistence.util.DBConstants.NO_FLAG;
import static com.icteam.fiji.persistence.util.DBConstants.YES_FLAG;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class SaveSocCmd extends DefaultCommand implements Serializable {

    private SocManager socManager = null;

    private CodEstEntBsnsManager codEstEntBsnsManager = null;

    private Soc soc = null;

    private SistCfgnManager sistCfgnManager;

    private LocazManager locazManager;

    public void setSoc(Soc soc) {
        this.soc = soc;
    }

    public Soc getSoc() {
        return soc;
    }

    @Override
    protected void doInitialize(CommandContext context) throws CommandException {
        try {
            ServiceLocator svcLocator = context.getServiceLocator();
            socManager = svcLocator.getService(SocManager.class);
            codEstEntBsnsManager = svcLocator.getService(CodEstEntBsnsManager.class);
            locazManager = svcLocator.getService(LocazManager.class);
            sistCfgnManager = svcLocator.getService(SistCfgnManager.class);
        } catch (ServiceLocationException ex) {
            throw new CommandException(ex);
        }
    }

    @Override
    protected CommandValidation doValidate(CommandContext p_context) throws CommandException {
        if (soc == null) {
            return commandValidationFailed(ErrorCode.NULL_OBJECT, "Soc can't be null");
        } else if ((soc.getNDnmn() == null) || (soc.getNDnmn().length() <= 0)) {
            return commandValidationFailed(ErrorCode.NULL_OBJECT, "Soc.getNDnmn() can't be null or empty");
        } else {
            return commandValidationSucceded();
        }
    }

    @Override
    protected CommandResult doExecute(CommandContext context) throws CommandException {
        logger.trace("doExeecute(): start...");
        DefaultCommandResult res = new DefaultCommandResult();
        if (soc.getCEntBsns() == null) {
            try {
                soc.setTipSoc(TipSocEnum.SOCIETA.getValue());
                soc.setFInt(YES_FLAG);
                soc.setDIniVal(new Date());
                soc.setDMdfVal(new Date());
                soc.setTipEntBsns(TipEntBsnsEnum.SOCIETA.getValue());
                soc.setNEntBsns(soc.getNDnmn());
                soc = socManager.createSoc(soc);
                CodEstEntBsns codEstEntBsns = new CodEstEntBsns();
                codEstEntBsns.setCCodEst(soc.getCEntBsns().toString());
                codEstEntBsns.setCEntBsns(soc.getCEntBsns());
                codEstEntBsns.setTipCodEst(TipCodEstEnum.FIJI_SOC.getValue());
                codEstEntBsns.setTipFntEst(TipFntEstEnum.FIJI.getValue());
                codEstEntBsns = codEstEntBsnsManager.createCodEstEntBsns(codEstEntBsns);
            } catch (CreateException ex) {
                Logger.getLogger(SaveSocCmd.class.getName()).log(Level.SEVERE, null, ex);
                throw new CommandException(ErrorCode.GENERIC_ERROR, "Creation FIJI-Soc failed");
            }
        }
        res.setValue("soc", soc);
        logger.trace("doExecute(): end!");
        return res;
    }

    protected void doSetAudt(CommandContext p_context, CommandResult p_result) throws CommandException {
        Audt audt = createAudt(p_context, TipAudtEnum.AGGIORNAMENTO_SOCIETA.getValue());
        audt.setCEntBsns(soc.getCEntBsns());
        TipSoc ts = soc.getTipSoc();
        if (ts != null && ts.getCTipSoc() != null && !ts.getCTipSoc().equals(-1)) audt.getAudtParms().add(createAudtParm("NTipSoc", ts.getNTipSoc()));
        if (!StringUtils.isBlank(soc.getFInt())) audt.getAudtParms().add(createAudtParm("FInt", soc.getFInt()));
        if (!StringUtils.isBlank(soc.getDIniVal().toString())) audt.getAudtParms().add(createAudtParm("DIniVal", getItaDateString(soc.getDIniVal())));
        if (!StringUtils.isBlank(soc.getDMdfVal().toString())) audt.getAudtParms().add(createAudtParm("DMdfVal", getItaDateString(soc.getDMdfVal())));
        setAudt(audt);
    }
}
