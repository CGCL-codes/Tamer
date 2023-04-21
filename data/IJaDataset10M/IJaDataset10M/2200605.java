package br.ita.doacoes.view.cadastrodoacoes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import br.ita.doacoes.core.templates.GenericDAOJPA;
import br.ita.doacoes.domain.cadastrodoacoes.Item;
import br.ita.doacoes.domain.cadastrodoacoes.Pacote;
import br.ita.doacoes.view.cadastrofamilia.FamiliaFrame;

public class ConsultaPacotePanel extends ConsultaMestreDetalhe {

    JFrame detalhesPacote;

    ConsultaPacotePanel() {
        this.setClasse(Pacote.class);
        this.setOpcao(new OpcaoConsultaPacote());
        this.setXmlForm("consultaPacoteForm.xml");
        this.setXmlTable("pacoteTable.xml");
        this.setFormName("consultaPacoteForm");
        this.setTableName("pacoteTable");
        this.setDao(new GenericDAOJPA(Pacote.class));
        this.setXmlTableDetalhe("itemTable.xml");
        this.setTableDetalheName("ItemTable");
        this.setClasseDetalhe(Item.class);
        this.setDetalheProperty("itemsList");
        this.setLarguraTabela(120);
        this.setAlturaTabelaDetalhe(300);
        this.init();
    }

    public static void main(String[] args) {
        JPanel consulta = new ConsultaPacotePanel();
        JFrame frame = new JFrame("Consulta Pacote");
        frame.add(consulta);
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void doubleClick(Object o) {
        detalhesPacote = new DetalhePacote(o);
    }
}
