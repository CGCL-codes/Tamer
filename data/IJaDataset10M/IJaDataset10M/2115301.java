package org.fudaa.ebli.visuallibrary.actions;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.KeyStroke;
import org.fudaa.ctulu.CtuluCommand;
import org.fudaa.ctulu.CtuluResource;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.EbliWidget;
import org.fudaa.ebli.visuallibrary.WidgetResource;

/**
 * @author deniger
 */
@SuppressWarnings("serial")
public class EbliWidgetActionDelete extends EbliWidgetActionFilteredAbstract {

    public EbliWidgetActionDelete(final EbliScene _scene) {
        super(_scene, EbliLib.getS("Supprimer"), CtuluResource.CTULU.getIcon("crystal_non"), "DELETE_SELECTED");
        setKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        setDefaultToolTip(EbliLib.getS("Supprimer les objets s�lectionn�s"));
    }

    public EbliWidgetActionDelete(final EbliNode node) {
        super(node, EbliLib.getS("Supprimer"), CtuluResource.CTULU.getIcon("crystal_non"), "DELETE_SELECTED");
        setKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        setDefaultToolTip(WidgetResource.getS("Supprimer l'objet"));
    }

    @Override
    protected CtuluCommand act(Set<EbliNode> filteredNode) {
        return deleteNodes(filteredNode);
    }

    @Override
    protected boolean clearSelectionBefore() {
        return true;
    }

    public static CtuluCommand deleteNodes(Collection<EbliNode> filteredNode) {
        Set<EbliNode> allNodeWithSatellite = new HashSet<EbliNode>(filteredNode.size());
        for (EbliNode ebliNode : filteredNode) {
            allNodeWithSatellite.add(ebliNode);
            ebliNode.getController().fillWithSatelliteNodes(allNodeWithSatellite);
        }
        List<EbliNode> nodes = new ArrayList<EbliNode>(allNodeWithSatellite.size());
        List<EbliScene> scenes = new ArrayList<EbliScene>(nodes.size());
        for (final Object obj : allNodeWithSatellite) {
            final EbliNode node = (EbliNode) obj;
            nodes.add(node);
            EbliWidget widget = node.getWidget();
            if (widget != null) {
                EbliScene ebliScene = widget.getEbliScene();
                scenes.add(ebliScene);
                ebliScene.removeNode(node);
            }
        }
        return new CommandSupprimer(nodes, scenes);
    }
}
