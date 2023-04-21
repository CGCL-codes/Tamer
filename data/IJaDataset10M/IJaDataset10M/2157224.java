package org.akrogen.tkui.ui.swtform.controls.trees;

import org.akrogen.tkui.core.ui.elements.trees.IUITree;
import org.akrogen.tkui.ui.swt.controls.trees.SWTTreeImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * {@link IUITree} implementation with SWT Form {@link Tree}.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SWTFormTreeImpl extends SWTTreeImpl {

    private static Log logger = LogFactory.getLog(SWTFormTreeImpl.class);

    protected FormToolkit formToolkit;

    public SWTFormTreeImpl(FormToolkit formToolkit) {
        this.formToolkit = formToolkit;
    }

    protected Tree createTree(Composite parent, int style) {
        Tree tree = formToolkit.createTree(parent, style);
        if (logger.isDebugEnabled()) logger.debug("Create SWT Form Tree.");
        return tree;
    }

    public void doLayout(boolean refreshParent) {
        super.doLayout(refreshParent);
        if (logger.isDebugEnabled()) logger.debug("Do layout for SWT Form tree.");
    }
}
