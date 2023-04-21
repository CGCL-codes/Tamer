package com.application.areca.launcher.gui;

import com.application.areca.AbstractTarget;
import com.application.areca.adapters.XMLTags;
import com.application.areca.impl.policy.EncryptionPolicy;
import com.myJava.util.xml.AdapterException;

public class MissingDataListener implements com.application.areca.adapters.MissingDataListener, XMLTags {

    public EncryptionPolicy missingEncryptionDataDetected(AbstractTarget target, String algorithm, Boolean encryptNames) throws AdapterException {
        MissingEncryptionDataWindow frm = new MissingEncryptionDataWindow(target, algorithm, encryptNames);
        Application.getInstance().showDialog(frm);
        if (frm.isSaved()) {
            EncryptionPolicy policy = new EncryptionPolicy();
            policy.setEncrypted(true);
            policy.setEncryptionAlgorithm(frm.getAlgo());
            policy.setEncryptionKey(frm.getPassword());
            policy.setEncryptNames(frm.isEncryptFileNames().booleanValue());
            return policy;
        } else {
            return null;
        }
    }

    public Object missingFTPDataDetected(AbstractTarget target) throws AdapterException {
        MissingFTPDataWindow frm = new MissingFTPDataWindow(target);
        Application.getInstance().showDialog(frm);
        return new Object[] { frm.getPassword() };
    }
}
