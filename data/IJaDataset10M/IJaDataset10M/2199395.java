package br.ujr.scorecard.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import br.ujr.scorecard.model.ativo.Ativo;
import br.ujr.scorecard.model.ativo.salario.Salario;
import br.ujr.scorecard.model.ativo.saldoanterior.SaldoAnterior;
import br.ujr.scorecard.model.banco.Banco;
import br.ujr.scorecard.model.cc.ContaCorrente;
import br.ujr.scorecard.model.conta.Conta;
import br.ujr.scorecard.model.conta.ContaOrdenador;
import br.ujr.scorecard.model.observacao.Observacao;
import br.ujr.scorecard.model.orcamento.Orcamento;
import br.ujr.scorecard.model.passivo.Passivo;
import br.ujr.scorecard.model.passivo.PassivoOrdenador;
import br.ujr.scorecard.model.passivo.cartao.Cartao;
import br.ujr.scorecard.model.passivo.cheque.Cheque;
import br.ujr.scorecard.model.passivo.parcela.Parcela;
import br.ujr.scorecard.model.passivo.parcela.ParcelaOrdenador;
import br.ujr.scorecard.model.transferencia.Transferencia;

/**
 * Scorecard Facade Specification
 * @author Ualter
 */
public interface ScorecardManager {

    public Conta saveConta(Conta conta);

    public void deleteConta(Conta conta);

    public String getContaProximoNivel();

    public String getContaProximoNivel(String crt);

    public Conta getContaPorId(int id);

    public List<Conta> getContasPorNivel(String nivel);

    public List<Conta> getContaPorDescricao(String descricao);

    public List<Conta> listarContas(ContaOrdenador<Conta> contaComparator);

    public void savePassivo(Passivo passivo);

    public void deletePassivo(Passivo passivo);

    public Set<Passivo> getPassivoPorHistorico(String historico);

    public Set<Cartao> getCartaoPorFiltro(long referenciaInicial, long referenciaFinal, Cartao cartao);

    public Set<Passivo> getPassivoPorValor(ContaCorrente cc, float valor);

    public Set<Passivo> getPassivoPorValor(float valor);

    public Passivo getPassivoPorId(int id);

    public Set<Passivo> getPassivosPorReferencia(ContaCorrente contaCorrente, long referencia);

    public Set<Passivo> getPassivosPorReferencia(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public Set<Passivo> getEspecificoPassivoPorReferencia(ContaCorrente contaCorrente, Class clazz, long referenciaInicial, long referenciaFinal);

    public Cheque getChequePorNumero(ContaCorrente contaCorrente, String numero);

    public Set<Cartao> getCartaoPorOperadora(ContaCorrente contaCorrente, Cartao.Operadora enumOperadora, long referenciaInicial, long referenciaFinal);

    public void saveAtivo(Ativo ativo);

    public void deleteAtivo(Ativo ativo);

    public Ativo getAtivoPorId(int id);

    public List<Ativo> getAtivosPorReferencia(ContaCorrente contaCorrente, long referencia);

    public List<Ativo> getAtivosPorReferencia(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public List<Ativo> getAtivosPorReferencia(ContaCorrente contaCorrente, Class clazz, long referenciaInicial, long referenciaFinal);

    public SaldoAnterior getSaldoAnterior(ContaCorrente contaCorrente, long referencia);

    public BigDecimal getSaldoAnteriorPeriodo(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public Salario getSalario(ContaCorrente contaCorrente, long referencia);

    public BigDecimal getSalarioPeriodo(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public void saveObservacao(Observacao obs);

    public List<Observacao> getObservacao(String descricao);

    public Observacao getObservacao(int id);

    public void deleteObservacao(Observacao obs);

    /**
     * Reuni as informa��es de movimento de um per�odo encapsulando em objeto Resumo
     * @param referenciaInicial m�s e ano no formato YYYYMM do in�cio do per�odo
     * @param referenciaFinal   m�s e ano no formato YYYYMM do final  do per�odo
     * @param considerarOrcamento retirar do saldo previsto o valor de or�amento previsto ainda n�o realizado (o restante) ?
     * @return objeto Resumo com informa��es do movimento do per�odo desejado
     */
    public ResumoPeriodo getResumoPeriodo(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal, boolean considerarOrcamento);

    /**
     * Resumo GERAL do Periodo, inclui todas as Contas Correntes Cadastradas
     * @param contaCorrente
     * @param referenciaInicial
     * @param referenciaFinal
     * @return
     */
    public ResumoPeriodo getResumoPeriodo(long referenciaInicial, long referenciaFinal);

    public void addScorecardManagerListener(ScorecardManagerListener listener);

    public void removeScorecardManagerListener(ScorecardManagerListener listener);

    public Session getHibernateSession();

    public void ordenarPassivos(List<Passivo> passivos, PassivoOrdenador ordenador);

    public void ordenarParcelas(List<Parcela> passivos, ParcelaOrdenador ordenador);

    public void saveContaCorrente(ContaCorrente contaCorrente);

    public void deleteContaCorrente(ContaCorrente contaCorrente);

    public ContaCorrente getContaCorrentePorId(int id);

    public List<ContaCorrente> getContaCorrentePorDescricao(String descricao);

    public List<ContaCorrente> listarContaCorrente();

    public void saveBanco(Banco Banco);

    public void deleteBanco(Banco Banco);

    public boolean isBancoRemovable(Banco banco);

    public Banco getBancoPorId(int id);

    public List<Banco> getBancoPorNome(String nome);

    public List<Banco> listarBanco();

    public boolean saveOrcamento(Orcamento orcamento);

    public void deleteOrcamento(Orcamento orcamento);

    public Orcamento getOrcamentoPorId(int id);

    public Set<Orcamento> getOrcamentoPorDescricao(String descricao);

    public Set<Orcamento> getOrcamentosPorReferencia(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public void saveTransferencia(Transferencia transferencia);

    public void deleteTransferencia(Transferencia transferencia);

    public Transferencia getTransferenciaPorId(int id);

    public List<Transferencia> getTransferenciasPorReferencia(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal);

    public int getMavenTests();

    public void setMavenTests(int bool);

    public boolean isContaCorrenteRemovable(ContaCorrente contaCorrente);

    /**
     * Verificar e Gravar Saldo Anterior do seis meses passados, se estes n�o existerem gravados
     * N�o refaz, apenas cria se n�o existirem ainda!
     */
    public void consistirSaldosAnteriores(ContaCorrente contaCorrente);

    /**
     * Processa Saldos de acordo com as referencias inicial e final, opcionalmente os reprocessa if(force == true)
     * @param contaCorrente
     * @param refIni
     * @param refFim
     * @param force
     */
    public void consistirSaldosAnteriores(ContaCorrente contaCorrente, long refIni, long refFim, boolean force);

    public BigDecimal calcularSaldo(ContaCorrente contaCorrente, long referencia);

    public Set<Passivo> getPassivosPorNiveisContaContabil(ContaCorrente contaCorrente, String[] niveis, boolean incluirDescendentes, long refIni, long refFim);

    public Set<Passivo> getPassivosAssociadosOrcamento(ContaCorrente contaCorrente, long referenciaInicial, long referenciaFinal, String nivelConta);
}
