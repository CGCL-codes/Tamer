package rn;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.ForeignKey;
import db.DAO;

@Entity
public class Produto {

    @Id
    @GeneratedValue
    private long idProduto;

    @Column(columnDefinition = "VARCHAR(20)")
    private String codigoBarras;

    private String descricao;

    private float custo;

    private float lucro;

    @Column(columnDefinition = "Text")
    private String observacoes;

    private Date validade;

    private int estado;

    private Date ts;

    @ManyToOne
    @JoinColumn(name = "idSessao")
    @ForeignKey(name = "FK_Produto_Sessao")
    private Sessao sessao;

    /**
	 * Construtor
	 *
	 */
    public Produto() {
        super();
    }

    public Produto(long idProduto, int estado) {
        super();
        this.idProduto = idProduto;
        this.estado = estado;
    }

    public Produto(long idProduto) {
        super();
        this.idProduto = idProduto;
    }

    /**
	 * Constrututor Editar
	 * @param idProduto
	 * @param codigoBarras
	 * @param descricao
	 * @param observacoes
	 * @param estado
	 * @param ts
	 * @param sessao
	 */
    public Produto(long idProduto, String codigoBarras, String descricao, String observacoes, int estado, Date ts, Sessao sessao, float custo, float lucro) {
        this(codigoBarras, descricao, observacoes, estado, ts, sessao, custo, lucro);
        this.idProduto = idProduto;
    }

    /**
	 * Constrututor Novo
	 * @param codigoBarras
	 * @param descricao
	 * @param observacoes
	 * @param estado
	 * @param ts
	 * @param sessao
	 */
    public Produto(String codigoBarras, String descricao, String observacoes, int estado, Date ts, Sessao sessao, float custo, float lucro) {
        super();
        this.codigoBarras = codigoBarras;
        this.descricao = descricao;
        this.observacoes = observacoes;
        this.estado = estado;
        this.ts = ts;
        this.sessao = sessao;
        this.custo = custo;
        this.lucro = lucro;
    }

    public List lista() {
        DAO<Produto> dao = new DAO<Produto>();
        return dao.lista(this.getClass().getSimpleName());
    }

    public List listaAtivos() {
        List listaAll = lista();
        List<Produto> listaAtivos = new ArrayList<Produto>();
        Produto produto = new Produto();
        for (Iterator it = listaAll.iterator(); it.hasNext(); ) {
            produto = (Produto) it.next();
            if (produto.getEstado() == 1) {
                listaAtivos.add(produto);
            }
        }
        return listaAtivos;
    }

    /**
	 * Cadastrar
	 * @return
	 */
    public boolean cadastrar() {
        DAO<Produto> bd = new DAO<Produto>();
        return bd.cadastra(this);
    }

    /**
	 * Editar
	 * @return boolean
	 */
    public boolean editar() {
        DAO<Produto> bd = new DAO<Produto>();
        return bd.edita(this);
    }

    /**
	 * Excluir
	 * @return
	 */
    public boolean excluir() {
        DAO<Produto> bd = new DAO<Produto>();
        return bd.edita(this);
    }

    /**
	 * Busca um Acervo pelo Id
	 * @param id
	 * @return
	 */
    public Produto getById(long id) {
        DAO<Produto> dao = new DAO<Produto>(Produto.class);
        return (Produto) dao.getById(id);
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(long idProduto) {
        this.idProduto = idProduto;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public float getLucro() {
        return lucro;
    }

    public void setLucro(float lucro) {
        this.lucro = lucro;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }
}
