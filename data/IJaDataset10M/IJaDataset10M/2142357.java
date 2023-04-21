package controller;

import dao.ContaDAO;
import java.awt.Toolkit;
import javax.swing.JTable;
import model.Conta;
import view.componentes.JComboBoxModelX;

/**
 *
 * @author aluno
 */
public class ContaController {

    private Conta conta;

    private ContaDAO contaDAO;

    private JComboBoxModelX contaJCModel;

    /**
     * 
     * Construtor
     */
    public ContaController() {
        super();
        conta = new Conta();
        contaDAO = new ContaDAO();
    }

    /**
     * 
     * @return
     */
    public boolean verificaRegistros() {
        boolean encontrou = false;
        if (contaDAO.getMaiorCodigo() == 0) {
            encontrou = false;
        } else {
            encontrou = true;
        }
        return encontrou;
    }

    /**
     * 
     * @return
     */
    public String getNovoCodigo() {
        String novoCodigo = "0";
        contaDAO = new ContaDAO();
        int codigo = contaDAO.getMaiorCodigo() + 1;
        novoCodigo = String.valueOf(codigo);
        return novoCodigo;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public Conta getConta(String codigo) {
        conta = contaDAO.getConta(codigo);
        return conta;
    }

    /**
     * 
     * @return
     */
    public Conta getConta() {
        conta = contaDAO.getConta(String.valueOf(contaDAO.getMaiorCodigo()));
        return conta;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public Conta getAnterior(String codigo) {
        conta = contaDAO.getAnterior(codigo);
        if (conta.getConCodigo() == 0) {
            conta = contaDAO.getConta(codigo);
            Toolkit t = Toolkit.getDefaultToolkit();
            t.beep();
        }
        return conta;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public Conta getProximo(String codigo) {
        conta = contaDAO.getProximo(codigo);
        if (conta.getConCodigo() == 0) {
            conta = contaDAO.getConta(codigo);
            Toolkit t = Toolkit.getDefaultToolkit();
            t.beep();
        }
        return conta;
    }

    /**
     * 
     * @return
     */
    public Conta getPrimeiro() {
        conta = contaDAO.getPrimeiro();
        if (conta.getConCodigo() == 0) {
            conta = new Conta();
        }
        return conta;
    }

    /**
     * 
     * @return
     */
    public Conta getUltimo() {
        conta = contaDAO.getUltimo();
        if (conta.getConCodigo() == 0) {
            conta = new Conta();
        }
        return conta;
    }

    /**
     * 
     * @param jTable
     */
    public void getTableContas(JTable jTable) {
        contaDAO.getTabelaContas(jTable);
    }

    /**
     * 
     * @param pConta
     * @return
     */
    public boolean alterar(Conta pConta) {
        boolean atualizou = false;
        if (contaDAO.atualizar(pConta)) {
            atualizou = true;
        }
        return atualizou;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public boolean selecionar(String codigo) {
        boolean atualizou = false;
        if (contaDAO.selecionar(codigo)) {
            atualizou = true;
        }
        return atualizou;
    }

    /**
     * 
     * @param selecionado
     * @return
     */
    public boolean selecao(boolean selecionado) {
        boolean atualizou = false;
        if (contaDAO.selecao(selecionado)) {
            atualizou = true;
        }
        return atualizou;
    }

    /**
     * 
     * @param pConta
     * @return
     */
    public boolean salvar(Conta pConta) {
        boolean salvou = false;
        if (contaDAO.salvar(pConta)) {
            salvou = true;
        }
        return salvou;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public boolean excluir(String codigo) {
        boolean excluiu = false;
        if (contaDAO.excluir(codigo)) {
            excluiu = true;
        }
        return excluiu;
    }

    /**
     * 
     * @return
     */
    public JComboBoxModelX getJCModelConta() {
        contaJCModel = contaDAO.getContaJCModel();
        return contaJCModel;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public Conta getContaModel(int codigo) {
        String indice = contaJCModel.getCodigos().get(codigo).toString();
        conta = contaDAO.getConta(indice);
        return conta;
    }
}
