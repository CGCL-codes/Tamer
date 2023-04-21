package org.jowidgets.examples.common.workbench.demo2.view;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.ITree;
import org.jowidgets.api.widgets.ITreeNode;
import org.jowidgets.api.widgets.blueprint.ITreeBluePrint;
import org.jowidgets.api.widgets.blueprint.factory.IBluePrintFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.workbench.base.AbstractDemoView;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.workbench.api.IView;
import org.jowidgets.workbench.api.IViewContext;

public class ReportsView extends AbstractDemoView implements IView {

    public static final String ID = ReportsView.class.getName();

    public static final String DEFAULT_LABEL = "Reports";

    public static final String DEFAULT_TOOLTIP = "Reports view";

    public static final IImageConstant DEFAULT_ICON = SilkIcons.ARROW_RIGHT;

    public ReportsView(final IViewContext context) {
        super(ID);
        final IBluePrintFactory bpf = Toolkit.getBluePrintFactory();
        final IContainer container = context.getContainer();
        container.setLayout(new MigLayoutDescriptor("0[grow, 0::]0", "[grow, 0::]"));
        final ITreeBluePrint treeBp = bpf.tree();
        final ITree tree = container.add(treeBp, MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
        container.setBackgroundColor(tree.getBackgroundColor());
        final ITreeNode externalNode = tree.addNode(bpf.treeNode().setText("External").setIcon(SilkIcons.REPORT));
        final ITreeNode internalNode = tree.addNode(bpf.treeNode().setText("Internal").setIcon(SilkIcons.EMAIL));
        externalNode.addNode(bpf.treeNode().setText("Customer Report").setIcon(SilkIcons.REPORT_GO));
        externalNode.addNode(bpf.treeNode().setText("Info Post").setIcon(SilkIcons.GROUP_GO));
        internalNode.addNode(bpf.treeNode().setText("Malcom").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Paul").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Marry").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Lisa").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Pete").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Bruce").setIcon(SilkIcons.USER_GO));
        internalNode.addNode(bpf.treeNode().setText("Joe").setIcon(SilkIcons.USER_GO));
        externalNode.setExpanded(true);
        context.getToolBar().addActionItem(null, "Add external", SilkIcons.REPORT);
        context.getToolBar().addActionItem(null, "Add mail", SilkIcons.EMAIL);
    }
}
