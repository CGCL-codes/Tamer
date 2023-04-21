package org.rubypeople.rdt.refactoring.ui.pages.inlinemethod;

import org.eclipse.jface.window.Window;
import org.jruby.ast.types.INameNode;
import org.rubypeople.rdt.refactoring.core.inlinemethod.ITargetClassFinder;
import org.rubypeople.rdt.refactoring.core.inlinemethod.TargetClassFinder;
import org.rubypeople.rdt.refactoring.documentprovider.IDocumentProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.MethodCallNodeWrapper;
import org.rubypeople.rdt.refactoring.ui.IncludedClassesSelectionDialog;

public class TargetClassFinderUI implements ITargetClassFinder {

    private TargetClassFinder targetClassFinder;

    public TargetClassFinderUI() {
        targetClassFinder = new TargetClassFinder();
    }

    public String findTargetClass(MethodCallNodeWrapper call, IDocumentProvider doc) {
        String result = targetClassFinder.findTargetClass(call, doc);
        if ("".equals(result) && call.getReceiverNode() == null) {
            return "";
        }
        if (result == null || "".equals(result)) {
            final String title = Messages.TargetClassFinderUI_ChooseType + ((INameNode) call.getReceiverNode()).getName() + ':';
            IncludedClassesSelectionDialog dialog = new IncludedClassesSelectionDialog(doc, title, call.getName());
            if (dialog.open() == Window.OK) {
                result = dialog.getSelectedName();
            }
        }
        return result;
    }
}
