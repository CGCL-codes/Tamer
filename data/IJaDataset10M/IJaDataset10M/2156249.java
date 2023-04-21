package de.tmtools.ui.view.tm4j.panckoucke;

import org.eclipse.swt.widgets.Composite;
import org.tm4j.tmnav.renderer.tg.TMOTouchgraphNode;
import org.tm4j.tmnav.app.util.Util;
import org.tm4j.panckoucke.model.AMMember;
import org.tm4j.panckoucke.model.AMGestaltRegistry;
import org.tm4j.panckoucke.model.AMLocator;
import org.tm4j.topicmap.Topic;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.DragNodeUI;
import de.tmtools.core.Activator;
import de.tmtools.core.tm4j.TopicMapModel;

public class TouchgraphMouseListener extends MouseAdapter {

    TGPanel tgPanel;

    Composite swtParentPanel;

    DragNodeUI dragNodeUI;

    TopicMapModel model;

    public TouchgraphMouseListener(TGPanel tgPanel, Composite swtParentPanel) {
        this.tgPanel = tgPanel;
        this.swtParentPanel = swtParentPanel;
        dragNodeUI = new DragNodeUI(tgPanel);
    }

    public AMMember getMember(MouseEvent e) {
        TMOTouchgraphNode popupNode = (TMOTouchgraphNode) tgPanel.getMouseOverN();
        if (popupNode != null) {
            return popupNode.getAbstractModelMember();
        } else {
            return null;
        }
    }

    public void setTopicMapModel(TopicMapModel model) {
        this.model = model;
    }

    public void mousePressed(MouseEvent e) {
        if (!e.isPopupTrigger() && tgPanel.getMouseOverN() != null) dragNodeUI.activate(e);
    }

    public void mouseClicked(MouseEvent e) {
        TMOTouchgraphNode mouseOverN = (TMOTouchgraphNode) tgPanel.getMouseOverN();
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0) return;
        if ((mouseOverN == null) || (e.getClickCount() != 1)) return;
        if (mouseOverN.edgeCount() > mouseOverN.visibleEdgeCount()) {
            openPopupChooser(mouseOverN, e);
        } else {
            Component src = e.getComponent();
            if ((src != null) && (!src.isEnabled())) {
                return;
            }
            AMMember mem = getMember(e);
            if (mem == null) {
                super.mouseClicked(e);
            } else {
                if (AMGestaltRegistry.GESTALT_OCCURRENCE.equals(mem.getGestalt()) || mem.getRepresentedObjects().size() == 0) {
                    AMLocator loc = mem.getDataLocator();
                    if (loc != null) {
                        Util.openURL(loc.getAddress());
                    } else {
                    }
                } else {
                    final Topic topic_current = model.getTopicByMember(mem);
                    swtParentPanel.getDisplay().syncExec(new Runnable() {

                        public void run() {
                            Activator.getDefault().setSelectedTopic(topic_current);
                        }
                    });
                }
            }
        }
    }

    /**
     * Opens a PopupMenu with all Nodes, that are connected to
     * Node n
     */
    private void openPopupChooser(TMOTouchgraphNode n, MouseEvent ev) {
    }
}
