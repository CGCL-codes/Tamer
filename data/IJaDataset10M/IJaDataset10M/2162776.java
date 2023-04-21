package net.sf.rcpforms.widgetwrapper.wrapper.advanced.tree;

import org.eclipse.jface.viewers.ILabelProvider;

public class AdvColumnConfigUsesLabelProvider<ROW_TYPE extends TreeNode3> extends RCPAdvancedTreeColumn<ROW_TYPE> {

    public AdvColumnConfigUsesLabelProvider(final String header, final String property, final int size, final int style, final ILabelProvider labelProvider) {
        super(header, property, size, style);
        set2LabelProvider(labelProvider);
    }
}
