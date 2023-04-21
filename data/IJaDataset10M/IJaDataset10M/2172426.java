package org.personalsmartspace.pm.prefmgr.impl.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.platform.CtxAssociationTypes;
import org.personalsmartspace.cm.model.api.platform.CtxAttributeTypes;
import org.personalsmartspace.cm.model.api.platform.CtxEntityTypes;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAssociation;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.platform.IPreferenceTreeModel;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * @author Elizabeth
 *
 */
public class PreferenceStorer {

    private final PSSLog logging = new PSSLog(this);

    private final ICtxBroker broker;

    public PreferenceStorer(ICtxBroker broker) {
        this.broker = broker;
    }

    public void deletePreference(IDigitalPersonalIdentifier dpi, ICtxIdentifier id) {
        ICtxAttribute attrPreference;
        try {
            attrPreference = (ICtxAttribute) broker.retrieve(id);
            if (attrPreference == null) {
                this.logging.debug("Cannot delete preference. Doesn't exist");
            } else {
                broker.remove(id);
            }
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    public boolean storeExisting(IDigitalPersonalIdentifier dpi, ICtxIdentifier id, IPreferenceTreeModel p) {
        try {
            p.setLastModifiedDate(new Date());
            ICtxAttribute attrPreference = (ICtxAttribute) broker.retrieve(id);
            if (attrPreference == null) {
                return false;
            }
            attrPreference.setBlobValue(p);
            broker.update(attrPreference);
            return true;
        } catch (ContextException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ICtxIdentifier storeNewPreference(IDigitalPersonalIdentifier dpi, IPreferenceTreeModel iptm, String key) {
        iptm.setLastModifiedDate(new Date());
        try {
            List<ICtxIdentifier> ctxIDs = broker.lookup(dpi, CtxModelType.ENTITY, CtxEntityTypes.PREFERENCE);
            if (ctxIDs.size() == 0) {
                List<ICtxIdentifier> assocCtxIDs = broker.lookup(dpi, CtxModelType.ASSOCIATION, CtxAssociationTypes.HAS_PREFERENCES);
                ICtxAssociation assoc = null;
                if (assocCtxIDs.size() == 0) {
                    ICtxEntity person = broker.retrieveOperator(dpi);
                    if (person == null) {
                        this.logging.debug("ICtxEntity for operator with dpi: " + dpi.toUriString() + " does not exist. aborting storing and exiting");
                        return null;
                    }
                    assoc = broker.createAssociation(dpi, CtxAssociationTypes.HAS_PREFERENCES);
                    assoc.setParentEntity(person);
                    broker.update(assoc);
                } else {
                    if (assocCtxIDs.size() > 1) {
                        this.logging.debug("There's more than one association of type hasPreferences for DPI:" + dpi.toUriString() + "\nStoring Preference under the first in the list");
                    }
                    assoc = (ICtxAssociation) broker.retrieve(assocCtxIDs.get(0));
                }
                ICtxEntity preferenceEntity = broker.createEntity(dpi, CtxEntityTypes.PREFERENCE);
                assoc.addEntity(preferenceEntity);
                broker.update(assoc);
                ICtxAttribute attr = broker.createAttribute(preferenceEntity.getCtxIdentifier(), key, iptm);
                this.logging.debug("Created attribute: " + attr.getType());
                return attr.getCtxIdentifier();
            } else {
                if (ctxIDs.size() > 1) {
                    this.logging.debug("There's more than one entity of type Preference for DPI: " + dpi.toUriString() + "\nStoring preference under the first in the list");
                }
                ICtxIdentifier preferenceEntityID = ctxIDs.get(0);
                ICtxAttribute attr = broker.createAttribute((ICtxEntityIdentifier) preferenceEntityID, key, iptm);
                this.logging.debug("Created attribute: " + attr.getType());
                return attr.getCtxIdentifier();
            }
        } catch (ContextException e) {
            this.logging.debug("Unable to store preference: " + key);
            e.printStackTrace();
            return null;
        }
    }

    public void storeRegistry(IDigitalPersonalIdentifier dpi, Registry registry) {
        try {
            List<ICtxIdentifier> attrList = broker.lookup(dpi, CtxModelType.ATTRIBUTE, CtxAttributeTypes.PREFERENCE_REGISTRY);
            if (attrList.size() > 0) {
                ICtxIdentifier identifier = attrList.get(0);
                ICtxAttribute attr = (ICtxAttribute) broker.retrieve(identifier);
                attr.setBlobValue(registry);
                broker.update(attr);
                this.logging.debug("Successfully updated preference registry for dpi: " + dpi.toUriString());
            } else {
                this.logging.debug("PreferenceRegistry not found in DB for dpi:" + dpi.toUriString() + ". Creating new registry");
                ICtxEntity operatorEntity = broker.retrieveOperator(dpi);
                ICtxAttribute attr = broker.createAttribute(operatorEntity.getCtxIdentifier(), CtxAttributeTypes.PREFERENCE_REGISTRY);
                attr.setBlobValue(registry);
                broker.update(attr);
                this.logging.debug("Successfully stored new preference registry");
            }
        } catch (ContextException e) {
            this.logging.debug("Exception while storing PreferenceRegistry to DB for dpi:" + dpi.toUriString());
            e.printStackTrace();
        }
    }

    private void calculateSizeOfObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            oos.close();
            bos.close();
            this.logging.debug("Trying to store preference of size: " + bos.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
