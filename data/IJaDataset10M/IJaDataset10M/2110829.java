package curso.struts.modelo.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Walter dos Santos Filho
 * @version $Revision$
 * @since 0.0
 * @hibernate.class
 */
public class Empresa implements Serializable {

    private Integer id;

    private String nome;

    private String endereco;

    private String cidade;

    private String estado;

    private String telefone;

    private boolean habilitado;

    private BigDecimal faturamento;

    private Set contatos;

    private RamoAtividade ramoAtividade;

    /**
     * Retorna o valor da propriedade <code>cidade</code>.
     * @return Retorna o valor da propriedade <code>cidade</code>.
     * @hibernate.property
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Define o valor da propriedade <code>cidade</code>.
     * @param cidade O novo valor para a propriedade <code>cidade</code>.
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * Retorna o valor da propriedade <code>endereco</code>.
     * @return Retorna o valor da propriedade <code>endereco</code>.
     * @hibernate.property
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o valor da propriedade <code>endereco</code>.
     * @param endereco O novo valor para a propriedade <code>endereco</code>.
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna o valor da propriedade <code>estado</code>.
     * @return Retorna o valor da propriedade <code>estado</code>.
     * @hibernate.property
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define o valor da propriedade <code>estado</code>.
     * @param estado O novo valor para a propriedade <code>estado</code>.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Retorna o valor da propriedade <code>faturamento</code>.
     * @return Retorna o valor da propriedade <code>faturamento</code>.
     * @hibernate.property
     */
    public BigDecimal getFaturamento() {
        return faturamento;
    }

    /**
     * Define o valor da propriedade <code>faturamento</code>.
     * @param faturamento O novo valor para a propriedade <code>faturamento</code>.
     */
    public void setFaturamento(BigDecimal faturamento) {
        this.faturamento = faturamento;
    }

    /**
     * Retorna o valor da propriedade <code>habilitado</code>.
     * @return Retorna o valor da propriedade <code>habilitado</code>.
     * @hibernate.property
     */
    public boolean isHabilitado() {
        return habilitado;
    }

    /**
     * Define o valor da propriedade <code>habilitado</code>.
     * @param habilitado O novo valor para a propriedade <code>habilitado</code>.
     */
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    /**
     * Retorna o valor da propriedade <code>id</code>.
     * @return Retorna o valor da propriedade <code>id</code>.
     * @hibernate.id generator-class = "native"
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o valor da propriedade <code>id</code>.
     * @param id O novo valor para a propriedade <code>id</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna o valor da propriedade <code>nome</code>.
     * @return Retorna o valor da propriedade <code>nome</code>.
     * @hibernate.property
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade <code>nome</code>.
     * @param nome O novo valor para a propriedade <code>nome</code>.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o valor da propriedade <code>telefone</code>.
     * @return Retorna o valor da propriedade <code>telefone</code>.
     * @hibernate.property
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o valor da propriedade <code>telefone</code>.
     * @param telefone O novo valor para a propriedade <code>telefone</code>.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o valor da propriedade <code>contatos</code>.
     * @return Retorna o valor da propriedade <code>contatos</code>.
     * @hibernate.set inverse = "true" lazy = "false" 
     * @hibernate.collection-one-to-many 
     *  class = "curso.struts.modelo.entidade.Contato"
     * @hibernate.collection-key column = "idEmpresa" 
     */
    public Set getContatos() {
        return contatos;
    }

    /**
     * Define o valor da propriedade <code>contatos</code>.
     * @param contatos O novo valor para a propriedade <code>contatos</code>.
     */
    protected void setContatos(Set contatos) {
        this.contatos = contatos;
    }

    /**
     * Retorna o valor da propriedade <code>ramoAtividade</code>.
     * @return Retorna o valor da propriedade <code>ramoAtividade</code>.
     * @hibernate.many-to-one column = "idRamoAtividade"
     */
    public RamoAtividade getRamoAtividade() {
        return ramoAtividade;
    }

    /**
     * Define o valor da propriedade <code>ramoAtividade</code>.
     * @param ramoAtividade O novo valor para a propriedade <code>ramoAtividade</code>.
     */
    public void setRamoAtividade(RamoAtividade ramoAtividade) {
        this.ramoAtividade = ramoAtividade;
    }
}
