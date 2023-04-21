package org.esprit.ocm.client.model.ec2;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class KeyPairRecord extends ListGridRecord {

    public KeyPairRecord() {
    }

    public KeyPairRecord(String keyName, String keyFingerprint, String keyMaterial) {
        setKeyName(keyName);
        setKeyFingerprint(keyFingerprint);
        setKeyMaterial(keyMaterial);
    }

    public String getKeyName() {
        return getAttributeAsString("keyName");
    }

    public String getKeyFingerprint() {
        return getAttributeAsString("keyFingerprint");
    }

    public String getKeyMaterial() {
        return getAttributeAsString("keyMaterial");
    }

    public void setKeyName(String keyName) {
        setAttribute("keyName", keyName);
    }

    public void setKeyFingerprint(String keyFingerprint) {
        setAttribute("keyFingerprint", keyFingerprint);
    }

    public void setKeyMaterial(String keyMaterial) {
        setAttribute("keyMaterial", keyMaterial);
    }
}
