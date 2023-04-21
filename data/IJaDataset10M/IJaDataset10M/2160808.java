package acao.command;

import acao.AcaoAgente;
import acao.AcaoArrumar;
import acao.AcaoAtenderRequisicao;
import acao.AcaoAtualizarQuadroTarefas;
import acao.AcaoChamarEmpregada;
import acao.AcaoDesarrumar;
import acao.AcaoFazNada;
import acao.AcaoIrParaACentralAtendimento;
import acao.AcaoLimpar;
import acao.AcaoPegarFaxina;
import acao.AcaoSujar;
import acao.AcaoTrocarComodo;
import acao.AcaoVerificarComodo;
import acao.AcaoVisitarResidencia;
import java.util.HashMap;
import java.util.Map;
import static util.ConstantesAplicacao.*;

/**
 *
 * @author heliokann
 */
public class ComandoAcao {

    private static Map<String, AcaoAgente> acoes = new HashMap();

    static {
        acoes.put(ACAO_ARRUMAR, new AcaoArrumar());
        acoes.put(ACAO_LIMPAR, new AcaoLimpar());
        acoes.put(ACAO_CHAMAR_EMPREGADA, new AcaoChamarEmpregada());
        acoes.put(ACAO_DESARRUMAR, new AcaoDesarrumar());
        acoes.put(ACAO_SUJAR, new AcaoSujar());
        acoes.put(ACAO_VERIFICAR_COMODO, new AcaoVerificarComodo());
        acoes.put(ACAO_ATENDER_REQUISICAO, new AcaoAtenderRequisicao());
        acoes.put(ACAO_ATUALIZAR_QUADRO_TAREFAS, new AcaoAtualizarQuadroTarefas());
        acoes.put(ACAO_TROCAR_COMODO, new AcaoTrocarComodo());
        acoes.put(ACAO_FAZ_NADA, new AcaoFazNada());
        acoes.put(ACAO_IR_PARA_A_CENTRAL, new AcaoIrParaACentralAtendimento());
        acoes.put(ACAO_PEGAR_FAXINA, new AcaoPegarFaxina());
        acoes.put(ACAO_VISITAR_RESIDENCIA, new AcaoVisitarResidencia());
    }

    public static AcaoAgente getAcao(String msg) {
        return acoes.get(msg);
    }
}
