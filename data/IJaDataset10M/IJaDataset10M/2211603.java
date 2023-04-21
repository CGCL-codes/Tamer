package br.ujr.scorecard.gui.view.screen;

import java.util.Date;
import javax.swing.JFrame;
import br.ujr.scorecard.model.ScorecardManager;
import br.ujr.scorecard.model.ativo.Ativo;
import br.ujr.scorecard.model.ativo.investimento.Investimento;
import br.ujr.scorecard.model.cc.ContaCorrente;
import br.ujr.scorecard.util.Util;

public class InvestimentoFrame extends AbstractAtivoFrame {

    public InvestimentoFrame(JFrame owner, ContaCorrente contaCorrente, Date periodoDataIni, Ativo ativo) {
        super(owner, contaCorrente, periodoDataIni, ativo);
    }

    public InvestimentoFrame(JFrame owner, ContaCorrente contaCorrente, Date periodoDataIni) {
        super(owner, contaCorrente, periodoDataIni);
    }

    @Override
    public String getTitulo() {
        return "INVESTIMENTO";
    }

    @Override
    public Ativo newAtivo() {
        return new Investimento();
    }
}
