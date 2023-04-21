package org.pachyderm.woc;

import java.io.File;
import org.apache.log4j.Logger;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.authoring.PXContextualItem;
import org.pachyderm.authoring.ScreenTemplateSelectorNPD;
import org.pachyderm.foundation.PXBuildController;
import org.pachyderm.foundation.PXBuildJob;
import org.pachyderm.foundation.PXBuildTarget;
import org.pachyderm.foundation.PXBundle;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.PXUtility;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

/**
 * @author jarcher
 *
 */
public class EditPresentationPage extends MCPage implements EditPageInterface {

    private static Logger LOG = Logger.getLogger(EditPresentationPage.class.getName());

    private static final long serialVersionUID = 8409654368341410387L;

    public PXScreen screen;

    private static final String PresViewDemoIdentifier = "PresViewDemo";

    private static final String PresPublishIdentifier = "PresPublish";

    private static final int IconDisplayMode = 0;

    private static final int ListDisplayMode = 1;

    public EditPresentationPage(WOContext context) {
        super(context);
        session().setObjectForKey(new String(), "collapseExpandInfo");
    }

    /**
	 * @return
	 */
    public PXPresentationDocument presentation() {
        return (PXPresentationDocument) getDocument();
    }

    public String presentationURL() {
        String presentationURL = CXDefaults.sharedDefaults().getString("PresosURL") + File.separator + PXUtility.stripNonAlphaNumerics(((PXPresentationDocument) getDocument()).getInfoModel().title() + ((PXPresentationDocument) getDocument()).getInfoModel()._id()) + File.separator + "index.html";
        return presentationURL;
    }

    /**
	 * @return
	 */
    public String primaryTitle() {
        return ((PXPresentationDocument) getDocument()).getScreenModel().getPrimaryScreen().title();
    }

    /**
	 * @return
	 */
    public NSTimestamp primaryModificationDate() {
        return ((PXPresentationDocument) getDocument()).getScreenModel().getPrimaryScreen().dateModified();
    }

    /**
	 * @return
	 */
    public String zipURL() {
        String zipURL = CXDefaults.sharedDefaults().getString("PresosURL") + File.separator + PXUtility.stripNonAlphaNumerics(((PXPresentationDocument) getDocument()).getInfoModel().title() + ((PXPresentationDocument) getDocument()).getInfoModel()._id()) + ".zip";
        return zipURL;
    }

    public PXScreen primaryScreenSummary() {
        return ((PXPresentationDocument) getDocument()).getScreenModel().getPrimaryScreen();
    }

    public NSArray<?> secondaryScreens() {
        return EOSortOrdering.sortedArrayUsingKeyOrderArray(presentation().getScreenModel().screensExcludingPrimary(), new NSArray<EOSortOrdering>(EOSortOrdering.sortOrderingWithKey("dateModified", EOSortOrdering.CompareCaseInsensitiveDescending)));
    }

    public String linkCount() {
        return "1";
    }

    /**
 * @return
 */
    public WOComponent makePrimaryAction() {
        LOG.info("[[ PRIMARY ]]");
        presentation().getScreenModel().setPrimaryScreen(screen);
        return context().page();
    }

    /**
	 * @return
	 */
    public WOComponent editScreenAction() {
        LOG.info("[[  EDIT  ]]");
        return _editScreen(screen);
    }

    /**
	 * @return
	 */
    public WOComponent editPrimaryScreenAction() {
        LOG.info("[[ EDIT PRIMARY ]]");
        return _editScreen(presentation().getScreenModel().getPrimaryScreen());
    }

    /**
	 * @return
	 */
    public WOComponent deleteScreenAction() {
        LOG.info("[[ DELETE ]]");
        PXPresentationDocument presentation = presentation();
        if (screen != null) {
            presentation.getScreenModel().removeScreen(screen);
            presentation.saveDocument();
        }
        return context().page();
    }

    /**
	 * @return
	 */
    public WOComponent deleteScreenConfirmationAction() {
        if (screen != null) {
            PXPresentationDocument presentation = presentation();
            WOComponent page = MC.mcfactory().pageForTaskTypeTarget("delete", "pachyderm.screen", "web", session());
            page.takeValueForKey(screen, "screen");
            page.takeValueForKey(presentation, "presentation");
            LOG.info("deleteScreenConfirmationAction: test page=" + page.baseURL() + " " + page.pathURL());
            return page;
        }
        LOG.error("deleteScreenConfirmationAction: no page");
        return context().page();
    }

    /**
	 * @return
	 */
    public WOComponent deletePrimaryScreenAction() {
        return context().page();
    }

    private WOComponent _editScreen(PXScreen screen) {
        EditPageInterface page = MC.mcfactory().editPageForTypeTarget("pachyderm.screen", "web", session());
        page.setObject(screen);
        return (WOComponent) page;
    }

    /**
	 *  Adds a new Screen to the Presentation
	 */
    public WOComponent addScreen() {
        MCPage page = MC.mcfactory().pageForTaskTypeTarget("select", "pachyderm.template.component", "web", session());
        page.setNextPageDelegate(new ScreenTemplateSelectorNPD());
        return (WOComponent) page;
    }

    /**
	 * @return
	 */
    public WOComponent editPresentationInfo() {
        MCPage page = MC.mcfactory().pageForTaskTypeTarget("edit", "pachyderm.presentation", "web", context(), new NSDictionary<String, Object>("presinfo", "subtype"));
        page.setNextPage(this);
        return page;
    }

    /**
	 *  Description of the Method
	 *
	 * @param  page  Description of the Parameter
	 * @return       Description of the Return Value
	 */
    public NSArray contextualItemIdentifiers(WOComponent page) {
        return (NSArray) getLocalContext().valueForKey("contextualIdentifiers");
    }

    /**
	 *  Description of the Method
	 *
	 * @param  page        Description of the Parameter
	 * @param  identifier  Description of the Parameter
	 * @return             Description of the Return Value
	 */
    public PXContextualItem contextualItemForItemIdentifier(WOComponent page, String identifier) {
        PXContextualItem item = new PXContextualItem(identifier);
        if (PresViewDemoIdentifier.equals(identifier)) {
            item.setLabel("View Demo");
            item.setTargetAction(this, "handleViewDemoCommand");
        } else if (PresPublishIdentifier.equals(identifier)) {
            item.setLabel("Publish");
            item.setTargetAction(this, "handlePublishCommand");
        } else {
            item = null;
        }
        return item;
    }

    /**
	 * @return
	 */
    public WOComponent handleViewDemoCommand() {
        LOG.info("handleViewDemoCommand");
        return context().page();
    }

    /**
	 * @return
	 */
    public WOComponent handlePublishCommand() {
        WOComponent page = MC.mcfactory().pageForTaskTypeTarget("publish", "pachyderm.presentation", "web", session());
        return page;
    }

    /**
	 * @return
	 */
    public WOComponent previewCurrentScreen() {
        LOG.info("previewCurrentScreen: calling handleScreenPreviewCommand(" + screen + ")");
        return handleScreenPreviewCommand(screen);
    }

    /**
	 * @return
	 */
    public WOComponent previewHomeScreen() {
        PXPresentationDocument document = (PXPresentationDocument) getDocument();
        PXScreen screen = document.getScreenModel().getPrimaryScreen();
        LOG.info("previewHomeScreen: calling handleScreenPreviewCommand(" + screen + ")");
        return handleScreenPreviewCommand(screen);
    }

    private WOComponent handleScreenPreviewCommand(PXScreen screenToPreview) {
        PXPresentationDocument document = (PXPresentationDocument) getDocument();
        String presentationIdentifier = PXUtility.stripNonAlphaNumerics(document.getInfoModel().title() + document.getInfoModel()._id()) + "p" + System.currentTimeMillis();
        String bundlePath = CXDefaults.sharedDefaults().getString("AbsUploadPresosDir") + File.separator + presentationIdentifier;
        LOG.info("handleScreenPreviewCommand: bundlePath = " + bundlePath);
        PXBundle bundle = PXBundle.bundleWithPath(bundlePath);
        NSMutableDictionary<String, Object> parameters = new NSMutableDictionary<String, Object>(new Object[] { getDocument(), bundle, PXBuildTarget.systemTargetForIdentifier(PXBuildTarget.WebPreviewSysIdentifier), new NSArray(screenToPreview) }, new String[] { PXBuildJob.PresentationParamKey, PXBuildJob.BundleParamKey, PXBuildJob.TargetParamKey, "Screens" });
        PXBuildController controller = PXBuildController.defaultController();
        PXBuildJob job = controller.prepareJobWithParameters(parameters);
        controller._doJobNowIgnoringEveryoneElseAndDontReturnUntilDoneOrElseDotDotDot(job);
        LOG.info("handleScreenPreviewCommand: ScreenPreviewPage built.");
        ScreenPreviewPage previewPage = (ScreenPreviewPage) pageWithName(ScreenPreviewPage.class.getName());
        previewPage.getLocalContext().takeValueForKey("minimal", "style");
        LOG.info("handleScreenPreviewCommand: previewPage = " + previewPage);
        previewPage.setPresentationRootURL(CXDefaults.sharedDefaults().getString("PresosURL") + "/" + presentationIdentifier + "/");
        LOG.info("handleScreenPreviewCommand: presentation Root URL = " + previewPage.getPresentationRootURL());
        previewPage.setNextPage(this);
        LOG.info("handleScreenPreviewCommand: returning previewPage...");
        return previewPage;
    }

    /**
	 * @return
	 */
    public WOComponent iconsDisplayMode() {
        _setDisplayMode(IconDisplayMode);
        return context().page();
    }

    /**
	 * @return
	 */
    public WOComponent listDisplayMode() {
        _setDisplayMode(ListDisplayMode);
        return context().page();
    }

    private void _setDisplayMode(int mode) {
        ((CXSession) session()).setObjectForKey(mode, "ScreensDisplayMode");
    }

    private int _displayMode() {
        return (Integer) ((CXSession) session()).objectForKey("ScreensDisplayMode");
    }

    /**
	 * @return
	 */
    public boolean displayModeIsIcons() {
        return (_displayMode() == IconDisplayMode);
    }

    /**
	 * @return
	 */
    public boolean displayModeIsList() {
        return (_displayMode() == ListDisplayMode);
    }

    /**
	 *  View Presentation
	 *
	 * @return	WOComponent presentation view
	 */
    public WOComponent viewPresentation() {
        if (PXUtility.detectPresentationPublishState((PXPresentationDocument) getDocument())) {
            WORedirect redirect = new WORedirect(this.context());
            redirect.setUrl(presentationURL());
            return redirect;
        }
        WOComponent publisher = handlePublishCommand();
        return publisher;
    }

    /**
	 *  Gets the published attribute of the EditPresentationPage object
	 *
	 * @return    The published value
	 */
    public boolean isPublished() {
        return PXUtility.detectPresentationPublishState((PXPresentationDocument) getDocument());
    }

    /**
	 * @return
	 */
    public WOComponent intentionallyThrowException() {
        throw new IllegalStateException("A fake exception.");
    }
}
