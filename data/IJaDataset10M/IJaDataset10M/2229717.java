package org.micthemodel.gui.windows;

import org.micthemodel.elements.Reaction;
import org.micthemodel.elements.Reactor;
import org.micthemodel.gui.listModels.AllPluginsListModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import org.micthemodel.plugins.MicPlugin;
import org.micthemodel.plugins.densityVariation.DensityVariation;
import org.micthemodel.plugins.grainProportionInitialiser.GrainProportionInitialiser;
import org.micthemodel.plugins.kinetics.Kinetics;
import org.micthemodel.plugins.materialDistributionProfile.MaterialDistributionProfile;
import org.micthemodel.plugins.nucleiGenerator.NucleiGenerator;
import org.micthemodel.plugins.packer.Packer;
import org.micthemodel.plugins.particleDistributionProfile.ParticleDistributionProfile;
import org.micthemodel.plugins.particleIterator.ParticleIterator;
import org.micthemodel.plugins.postProcessor.PostProcessor;
import org.micthemodel.plugins.reactionTrigger.ReactionTrigger;
import org.micthemodel.plugins.recalculator.Recalculator;

/**
 *
 * @author bishnoi
 */
public class JInternalFrameListAllPlugins extends JInternalFrame {

    static final long serialVersionUID = 4287321940337646311L;

    JScrollPane mainScrollPane;

    JPanel mainPanel;

    JScrollPane pluginsListScrollPane;

    JList pluginsList;

    JPanel buttonsPanel;

    JButton jButtonClose;

    JButton jButtonUpdate;

    JButton jButtonShow;

    JButton jButtonEdit;

    JButton jButtonDelete;

    JCheckBox jCheckBoxShowDefault;

    AllPluginsListModel pluginsListModel;

    Reactor reactor;

    public JInternalFrameListAllPlugins(Reactor reactor) {
        initComponents();
        this.reactor = reactor;
    }

    private void initComponents() {
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.mainPanel = new JPanel();
        this.mainScrollPane = new JScrollPane(this.mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.pluginsListModel = new AllPluginsListModel();
        this.pluginsList = new JList(this.pluginsListModel);
        this.pluginsListScrollPane = new JScrollPane(this.pluginsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setContentPane(this.mainScrollPane);
        this.setTitle("View plugins");
        this.mainPanel.setBorder(new TitledBorder("List of currently defined plugins"));
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setClosable(true);
        this.buttonsPanel = new JPanel();
        this.jCheckBoxShowDefault = new JCheckBox("Show default plugins");
        this.jCheckBoxShowDefault.setSelected(this.pluginsListModel.isShowDefault());
        this.jButtonClose = new JButton("Close");
        this.jButtonShow = new JButton("Show");
        this.jButtonUpdate = new JButton("Update");
        this.jButtonEdit = new JButton("Create Copy");
        this.jButtonDelete = new JButton("Delete");
        this.buttonsPanel.add(this.jCheckBoxShowDefault);
        this.buttonsPanel.add(this.jButtonShow);
        this.buttonsPanel.add(this.jButtonEdit);
        this.buttonsPanel.add(this.jButtonDelete);
        this.buttonsPanel.add(this.jButtonUpdate);
        this.buttonsPanel.add(this.jButtonClose);
        GroupLayout layout = new GroupLayout(this.mainPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.mainPanel.setLayout(layout);
        GroupLayout.ParallelGroup column1 = layout.createParallelGroup();
        column1.addComponent(this.pluginsListScrollPane).addComponent(this.buttonsPanel);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.pluginsListScrollPane));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.buttonsPanel));
        hGroup.addGroup(column1);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        this.jButtonClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fold();
            }
        });
        this.jButtonShow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showPlugin();
            }
        });
        this.jButtonEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editPlugin();
            }
        });
        this.jButtonDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlugin();
            }
        });
        this.jButtonUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        this.jCheckBoxShowDefault.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateShowDefault();
            }
        });
        this.pack();
    }

    private void updateShowDefault() {
        this.pluginsListModel.setShowDefault(this.jCheckBoxShowDefault.isSelected(), reactor);
    }

    private void fold() {
        this.setVisible(false);
    }

    public void update() {
        this.pluginsListModel.update(reactor);
    }

    private void showPlugin() {
        int selectedIndex = this.pluginsList.getSelectedIndex();
        MicPlugin plugin = this.pluginsListModel.getElementAt(selectedIndex);
        JInternalFrameShowPlugin jInternalFrameShowPlugin = new JInternalFrameShowPlugin(plugin);
        this.getParent().add(jInternalFrameShowPlugin);
        jInternalFrameShowPlugin.setVisible(true);
    }

    private void editPlugin() {
        int selectedIndex = this.pluginsList.getSelectedIndex();
        MicPlugin plugin = this.pluginsListModel.getElementAt(selectedIndex);
        JInternalFrameCreatePlugin jInternalFrameEditPlugin = new JInternalFrameCreatePlugin(plugin, reactor);
        this.getParent().add(jInternalFrameEditPlugin);
        jInternalFrameEditPlugin.setVisible(true);
    }

    private void deletePlugin() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this plugin", "Confirm", JOptionPane.WARNING_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        int selectedIndex = this.pluginsList.getSelectedIndex();
        MicPlugin plugin = this.pluginsListModel.getElementAt(selectedIndex);
        if (plugin instanceof ParticleIterator) {
            JOptionPane.showMessageDialog(this, "Plugins of this type cannot be deleted from the interface.\n" + "Please remove the plugin manually from the text file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (plugin instanceof DensityVariation) {
            DensityVariation.getNewInstance(((DensityVariation) plugin).getMaterial());
            this.update();
            return;
        }
        if (plugin instanceof GrainProportionInitialiser) {
            GrainProportionInitialiser.getNewInstance(((GrainProportionInitialiser) plugin).getModel());
            this.update();
            return;
        }
        if (plugin instanceof Kinetics) {
            Kinetics kinetics = (Kinetics) plugin;
            for (Reaction reaction : this.reactor.getReactions()) {
                reaction.getKinetics().remove(kinetics);
            }
            this.update();
            return;
        }
        if (plugin instanceof MaterialDistributionProfile) {
            MaterialDistributionProfile.getNewInstance(((MaterialDistributionProfile) plugin).getMaterial());
            this.update();
            return;
        }
        if (plugin instanceof NucleiGenerator) {
            NucleiGenerator nucleiGenerator = (NucleiGenerator) plugin;
            nucleiGenerator.getModelGrain().getNuclei().remove(nucleiGenerator);
            this.update();
            return;
        }
        if (plugin instanceof Packer) {
            Packer.getNewInstance(((Packer) plugin).getReactor());
            this.update();
            return;
        }
        if (plugin instanceof ParticleDistributionProfile) {
            ParticleDistributionProfile.getNewInstance(((ParticleDistributionProfile) plugin).getModelGrain());
            this.update();
            return;
        }
        if (plugin instanceof ReactionTrigger) {
            for (Reaction reaction : this.reactor.getReactions()) {
                if (reaction.getTrigger() == plugin) {
                    ReactionTrigger.getNewInstance(reaction);
                }
            }
            this.update();
            return;
        }
        if (plugin instanceof Recalculator) {
            Recalculator.getNewInstance(((Recalculator) plugin).getReaction());
            this.update();
            return;
        }
        if (plugin instanceof PostProcessor) {
            PostProcessor processor = (PostProcessor) plugin;
            processor.getReactor().removePostProcessor(processor);
            this.update();
            return;
        }
    }
}
