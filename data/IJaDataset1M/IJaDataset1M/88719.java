package es.eucm.eadventure.editor.gui.metadatadialog.lomes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMContributeDialog;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.metadata.lomes.LOMESMetaMetaDataControl;

public class LOMESMetaMetaDataPanel extends JPanel {

    public LOMESMetaMetaDataPanel(LOMESMetaMetaDataControl metaController) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        LOMESTextPanel meta = new LOMESTextPanel(metaController.getMetadataschemeController(), TC.get("IMS.MetaMetaData.Metadatascheme"), LOMESTextPanel.TYPE_FIELD);
        LOMESCreateContainerPanel identifierPanel = new LOMESCreateContainerPanel(metaController.getIdentifier(), TC.get("LOMES.General.Identifier"), LOMContributeDialog.NONE);
        LOMESCreateContainerPanel contribute = new LOMESCreateContainerPanel(metaController.getContribute(), TC.get("LOMES.LifeCycle.Contribute"), LOMContributeDialog.METAMETADATA);
        LOMESOptionsPanel language = new LOMESOptionsPanel(metaController.getLanguageController(), TC.get("LOMES.MetaMetaData.Language"));
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        container.add(meta, c);
        c.gridy = 1;
        container.add(identifierPanel, c);
        c.gridy = 2;
        container.add(contribute, c);
        c.gridy = 3;
        container.add(language, c);
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1.0;
        add(container, c);
    }
}
