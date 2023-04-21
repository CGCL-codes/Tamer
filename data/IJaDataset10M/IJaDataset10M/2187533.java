package org.akrogen.tkui.wizards.awl;

import java.awt.Component;
import javax.swing.JPanel;
import org.akrogen.tkui.core.controller.ITkuiController;
import org.akrogen.tkui.core.controller.ITkuiControllerErrorListener;
import org.akrogen.tkui.core.dom.ITkuiDocument;
import org.akrogen.tkui.core.exceptions.TkuiRuntimeException;
import org.akrogen.tkui.core.gui.GuiConstants;
import org.akrogen.tkui.core.gui.GuiContext;
import org.akrogen.tkui.core.gui.wizards.ITkuiWizard;
import org.akrogen.tkui.core.gui.wizards.ITkuiWizardContainer;
import org.akrogen.tkui.core.gui.wizards.ITkuiWizardPage;
import org.akrogen.tkui.core.loader.IScriptableLoader;
import org.akrogen.tkui.core.loader.ITkuiLoader;
import org.akrogen.tkui.core.resources.ITkuilURIResolver;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.NavigationAuthorization;
import org.awl.Wizard;
import org.w3c.dom.Element;

public class TkuiWizardPage extends DefaultWizardPageDescriptor implements ITkuiWizardPage {

    private String pageId;

    private TkuiWizard wizard;

    /**
	 * Gui context (GUI Builder, URI Resolver...)
	 */
    protected GuiContext guiContext;

    /**
	 * XML source Element.
	 */
    protected Element sourceElement;

    /**
	 * True if global script was already executed on page show and false
	 * otherwise.
	 */
    protected boolean globalScriptWasAlredyBeenExecuted;

    /**
	 * Tkui Document
	 */
    protected ITkuiDocument tkuiDocument = null;

    public TkuiWizardPage(String pageId, GuiContext guiContext, Element sourceElement) {
        this.pageId = pageId;
        this.guiContext = guiContext;
        this.sourceElement = sourceElement;
        this.globalScriptWasAlredyBeenExecuted = false;
    }

    public Component createPageContents() {
        JPanel container = new JPanel();
        if (this.guiContext == null) this.guiContext = ((ITkuiWizard) getWizard()).getGuiContext();
        if (this.guiContext == null) throw new TkuiRuntimeException("GUI context cannot be null!");
        ITkuiLoader tkuiLoader = guiContext.getTkuiLoader();
        String guiBuilderId = guiContext.getGuiBuilderId();
        ITkuilURIResolver uriResolver = guiContext.getURIResolver();
        tkuiDocument = tkuiLoader.newDocument();
        tkuiDocument.setDocumentContainer(container);
        tkuiDocument.setURIResolver(uriResolver);
        tkuiDocument.getController().setLazyExecution(false);
        String id = tkuiLoader.getGuiBuilderId(sourceElement, tkuiDocument);
        if (id != null) guiBuilderId = id; else guiBuilderId = getDefaultGuiBuilderId();
        tkuiDocument.setGuiBuilderId(guiBuilderId);
        try {
            onBeforeGuiDocumentLoaded(tkuiDocument);
            tkuiLoader.loadGuiDocument(sourceElement, tkuiDocument);
            onAfterGuiDocumentLoaded(tkuiDocument);
            addControllerErrorListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return container;
    }

    public void aboutToDisplayPanel(Wizard wizard1) {
        super.aboutToDisplayPanel(wizard1);
        onShow();
    }

    protected void onBeforeGuiDocumentLoaded(ITkuiDocument tkuiDocument) {
    }

    protected void onAfterGuiDocumentLoaded(ITkuiDocument tkuiDocument) {
    }

    /**
	 * Add controller error listner to display icon with error when controller
	 * throw error.
	 */
    protected void addControllerErrorListener() {
        ITkuiController controller = tkuiDocument.getController();
        if (controller != null && this.getWizard() instanceof ITkuiWizardContainer) {
            final ITkuiWizardContainer tkuiContainer = (ITkuiWizardContainer) this.getWizard();
            final ITkuiWizardPage page = this;
            controller.addControllerErrorListener(new ITkuiControllerErrorListener() {

                public void errorThrowed(Exception e) {
                    tkuiContainer.addControllerError(page, e);
                }
            });
        }
    }

    public void onShow() {
        if (!this.globalScriptWasAlredyBeenExecuted) {
            ITkuiLoader tkuiLoader = guiContext.getTkuiLoader();
            if (tkuiLoader instanceof IScriptableLoader) {
                ((IScriptableLoader) tkuiLoader).executeGlobalScripts(tkuiDocument);
            }
            this.globalScriptWasAlredyBeenExecuted = true;
        }
    }

    public ITkuiDocument getDocument() {
        return tkuiDocument;
    }

    public String getPageid() {
        return pageId;
    }

    protected String getDefaultGuiBuilderId() {
        return GuiConstants.SWING_GUI_BUILDER;
    }

    public TkuiWizard getWizard() {
        return wizard;
    }

    public void setWizard(TkuiWizard wizard) {
        this.wizard = wizard;
    }

    public void resetMessage() {
        super.resetMessages();
        this.setPageComplete(true);
    }

    public void setErrorMessage(String message) {
        super.setErrorMessage(message);
        this.setPageComplete(false);
    }

    public void setInfoMessage(String message) {
        super.resetMessages();
        super.setInfoMessage(message);
        this.setPageComplete(true);
    }

    public void setWarnMessage(String message) {
        super.setWarnMessage(message);
        this.setPageComplete(true);
    }

    public void setPageComplete(boolean complete) {
        if (!complete) {
            super.setFinishAuthorization(NavigationAuthorization.FORBIDDEN);
            super.setNextPageAuthorization(NavigationAuthorization.FORBIDDEN);
        } else {
            super.setFinishAuthorization(NavigationAuthorization.DEFAULT);
            super.setNextPageAuthorization(NavigationAuthorization.DEFAULT);
        }
    }
}
