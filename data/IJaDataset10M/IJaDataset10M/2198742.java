package es.prodevelop.cit.gvsig.arcims.gui.toc;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.TocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.gui.FPopupMenu;
import es.prodevelop.cit.gvsig.arcims.fmap.layers.FFeatureLyrArcIMS;
import es.prodevelop.cit.gvsig.arcims.fmap.layers.FRasterLyrArcIMS;
import es.prodevelop.cit.gvsig.arcims.gui.dialogs.ArcImsPropsDialog;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

/**
 * This class implements the layer properties menu item that is
 * added to the ArcIMS layer's popup menu.
 *
 * @author jldominguez
 */
public class ArcImsPropsTocMenuEntry extends TocMenuEntry {

    private JMenuItem propsMenuItem;

    FLayer lyr = null;

    private ArcImsPropsDialog propsDialog;

    public void initialize(FPopupMenu m) {
        super.initialize(m);
        if (isTocItemBranch()) {
            lyr = getNodeLayer();
            if ((lyr instanceof FRasterLyrArcIMS)) {
                propsMenuItem = new JMenuItem(PluginServices.getText(this, "arcims_properties"));
                getMenu().add(propsMenuItem);
                propsMenuItem.setFont(FPopupMenu.theFont);
                getMenu().setEnabled(true);
                propsMenuItem.addActionListener(this);
            }
            if ((lyr instanceof FFeatureLyrArcIMS)) {
                propsMenuItem = new JMenuItem(PluginServices.getText(this, "arcims_properties"));
                getMenu().add(propsMenuItem);
                propsMenuItem.setFont(FPopupMenu.theFont);
                getMenu().setEnabled(true);
                propsMenuItem.addActionListener(this);
            }
        }
    }

    /**
     * Creates an ArcImsPropsDialog object and adds it to the MDIManager.
     */
    public void actionPerformed(ActionEvent e) {
        lyr = getNodeLayer();
        if (lyr instanceof FRasterLyrArcIMS) {
            propsDialog = new ArcImsPropsDialog((FRasterLyrArcIMS) lyr);
            PluginServices.getMDIManager().addWindow(propsDialog);
        }
        if (lyr instanceof FFeatureLyrArcIMS) {
            propsDialog = new ArcImsPropsDialog((FFeatureLyrArcIMS) lyr);
            PluginServices.getMDIManager().addWindow(propsDialog);
        }
    }
}
