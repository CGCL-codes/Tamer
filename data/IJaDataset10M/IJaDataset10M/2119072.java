package org.openscience.cdk.applications.jchempaint.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import org.openscience.cdk.applications.jchempaint.JChemPaintModel;
import org.openscience.cdk.renderer.Renderer2DModel;

/**
 * Opens a dialog allowing to adjust the coloring scheme for 
 * drawn molecules
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 */
public class SetColorSchemeAction extends JCPAction {

    private static final long serialVersionUID = 8812815445839360119L;

    public SetColorSchemeAction() {
        super();
    }

    public void actionPerformed(ActionEvent e) {
        logger.info("Changing color scheme to: ", type);
        if (jcpPanel.getJChemPaintModel() != null) {
            JChemPaintModel jcpmodel = jcpPanel.getJChemPaintModel();
            Renderer2DModel renderModel = jcpmodel.getRendererModel();
            if ("blackOnWhite".equals(type)) {
                renderModel.setForeColor(Color.BLACK);
                renderModel.setBackColor(Color.WHITE);
            } else if ("whiteOnBlack".equals(type)) {
                renderModel.setForeColor(Color.WHITE);
                renderModel.setBackColor(Color.BLACK);
            }
            jcpmodel.fireChange();
        }
    }
}
