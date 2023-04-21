package org.pachyderm.foundation;

import org.apache.log4j.Logger;
import org.pachyderm.foundation.eof.PDBPresentation;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.eof.ERXEC;

/**
 * PDBPresentationDocument -- On creation, it acquires an EOEditingContext and a PDBPresentation (either
 * by creating a new one, or by using one referenced by EOGlobalID.  So every PDBPresentationDocument owns 
 * its own EOEditingContext.
 * 
 * "identifier" : "001001100-1-1-1-13000148-711816-2511-17-85-3"
 *         "pk" : 42
 * 
 * @author gavin
 */
public abstract class PDBPresentationDocument extends PXPresentationDocument {

    private static Logger LOG = Logger.getLogger(PDBPresentationDocument.class.getName());

    public PDBPresentationDocument() {
        super(ERXEC.newEditingContext());
        getEditingContext().setSharedEditingContext(null);
        PDBPresentation presoEO = (PDBPresentation) EOUtilities.createAndInsertInstance(getEditingContext(), PDBPresentation.ENTITY_NAME);
        initWithStoredDocument(presoEO);
    }

    public PDBPresentationDocument(EOGlobalID gid, String type, EOEditingContext ec) {
        super(gid, type, ec);
    }

    public PDBPresentationDocument(PDBPresentationDocument pdbdoc) {
        super(ERXEC.newEditingContext());
        PDBPresentation presentation = (PDBPresentation) EOUtilities.createAndInsertInstance(getEditingContext(), PDBPresentation.ENTITY_NAME);
        initWithStoredDocument(presentation);
        EOEditingContext ec = getEditingContext();
        ec.setSharedEditingContext(null);
    }

    private void initWithStoredDocument(PDBPresentation presentation) {
        setStoredDocument(presentation);
        setScreenModel(new PDBScreenModel(this));
        setResourceModel(new PDBResourceModel(this));
        setBuildModel(new PDBBuildModel(this));
        setInfoModel(new PDBInfoModel(this));
        setStateModel(new PDBStateModel(this));
    }

    public boolean loadPersistentRepresentationOfType(EOEnterpriseObject storedDocument, String type) {
        if (PDBPresentation.ENTITY_NAME.equals(type)) {
            initWithStoredDocument((PDBPresentation) storedDocument);
            return true;
        }
        return false;
    }

    public boolean preparePersistentRepresentationOfType(String type) {
        if (PDBPresentation.ENTITY_NAME.equals(type)) {
            prepareModelsForSave();
            _ensurePresentationModificationDateIsUpdated();
            return true;
        }
        return false;
    }

    private void _ensurePresentationModificationDateIsUpdated() {
        PDBPresentation presoEO = (PDBPresentation) storedDocument();
        presoEO.setDateModified(new NSTimestamp());
    }
}
