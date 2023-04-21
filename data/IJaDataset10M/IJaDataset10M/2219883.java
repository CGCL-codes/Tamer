package com.intel.gpe.client2.common.panels.registries;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import com.intel.gpe.client2.adapters.MessageAdapter;
import com.intel.gpe.client2.common.i18n.Messages;
import com.intel.gpe.client2.common.i18n.MessagesKeys;
import com.intel.gpe.client2.common.panels.wrappers.OKCancelWrapper;
import com.intel.gpe.client2.defaults.preferences.CommonNodes;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gpe.client2.panels.GPEPanelResult;
import com.intel.gui.controls2.configurable.ConfigurablePanel;
import com.intel.gui.controls2.configurable.IConfigurable;

/**
 * 
 * The panel with registry entry table. The entries correspond to the defined GPE Registry locations.
 * 
 * @author Denis Zhigula
 * @author Alexander Lukichev
 * @version $Id: RegistryListFramePanel.java,v 1.11 2007/02/27 12:42:53 dizhigul Exp $
 * 
 */
public class RegistryListFramePanel extends ConfigurablePanel {

    private RegistryTable registryTable;

    private JPanel buttonPanel;

    private ButtonListener buttonListener;

    private JButton addButton;

    private JButton deleteButton;

    private JButton editButton;

    private MessageAdapter messageAdapter;

    private Map<String, List<String>> registries;

    public RegistryListFramePanel(IConfigurable parent, INode node, Map<String, List<String>> registries, MessageAdapter messageAdapter) {
        super(parent, node);
        this.registries = registries;
        this.messageAdapter = messageAdapter;
        buildComponents();
    }

    private void buildComponents() {
        RegistriesTableModel model = new RegistriesTableModel<RegistryEntry>(registries);
        registryTable = new RegistryTable(this, CommonNodes.RegistryTable, model);
        JScrollPane scrollPane = new JScrollPane(registryTable);
        buildButtonPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel secondPanel = new JPanel();
        secondPanel.add(buttonPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(secondPanel, BorderLayout.EAST);
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Registries)));
        add(mainPanel, BorderLayout.CENTER);
    }

    private void buildButtonPanel() {
        buttonListener = new ButtonListener();
        buttonPanel = new JPanel(new GridLayout(4, 1));
        addButton = new JButton(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Add));
        addButton.setToolTipText(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Add_registry_URL));
        addButton.addActionListener(buttonListener);
        buttonPanel.add(addButton);
        deleteButton = new JButton(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Delete));
        deleteButton.setToolTipText(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Delete_registry_URL));
        deleteButton.addActionListener(buttonListener);
        buttonPanel.add(deleteButton);
        editButton = new JButton(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Edit));
        editButton.setToolTipText(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Edit_registry_URL));
        editButton.addActionListener(buttonListener);
        buttonPanel.add(editButton);
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            Object src = e.getSource();
            if (src == deleteButton) {
                deleteEntries();
            } else if (src == addButton) {
                addEntry();
            } else if (src == editButton) {
                editEntries();
            }
        }
    }

    private void editEntries() {
        List<RegistryEntry> registries = registryTable.getSelectedEntries();
        if (registries.size() == 0) {
            return;
        }
        if (registries.size() > 1) {
            messageAdapter.showMessage(Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Can_edit_only_one_entry_at_a_time__nSelected_first_entry));
        }
        RegistryEntry old = registries.get(0);
        RegistryEntryPanel dialog = new RegistryEntryPanel(this, CommonNodes.RegistryEntryPanel, messageAdapter, old);
        OKCancelWrapper wrapper = new OKCancelWrapper(dialog, Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_Edit_Registry_Entry), dialog);
        messageAdapter.show(wrapper);
        if (wrapper.getResult() == GPEPanelResult.OK) {
            registryTable.setEntry(old, dialog.getRegistryName(), dialog.getNewURL(), dialog.getStatus());
        }
    }

    private List<String> listNames() {
        List<String> names = new ArrayList<String>();
        for (RegistryEntry entry : registryTable.getRegistriesModel().getData()) {
            names.add(entry.getRegistryName());
        }
        return names;
    }

    private void addEntry() {
        RegistryEntry lastEntry = registryTable.getRegistriesModel().getLastEntry();
        RegistryEntryPanel dialog = new RegistryEntryPanel(this, CommonNodes.RegistryEntryPanel, messageAdapter, lastEntry);
        OKCancelWrapper wrapper = new OKCancelWrapper(dialog, Messages.getString(MessagesKeys.common_panels_registries_RegistryListFramePanel_New_registry_URL), dialog);
        messageAdapter.show(wrapper);
        if (wrapper.getResult() == GPEPanelResult.OK) {
            registryTable.addEntry(dialog.getRegistryName(), dialog.getNewURL(), true);
        }
    }

    private void deleteEntries() {
        registryTable.removeEntries();
    }
}
