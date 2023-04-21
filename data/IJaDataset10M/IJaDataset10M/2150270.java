package sjrd.tricktakinggame.gui.tablelist;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import sjrd.tricktakinggame.client.*;
import sjrd.tricktakinggame.gui.tableinfo.*;
import sjrd.tricktakinggame.gui.client.*;
import sjrd.tricktakinggame.gui.networkactions.*;
import sjrd.tricktakinggame.gui.util.*;
import sjrd.tricktakinggame.network.*;

/**
 * Panel de liste des tables
 * @author sjrd
 */
public class TableListPanel extends ClientSubPanel implements TableSelector, RulesSelector {

    /**
     * ID de sérialisation
     */
    private static final long serialVersionUID = 1;

    /**
     * Délai de mise à jour
     */
    private static final int REFRESH_DELAY = 3000;

    /**
     * Délai de mise à jour
     */
    private static final int REFRESH_INITIAL_DELAY = 0;

    /**
     * Modèle de la liste des tables
     */
    private TableListModel tableListModel;

    /**
     * Liste des tables
     */
    private JList tableList;

    /**
     * Modèle de la liste des règles disponibles
     */
    private RulesListModel rulesListModel;

    /**
     * Liste des règles disponibles
     */
    private JList rulesList;

    /**
     * Modèle de liste de la table sélectionnée
     */
    private TableInfoListModel selectedTableListModel;

    /**
     * Modèle de liste de la table courante
     */
    private TableInfoListModel currentTableListModel;

    /**
     * Champ texte pour le nombre de joueurs
     */
    private JTextField playerCountField;

    /**
     * Timer pour la mise à jour des tables ouvertes
     */
    private Timer refreshTimer;

    /**
     * Action de joindre une table
     */
    private JoinTableAction joinTableAction;

    /**
     * Action de quitter la table
     */
    private LeaveTableAction leaveTableAction;

    /**
     * Action de créer une table
     */
    private CreateTableAction createTableAction;

    /**
     * Crée un panel de liste des tables
     * @param aOwner Panel client propriétaire
     */
    public TableListPanel(ClientPanel aOwner) {
        super(aOwner);
        build();
    }

    /**
     * Construit le panel
     */
    private void build() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints;
        JScrollPane scrollPane;
        JButton button;
        tableListModel = new TableListModel();
        joinTableAction = new JoinTableAction(this, this, "Joindre");
        leaveTableAction = new LeaveTableAction(this, "Quitter");
        rulesListModel = new RulesListModel();
        createTableAction = new CreateTableAction(this, this, "Créer une table");
        rulesListModel.addListDataListener(new RulesDataListener());
        refreshTimer = new Timer(REFRESH_DELAY, new RefreshTimerListener());
        refreshTimer.setInitialDelay(REFRESH_INITIAL_DELAY);
        selectedTableListModel = new TableInfoListModel();
        currentTableListModel = new TableInfoListModel();
        tableList = new JList(tableListModel);
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableList.getSelectionModel().addListSelectionListener(new TableOrRulesSelectionListener());
        scrollPane = new JScrollPane(tableList);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridheight = 4;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        add(scrollPane, constraints);
        button = new JButton(joinTableAction);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(button, constraints);
        button = new JButton(leaveTableAction);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(button, constraints);
        JList selectedTableList = new JList(selectedTableListModel);
        selectedTableList.setMinimumSize(new Dimension(150, 30));
        selectedTableList.setOpaque(false);
        selectedTableList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Table sélectionnée"));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        add(selectedTableList, constraints);
        JList currentTableList = new JList(currentTableListModel);
        currentTableList.setMinimumSize(new Dimension(150, 30));
        currentTableList.setOpaque(false);
        currentTableList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Votre table"));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        add(currentTableList, constraints);
        rulesList = new JList(rulesListModel);
        rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rulesList.getSelectionModel().addListSelectionListener(new TableOrRulesSelectionListener());
        scrollPane = new JScrollPane(rulesList);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        add(scrollPane, constraints);
        playerCountField = new JTextField(4);
        playerCountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        playerCountField.setEditable(true);
        JPanel actionsPanel = new JPanel(new GridLayout(0, 1));
        actionsPanel.add(new JButton(createTableAction));
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1 * 20));
        Box rightBox = new Box(BoxLayout.Y_AXIS);
        rightBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightBox.add(new JLabel("Nombre de joueurs :"));
        rightBox.add(playerCountField);
        rightBox.add(Box.createVerticalStrut(20));
        rightBox.add(actionsPanel);
        rightBox.add(Box.createVerticalGlue());
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        add(rightBox, constraints);
    }

    /**
     * Active ce panneau
     */
    @Override
    public void activate() {
        super.activate();
        rulesListModel.update(getClient());
    }

    /**
     * Désactive ce panneau
     */
    @Override
    public void deactivate() {
        refreshTimer.stop();
        super.deactivate();
    }

    /**
     * Table sélectionnée
     * @return Table sélectionnée (peut être <tt>null</tt>)
     */
    public TableInfo getSelectedTable() {
        return (TableInfo) tableList.getSelectedValue();
    }

    /**
     * Règles sélectionnées
     * @return Règles sélectionnées (peut être <tt>null</tt>)
     */
    public RulesInfo getSelectedRules() {
        return (RulesInfo) rulesList.getSelectedValue();
    }

    /**
     * Nombre de joueurs sélectionné
     * @return Nombre de joueurs sélectionné (-1 si nombre invalide)
     */
    public int getSelectedPlayerCount() {
        try {
            return Integer.parseInt(playerCountField.getText());
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateActionsEnabled() {
        super.updateActionsEnabled();
        ClientStatus status = this.getClientStatus();
        if (isWorking()) {
            createTableAction.setEnabled(false);
            joinTableAction.setEnabled(false);
            leaveTableAction.setEnabled(false);
        } else if ((status == null) || (status.getTableInfo() == null)) {
            TableInfo selectedTable = getSelectedTable();
            RulesInfo selectedRules = getSelectedRules();
            createTableAction.setEnabled(selectedRules != null);
            joinTableAction.setEnabled((selectedTable != null) && !selectedTable.isFull());
            leaveTableAction.setEnabled(false);
        } else {
            createTableAction.setEnabled(false);
            joinTableAction.setEnabled(false);
            leaveTableAction.setEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientStatusUpdated(ClientStatus status) {
        super.clientStatusUpdated(status);
        updateActionsEnabled();
        currentTableListModel.setTableInfo(status.getTableInfo());
    }

    /**
     * Listener pour le timer de rafraîchissement
     * @author sjrd
     */
    private class RefreshTimerListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent event) {
            if (getClient() == null) return;
            tableListModel.update(getClient());
            if (tableListModel.getSize() == 0) selectedTableListModel.setTableInfo(null); else new UpdateSelectedTableInfoWorker(selectedTableListModel.getTableInfo()).execute();
        }
    }

    /**
     * Listener pour les changements dans la liste des règles disponibles
     * @author sjrd
     */
    public class RulesDataListener implements ListDataListener {

        /**
         * {@inheritDoc}
         */
        public void contentsChanged(ListDataEvent event) {
            tableListModel.setRulesInfo(rulesListModel.getElements());
            refreshTimer.restart();
        }

        /**
         * {@inheritDoc}
         */
        public void intervalAdded(ListDataEvent event) {
        }

        /**
         * {@inheritDoc}
         */
        public void intervalRemoved(ListDataEvent event) {
        }
    }

    /**
     * Worker qui met à jour les infos sur la table sélectionnée
     * @author sjrd
     */
    private class UpdateSelectedTableInfoWorker extends SilentNetworkOperationWorker<TableInfo, Object> {

        /**
         * Informations initiales, incomplètes
         */
        private TableInfo partialTableInfo;

        /**
         * Crée le worker
         * @param aPartialTableInfo Informations initiales, incomplètes
         */
        public UpdateSelectedTableInfoWorker(TableInfo aPartialTableInfo) {
            super();
            partialTableInfo = aPartialTableInfo;
        }

        /**
         * {@inheritDoc}
         */
        protected TableInfo doInBackground() throws IOException {
            if (partialTableInfo == null) return null;
            return getClient().getCommander().tableInfo(rulesListModel.getElements(), partialTableInfo.getID());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void useResult(TableInfo result) {
            selectedTableListModel.setTableInfo(result);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onNetworkException(NetworkException error) {
            selectedTableListModel.setTableInfo(partialTableInfo);
        }
    }

    /**
     * Listener de la sélection de la liste des tables et des règles
     * @author sjrd
     */
    private class TableOrRulesSelectionListener implements ListSelectionListener {

        /**
         * {@inheritDoc}
         */
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) return;
            updateActionsEnabled();
            if (event.getSource() == tableList.getSelectionModel()) new UpdateSelectedTableInfoWorker(getSelectedTable()).execute();
            if (event.getSource() == rulesList.getSelectionModel()) playerCountField.setText(new Integer(getSelectedRules().getMinPlayerCount()).toString());
        }
    }
}
