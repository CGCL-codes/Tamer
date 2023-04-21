package com.locafacil.gui;

import java.awt.Container;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FormBaixaAluguel extends FormCadastro {

    static String[] fieldsDef = { "lorem", "cli.vc_nome", "car.vc_nome", "data_prevista" };

    public static String[] cols = { "Cliente", "Carro", "Previsao de Entrega" };

    public static String[] fields = { "Todos", "Cliente", "Carro", "Previsao de Entrega" };

    private JTextField txKilometrage;

    private JTextField txTank;

    private JTextField txData;

    private JButton btCalcular;

    private JButton btBaixa;

    private JTable formTable;

    private DefaultTableModel tableModel;

    private JScrollPane panelTable;

    private JPanel panelHolder;

    public FormBaixaAluguel() {
        init();
    }

    private void init() {
        this.tableModel = new DefaultTableModel();
        for (int x = 0; x < cols.length; x++) {
            this.tableModel.addColumn(cols[x]);
        }
        this.formTable = new JTable(this.tableModel);
        panelTable = new JScrollPane(formTable);
        setTitle("Baixa de Aluguel");
        panelCampos.setLayout(new GridBagLayout());
        panelCampos.add("Kilometragem", getTxKilometrage());
        panelCampos.add("Tanque", getTxTank());
        panelCampos.add("Data de Devolu��o", getTxData());
        panelCampos.add(panelTable);
    }

    public JTextField getTxKilometrage() {
        if (txKilometrage == null) {
            setTxKilometrage(new JTextField());
        }
        return txKilometrage;
    }

    public void setTxKilometrage(JTextField txKilometrage) {
        this.txKilometrage = txKilometrage;
    }

    public JTextField getTxTank() {
        if (txTank == null) {
            setTxTank(new JTextField());
        }
        return txTank;
    }

    public void setTxTank(JTextField txTank) {
        this.txTank = txTank;
    }

    public JTextField getTxData() {
        if (txData == null) {
            setTxData(new JTextField());
        }
        return txData;
    }

    public void setTxData(JTextField txData) {
        this.txData = txData;
    }

    public JButton getBtCalcular() {
        if (btCalcular == null) {
            btCalcular = new JButton("Calcular");
        }
        return btCalcular;
    }

    public void setBtCalcular(JButton btCalcular) {
        this.btCalcular = btCalcular;
    }

    public JButton getBtBaixa() {
        if (btBaixa == null) {
            btBaixa = new JButton("Baixa");
        }
        return btBaixa;
    }

    public void setBtBaixa(JButton btBaixa) {
        this.btBaixa = btBaixa;
    }

    public JTable getFormTable() {
        if (formTable == null) {
            formTable = new JTable();
        }
        return formTable;
    }

    public void setFormTable(JTable formTable) {
        this.formTable = formTable;
    }

    public DefaultTableModel getTableModel() {
        if (tableModel == null) {
            tableModel = new DefaultTableModel();
        }
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }
}
