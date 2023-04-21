package org.geoforge.guitlcolg.panel.ctr.wwd.opr.toolbar;

import org.geoforge.mdldat.event.GfrEvtMdlIdDtaAddedTlo;
import org.geoforge.mdl.event.GfrEvtMdlIdAbs;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.logging.Logger;
import org.geoforge.guillcolg.button.prd.BtnCheckAllNoPrd;
import org.geoforge.guillcolg.button.prd.BtnCheckAllYesPrd;
import org.geoforge.guillcolg.button.prd.BtnTransientDelAllObjPrd;
import org.geoforge.guillcolg.button.prd.BtnTransientNewObjPrd;
import org.geoforge.guillc.toolbar.TbrSubControlChkAbs;
import org.geoforge.guitlcolg.panel.ctr.wwd.opr.button.BIcnHlpOfflineOnthisSelOlgPrdCtr;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldatolg.opr.GfrMdlDtaSetTlosDskPip;
import org.geoforge.mdldatolg.opr.GfrMdlDtaSetTlosDskSbs;
import org.geoforge.mdldatolg.opr.GfrMdlDtaSetTlosDskSrf;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class TbrSubControlDskChkSelPrd extends TbrSubControlChkAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(TbrSubControlDskChkSelPrd.class.getName());

    static {
        TbrSubControlDskChkSelPrd._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public TbrSubControlDskChkSelPrd(ActionListener alrParentPanelMvc, ActionListener alrParentComponent, MouseListener mlrEffectsBorder) throws Exception {
        super(mlrEffectsBorder, alrParentComponent);
        super._btnHelpThisFrame = new BIcnHlpOfflineOnthisSelOlgPrdCtr(mlrEffectsBorder);
        super._btnNewObject = new BtnTransientNewObjPrd();
        super._btnDeleteAll = new BtnTransientDelAllObjPrd();
        super._btnAllCheck_ = new BtnCheckAllYesPrd();
        super._btnAllUncheck_ = new BtnCheckAllNoPrd();
        super._btnAllCheck_.addActionListener(alrParentPanelMvc);
        super._btnAllUncheck_.addActionListener(alrParentPanelMvc);
        super._btnAllCheck_.addActionListener(alrParentComponent);
        super._btnAllUncheck_.addActionListener(alrParentComponent);
        super._btnAllCheck_.addActionListener((ActionListener) this);
        super._btnAllUncheck_.addActionListener((ActionListener) this);
    }

    @Override
    public void open(ActionListener alrController) throws Exception {
        super.open(alrController);
        GfrMdlDtaSetTlosDskPip.getInstance().addObserver(this);
        GfrMdlDtaSetTlosDskSrf.getInstance().addObserver(this);
        GfrMdlDtaSetTlosDskSbs.getInstance().addObserver(this);
    }

    @Override
    public void close() throws Exception {
        super.close();
        GfrMdlDtaSetTlosDskPip.getInstance().deleteObserver(this);
        GfrMdlDtaSetTlosDskSrf.getInstance().deleteObserver(this);
        GfrMdlDtaSetTlosDskSbs.getInstance().deleteObserver(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        GfrMdlDtaSetTlosDskPip.getInstance().deleteObserver(this);
        GfrMdlDtaSetTlosDskSrf.getInstance().deleteObserver(this);
        GfrMdlDtaSetTlosDskSbs.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obj instanceof GfrEvtMdlIdDtaAddedTlo) {
            GfrEvtMdlIdAbs dml = (GfrEvtMdlIdAbs) obj;
            String str = ">> TODO: added tlo, strId=" + dml.getId();
            TbrSubControlDskChkSelPrd._LOGGER_.info(str + "\n");
            return;
        }
        super.update(obs, obj);
    }
}
