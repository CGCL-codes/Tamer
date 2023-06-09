package org.deved.antlride.stringtemplate.internal.ui.text;

import org.deved.antlride.common.ui.SingleProjectProblem;
import org.deved.antlride.stringtemplate.internal.ui.editor.StringTemplateOutlineLabelDecorator;
import org.deved.antlride.stringtemplate.ui.StringTemplateUI;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.dltk.ui.text.ScriptOutlineInformationControl;
import org.eclipse.dltk.ui.viewsupport.AppearanceAwareLabelProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

public class StringTemplateOutlineInformationControl extends ScriptOutlineInformationControl {

    private ITextEditor fEditor;

    public StringTemplateOutlineInformationControl(ITextEditor editor, Shell parent, int shellStyle, int treeStyle, String commandId) {
        super(parent, shellStyle, treeStyle, commandId);
        fEditor = editor;
    }

    @Override
    protected TreeViewer createTreeViewer(Composite parent, int style) {
        TreeViewer treeViewer = super.createTreeViewer(parent, style);
        AppearanceAwareLabelProvider labelProvider = (AppearanceAwareLabelProvider) treeViewer.getLabelProvider();
        labelProvider.addLabelDecorator(new StringTemplateOutlineLabelDecorator());
        return treeViewer;
    }

    @SuppressWarnings("restriction")
    @Override
    @SingleProjectProblem
    protected Object getSelectedElement() {
        Object selectedElement = super.getSelectedElement();
        dispose();
        if (fEditor != null && selectedElement != null) {
            EditorUtility.revealInEditor(fEditor, (IModelElement) selectedElement);
        }
        return null;
    }

    @Override
    protected IPreferenceStore getPreferenceStore() {
        return StringTemplateUI.getDefault().getPreferenceStore();
    }
}
