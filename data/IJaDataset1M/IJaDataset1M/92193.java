package com.gestioni.adoc.apsadmin.protocollo.signature;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import com.agiletec.aps.system.ApsSystemUtils;
import com.gestioni.adoc.aps.system.services.protocollo.IProtocolloManager;
import com.gestioni.adoc.aps.system.services.protocollo.config.Signature;
import com.gestioni.adoc.apsadmin.system.AdocBaseAction;

public class SignatureConfigAction extends AdocBaseAction {

    public String edit() {
        try {
            Map<Integer, String> par = this.getProtocolloManager().getCurrentSignature().getProperties();
            Iterator it = par.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                Integer x = (Integer) pairs.getKey();
                this.getSignatureParams().put(x.toString(), (String) pairs.getValue());
            }
            this.setSelectedLabel(this.getProtocolloManager().getCurrentSignature().getLabel());
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "moveUp");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String update() {
        try {
            Signature signature = new Signature();
            Iterator it = this.getSignatureParams().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String x = (String) pairs.getKey();
                signature.getProperties().put(new Integer(x), (String) pairs.getValue());
                signature.setLabel(this.getSelectedLabel());
            }
            if (signature.getProperties().size() != this.getProtocolloManager().getCurrentSignature().getProperties().size()) {
                this.addActionError(this.getText("Error.update.siglature"));
                ApsSystemUtils.getLogger().severe("Errore in aggiornamento segnatura. La configurazione non è stata aggiornata");
                return INPUT;
            }
            this.getProtocolloManager().updateSignature(signature);
            this.addActionMessage(this.getText("Message.signature.updated"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "update");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String moveUp() {
        try {
            if (0 == this.getSelectedItem()) {
                this.addActionError(this.getText("Error.empy.selection"));
            }
            if (this.getSelectedItem() > 1) {
                String targetValue = this.getSignatureParams().get(new Integer(this.getSelectedItem()).toString());
                String targetValue2 = this.getSignatureParams().get(new Integer(this.getSelectedItem() - 1).toString());
                this.getSignatureParams().put(new Integer(this.getSelectedItem()).toString(), targetValue2);
                this.getSignatureParams().put(new Integer(this.getSelectedItem() - 1).toString(), targetValue);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "moveUp");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String moveDown() {
        try {
            if (0 == this.getSelectedItem()) {
                this.addActionError(this.getText("Error.empy.selection"));
            }
            if (this.getSelectedItem() < this.getSignatureParams().size()) {
                String targetValue = this.getSignatureParams().get(new Integer(this.getSelectedItem()).toString());
                String targetValue2 = this.getSignatureParams().get(new Integer(this.getSelectedItem() + 1).toString());
                this.getSignatureParams().put(new Integer(this.getSelectedItem()).toString(), targetValue2);
                this.getSignatureParams().put(new Integer(this.getSelectedItem() + 1).toString(), targetValue);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "moveDown");
            return FAILURE;
        }
        return SUCCESS;
    }

    public void setProtocolloManager(IProtocolloManager protocolloManager) {
        this._protocolloManager = protocolloManager;
    }

    protected IProtocolloManager getProtocolloManager() {
        return _protocolloManager;
    }

    public void setSelectedItem(int selectedItem) {
        this._selectedItem = selectedItem;
    }

    public int getSelectedItem() {
        return _selectedItem;
    }

    public void setSignatureParams(Map<String, String> signatureParams) {
        this._signatureParams = signatureParams;
    }

    public Map<String, String> getSignatureParams() {
        return _signatureParams;
    }

    public void setSelectedLabel(String selectedLabel) {
        this._selectedLabel = selectedLabel;
    }

    public String getSelectedLabel() {
        return _selectedLabel;
    }

    public Map<String, String> getLabels() {
        return this.getProtocolloManager().getLabelOptions();
    }

    private IProtocolloManager _protocolloManager;

    private String _selectedLabel;

    private int _selectedItem;

    private Map<String, String> _signatureParams = new TreeMap<String, String>();
}
