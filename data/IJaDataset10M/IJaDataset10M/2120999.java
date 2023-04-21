package joodin.impl.widgets.internal;

import java.util.HashSet;
import java.util.Set;
import joodin.impl.image.VaadinImageRegistry;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.spi.impl.controler.TreeNodeObservable;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.ITreeNodeSpi;
import org.jowidgets.util.Assert;
import com.vaadin.data.Item;
import com.vaadin.terminal.Resource;

public class TreeNodeImpl extends TreeNodeObservable implements ITreeNodeSpi {

    private final Set<IPopupDetectionListener> popupDetectionListeners;

    private final TreeImpl parentTree;

    private final Item item;

    private final Integer id;

    public TreeNodeImpl(final TreeImpl parentTree, final Integer parentItemId, final Integer index) {
        super();
        Assert.paramNotNull(parentTree, "parentTree");
        this.popupDetectionListeners = new HashSet<IPopupDetectionListener>();
        this.parentTree = parentTree;
        parentTree.getUiReference().getItemIds();
        this.id = (Integer) parentTree.getUiReference().addItem();
        this.item = parentTree.getUiReference().getItem(id);
        parentTree.getUiReference().setChildrenAllowed(id, false);
        if (index != null) {
            if (parentItemId != null) {
                parentTree.getUiReference().setParent(id, parentItemId);
            }
        } else {
            if (parentItemId != null) {
                parentTree.getUiReference().setParent(id, parentItemId);
            }
        }
    }

    @Override
    public Item getUiReference() {
        return item;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (!enabled) {
            throw new UnsupportedOperationException("Tree items could not be disabled");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setText(final String text) {
        if (text != null) {
            parentTree.getUiReference().setItemCaption(id, text);
        } else {
            parentTree.getUiReference().setItemCaption(id, "");
        }
    }

    @Override
    public void setToolTipText(final String toolTipText) {
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        if (icon == null) {
            return;
        }
        Resource res = VaadinImageRegistry.getInstance().getImage(icon);
        parentTree.getUiReference().setItemIcon(id, res);
    }

    @Override
    public void setMarkup(final Markup markup) {
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
    }

    @Override
    public void setExpanded(final boolean expanded) {
        if (expanded) {
            parentTree.getUiReference().expandItem(id);
        } else {
            parentTree.getUiReference().collapseItem(id);
        }
    }

    @Override
    public boolean isExpanded() {
        return parentTree.getUiReference().isExpanded(id);
    }

    @Override
    public void setSelected(final boolean selected) {
        if (selected) {
            parentTree.getUiReference().select(id);
        } else {
            parentTree.getUiReference().unselect(id);
        }
    }

    @Override
    public boolean isSelected() {
        return parentTree.getUiReference().isSelected(id);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.add(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        popupDetectionListeners.remove(listener);
    }

    protected void firePopupDetected(final Position position) {
        for (final IPopupDetectionListener listener : popupDetectionListeners) {
            listener.popupDetected(position);
        }
    }

    @Override
    public ITreeNodeSpi addNode(final Integer index) {
        parentTree.getUiReference().setChildrenAllowed(id, true);
        final TreeNodeImpl result = new TreeNodeImpl(parentTree, id, index);
        parentTree.registerItem(result.getUiReference(), result);
        return result;
    }

    @Override
    public void removeNode(final int index) {
        parentTree.unRegisterItem(getUiReference());
        if (parentTree.getUiReference().getChildren(parentTree.getUiReference().getParent(id)).size() < 1) {
            parentTree.getUiReference().setChildrenAllowed(parentTree.getUiReference().getParent(id), false);
        }
        parentTree.getUiReference().removeItem(id);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return new PopupMenuImpl(parentTree.getUiReference());
    }
}
