package org.personalsmartspace.spm.preference.api.platform;

import java.io.Serializable;
import javax.swing.tree.DefaultTreeModel;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.spm.preference.api.platform.constants.PrivacyPreferenceTypeConstants;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

/**
 * @author Elizabeth
 *
 */
public class PPNPrivacyPreferenceTreeModel extends DefaultTreeModel implements IPrivacyPreferenceTreeModel, Serializable {

    private ICtxIdentifier affectedCtxId;

    private String myContextType;

    private IDigitalPersonalIdentifier providerDPI;

    private IServiceIdentifier serviceID;

    private PrivacyPreferenceTypeConstants myPrivacyType;

    private IPrivacyPreference pref;

    public PPNPrivacyPreferenceTreeModel(String myCtxType, IPrivacyPreference preference) {
        super(preference);
        this.myContextType = myCtxType;
        this.myPrivacyType = PrivacyPreferenceTypeConstants.PPNP;
        this.pref = preference;
    }

    public ICtxIdentifier getAffectedContextIdentifier() {
        return this.getAffectedCtxId();
    }

    public String getContextType() {
        return this.myContextType;
    }

    @Override
    public PrivacyPreferenceTypeConstants getPrivacyType() {
        return this.getPrivacyType();
    }

    @Override
    public IPrivacyPreference getRootPreference() {
        return this.pref;
    }

    public void setAffectedCtxId(ICtxIdentifier affectedCtxId) {
        this.affectedCtxId = affectedCtxId;
    }

    public ICtxIdentifier getAffectedCtxId() {
        return affectedCtxId;
    }

    public void setProviderDPI(IDigitalPersonalIdentifier providerDPI) {
        this.providerDPI = providerDPI;
    }

    public IDigitalPersonalIdentifier getProviderDPI() {
        return providerDPI;
    }

    public void setServiceID(IServiceIdentifier serviceID) {
        this.serviceID = serviceID;
    }

    public IServiceIdentifier getServiceID() {
        return serviceID;
    }
}
