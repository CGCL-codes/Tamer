package com.locafacil.gui;

import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class FormCadastroAluguelCarro extends FormCadastro {

    private JComboBox cbCliente;

    private JComboBox cbCarro;

    private JTextField txtDataInicial;

    private JTextField txtDataFinal;

    private JComboBox cbTipoAluguel;

    public FormCadastroAluguelCarro() {
        panelCampos.setLayout(new GridBagLayout());
        cbCliente = new JComboBox();
        cbCarro = new JComboBox();
        txtDataInicial = new JTextField();
        txtDataFinal = new JTextField();
        cbTipoAluguel = new JComboBox();
        panelCampos.add("Cliente", cbCliente);
        panelCampos.add("Carro", cbCarro);
        panelCampos.add("Data Inicial do Aluguel", txtDataInicial);
        panelCampos.add("Data Final do Aluguel", txtDataFinal);
        panelCampos.add("Tipo do aluguel", cbTipoAluguel);
    }
}
