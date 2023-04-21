package org.fudaa.fudaa.crue.options;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.common.BusinessMessages;
import org.fudaa.dodico.crue.projet.conf.Option;
import org.fudaa.dodico.crue.projet.conf.OptionsEnum;
import org.fudaa.dodico.crue.projet.conf.OptionsManager;
import org.fudaa.dodico.crue.projet.conf.UserConfiguration;
import org.fudaa.dodico.crue.projet.conf.UserOption;
import org.fudaa.fudaa.crue.common.helper.DialogHelper;
import org.fudaa.fudaa.crue.common.log.LogsDisplayer;
import org.fudaa.fudaa.crue.options.node.OptionNode;
import org.fudaa.fudaa.crue.options.node.OptionNodeChildFactory;
import org.fudaa.fudaa.crue.options.services.ConfigurationManagerService;
import org.fudaa.fudaa.crue.options.services.InstallationService;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

final class CrueOptionsPanel extends javax.swing.JPanel implements ExplorerManager.Provider, Lookup.Provider {

    private final ExplorerManager mgr = new ExplorerManager();

    ConfigurationManagerService configurationManagerService = Lookup.getDefault().lookup(ConfigurationManagerService.class);

    InstallationService installationService = Lookup.getDefault().lookup(InstallationService.class);

    Lookup lookup;

    CrueOptionsPanelController controller;

    CrueOptionsPanel(CrueOptionsPanelController controller) {
        initComponents();
        this.controller = controller;
        String txt = installationService.getSiteConfigFile().getAbsolutePath();
        txtSiteFile.setText(txt);
        txtSiteFile.setToolTipText(txt);
        txt = installationService.getUserConfigFile().getAbsolutePath();
        txtUserFile.setText(txt);
        txtUserFile.setToolTipText(txt);
        optionsOutlineView.getOutline().setRootVisible(false);
        this.optionsOutlineView.addPropertyColumn(OptionNode.PROP_VALUE, OptionNode.getPropValueDisplayName(), OptionNode.getPropValueDescription());
        this.optionsOutlineView.addPropertyColumn(OptionNode.PROP_DEFAULT, OptionNode.getPropDefaultDisplayName(), OptionNode.getPropDefaultDescription());
        this.optionsOutlineView.addPropertyColumn(OptionNode.PROP_ID, OptionNode.getPropIdDisplayName(), OptionNode.getPropIdDescription());
        lookup = ExplorerUtils.createLookup(mgr, getActionMap());
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    private void initComponents() {
        jLabelSiteFile = new javax.swing.JLabel();
        txtSiteFile = new javax.swing.JTextField();
        jLabelUserFile = new javax.swing.JLabel();
        txtUserFile = new javax.swing.JTextField();
        optionsOutlineView = new org.openide.explorer.view.OutlineView(NbBundle.getMessage(CrueOptionsPanel.class, "ColumnOptions"));
        jbReloadOptions = new javax.swing.JButton();
        org.openide.awt.Mnemonics.setLocalizedText(jLabelSiteFile, org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "CrueOptionsPanel.jLabelSiteFile.text"));
        txtSiteFile.setEditable(false);
        txtSiteFile.setText(org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "CrueOptionsPanel.txtSiteFile.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jLabelUserFile, org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "CrueOptionsPanel.jLabelUserFile.text"));
        txtUserFile.setEditable(false);
        txtUserFile.setText(org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "CrueOptionsPanel.txtUserFile.text"));
        org.openide.awt.Mnemonics.setLocalizedText(jbReloadOptions, org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "CrueOptionsPanel.jbReloadOptions.text"));
        jbReloadOptions.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReloadOptionsActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(optionsOutlineView, javax.swing.GroupLayout.DEFAULT_SIZE, 1089, Short.MAX_VALUE).addComponent(jbReloadOptions).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelUserFile).addComponent(jLabelSiteFile)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtSiteFile).addComponent(txtUserFile, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelSiteFile).addComponent(txtSiteFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelUserFile).addComponent(txtUserFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(29, 29, 29).addComponent(optionsOutlineView, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbReloadOptions).addContainerGap(24, Short.MAX_VALUE)));
    }

    private void jbReloadOptionsActionPerformed(java.awt.event.ActionEvent evt) {
        if (DialogHelper.showQuestion(org.openide.util.NbBundle.getMessage(CrueOptionsPanel.class, "ReloadOption.WarnNotCancellable.DialogMessage"))) {
            configurationManagerService.reloadOptions();
            load();
        }
    }

    void load() {
        OptionNodeChildFactory childFactory = new OptionNodeChildFactory((configurationManagerService.getOptionsManager()));
        Node rootNode = new AbstractNode(Children.create(childFactory, false), Lookup.EMPTY);
        Node[] nodes = rootNode.getChildren().getNodes();
        for (Node node : nodes) {
            node.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    controller.changed();
                }
            });
        }
        mgr.setRootContext(rootNode);
    }

    void store() {
        if (!valid(true)) {
            return;
        }
        Node[] nodes = mgr.getRootContext().getChildren().getNodes();
        List<UserOption> userNode = new ArrayList<UserOption>();
        for (Node node : nodes) {
            OptionNode optionNode = (OptionNode) node;
            if (optionNode.getOption().isEditable()) {
                userNode.add((UserOption) optionNode.getOption());
            }
        }
        OptionsManager optionsManager = configurationManagerService.getOptionsManager();
        Collection<UserOption> modifiedFromSiteOptions = optionsManager.getModifiedFromSiteOptions(userNode);
        UserConfiguration userConfiguration = new UserConfiguration();
        userConfiguration.setOptions(modifiedFromSiteOptions);
        configurationManagerService.saveUserOptions(userConfiguration);
    }

    boolean valid(boolean showMessages) {
        Node[] nodes = mgr.getRootContext().getChildren().getNodes();
        CtuluLog logs = new CtuluLog(BusinessMessages.RESOURCE_BUNDLE);
        boolean valid = true;
        for (Node node : nodes) {
            OptionNode optionNode = (OptionNode) node;
            OptionsEnum optionEnum = optionNode.getOptionEnum();
            final Option option = optionNode.getOption();
            if (option.isEditable() && optionEnum.getValidator() != null) {
                if (!optionEnum.getValidator().isValid(option.getValeur(), logs, optionEnum.getId())) {
                    valid = false;
                }
            }
        }
        if (showMessages && !logs.isEmpty()) {
            LogsDisplayer.displayError(logs, NbBundle.getMessage(CrueOptionsPanel.class, "Options.Validation.MessageTitle"));
        }
        return valid;
    }

    private javax.swing.JLabel jLabelSiteFile;

    private javax.swing.JLabel jLabelUserFile;

    private javax.swing.JButton jbReloadOptions;

    private org.openide.explorer.view.OutlineView optionsOutlineView;

    private javax.swing.JTextField txtSiteFile;

    private javax.swing.JTextField txtUserFile;
}
