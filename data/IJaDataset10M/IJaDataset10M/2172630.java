package org.adapit.wctoolkit.fomda.diagram.featuresdiagram.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.adapit.wctoolkit.fomda.features.view.FeatureRelationViewComponent;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;

public class DeleteFeatureRelationComponentAction implements ActionListener {

    private FeatureRelationViewComponent frvc;

    public DeleteFeatureRelationComponentAction(FeatureRelationViewComponent frvc) {
        super();
        this.frvc = frvc;
    }

    public void actionPerformed(ActionEvent evt) {
        int ans = JOptionPane.showConfirmDialog(DefaultApplicationFrame.getInstance(), FeatureRelationViewComponent.messages.getMessage("Remove_the_raltionship") + "?");
        if (ans == JOptionPane.YES_OPTION) {
            frvc.getParentFeatureComponent().getFeature().getRelations().remove(frvc.getElement());
            frvc.getDiagram().getFeatureRelationViewComponents().remove(frvc);
            frvc.getDiagram().removeOphanNodes(frvc);
            frvc.getDiagram().repaint();
        }
    }
}
