package br.com.jnfe.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.helianto.core.Entity;
import org.helianto.partner.Division;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.jnfe.base.CRT;
import br.com.jnfe.base.TpAmb;
import br.com.jnfe.base.TpServico;
import br.com.jnfe.base.UF;
import br.com.jnfe.util.CNPJUtils;

/**
 * Configura o ambiente de servi�o no portal da
 * Secretaria de Fazenda Estadual.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated
 */
@javax.persistence.Entity
@Table(name = "jnfe_Ambiente", uniqueConstraints = { @UniqueConstraint(columnNames = { "divisionId", "tpAmb" }) })
public class Ambiente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private Integer version;

    private Division emitente;

    private String email;

    private TpAmb tpAmb;

    private char crt;

    private Map<TpServico, Servico> servicos = new HashMap<TpServico, Servico>();

    /**
	 * Construtor vazio.
	 */
    public Ambiente() {
        setTpAmb(TpAmb.HOMOLOGACAO);
        if (emitente != null) {
            setEmail(emitente.getMainEmail());
        } else {
            setEmail("");
        }
        setCrt(CRT.NORMAL);
    }

    /**
	 * Construtor preferido.
	 * 
	 * @param emitente
	 */
    public Ambiente(Division emitente) {
        this();
        setEmitente(emitente);
    }

    /**
	 * Construtor chave.
	 * 
	 * @param emitente
	 * @param tpAmb
	 */
    public Ambiente(Division emitente, TpAmb tpAmb) {
        this(emitente);
        setTpAmb(tpAmb);
    }

    /**
     * Chave prim�ria.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vers�o do registro na base de dados.
     */
    @Version
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * <<NaturalKey>> Emitente.
     * 
     * <p>
     * Uma �nica entidade (chamamos entidade para manter a
     * linguagem usada para organiza��es no projeto Helianto)
     * pode utilizar v�rios CNPJs diferentes, como no caso de 
     * filiais. Para permitir gest�o unificada destes CNPJs, � 
     * utilizada a no��o de divis�o.
     * </p>
     * 
     * <p>
     * A camada de servi�o deveria assegurar que houvesse pelo menos
     * uma divis�o para cada entidade antes da cria��o de uma s�rie,
     * preferencialmente criada de forma transparente para o usu�rio.
     * </p>
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "divisionId", nullable = true)
    public Division getEmitente() {
        return this.emitente;
    }

    public void setEmitente(Division division) {
        this.emitente = division;
    }

    /**
     * <<Transient>> Conveniente para recuperar a entidade.
     */
    @Transient
    public Entity getEntity() {
        if (getEmitente() == null) {
            throw new IllegalArgumentException("Imposs�vel identificar entidade: emitente n�o definido para o ambiente " + this + ".");
        }
        return getEmitente().getPrivateEntity().getEntity();
    }

    /**
     * Email do emitente.
     */
    @Column(length = 40)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <<Transient>> Conveniente para recuperar o CNPJ do emitente.
     */
    @Transient
    public String getCNPJ() {
        if (getEmitente() != null) {
            return CNPJUtils.getCNPJ(getEmitente());
        }
        return "00000000000000";
    }

    /**
     * <<Transient>> Conveniente para recuperar o c�digo da unidade da federa��o.
     * 
     * <p>
     * Note que o emitente torna-se vinculado a um ambiente atrav�s do 
     * c�digo da unidade da federa��o.
     * </p>
     * 
     * @see {@link UF}
     */
    @Transient
    public String getCUf() {
        if (getEmitente() != null) {
            Municipio municipio = (Municipio) emitente.getProvince();
            return municipio.getCUf();
        }
        logger.warn("CUF INV�LIDA!");
        return "";
    }

    /**
	 * <<NaturalKey>> Tipo de ambiente.
	 */
    @Enumerated(EnumType.STRING)
    public TpAmb getTpAmb() {
        return tpAmb;
    }

    public void setTpAmb(TpAmb tpAmb) {
        this.tpAmb = tpAmb;
    }

    @OneToMany(mappedBy = "ambiente", cascade = CascadeType.ALL)
    @MapKey(name = "tpServico")
    public Map<TpServico, Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Map<TpServico, Servico> servicos) {
        this.servicos = servicos;
    }

    /**
	 * C�digo de regime tribut�rio.
	 */
    public char getCrt() {
        return crt;
    }

    public void setCrt(char crt) {
        this.crt = crt;
    }

    public void setCrt(CRT crt) {
        this.crt = crt.getValue();
    }

    /**
     * toString
     * @return String
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("emitente").append("='").append(getEmitente()).append("' ");
        buffer.append("tpAmb").append("='").append(getTpAmb()).append("' ");
        buffer.append("]");
        return buffer.toString();
    }

    /**
	 * Igualidade.
	 */
    @Override
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Ambiente)) return false;
        Ambiente castOther = (Ambiente) other;
        return ((this.getEmitente() == castOther.getEmitente()) || (this.getEmitente() != null && castOther.getEmitente() != null && this.getEmitente().equals(castOther.getEmitente()))) && ((this.getTpAmb() == castOther.getTpAmb()) || (this.getTpAmb() != null && castOther.getTpAmb() != null && this.getTpAmb().equals(castOther.getTpAmb())));
    }

    /**
	 * hashCode
	 */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getEmitente() == null ? 0 : this.getEmitente().hashCode());
        result = 37 * result + (getTpAmb() == null ? 0 : this.getTpAmb().hashCode());
        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(Ambiente.class);
}
