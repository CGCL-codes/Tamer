package br.gov.ba.mam.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import br.gov.ba.mam.beans.Artista;
import br.gov.ba.mam.beans.ESTADO;
import br.gov.ba.mam.gerente.GerenteAplicacao;
import br.gov.ba.mam.swing.PainelConsulta.TELA;

/**
 * @author Dora
 *
 */
public class PainelQtdArtistaEstado extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane scroll = null;

    private JTable tabelaArtista = null;

    private ChangeListener change;

    /**
	 * @param telaPrincipal
	 */
    public PainelQtdArtistaEstado(ChangeListener telaPrincipal) {
        change = telaPrincipal;
        this.setLayout(null);
        scroll = new JScrollPane(this.tabelaArtista);
        JButton btVoltar = new JButton("Voltar");
        btVoltar.setBounds(420, 511, 100, 25);
        btVoltar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                change.stateChanged(new ChangeEvent(TELA.TELA_CONSULTA));
            }
        });
        add(btVoltar);
        atualizarJTable();
    }

    public void atualizarJTable() {
        if (scroll != null) {
            this.remove(scroll);
        }
        Vector<String> titulo = new Vector<String>();
        titulo.add("Estado");
        titulo.add("Quantidade");
        Vector<Vector<Object>> colunas = new Vector<Vector<Object>>();
        int[] listaValores = new int[ESTADO.values().length];
        List<Artista> listaArtista = GerenteAplicacao.getInstancia().getListaArtistas();
        for (Artista artista : listaArtista) {
            ESTADO est = ESTADO.values()[artista.getEndereco().getEstado()];
            listaValores[est.ordinal()] = listaValores[est.ordinal()] + 1;
        }
        for (int i = 0; i < listaValores.length; i++) {
            Vector<Object> row = new Vector<Object>();
            row.add(ESTADO.values()[i]);
            row.add(listaValores[i]);
            colunas.add(row);
        }
        DefaultTableModel model = new DefaultTableModel(colunas, titulo);
        this.tabelaArtista = new JTable(model) {

            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        this.tabelaArtista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.tabelaArtista.setVisible(true);
        this.scroll = new JScrollPane(this.tabelaArtista);
        this.scroll.setBounds(5, 60, 515, 450);
        this.add(scroll);
        this.scroll.revalidate();
        this.tabelaArtista.revalidate();
        this.revalidate();
    }
}
