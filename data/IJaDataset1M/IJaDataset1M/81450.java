package org.libertya.attributeSet.model;

import java.util.Properties;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.MPluginPO;
import org.openXpertya.plugin.MPluginStatusPO;

public class MAttributeSet extends MPluginPO {

    public MAttributeSet(PO po, Properties ctx, String trxName, String aPackage) {
        super(po, ctx, trxName, aPackage);
    }

    public MPluginStatusPO preBeforeSave(PO po, boolean newRecord) {
        org.openXpertya.model.MAttributeSet anAttributeSet = (org.openXpertya.model.MAttributeSet) po;
        anAttributeSet.setIsLot(false);
        anAttributeSet.setIsSerNo(false);
        anAttributeSet.setIsDueDate(false);
        anAttributeSet.setIsGuaranteeDate(false);
        anAttributeSet.setIsInstanceAttribute(true);
        anAttributeSet.setMandatoryType(org.openXpertya.model.MAttributeSet.MANDATORYTYPE_AlwaysMandatory);
        return status_po;
    }
}
