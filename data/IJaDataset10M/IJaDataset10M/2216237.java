package org.digitall.common.resourcescontrol.interfaces.providers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.digitall.common.resourcescontrol.classes.Provider;
import org.digitall.common.resourcescontrol.interfaces.providers.CompanyPanel;
import org.digitall.common.resourcescontrol.interfaces.providers.ProvidersMain;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.inputpanels.TFInput;

public class ProvidersMgmt extends BasicPrimitivePanel {

    private ProvidersMain parentMain;

    private CompanyPanel panel = new CompanyPanel(null, CompanyPanel.PROVIDERLIST);

    private Provider provider;

    private BasicPanel jPanel1 = new BasicPanel();

    public ProvidersMgmt(ProvidersMain _parentMain) {
        try {
            parentMain = _parentMain;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(567, 346));
        this.setPreferredSize(new Dimension(620, 390));
        jPanel1.setMinimumSize(new Dimension(10, 100));
        jPanel1.setPreferredSize(new Dimension(10, 20));
        jPanel1.setOpaque(false);
        jPanel1.setSize(new Dimension(620, 20));
        panel.setSize(new Dimension(560, 345));
        this.add(panel, BorderLayout.CENTER);
        this.add(jPanel1, BorderLayout.NORTH);
        panel.setParentMain(parentMain);
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
        panel.setProvider(provider);
    }
}
