package it.univaq.di.ChameleonIDE.ajavaeditor;

import it.univaq.di.ChameleonIDE.ajavamodel.AJavaResourceDefinitions;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.*;

/**
 * Contributes interesting AJava actions to the desktop's Edit menu and the toolbar.
 * => Manages the installation and de-installation of global actions for the default text editor. 
 */
public class AJavaActionContributor extends TextEditorActionContributor {

    protected RetargetTextEditorAction fContentAssistProposal;

    protected RetargetTextEditorAction fContentFormatProposal;

    /**
	 * Default constructor.
	 */
    public AJavaActionContributor() {
        super();
        fContentAssistProposal = new RetargetTextEditorAction(AJavaResourceDefinitions.getResourceBundle(), "ContentAssistProposal.");
        fContentAssistProposal.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        fContentFormatProposal = new RetargetTextEditorAction(AJavaResourceDefinitions.getResourceBundle(), "ContentFormatProposal.");
    }

    public void init(IActionBars bars) {
        super.init(bars);
        IMenuManager menuManager = bars.getMenuManager();
        IMenuManager editMenu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
        if (editMenu != null) {
            editMenu.add(new Separator());
            editMenu.add(fContentAssistProposal);
            editMenu.add(fContentFormatProposal);
        }
        IToolBarManager toolBarManager = bars.getToolBarManager();
        if (toolBarManager != null) {
            toolBarManager.add(new Separator());
        }
    }

    private void doSetActiveEditor(IEditorPart part) {
        super.setActiveEditor(part);
        ITextEditor editor = null;
        if (part instanceof ITextEditor) editor = (ITextEditor) part;
        fContentAssistProposal.setAction(getAction(editor, "ContentAssistProposal"));
        fContentFormatProposal.setAction(getAction(editor, "ContentFormatProposal"));
    }

    public void setActiveEditor(IEditorPart part) {
        super.setActiveEditor(part);
        doSetActiveEditor(part);
    }

    public void dispose() {
        doSetActiveEditor(null);
        super.dispose();
    }
}
