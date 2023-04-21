package br.com.hsj.financeiro.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import br.com.hsj.financeiro.entidade.Movimentacao;
import br.com.hsj.financeiro.entidade.SubCategoria;
import br.com.hsj.financeiro.entidade.TipoMovimentacao;
import br.com.hsj.financeiro.exception.BusinessException;
import br.com.hsj.financeiro.servico.CategoriaServico;
import br.com.hsj.financeiro.servico.MovimentacaoServico;
import br.com.hsj.financeiro.util.ComboUtils;

/**
 * Managed bean que manipula as informações das movimentações do mês atual
 * 
 * @author Hamilton dos Santos Junior
 * @date 07/10/2011
 *
 */
@ManagedBean(name = "movimentacaoController")
@ViewScoped
public class MovimentacaoController extends BaseController {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1127463673218687499L;

    private static Logger logger = Logger.getLogger(MovimentacaoController.class);

    private Movimentacao movimentacao;

    private Movimentacao saldoDisponivel;

    private List<Movimentacao> listaMovimentacoes;

    private List<SelectItem> itens;

    private List<SelectItem> itensCategorias;

    @ManagedProperty(value = "#{movimentacaoServico}")
    private MovimentacaoServico movimentacaoServico;

    @ManagedProperty(value = "#{categoriaServico}")
    private CategoriaServico categoriaServico;

    @PostConstruct
    public void init() {
        inicializarMovimentacao();
        itens = ComboUtils.montarComboTipoMovimentacao();
        buscarSubcategorias();
        buscar();
    }

    /**
	 * Método que inicializa o objeto {@link Movimentacao}
	 */
    private void inicializarMovimentacao() {
        movimentacao = new Movimentacao();
        movimentacao.setSubcategoria(new SubCategoria());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.DEBITO);
    }

    /**
	 * Método que busca as subcategorias com o tipo de movimentação selecionado 
	 */
    private void buscarSubcategorias() {
        List<SubCategoria> lista = null;
        try {
            lista = categoriaServico.buscarSubCategoriasPorTipoMovimentacao(movimentacao.getTipoMovimentacao());
        } catch (BusinessException e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar as Categorias", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (lista != null) {
            itensCategorias = new ArrayList<SelectItem>();
            itensCategorias.add(new SelectItem(new Long(0), "Selecione"));
            for (SubCategoria sub : lista) {
                itensCategorias.add(new SelectItem(sub.getId(), sub.getDescricao()));
            }
        }
    }

    /**
	 * Método utilizado para salvar uma movimentação
	 */
    public void salvar() {
        logger.info("Iniciando o método salvar");
        FacesMessage msg = null;
        try {
            movimentacaoServico.salvar(movimentacao);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro salvo com sucesso");
            inicializarMovimentacao();
        } catch (BusinessException e) {
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no cadastro", e.getMessage());
        }
        logger.info("Finalizando o método salvar");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        buscar();
    }

    /**
	 * Método utilziado para buscar as movimentacoes
	 */
    public void buscar() {
        logger.info("Iniciando o método buscar");
        FacesMessage msg = null;
        try {
            listaMovimentacoes = movimentacaoServico.buscarMovimentacoesUltimos30Dias();
            saldoDisponivel = movimentacaoServico.calcularSaldoDisponivel(listaMovimentacoes);
        } catch (BusinessException e) {
            e.printStackTrace();
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na consulta", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        logger.info("Finalizando o método buscar");
    }

    /**
	 * Método que recebe uma movimentação por parametro e 
	 * se for de Débito retorna <code>true</code>
	 * @param _movimentacao
	 * @return
	 */
    public boolean verificarDebito(Movimentacao _movimentacao) {
        boolean debito = false;
        if (TipoMovimentacao.DEBITO.equals(_movimentacao.getTipoMovimentacao())) {
            debito = true;
        }
        return debito;
    }

    /************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
    public void setMovimentacaoServico(MovimentacaoServico movimentacaoServico) {
        this.movimentacaoServico = movimentacaoServico;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    public List<SelectItem> getItens() {
        return itens;
    }

    public List<Movimentacao> getListaMovimentacoes() {
        return listaMovimentacoes;
    }

    public void setListaMovimentacoes(List<Movimentacao> listaMovimentacoes) {
        this.listaMovimentacoes = listaMovimentacoes;
    }

    public void setCategoriaServico(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public List<SelectItem> getItensCategorias() {
        return itensCategorias;
    }

    public Movimentacao getSaldoDisponivel() {
        return saldoDisponivel;
    }
}
