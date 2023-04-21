package com.lolcode.editors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.ResourceAction;
import com.lolcode.eclipselol.ui.Activator;

/**
 * This class is used to edit LOL code files.
 * 
 * @author J. Suereth
 *
 */
public class LOLCodeEditor extends TextEditor implements IPropertyChangeListener {

    private ColorManager colorManager;

    private LOLConfiguration configuration;

    public LOLCodeEditor() {
        super();
        colorManager = new ColorManager();
        configuration = new LOLConfiguration(colorManager);
        setSourceViewerConfiguration(configuration);
        setDocumentProvider(new LOLDocumentProvider());
        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent event) {
        colorManager.updateColor(event.getProperty());
        refresh();
    }

    protected void refresh() {
        ((ISourceViewerExtension2) getSourceViewer()).unconfigure();
        configuration = new LOLConfiguration(colorManager);
        getSourceViewer().configure(configuration);
    }

    public void dispose() {
        colorManager.dispose();
        Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
        super.dispose();
    }
}
