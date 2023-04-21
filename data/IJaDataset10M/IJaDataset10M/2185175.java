package org.epoline.phoenix.dossiernotepad.shared;

import org.epoline.phoenix.common.shared.Item;

/**
 *
 * Data to be sent on server for Replacing Document
 */
public class ItemReplaceDocument extends Item {

    private final String dossierKey;

    private final String packagePXIid;

    private final String docuementKey;

    private final String doccontrolCode;

    public ItemReplaceDocument(String nDossierKey, String nPackagePXIid, String nDocumentKey, String nDoccontrolCode) {
        super();
        if (nDossierKey == null || nPackagePXIid == null || nDocumentKey == null || nDoccontrolCode == null) {
            throw new NullPointerException("Null parameter in ItemReplaceDocument.constructor");
        }
        dossierKey = nDossierKey;
        packagePXIid = nPackagePXIid;
        docuementKey = nDocumentKey;
        doccontrolCode = nDoccontrolCode;
    }

    public String getDossierKey() {
        return dossierKey;
    }

    public String getPackagePXIid() {
        return packagePXIid;
    }

    public String getDocuementKey() {
        return docuementKey;
    }

    public String getDoccontrolCode() {
        return doccontrolCode;
    }
}
