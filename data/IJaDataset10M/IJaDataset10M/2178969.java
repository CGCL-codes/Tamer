package jm2pc.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import jm2pc.server.i18n.Messages;
import jm2pc.server.service.plugin.Plugin;
import jm2pc.server.service.plugin.PluginsList;

public class PluginManagerFrame extends JDialog {

    public static final long serialVersionUID = 1l;

    private JTextField tfClassName;

    private JButton btRefresh;

    private JButton btAdicionar;

    private JButton btExcluir;

    private JButton btSalvar;

    private JButton btFechar;

    private JTable tbPlugins;

    private DefaultTableModel tbModel;

    private Object[] columNames;

    private Object[][] dados;

    public PluginManagerFrame(JFrame frame) {
        super(frame, "Plug-in", true);
        columNames = new Object[] { "Plug-in", Messages.getMessage("version"), Messages.getMessage("active") };
        dados = new Object[20][3];
        tfClassName = new JTextField(20);
        btAdicionar = makeButtonAdd();
        btExcluir = makeButtonExcluir();
        btRefresh = makeButtonRefresh();
        btSalvar = makeButtonSalvar();
        btFechar = makeButtonFechar();
        loadDados();
        tbModel = new DefaultTableModel(dados, columNames) {

            public static final long serialVersionUID = 1l;

            public boolean isCellEditable(int row, int column) {
                if (column == 0) return false; else return true;
            }

            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            public void fireTableCellUpdated(int row, int column) {
                if (column == 2) {
                    PluginsList.changePluginActive(row);
                    loadDados();
                }
            }
        };
        tbPlugins = new JTable(tbModel);
        tbPlugins.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbPlugins.getColumnModel().getColumn(0).setPreferredWidth(255);
        tbPlugins.getColumnModel().getColumn(0).setResizable(false);
        tbPlugins.getColumnModel().getColumn(1).setPreferredWidth(90);
        tbPlugins.getColumnModel().getColumn(1).setResizable(false);
        tbPlugins.getColumnModel().getColumn(2).setPreferredWidth(60);
        tbPlugins.getColumnModel().getColumn(2).setResizable(false);
        tbPlugins.setAutoscrolls(false);
        JPanel panelClassName = new JPanel();
        panelClassName.add(new JLabel("ClassName:"));
        panelClassName.add(tfClassName);
        panelClassName.add(btAdicionar);
        JPanel panelButtons = new JPanel();
        panelButtons.add(btExcluir);
        panelButtons.add(new JSeparator());
        panelButtons.add(new JSeparator());
        panelButtons.add(btRefresh);
        panelButtons.add(new JSeparator());
        panelButtons.add(new JSeparator());
        panelButtons.add(btSalvar);
        panelButtons.add(new JSeparator());
        panelButtons.add(new JSeparator());
        panelButtons.add(btFechar);
        JPanel panelControl = new JPanel(new GridLayout(2, 1));
        panelControl.add(panelClassName);
        panelControl.add(panelButtons);
        JScrollPane scroll = new JScrollPane(tbPlugins);
        getContentPane().add(panelControl, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(430, 240));
        this.setResizable(false);
        pack();
        setLocationRelativeTo(frame);
    }

    private void loadDados() {
        ArrayList<Plugin> plugins = PluginsList.getPlugins();
        int qtdPlugins = plugins.size();
        dados = new Object[qtdPlugins][3];
        for (int i = 0; i < plugins.size(); i++) {
            Plugin p = plugins.get(i);
            dados[i][0] = p;
            dados[i][1] = p.getCommand().getVersion();
            dados[i][2] = new Boolean(p.isActive());
        }
    }

    public void updateTable() {
        loadDados();
        tbModel.setDataVector(dados, columNames);
        tbPlugins.getColumnModel().getColumn(0).setPreferredWidth(255);
        tbPlugins.getColumnModel().getColumn(0).setResizable(false);
        tbPlugins.getColumnModel().getColumn(1).setPreferredWidth(90);
        tbPlugins.getColumnModel().getColumn(1).setResizable(false);
        tbPlugins.getColumnModel().getColumn(2).setPreferredWidth(60);
        tbPlugins.getColumnModel().getColumn(2).setResizable(false);
        tbPlugins.updateUI();
    }

    public JButton makeButtonRefresh() {
        JButton btRefresh = new JButton(Messages.getMessage("refresh"));
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    PluginsList.loadPlugins();
                    updateTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("errorLoadPlugin") + "\n\n" + e.toString(), Messages.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        btRefresh.addActionListener(listener);
        return btRefresh;
    }

    public JButton makeButtonAdd() {
        JButton btAdd = new JButton(Messages.getMessage("add"));
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (tfClassName.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("errorNoClassName"), Messages.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    PluginsList.loadClasses();
                    PluginsList.addPlugin(tfClassName.getText());
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("pluginAddOk"), Messages.getMessage("success"), JOptionPane.INFORMATION_MESSAGE);
                    updateTable();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("errorPluginAdd") + " \n\n" + e.toString(), Messages.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        btAdd.addActionListener(listener);
        return btAdd;
    }

    public JButton makeButtonSalvar() {
        JButton btSalvar = new JButton(Messages.getMessage("save"));
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    PluginsList.savePlugins();
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("pluginSaveOk"), Messages.getMessage("success"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("errorPluginSaveList") + "\n\n" + e.toString(), Messages.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        btSalvar.addActionListener(listener);
        return btSalvar;
    }

    public JButton makeButtonExcluir() {
        JButton button = new JButton(Messages.getMessage("remove"));
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int i = tbPlugins.getSelectedRow();
                if (i == -1) {
                    JOptionPane.showMessageDialog(PluginManagerFrame.this, Messages.getMessage("errorNoPluginSelected"), Messages.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    PluginsList.removePlugin(i);
                    updateTable();
                }
            }
        };
        button.addActionListener(listener);
        return button;
    }

    public JButton makeButtonFechar() {
        JButton btFechar = new JButton(Messages.getMessage("close"));
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                PluginManagerFrame.this.setVisible(false);
            }
        };
        btFechar.addActionListener(listener);
        return btFechar;
    }

    public void alterarIdioma() {
        btAdicionar.setText(Messages.getMessage("add"));
        btExcluir.setText(Messages.getMessage("remove"));
        btFechar.setText(Messages.getMessage("close"));
        btRefresh.setText(Messages.getMessage("refresh"));
        btSalvar.setText(Messages.getMessage("save"));
        columNames = new Object[] { "Plug-in", Messages.getMessage("version"), Messages.getMessage("active") };
        tbModel.setColumnIdentifiers(columNames);
        tbPlugins.getColumnModel().getColumn(0).setPreferredWidth(255);
        tbPlugins.getColumnModel().getColumn(0).setResizable(false);
        tbPlugins.getColumnModel().getColumn(1).setPreferredWidth(90);
        tbPlugins.getColumnModel().getColumn(1).setResizable(false);
        tbPlugins.getColumnModel().getColumn(2).setPreferredWidth(60);
        tbPlugins.getColumnModel().getColumn(2).setResizable(false);
    }
}
