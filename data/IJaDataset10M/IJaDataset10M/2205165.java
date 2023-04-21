package com.eip.yost.web.pages.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.slf4j.Logger;
import com.eip.yost.commun.exceptions.ClientInexistantException;
import com.eip.yost.commun.exceptions.FactureInexistanteException;
import com.eip.yost.commun.exceptions.SouscriptionInexistanteException;
import com.eip.yost.dto.ClientDTO;
import com.eip.yost.dto.FactureDTO;
import com.eip.yost.dto.SouscriptionDTO;
import com.eip.yost.services.interfaces.IClientManager;
import com.eip.yost.utils.FactoryBundle;
import com.eip.yost.utils.FileDownloadHelper;

public class ClientDetails {

    @Inject
    private Logger mLogger;

    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private Locale currentLocale;

    @InjectComponent
    private Zone souscriptionGrid;

    @InjectComponent
    private Zone factureGrid;

    @Inject
    @Service("clientManager")
    private IClientManager mClientManager;

    @Property
    private ClientDTO mClient;

    @Property
    private SouscriptionDTO mSouscription;

    @Property
    private FactureDTO mFacture;

    public void onActivate(Integer pId) {
        try {
            mClient = mClientManager.getClient(pId);
            mClient.setSouscriptionList(mClientManager.findAllClientSouscription(mClient.getIdclient()));
        } catch (ClientInexistantException e) {
            mLogger.warn(e.getMLibelleErreur(), e);
        }
    }

    public Integer onPassivate() {
        return mClient.getIdclient();
    }

    public Object onActionFromToggleSouscription(Integer pSouscriptionId) {
        try {
            mClientManager.toggleSouscription(pSouscriptionId);
        } catch (SouscriptionInexistanteException e) {
            mLogger.warn(e.getMLibelleErreur(), e);
        }
        return souscriptionGrid.getBody();
    }

    public Object onActionFromToggleFacture(Integer pFactureId) {
        try {
            mClientManager.toggleFacture(pFactureId);
        } catch (FactureInexistanteException e) {
            mLogger.warn(e.getMLibelleErreur(), e);
        }
        return factureGrid.getBody();
    }

    public Object onActionFromResilierSouscription(Integer pSouscriptionId) {
        try {
            mClientManager.resilierSouscription(pSouscriptionId);
        } catch (SouscriptionInexistanteException e) {
            mLogger.warn(e.getMLibelleErreur(), e);
        }
        return souscriptionGrid.getBody();
    }

    public Object onActionFromGenererFacture() {
        try {
            for (SouscriptionDTO vSouscription : mClient.getSouscriptionList()) {
                mClientManager.genererFacture(vSouscription, currentLocale);
            }
        } catch (Exception e) {
            mLogger.warn(e.getMessage(), e);
        }
        return factureGrid.getBody();
    }

    public void onActionFromGetPdf(String pPath) {
        try {
            FileDownloadHelper.returnFile(new FileInputStream(new File(pPath)), "application/pdf", pPath.substring(pPath.lastIndexOf("/") + 1), requestGlobals);
        } catch (FileNotFoundException e) {
            mLogger.warn(e.getMessage(), e);
        }
        return;
    }

    public Object onActionFromDeleteInvoices(Integer pIdFacture) {
        mClientManager.deleteInvoices(pIdFacture);
        return factureGrid.getBody();
    }

    public boolean getIsToggle() {
        return (mSouscription.getEtat().equals(1));
    }

    public boolean getIsResilie() {
        return (mSouscription.getEtat().equals(0));
    }

    public String getResilie() {
        String vTablClass = null;
        Date dateResiliation = mSouscription.getDateResiliation();
        Date aujourdhui = new Date();
        Calendar vCal = new GregorianCalendar();
        Integer vEcheance = FactoryBundle.getInstance().getInt("administration.souscription.echeance");
        vCal.add(Calendar.DATE, vEcheance * 30);
        Date vDansXMois = vCal.getTime();
        if (mSouscription.getEtat().equals(2)) {
            vTablClass = "suspendue";
        } else if (aujourdhui.after(dateResiliation)) {
            vTablClass = "resilie";
        } else if (dateResiliation.after(aujourdhui) && dateResiliation.before(vDansXMois)) {
            vTablClass = "warning";
        }
        return vTablClass;
    }

    public String getPaye() {
        String vTablClass = "payed";
        if (mFacture.getPaye().equals(0)) {
            vTablClass = (mFacture.getSouscription().getDateResiliation().after(new Date())) ? "warning" : "resilie";
        }
        return vTablClass;
    }

    public Long getDelai() {
        Date dateResiliation = mSouscription.getDateResiliation();
        Date aujourdhui = new Date();
        Long delai = (dateResiliation.getTime() - aujourdhui.getTime()) / (1000 * 60 * 60 * 24);
        return (delai > 0 ? delai : 0);
    }

    public float getPrix() {
        return mSouscription.getFormule().getPrix();
    }

    public ClientDTO getClient() {
        return mClient;
    }

    public List<SouscriptionDTO> getSouscriptionList() {
        mClient.setSouscriptionList(mClientManager.findAllClientSouscription(mClient.getIdclient()));
        return mClient.getSouscriptionList();
    }

    public List<FactureDTO> getFactureList() {
        return mClientManager.getFacture(mClient.getIdclient());
    }

    public SouscriptionDTO getSouscription() {
        return mSouscription;
    }

    public void setSouscription(SouscriptionDTO souscription) {
        mSouscription = souscription;
    }

    public FactureDTO getFacture() {
        return mFacture;
    }

    public void setFacture(FactureDTO facture) {
        mFacture = facture;
    }
}
