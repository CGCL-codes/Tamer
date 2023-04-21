package org.plazmaforge.studio.appmanager.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.plazmaforge.studio.appmanager.model.ConfigurationTreeContentProvider;
import org.plazmaforge.studio.appmanager.model.ConfigurationTreeLabelProvider;
import org.plazmaforge.studio.core.model.nodes.DefaultNode;

/** 
 * @author Oleh Hapon
 * $Id: OverviewOutlinePage.java,v 1.3 2010/04/28 06:37:26 ohapon Exp $
 */
public class OverviewOutlinePage extends ContentOutlinePage implements IContentOutlinePage {

    public void createControl(Composite parent) {
        super.createControl(parent);
        TreeViewer viewer = this.getTreeViewer();
        viewer.setContentProvider(new ConfigurationTreeContentProvider());
        viewer.setLabelProvider(new ConfigurationTreeLabelProvider());
        DefaultNode root = new DefaultNode();
        root.setName("Root");
        DefaultNode child = new DefaultNode();
        child.setName("Child-1");
        child.setCategoryParent();
        root.addChild(child);
        child = new DefaultNode();
        child.setName("Child-2");
        child.setCategoryParent();
        root.addChild(child);
        viewer.setInput(root);
    }
}
