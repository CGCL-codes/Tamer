package br.ufrj.dcc.sistemasoperacionais.passagensaereas.cliente.gui.listeners;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JTable;
import br.ufrj.dcc.sistemasoperacionais.passagensaereas.cliente.controle.Cliente;

public class AtualizarReservas extends Listener {

    JLabel consultarReservas;

    public AtualizarReservas(JTable table, JLabel consultarReservas) {
        super(table);
        this.consultarReservas = consultarReservas;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int reservas = Cliente.getInstance().consultaReserva((Integer) table.getModel().getValueAt(table.getSelectedRow(), 0));
            if (reservas < 0) {
                reservas = 0;
            }
            if (reservas < 1) consultarReservas.setText("n�o existem reservas para o trecho selecionado."); else consultarReservas.setText("existem " + reservas + " assento(s) reservado(s).");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {
            consultarReservas.setText("primeiro selecione um trecho.");
        }
        super.actionPerformed(e);
    }
}
