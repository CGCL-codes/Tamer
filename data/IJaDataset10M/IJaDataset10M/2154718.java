package org.fudaa.ebli.visuallibrary.animation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuButton;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.gui.CtuluDialog;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluLibSwing;
import org.fudaa.ebli.animation.EbliAnimatedInterface;
import org.fudaa.ebli.animation.EbliAnimation;
import org.fudaa.ebli.animation.EbliAnimationAction;
import org.fudaa.ebli.animation.EbliAnimationSourceAbstract;
import org.fudaa.ebli.animation.EbliAnimationSourceInterface;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.EbliWidget;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * @author deniger
 */
public class EbliWidgetAnimAdapter extends EbliAnimationSourceAbstract {

    EbliScene scene_;

    EbliAnimationAction act_;

    BuButton optionCp_;

    public EbliWidgetAnimAdapter(final EbliScene _scene) {
        super();
        scene_ = _scene;
    }

    protected void chooseSources() {
        final LayerWidget widget = scene_.getVisu();
        final EbliWidgetAnimTreeNode animsNode = new EbliWidgetAnimTreeNode();
        final List<EbliWidgetAnimTreeNode> animItems = new ArrayList<EbliWidgetAnimTreeNode>();
        findAnimsTreeNode(widget, animsNode, animItems);
        final CtuluDialogPanel pn = new CtuluDialogPanel();
        pn.setLayout(new BuBorderLayout());
        final JXTreeTable treeTable = new JXTreeTable(new EbliWidgetAnimateTreeTableModel(animsNode, Arrays.asList(EbliLib.getS("Titre"), EbliLib.getS("Anim�"))));
        treeTable.expandAll();
        treeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        treeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        treeTable.setTreeCellRenderer(new AnimTreeCellRenderer());
        pn.add(new JScrollPane(treeTable));
        final CtuluDialog dialog = pn.createDialog(CtuluLibSwing.getActiveWindow());
        if (CtuluDialogPanel.isOkResponse(dialog.afficheDialogModal(optionCp_.getLocationOnScreen(), null))) {
            final List<EbliWidgetAnimatedItem> adapter = new ArrayList<EbliWidgetAnimatedItem>();
            for (final EbliWidgetAnimTreeNode item : animItems) {
                if (item.isSelected()) adapter.add(((EbliWidgetAnimatedItem) item.getUserObject()));
            }
            if (adapter.isEmpty()) {
                act_.setAnimAdapter(null);
            } else {
                act_.setAnimAdapter(new EbliWidgetAnimationComposition(scene_, adapter));
            }
        }
    }

    @SuppressWarnings("serial")
    public EbliAnimationAction createAction() {
        if (act_ != null) return act_;
        final EbliAnimation anim = new EbliAnimation();
        optionCp_ = new BuButton(EbliLib.getS("Choisir les sources"));
        optionCp_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                chooseSources();
            }
        });
        anim.setOptionCp(optionCp_);
        act_ = new EbliAnimationAction(this, anim) {
        };
        act_.setEnabled(true);
        return act_;
    }

    @SuppressWarnings("serial")
    private static class AnimTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(final JTree _tree, final Object _value, final boolean _sel, final boolean _expanded, final boolean _leaf, final int _row, final boolean _hasFocus) {
            final Component res = super.getTreeCellRendererComponent(_tree, _value, _sel, _expanded, _leaf, _row, _hasFocus);
            if (_value != null) {
                final DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) _value;
                final EbliWidgetAnimatedItem object = (EbliWidgetAnimatedItem) node.getUserObject();
                if (object != null) {
                    setText(object.getTitle());
                    setIcon(object.getIcon());
                }
            }
            return res;
        }
    }

    private void findAnimsTreeNode(final Widget _widget, final EbliWidgetAnimTreeNode _animsNode, final List<EbliWidgetAnimTreeNode> _animItems) {
        final EbliWidgetAnimationComposition adapter = (EbliWidgetAnimationComposition) getAdapter();
        final List<Widget> children = _widget.getChildren();
        for (final Widget w : children) {
            final EbliWidget ew = (EbliWidget) w;
            final EbliNode n = (EbliNode) scene_.findObject(ew);
            if (n.getWidget() == ew && ew.isVisible()) {
                final EbliAnimatedInterface animatedInterface = ew.getAnimatedInterface();
                if (animatedInterface != null) {
                    final EbliAnimationSourceInterface animationSrc = animatedInterface.getAnimationSrc();
                    final EbliWidgetAnimTreeNode treeNode = new EbliWidgetAnimTreeNode(new EbliWidgetAnimatedItem(animationSrc, null, ew.getId(), null, n.getTitle()));
                    final List<EbliWidgetAnimatedItem> animatedItems = ew.getAnimatedItems();
                    if (CtuluLibArray.isNotEmpty(animatedItems)) {
                        for (final EbliWidgetAnimatedItem ebliWidgetAnimatedItem : animatedItems) {
                            final EbliWidgetAnimTreeNode animTreeNode = new EbliWidgetAnimTreeNode(ebliWidgetAnimatedItem);
                            if (adapter != null && adapter.contains(ebliWidgetAnimatedItem.getAnimated())) {
                                animTreeNode.selected_ = true;
                            }
                            treeNode.add(animTreeNode);
                            _animItems.add(animTreeNode);
                        }
                        _animsNode.add(treeNode);
                    }
                } else {
                    findAnimsTreeNode(ew, _animsNode, _animItems);
                }
            }
        }
    }

    public Component getComponent() {
        return scene_.getView();
    }

    public Dimension getDefaultImageDimension() {
        return scene_.getDefaultImageDimension();
    }

    public BufferedImage produceImage(final int _w, final int _h, final Map _params) {
        return scene_.produceImage(_w, _h, _params);
    }

    public BufferedImage produceImage(final Map _params) {
        return scene_.produceImage(_params);
    }

    public void setVideoMode(final boolean _b) {
        if (_b) {
            final EbliWidgetAnimationComposition cmp = (EbliWidgetAnimationComposition) getAdapter();
            if (cmp.removeKilledItems()) {
                if (cmp.getNbItems() == 0) act_.setAnimAdapter(null);
            }
        }
    }
}
