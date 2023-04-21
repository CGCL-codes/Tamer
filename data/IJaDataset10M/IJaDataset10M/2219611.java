package org.pachyderm.woc;

import org.apache.log4j.Logger;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCListPage;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXAbstractDocument;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CXDocumentController;
import org.pachyderm.foundation.PDBPresentationDocument;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXUtility;
import org.pachyderm.foundation.eof.PDBPresentation;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import er.extensions.eof.ERXEC;

/**
 * @author jarcher
 * 
 */
public class ListPresentationsPage extends MCListPage {

    private static Logger LOG = Logger.getLogger(ListPresentationsPage.class);

    private static final long serialVersionUID = 744752220608981490L;

    private static final int IconDisplayMode = 0;

    private static final int ListDisplayMode = 1;

    private CXSession session;

    private CXDocumentController controller;

    public PDBPresentation presentation;

    public ListPresentationsPage(WOContext context) {
        super(context);
        session = (CXSession) session();
        controller = session.getDocumentController();
    }

    /**
   * @return
   */
    public NSArray<PDBPresentation> presentations() {
        return PDBPresentation.fetchPresentationNamesAndIdentities(session.defaultEditingContext(), session.getSessionPerson().stringId());
    }

    /**
   * @return
   */
    public int presentationScreenCount() {
        return presentation.getScreens().count();
    }

    /**
   * @return
   */
    public WOComponent openPresentationDocumentAction() {
        EOGlobalID gid = presentation.editingContext().globalIDForObject((EOEnterpriseObject) presentation);
        PXPresentationDocument document = (PXPresentationDocument) controller.openDocumentWithContentsAtGlobalID(gid);
        if (document != null) {
            return document.makeWindowController(context());
        }
        return context().page();
    }

    /**
   * @return
   */
    public WOComponent duplicatePresentationDocumentAction() {
        EOGlobalID gid = presentation.editingContext().globalIDForObject((EOEnterpriseObject) presentation);
        PXPresentationDocument document = (PXPresentationDocument) controller.openDocumentWithContentsAtGlobalID(gid);
        if (document != null) {
            PDBPresentation presentation = (PDBPresentation) ((PDBPresentationDocument) document).storedDocument();
            presentation.duplicate();
        }
        return context().page();
    }

    /**
   * @return
   */
    public WOComponent deletePresentationConfirmationAction() {
        EOGlobalID gid = presentation.editingContext().globalIDForObject((EOEnterpriseObject) presentation);
        WOComponent page = MC.mcfactory().pageForTaskTypeTarget("delete", "pachyderm.presentation", "web", session());
        PXPresentationDocument document = (PXPresentationDocument) controller.openDocumentWithContentsAtGlobalID(gid);
        page.takeValueForKey(gid, "gid");
        page.takeValueForKey(controller, "pageInControl");
        page.takeValueForKey(document, "presentation");
        LOG.info("page name: " + page.name());
        LOG.info("page: " + page.toString());
        return page;
    }

    /**
   * @return
   */
    public WOComponent deletePresentation() {
        EOGlobalID gid = presentation.editingContext().globalIDForObject((EOEnterpriseObject) presentation);
        NSArray<?> documents = controller.documents();
        CXAbstractDocument document;
        int count = documents.count();
        boolean flag = false;
        for (int i = 0; i < count && flag == false; i++) {
            document = (CXAbstractDocument) documents.objectAtIndex(i);
            if (document.isEOBased()) {
                CXAbstractDocument pdoc = document;
                if (gid.equals(pdoc.globalID())) {
                    controller.removeDocument(document);
                    EOEnterpriseObject eo = pdoc.storedDocument();
                    EOEditingContext ec = pdoc.getEditingContext();
                    ec.deleteObject(eo);
                    ec.saveChanges();
                    flag = true;
                }
            }
        }
        if (flag == false) {
            EOEditingContext ec = ERXEC.newEditingContext();
            ec.setSharedEditingContext(null);
            EOEntity entity = EOModelGroup.defaultGroup().entityNamed("PDBPresentation");
            NSDictionary<?, ?> pkDict = entity.primaryKeyForGlobalID(gid);
            try {
                EOEnterpriseObject eo = EOUtilities.objectWithPrimaryKey(ec, "PDBPresentation", pkDict);
                ec.deleteObject(eo);
                ec.saveChanges();
                flag = true;
            } catch (Exception x) {
                LOG.error("deletePresentation: ", x);
            }
        }
        if (flag == false) {
        }
        return context().page();
    }

    /**
   * @return
   */
    public WOComponent viewPresentation() {
        return context().page();
    }

    /**
   * @return
   */
    public String presentationURL() {
        String PublishedPresoURL = CXDefaults.sharedDefaults().getString("PresosURL");
        return PublishedPresoURL + "/" + PXUtility.stripNonAlphaNumerics(presentation.title() + presentation.valueForKey("id").toString()) + "/" + "index.html";
    }

    /**
   * @return
   */
    public boolean isPresentationPublished() {
        return PXUtility.detectPresentationPublishState(presentation());
    }

    private PXPresentationDocument presentation() {
        EOGlobalID gid = presentation.editingContext().globalIDForObject((EOEnterpriseObject) presentation);
        return (PXPresentationDocument) session.getDocumentController().openDocumentWithContentsAtGlobalID(gid);
    }

    /**
   * @return
   */
    public WOComponent newPresentation() {
        String type = controller._defaultType();
        LOG.info("newPresentation(): controller.defaultType() == " + type);
        CXAbstractDocument document = controller.openUntitledDocumentOfType(type);
        LOG.info("newPresentation(): document == " + document);
        if (session.hasSessionPerson() && document instanceof PDBPresentationDocument) {
            LOG.info("newPresentation(): Session has known person and document is instance of PDBPresentationDocument.\n");
            PDBPresentation presentation = (PDBPresentation) ((PDBPresentationDocument) document).storedDocument();
            presentation.takeStoredValueForKey(session.getSessionPerson().stringId(), "author");
        }
        MCPage page = (MCPage) pageWithName(NewPresentationPage.class.getName());
        page.setDocument(document);
        return page;
    }

    public WOComponent iconDisplayMode() {
        _setDisplayMode(IconDisplayMode);
        return context().page();
    }

    public WOComponent listDisplayMode() {
        _setDisplayMode(ListDisplayMode);
        return context().page();
    }

    private void _setDisplayMode(int mode) {
        session.setObjectForKey(mode, "PresentationsDisplayMode");
    }

    public boolean isIconDisplayMode() {
        return (_getDisplayMode() == IconDisplayMode);
    }

    public boolean isListDisplayMode() {
        return (_getDisplayMode() == ListDisplayMode);
    }

    private int _getDisplayMode() {
        return (Integer) session.objectForKey("PresentationsDisplayMode");
    }
}
