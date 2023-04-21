package carrancao.controlador;

import carrancao.entidades.Despesas;
import carrancao.exception.CadastrarDespesasException;
import carrancao.repositorio.DespesasRepositorio;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Lubnnia
 */
public class DespesasControlador {

    private DespesasRepositorio despesasRep;

    public DespesasControlador() {
        this.despesasRep = new DespesasRepositorio();
    }

    public void validarCaixaGeral(Despesas despesas) throws CadastrarDespesasException {
        if (despesas.getMotivoConta().equals("") || despesas.getValor() == 0) {
        }
    }

    public void cadastrarDespesas(Despesas despesas) throws CadastrarDespesasException {
        this.validarCaixaGeral(despesas);
        this.despesasRep.cadastrarDespesas(despesas);
        JOptionPane.showMessageDialog(null, "Despesa Cadastrada com Sucesso!", "Carrancão Hamburgueria", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean alterarDespesas(Despesas despesas) throws CadastrarDespesasException {
        boolean resposta = false;
        if (!(despesas.getValor() == 0 || despesas.getUsuario().getLogin().equals(""))) {
            this.despesasRep.alterarDespesas(despesas);
            resposta = true;
        } else {
            resposta = false;
        }
        return resposta;
    }

    public void deletarDespesas(Despesas despesas) {
        if (despesas != null) {
            this.despesasRep.deletarDespesas(despesas);
            JOptionPane.showMessageDialog(null, "Dados Excluídos com Sucesso!", "Carrancão Hamburgueria", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Operação inválida!", "Carrancão Hamburgueria", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Despesas pesquisarDespesasPorID(int id) {
        Despesas despesas = this.despesasRep.pesquisarDespesasPorID(id);
        return despesas;
    }

    public List pesquisarDespesasTodos() {
        List despesas = this.despesasRep.pesquisarDespesasTodos();
        return despesas;
    }

    public List pesquisarDespesasDoDia() {
        List despesas = this.despesasRep.pesquisarDespesasDoDia();
        return despesas;
    }
}
