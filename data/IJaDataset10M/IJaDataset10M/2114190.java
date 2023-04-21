package org.fudaa.fudaa.crue.study.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.StringUtils;
import org.fudaa.dodico.crue.common.BusinessMessages;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.io.common.CrueVersionType;
import org.fudaa.dodico.crue.metier.CrueLevelType;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.dodico.crue.metier.etude.FichierCrue;
import org.fudaa.dodico.crue.metier.etude.FichierCrueManager;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHContainerBase;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHModeleBase;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHScenario;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHSousModele;
import org.fudaa.dodico.crue.validation.ValidationPatternHelper;
import org.fudaa.fudaa.crue.common.helper.DialogHelper;
import org.fudaa.fudaa.crue.study.services.EMHProjetService;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Chris
 */
public class ManageManagerEMHContainerDialog extends javax.swing.JDialog {

    private static final class SimplifiedManagerItem {

        public String nom;

        @Override
        public String toString() {
            return this.nom;
        }
    }

    private final ManagerEMHContainerBase container;

    private final List<FileLinePanel> lines = new ArrayList<FileLinePanel>();

    private final String title;

    private boolean okClicked = false;

    private final EMHProjet projet = Lookup.getDefault().lookup(EMHProjetService.class).getSelectedProject();

    private List<ManagerEMHModeleBase> modeles;

    private List<ManagerEMHSousModele> sousModeles;

    private Map<String, ManagerEMHModeleBase> modelesByName = new HashMap<String, ManagerEMHModeleBase>();

    private Map<String, ManagerEMHSousModele> sousModelesByName = new HashMap<String, ManagerEMHSousModele>();

    private final boolean creation;

    /** Creates new form ManageManagerEMHContainerDialog */
    public ManageManagerEMHContainerDialog(java.awt.Frame parent, boolean modal, ManagerEMHContainerBase container, boolean creation) {
        super(parent, modal);
        this.container = container;
        this.creation = creation;
        initComponents();
        CrueLevelType level = container.getLevel();
        CrueVersionType version = container.getInfosVersions().getCrueVersion();
        if (creation) {
            this.title = NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.TitleCreation", this.convertLevel(level), this.convertVersion(version));
        } else {
            this.title = NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.TitleModification", this.convertLevel(level), this.convertVersion(version));
        }
        this.setTitle(title);
        this.jLabelPrefix.setText(level.getPrefix());
        this.nameField.setText(StringUtils.removeStart(container.getNom(), level.getPrefix()));
        this.activeCheckBox.setSelected(container.isActive());
        this.commentField.setText(container.getInfosVersions().getCommentaire());
        if (level == CrueLevelType.SCENARIO) {
            modeles = new ArrayList<ManagerEMHModeleBase>();
            for (ManagerEMHModeleBase modele : projet.getListeModeles()) {
                if (modele.getInfosVersions().getCrueVersion() == version) {
                    modeles.add(modele);
                    modelesByName.put(modele.getNom(), modele);
                }
            }
            ManagerEMHScenario scenario = (ManagerEMHScenario) container;
            this.fillModelesList(scenario.getFils());
            this.containerPanel.add(this.modelesPanel);
            if (version == CrueVersionType.CRUE10) {
                this.containerPanel.add(this.filesPanel, BorderLayout.SOUTH);
            }
        } else if (level == CrueLevelType.MODELE) {
            sousModeles = projet.getListeSousModeles();
            for (ManagerEMHSousModele ssModele : sousModeles) {
                sousModelesByName.put(ssModele.getNom(), ssModele);
            }
            ManagerEMHModeleBase modele = (ManagerEMHModeleBase) container;
            this.fillSousModelesList(modele.getFils());
            if (version == CrueVersionType.CRUE10) {
                this.containerPanel.add(this.sousModelesPanel);
            }
            this.containerPanel.add(this.filesPanel, BorderLayout.SOUTH);
        } else if (level == CrueLevelType.SOUS_MODELE) {
            this.containerPanel.add(this.filesPanel);
        }
        final FichierCrueManager filesManager = container.getListeFichiers();
        for (CrueFileType type : CrueFileType.values()) {
            if (!EMHProjetService.isOptrAccepted(projet) && CrueFileType.OPTR.equals(type)) {
                continue;
            }
            if (!type.isResultFileType() && (type.getCrueVersionType() == version) && (type.getLevel() == level)) {
                this.addFileLine(type, filesManager == null ? null : filesManager.getFile(type));
            }
        }
        new RadicalValidationInstaller(this.nameField, jLabelMessage);
        modelesScrollPane.setPreferredSize(new Dimension(150, 120));
        sousModelesScrollPane.setPreferredSize(new Dimension(150, 120));
        fileScrollPane.setPreferredSize(new Dimension(150, 120));
        jButtonModeleEdit.setEnabled(false);
        jButtonSousModeleEdit.setEnabled(false);
        modelesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sousModelesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modelesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                jButtonModeleEdit.setEnabled(!modelesList.isSelectionEmpty());
            }
        });
        sousModelesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                jButtonSousModeleEdit.setEnabled(!sousModelesList.isSelectionEmpty());
            }
        });
        modelesList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedModele();
                }
            }
        });
        sousModelesList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedSousModele();
                }
            }
        });
        this.setLocationRelativeTo(null);
    }

    protected void editSelectedModele() {
        int idx = modelesList.getSelectedIndex();
        if (idx >= 0) {
            SimplifiedManagerItem currentValue = (SimplifiedManagerItem) modelesList.getSelectedValue();
            DefaultListModel listModel = (DefaultListModel) modelesList.getModel();
            JComboBox combox = createModelCombo(currentValue.nom);
            combox.setSelectedItem(currentValue.nom);
            if (JOptionPane.showConfirmDialog(this, combox, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.editModele"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String newName = (String) combox.getSelectedItem();
                if (newName != null && !newName.equals(currentValue.nom)) {
                    SimplifiedManagerItem newValue = new SimplifiedManagerItem();
                    newValue.nom = newName;
                    listModel.set(idx, newValue);
                }
            }
        }
    }

    protected void editSelectedSousModele() {
        int idx = sousModelesList.getSelectedIndex();
        if (idx >= 0) {
            SimplifiedManagerItem currentValue = (SimplifiedManagerItem) sousModelesList.getSelectedValue();
            DefaultListModel listModel = (DefaultListModel) sousModelesList.getModel();
            JComboBox combox = createSousModelCombo(currentValue.nom);
            combox.setSelectedItem(currentValue.nom);
            if (JOptionPane.showConfirmDialog(this, combox, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.editSousModele"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String newName = (String) combox.getSelectedItem();
                if (newName != null && !newName.equals(currentValue.nom)) {
                    SimplifiedManagerItem newValue = new SimplifiedManagerItem();
                    newValue.nom = newName;
                    listModel.set(idx, newValue);
                }
            }
        }
    }

    public void display() {
        pack();
        setVisible(true);
    }

    private Set<String> getUsedModeleNames() {
        ListModel model = modelesList.getModel();
        return getUseNames(model);
    }

    private Set<String> getUsedSousModeleNames() {
        ListModel model = sousModelesList.getModel();
        return getUseNames(model);
    }

    public Set<String> getUseNames(ListModel model) {
        Set<String> names = new HashSet<String>();
        int nbUsed = model.getSize();
        for (int i = 0; i < nbUsed; i++) {
            SimplifiedManagerItem item = (SimplifiedManagerItem) model.getElementAt(i);
            names.add(item.nom);
        }
        return names;
    }

    private void fillModelesList(List<ManagerEMHModeleBase> modeles) {
        final DefaultListModel model = new DefaultListModel();
        for (ManagerEMHModeleBase modele : modeles) {
            final SimplifiedManagerItem item = new SimplifiedManagerItem();
            item.nom = modele.getNom();
            model.addElement(item);
        }
        if (!modeles.isEmpty()) {
            this.modelesAddButton.setEnabled(false);
            this.modelesRemoveButton.setEnabled(true);
        }
        modelesList.setModel(model);
    }

    private void fillSousModelesList(List<ManagerEMHSousModele> sousModeles) {
        final DefaultListModel model = new DefaultListModel();
        for (ManagerEMHSousModele sousModele : sousModeles) {
            final SimplifiedManagerItem item = new SimplifiedManagerItem();
            item.nom = sousModele.getNom();
            model.addElement(item);
        }
        sousModelesList.setModel(model);
    }

    private JComboBox createModelCombo(String nameToAdd) {
        JComboBox modelesCombo = new JComboBox();
        Set<String> alreadyUsed = getUsedModeleNames();
        for (ManagerEMHModeleBase modele : modeles) {
            final String modeleName = modele.getNom();
            if (StringUtils.equals(nameToAdd, modeleName) || !alreadyUsed.contains(modeleName)) {
                modelesCombo.addItem(modeleName);
            }
        }
        return modelesCombo;
    }

    private JComboBox createSousModelCombo(String nameToAdd) {
        JComboBox sousModelesCombo = new JComboBox();
        Set<String> alreadyUsed = getUsedSousModeleNames();
        for (ManagerEMHSousModele sousModele : sousModeles) {
            final String sousModeleName = sousModele.getNom();
            if (StringUtils.equals(nameToAdd, sousModeleName) || !alreadyUsed.contains(sousModeleName)) {
                sousModelesCombo.addItem(sousModeleName);
            }
        }
        return sousModelesCombo;
    }

    private void addFileLine(CrueFileType type, FichierCrue file) {
        final FileLinePanel line = new FileLinePanel(type, file);
        this.lines.add(line);
        this.linesPanel.add(line);
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    private String convertVersion(CrueVersionType version) {
        switch(version) {
            case CRUE10:
                {
                    return NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.Crue10");
                }
            case CRUE9:
                {
                    return NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.Crue9");
                }
        }
        return "";
    }

    private String convertLevel(CrueLevelType level) {
        switch(level) {
            case SCENARIO:
                {
                    return NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.Scenario");
                }
            case MODELE:
                {
                    return NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.Modele");
                }
            case SOUS_MODELE:
                {
                    return NbBundle.getMessage(FileDialog.class, "ManageManagerEMHContainerDialog.SousModele");
                }
        }
        return "";
    }

    private void initComponents() {
        modelesPanel = new javax.swing.JPanel();
        modelesButtonsPanel = new javax.swing.JPanel();
        modelesAddButton = new javax.swing.JButton();
        modelesRemoveButton = new javax.swing.JButton();
        jButtonModeleEdit = new javax.swing.JButton();
        modelesScrollPane = new javax.swing.JScrollPane();
        modelesList = new javax.swing.JList();
        sousModelesPanel = new javax.swing.JPanel();
        sousModelesButtonsPanel = new javax.swing.JPanel();
        sousModelesAddButton = new javax.swing.JButton();
        sousModelesRemoveButton = new javax.swing.JButton();
        jButtonSousModeleEdit = new javax.swing.JButton();
        sousModelesScrollPane = new javax.swing.JScrollPane();
        sousModelesList = new javax.swing.JList();
        filesPanel = new javax.swing.JPanel();
        fileScrollPane = new javax.swing.JScrollPane();
        linesPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        commentLabel = new javax.swing.JLabel();
        commentField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        activeCheckBox = new javax.swing.JCheckBox();
        jLabelMessage = new javax.swing.JLabel();
        jLabelPrefix = new javax.swing.JLabel();
        jLabelActif = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        containerPanel = new javax.swing.JPanel();
        modelesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.modelesPanel.border.title")));
        modelesPanel.setLayout(new java.awt.BorderLayout());
        modelesAddButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.modelesAddButton.text"));
        modelesAddButton.setMaximumSize(new java.awt.Dimension(81, 23));
        modelesAddButton.setMinimumSize(new java.awt.Dimension(81, 23));
        modelesAddButton.setPreferredSize(new java.awt.Dimension(81, 23));
        modelesAddButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelesAddButtonActionPerformed(evt);
            }
        });
        modelesRemoveButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.modelesRemoveButton.text"));
        modelesRemoveButton.setEnabled(false);
        modelesRemoveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelesRemoveButtonActionPerformed(evt);
            }
        });
        jButtonModeleEdit.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.jButtonModeleEdit.text"));
        jButtonModeleEdit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModeleEditActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout modelesButtonsPanelLayout = new javax.swing.GroupLayout(modelesButtonsPanel);
        modelesButtonsPanel.setLayout(modelesButtonsPanelLayout);
        modelesButtonsPanelLayout.setHorizontalGroup(modelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(modelesButtonsPanelLayout.createSequentialGroup().addContainerGap().addGroup(modelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(modelesRemoveButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addComponent(modelesAddButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addComponent(jButtonModeleEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)).addContainerGap()));
        modelesButtonsPanelLayout.setVerticalGroup(modelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(modelesButtonsPanelLayout.createSequentialGroup().addContainerGap().addComponent(modelesAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonModeleEdit).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(modelesRemoveButton).addContainerGap()));
        modelesPanel.add(modelesButtonsPanel, java.awt.BorderLayout.EAST);
        modelesScrollPane.setViewportView(modelesList);
        modelesPanel.add(modelesScrollPane, java.awt.BorderLayout.CENTER);
        sousModelesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.sousModelesPanel.border.title")));
        sousModelesPanel.setLayout(new java.awt.BorderLayout());
        sousModelesAddButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.sousModelesAddButton.text"));
        sousModelesAddButton.setMaximumSize(new java.awt.Dimension(81, 23));
        sousModelesAddButton.setMinimumSize(new java.awt.Dimension(81, 23));
        sousModelesAddButton.setPreferredSize(new java.awt.Dimension(81, 23));
        sousModelesAddButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sousModelesAddButtonActionPerformed(evt);
            }
        });
        sousModelesRemoveButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.sousModelesRemoveButton.text"));
        sousModelesRemoveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sousModelesRemoveButtonActionPerformed(evt);
            }
        });
        jButtonSousModeleEdit.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.jButtonSousModeleEdit.text"));
        jButtonSousModeleEdit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSousModeleEditActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout sousModelesButtonsPanelLayout = new javax.swing.GroupLayout(sousModelesButtonsPanel);
        sousModelesButtonsPanel.setLayout(sousModelesButtonsPanelLayout);
        sousModelesButtonsPanelLayout.setHorizontalGroup(sousModelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(sousModelesButtonsPanelLayout.createSequentialGroup().addContainerGap().addGroup(sousModelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sousModelesRemoveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE).addComponent(sousModelesAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE).addComponent(jButtonSousModeleEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)).addContainerGap()));
        sousModelesButtonsPanelLayout.setVerticalGroup(sousModelesButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(sousModelesButtonsPanelLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(sousModelesAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonSousModeleEdit).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(sousModelesRemoveButton).addContainerGap(43, Short.MAX_VALUE)));
        sousModelesPanel.add(sousModelesButtonsPanel, java.awt.BorderLayout.EAST);
        sousModelesScrollPane.setViewportView(sousModelesList);
        sousModelesPanel.add(sousModelesScrollPane, java.awt.BorderLayout.CENTER);
        filesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.filesPanel.border.title")));
        filesPanel.setLayout(new java.awt.BorderLayout());
        fileScrollPane.setBorder(null);
        linesPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        linesPanel.setLayout(new javax.swing.BoxLayout(linesPanel, javax.swing.BoxLayout.Y_AXIS));
        fileScrollPane.setViewportView(linesPanel);
        filesPanel.add(fileScrollPane, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(350, 420));
        setResizable(false);
        commentLabel.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.commentLabel.text"));
        commentField.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.commentField.text"));
        commentField.setPreferredSize(new java.awt.Dimension(290, 20));
        nameLabel.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.nameLabel.text"));
        nameField.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.nameField.text"));
        nameField.setPreferredSize(new java.awt.Dimension(260, 20));
        activeCheckBox.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.activeCheckBox.text"));
        jLabelMessage.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.jLabelMessage.text"));
        jLabelPrefix.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.jLabelPrefix.text"));
        jLabelActif.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.jLabelActif.text"));
        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(headerPanelLayout.createSequentialGroup().addContainerGap().addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE).addGroup(headerPanelLayout.createSequentialGroup().addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(headerPanelLayout.createSequentialGroup().addComponent(nameLabel).addGap(36, 36, 36).addComponent(jLabelPrefix)).addComponent(jLabelActif)).addGap(4, 4, 4).addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(activeCheckBox).addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))).addGroup(headerPanelLayout.createSequentialGroup().addComponent(commentLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(commentField, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))).addContainerGap()));
        headerPanelLayout.setVerticalGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(headerPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabelMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelPrefix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelActif).addComponent(activeCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(commentLabel).addComponent(commentField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);
        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        okButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.okButton.text"));
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(okButton);
        cancelButton.setText(org.openide.util.NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.cancelButton.text"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(cancelButton);
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
        containerPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(containerPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        dispose();
    }

    private boolean scenarioContainsActiveModele() {
        for (int i = 0; i < this.modelesList.getModel().getSize(); i++) {
            final SimplifiedManagerItem name = (SimplifiedManagerItem) this.modelesList.getModel().getElementAt(i);
            ManagerEMHModeleBase modele = this.modelesByName.get(name.nom);
            if (modele.isActive()) {
                return true;
            }
        }
        return false;
    }

    private boolean modeleContainsActiveSousModele() {
        for (int i = 0; i < this.sousModelesList.getModel().getSize(); i++) {
            final SimplifiedManagerItem name = (SimplifiedManagerItem) this.sousModelesList.getModel().getElementAt(i);
            ManagerEMHSousModele sousModele = this.sousModelesByName.get(name.nom);
            if (sousModele.isActive()) {
                return true;
            }
        }
        return false;
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String radical = this.nameField.getText();
        String msg = ValidationPatternHelper.isRadicalValideMsg(radical);
        if (msg != null) {
            DialogHelper.showError(title, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.invalidName", BusinessMessages.getString(msg)));
            return;
        }
        radical = this.jLabelPrefix.getText() + radical;
        final ManagerEMHContainerBase existingContainer = Lookup.getDefault().lookup(EMHProjetService.class).getSelectedProject().getContainer(radical, this.container.getLevel());
        final boolean isCreationAndManagerExist = creation && existingContainer != null;
        final boolean isModificationAndOtherManagerWithSameName = !creation && existingContainer != null && existingContainer != container;
        if (isCreationAndManagerExist || isModificationAndOtherManagerWithSameName) {
            JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.nameExists", radical), title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        final List<FichierCrue> files = new ArrayList<FichierCrue>();
        for (FileLinePanel line : this.lines) {
            final FichierCrue file = line.getSelectedFile();
            if (file != null) {
                files.add(file);
            } else {
                JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.fileMissing"), title, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        this.container.setNom(radical);
        this.container.setActive(this.activeCheckBox.isSelected());
        this.container.getInfosVersions().setCommentaire(this.commentField.getText());
        this.container.setListeFichiers(files);
        final CrueLevelType level = this.container.getLevel();
        if (level == CrueLevelType.SCENARIO) {
            ManagerEMHScenario scenario = (ManagerEMHScenario) this.container;
            boolean hasActive = scenarioContainsActiveModele();
            if (!hasActive) {
                JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.activeModele"), title, JOptionPane.ERROR_MESSAGE);
                return;
            }
            scenario.removeAllManagerFils();
            for (int i = 0; i < this.modelesList.getModel().getSize(); i++) {
                final SimplifiedManagerItem name = (SimplifiedManagerItem) this.modelesList.getModel().getElementAt(i);
                ManagerEMHModeleBase modele = this.modelesByName.get(name.nom);
                scenario.addManagerFils(modele);
            }
        } else if ((level == CrueLevelType.MODELE) && (this.container.getInfosVersions().getCrueVersion() == CrueVersionType.CRUE10)) {
            ManagerEMHModeleBase modele = (ManagerEMHModeleBase) this.container;
            boolean hasActive = modeleContainsActiveSousModele();
            if (!hasActive) {
                JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.activeSousModele"), title, JOptionPane.ERROR_MESSAGE);
                return;
            }
            modele.removeAllManagerFils();
            for (int i = 0; i < this.sousModelesList.getModel().getSize(); i++) {
                final SimplifiedManagerItem name = (SimplifiedManagerItem) this.sousModelesList.getModel().getElementAt(i);
                ManagerEMHSousModele sousModele = this.sousModelesByName.get(name.nom);
                modele.addManagerFils(sousModele);
            }
        }
        this.okClicked = true;
        this.setVisible(false);
        dispose();
    }

    private void modelesAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JComboBox modelesCombo = createModelCombo(null);
        if (modelesCombo.getItemCount() > 0) {
            modelesCombo.setSelectedIndex(0);
        }
        if (JOptionPane.showConfirmDialog(this, modelesCombo, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.addModele"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            final int selectedIndex = modelesCombo.getSelectedIndex();
            if (selectedIndex > -1) {
                final SimplifiedManagerItem item = new SimplifiedManagerItem();
                item.nom = this.modeles.get(selectedIndex).getNom();
                ((DefaultListModel) this.modelesList.getModel()).addElement(item);
                this.modelesAddButton.setEnabled(false);
                this.modelesRemoveButton.setEnabled(true);
            }
        }
    }

    private void modelesRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final int selectedIndex = this.modelesList.getSelectedIndex();
        if (selectedIndex != -1) {
            if (JOptionPane.showConfirmDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.removeModele"), title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ((DefaultListModel) this.modelesList.getModel()).remove(selectedIndex);
                this.modelesAddButton.setEnabled(true);
                this.modelesRemoveButton.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.selectModele"), title, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sousModelesAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JComboBox sousModelesCombo = createSousModelCombo(null);
        if (sousModelesCombo.getItemCount() > 0) {
            sousModelesCombo.setSelectedIndex(0);
        }
        if (JOptionPane.showConfirmDialog(this, sousModelesCombo, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.addSousModele"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            final int selectedIndex = sousModelesCombo.getSelectedIndex();
            if (selectedIndex > -1) {
                final SimplifiedManagerItem item = new SimplifiedManagerItem();
                item.nom = this.sousModeles.get(selectedIndex).getNom();
                ((DefaultListModel) this.sousModelesList.getModel()).addElement(item);
            }
        }
    }

    private void sousModelesRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final int selectedIndex = this.sousModelesList.getSelectedIndex();
        if (selectedIndex != -1) {
            if (JOptionPane.showConfirmDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.removeSousModele"), title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ((DefaultListModel) this.sousModelesList.getModel()).remove(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, NbBundle.getMessage(ManageManagerEMHContainerDialog.class, "ManageManagerEMHContainerDialog.selectSousModele"), title, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonModeleEditActionPerformed(java.awt.event.ActionEvent evt) {
        editSelectedModele();
    }

    private void jButtonSousModeleEditActionPerformed(java.awt.event.ActionEvent evt) {
        editSelectedSousModele();
    }

    private javax.swing.JCheckBox activeCheckBox;

    private javax.swing.JPanel buttonsPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextField commentField;

    private javax.swing.JLabel commentLabel;

    private javax.swing.JPanel containerPanel;

    private javax.swing.JScrollPane fileScrollPane;

    private javax.swing.JPanel filesPanel;

    private javax.swing.JPanel headerPanel;

    private javax.swing.JButton jButtonModeleEdit;

    private javax.swing.JButton jButtonSousModeleEdit;

    private javax.swing.JLabel jLabelActif;

    private javax.swing.JLabel jLabelMessage;

    private javax.swing.JLabel jLabelPrefix;

    private javax.swing.JPanel linesPanel;

    private javax.swing.JButton modelesAddButton;

    private javax.swing.JPanel modelesButtonsPanel;

    private javax.swing.JList modelesList;

    private javax.swing.JPanel modelesPanel;

    private javax.swing.JButton modelesRemoveButton;

    private javax.swing.JScrollPane modelesScrollPane;

    private javax.swing.JTextField nameField;

    private javax.swing.JLabel nameLabel;

    private javax.swing.JButton okButton;

    private javax.swing.JButton sousModelesAddButton;

    private javax.swing.JPanel sousModelesButtonsPanel;

    private javax.swing.JList sousModelesList;

    private javax.swing.JPanel sousModelesPanel;

    private javax.swing.JButton sousModelesRemoveButton;

    private javax.swing.JScrollPane sousModelesScrollPane;
}
