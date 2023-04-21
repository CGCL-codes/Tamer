package br.com.dip.controller.cadastro;

import java.util.Date;
import br.com.dip.constantes.Constantes;
import br.com.dip.controller.ControllerPadrao;
import br.com.dip.entidade.HistoricoMatricula;
import br.com.dip.entidade.Matricula;
import br.com.dip.excecoes.ApplicationException;
import br.com.dip.excecoes.ValidadorException;
import br.com.dip.gerentes.GerenteCadastro;
import br.com.dip.inicializador.InicializadorMatriculaCompleta;
import br.com.dip.ouvinte.OuvinteSelecao;

public class RematricularController extends ControllerPadrao {

    private Matricula matricula;

    private HistoricoMatricula historicoAtual;

    private OuvinteSelecao ouvinte;

    public RematricularController(Matricula matricula) {
        super();
        setMatricula((Matricula) (new InicializadorMatriculaCompleta()).inicializar(matricula));
        getSessionScope().put("rematricular", this);
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public String acaoRematricular() {
        getMatricula().setDataAbertura(getHistoricoAtual().getDataInicio());
        getMatricula().setStatus(Constantes.CT_MATRICULAABERTA);
        getMatricula().setDataFechamento(null);
        getMatricula().setDataTrancamento(null);
        getMatricula().setUsuarioTrancamento(getUsuarioLogado());
        HistoricoMatricula ultimoHistorico = null;
        if (getMatricula().getListaHistoricoMatricula().size() != 0) {
            ultimoHistorico = getMatricula().getListaHistoricoMatricula().get(getMatricula().getListaHistoricoMatricula().size() - 1);
        }
        if (ultimoHistorico != null) {
            ultimoHistorico.setDataFim(getHistoricoAtual().getDataInicio());
            ultimoHistorico.setUsuarioFechamento(getUsuarioLogado());
            getMatricula().getListaHistoricoMatricula().set(getMatricula().getListaHistoricoMatricula().indexOf(ultimoHistorico), ultimoHistorico);
        }
        GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
        try {
            getHistoricoAtual().setMatricula(getMatricula());
            getMatricula().getListaHistoricoMatricula().add(getHistoricoAtual());
            gc.alterar(getMatricula());
            getOuvinte().selecaoFeita(getMatricula());
            return "MATRICULA";
        } catch (ApplicationException e) {
            setErroMessage(e);
            return "CURRENTPAGE";
        } catch (ValidadorException e) {
            setErroMessage(e);
            return "CURRENTPAGE";
        }
    }

    public HistoricoMatricula getHistoricoAtual() {
        if (historicoAtual == null) {
            historicoAtual = new HistoricoMatricula();
            historicoAtual.setDataInicio(new Date());
            historicoAtual.setStatus(Constantes.CT_MATRICULAABERTA);
            historicoAtual.setUsuarioAbertura(getUsuarioLogado());
        }
        return historicoAtual;
    }

    public void setHistoricoAtual(HistoricoMatricula historicoAtual) {
        this.historicoAtual = historicoAtual;
    }

    public OuvinteSelecao getOuvinte() {
        return ouvinte;
    }

    public void setOuvinte(OuvinteSelecao ouvinte) {
        this.ouvinte = ouvinte;
    }
}
