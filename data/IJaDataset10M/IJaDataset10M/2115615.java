package com.aptana.ide.debug.internal.ui.actions;

import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.texteditor.ITextEditor;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.debug.core.model.IJSLineBreakpoint;

/**
 * @author Max Stepanov
 *
 */
public class BreakpointPropertiesRulerAction extends AbstractBreakpointRulerAction {

    /**
	 * Creates the action to enable/disable breakpoints
	 */
    public BreakpointPropertiesRulerAction(ITextEditor editor, IVerticalRulerInfo info) {
        setInfo(info);
        setTextEditor(editor);
        setText(StringUtils.ellipsify(Messages.BreakpointPropertiesRulerAction_BreakpointProperties));
    }

    /**
	 * @see Action#run()
	 */
    public void run() {
        if (getBreakpoint() != null) {
            PropertyDialogAction action = new PropertyDialogAction(new SameShellProvider(getTextEditor().getEditorSite().getShell()), new ISelectionProvider() {

                public void addSelectionChangedListener(ISelectionChangedListener listener) {
                }

                public ISelection getSelection() {
                    return new StructuredSelection(getBreakpoint());
                }

                public void removeSelectionChangedListener(ISelectionChangedListener listener) {
                }

                public void setSelection(ISelection selection) {
                }
            });
            action.run();
        }
    }

    public void update() {
        setBreakpoint(determineBreakpoint());
        if (getBreakpoint() == null || !(getBreakpoint() instanceof IJSLineBreakpoint)) {
            setBreakpoint(null);
            setEnabled(false);
            return;
        }
        setEnabled(true);
    }
}
