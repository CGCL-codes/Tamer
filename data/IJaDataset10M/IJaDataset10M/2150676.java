package br.eng.eliseu.j2me.pelada.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import br.eng.eliseu.j2me.pelada.db.LancamentoQuery;
import br.eng.eliseu.j2me.pelada.db.LancamentoRegistro;
import br.eng.eliseu.j2me.pelada.db.PeladaQuery;
import br.eng.eliseu.j2me.pelada.db.PeladaRegistro;
import br.eng.eliseu.j2me.pelada.db.PeladeiroQuery;
import br.eng.eliseu.j2me.pelada.db.PeladeiroRegistro;

public class FormSelectPeladeiros extends List implements CommandListener {

    private Pelada hMain;

    private FormLancamentos hPai;

    private String titulo;

    private PeladaRegistro tblPelada;

    private LancamentoRegistro tblLancamento;

    private int idPelada;

    private Command cmdGrava, cmdVoltar;

    private PeladeiroRegistro tblPeladeiro = new PeladeiroRegistro();

    public FormSelectPeladeiros(Pelada hMain, FormLancamentos hPai, String titulo, PeladaRegistro tblPelada, LancamentoRegistro tblLancamento, int idPelada) {
        super(titulo, List.MULTIPLE);
        this.hMain = hMain;
        this.hPai = hPai;
        this.titulo = titulo;
        this.tblPelada = tblPelada;
        this.tblLancamento = tblLancamento;
        this.idPelada = idPelada;
        cmdGrava = new Command("Gravar", Command.SCREEN, 1);
        cmdVoltar = new Command("Voltar", Command.EXIT, 1);
        this.addCommand(cmdGrava);
        this.addCommand(cmdVoltar);
        this.setCommandListener(this);
        populaListNaPelada();
    }

    public void populaListNaPelada() {
        this.deleteAll();
        int i;
        PeladeiroRegistro[] p = PeladeiroQuery.selectPeladeiroPorNome(tblPeladeiro.getRS());
        LancamentoRegistro[] l = LancamentoQuery.selectHorarioNaoNulo(tblLancamento.getRS(), idPelada);
        if (p != null && p[0] != null) {
            int indice = 0, selecionados = 0;
            boolean achou = false;
            for (i = 0; i < p.length; i++) {
                achou = false;
                String hora = "";
                if (l != null && l[0] != null) {
                    for (int j = 0; j < l.length; j++) {
                        if (p[i].getId() == l[j].getId_Peladeiro()) {
                            hora = " (" + l[j].getHorariosToStr() + " horario(s))";
                            selecionados++;
                            achou = true;
                            break;
                        }
                    }
                }
                indice = this.append("[" + p[i].getId() + "] " + p[i].getNome() + hora, null);
                this.setSelectedIndex(indice, achou);
            }
            this.setTitle(this.titulo + " (" + selecionados + ")");
        }
    }

    private int getIdList(int indice) {
        if (indice >= 0) {
            String s = this.getString(indice);
            if (!"".equals(s)) {
                for (int i = 0; i < s.length(); i++) {
                    if ("]".equals(s.substring(i, i + 1))) {
                        return Integer.parseInt(s.substring(1, i));
                    }
                }
            }
        }
        return -1;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdGrava) {
            int indice = this.size();
            boolean[] selecionado = new boolean[indice];
            int idPeladeiro;
            PeladaRegistro[] pp = PeladaQuery.selectPeladasPorId(tblPelada.getRS(), idPelada);
            if (pp != null && pp[0] != null) {
                this.getSelectedFlags(selecionado);
                for (int i = 0; i < indice; i++) {
                    idPeladeiro = getIdList(i);
                    byte[] recData = null;
                    LancamentoRegistro[] l = LancamentoQuery.selectLancamento(tblLancamento.getRS(), idPelada, idPeladeiro);
                    if (l != null && l[0] != null) {
                        if (selecionado[i]) {
                            l[0].setHorarios(pp[0].getHorariosToStr());
                            recData = l[0].getSequenciaDeRegistrOut();
                            tblLancamento.updateSequencia(l[0].getIdRS(), recData);
                        } else {
                            tblLancamento.deleteSequencia(l[0].getIdRS());
                        }
                    } else {
                        if (selecionado[i]) {
                            LancamentoRegistro ll = new LancamentoRegistro();
                            recData = ll.gravaRegistro(this.idPelada, idPeladeiro, pp[0].getHorariosToStr(), "0.0", "0.0");
                            tblLancamento.insertSequencia(recData);
                        }
                    }
                }
            }
        }
        this.hPai.populaTela();
        this.hMain.telas.retiraTela();
    }
}
