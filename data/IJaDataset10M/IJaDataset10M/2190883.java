package br.nic.connector.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Classe responsável pela contagem da quantidade de links referentes a cada Domínio em cada site.
 * @author Heitor
 * @author Pedro Hadek
  */
@Entity
public class LinksDominios {

    /**
	 * These "NAME" values refer to the variable names on the DataBase. Used to guarantee that the name
	 * being used on a query about the value is updated.
	 */
    public static final String NAME_Table = "LinksDominios";

    public static final String NAME_Quantidade = "quantidade";

    public static final String NAME_ID = "ID";

    public static final String NAME_sitio_id = "sitio_id";

    public static final String NAME_dominio = "dominio";

    public static final String NAME_local = "local";

    public static final String NAME_tamanho = "tamanho";

    public static final String NAME_qteLinks = "qteLinks";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private Long sitio_id;

    private Boolean local;

    private String tld;

    private String cctld;

    private String dominio;

    private Integer quantidade;

    private Long tamanho;

    private int qteLinks;

    /**
	 * Obtêm qteLinks, ou seja, a quantidade de links que retorna o Content-length, 
	 */
    public int getQteLinks() {
        return qteLinks;
    }

    /**
	 * Define qteLinks, ou seja, a quantidade de links que retorna o Content-length.
	 */
    public void setQteLinks(int QteLinks) {
        qteLinks = QteLinks;
    }

    /**
	 * Obtêm o tamanho do domínio 
	 */
    public Long getTamanho() {
        return tamanho;
    }

    /**
	 * Define o tamanho do domínio
	 */
    public void setTamanho(Long Tamanho) {
        this.tamanho = Tamanho;
    }

    /**
	 * Obtêm o ID que identifica esta contagem de Domínios de links 
	 */
    public Long getID() {
        return ID;
    }

    /**
	 * Define o ID que identifica esta contagem de Dominios de links 
	 */
    public void setID(Long iD) {
        ID = iD;
    }

    /**
	 * Obtêm o Id do Sítio, tal qual está presente na tabela Sítio, cuja quantidade de links
	 * está sendo armazenada.
	 * Junto ao subdomínio referente ao Link e à localidade do link, forma uma chave única.
	 */
    public Long getIDSitio() {
        return sitio_id;
    }

    /**
	 * Define o Id do Sítio, tal qual está presente na tabela Sítio, cuja quantidade de links
	 * está sendo armazenada.
	 * Junto ao subdomínio referente ao Link e à localidade do link, forma uma chave única.
	 */
    public void setIDSitio(Long idHost) {
        this.sitio_id = idHost;
    }

    /**
	 * Obtêm o TLD (Top Level Domain) que será contabilizado no registro. Ignora o ccTLD
	 * (Country Code Top Level Domain), caso presente, de forma que para hosts terminados com
	 * ".com", ".com.br" ou ".com.es" teremos "com" como dado aqui.
	 */
    public String getTLD() {
        return tld;
    }

    /**
	 * Define o TLD (Top Level Domain) que será contabilizado no registro. Ignora o ccTLD
	 * (Country Code Top Level Domain), caso presente, de forma que para hosts terminados com
	 * ".com", ".com.br" ou ".com.es" teremos "com" como dado aqui.
	 */
    public void setTLD(String tld) {
        this.tld = tld;
    }

    /**
	 * Retorna a quantidade de links associada ao host referenciado, o subdomínio referenciado pelo
	 * link, e se o link é local ou não (definição de local em getLocal).
	 */
    public Integer getQuantidade() {
        return quantidade;
    }

    /**
	 * Define a quantidade de links associada ao host referenciado, o subdomínio referenciado pelo
	 * link, e a se o link é local ou não (definição de local em getLocal).
	 */
    public void setQuantidade(Integer quantity) {
        this.quantidade = quantity;
    }

    /**
	 * Obtêm o ccTLD (Country Code Top Level Domain) a que pertence ao subdomínio.
	 * Caso o link naõ possua um ccTLD, ou seja, é internacional, escreve-se "int".
	 */
    public String getCctld() {
        return cctld;
    }

    /**
	 * Define o ccTLD (Country Code Top Level Domain) a que pertence ao subdomínio.
	 * Caso o link naõ possua um ccTLD, ou seja, é internacional, escreve-se "int".
	 */
    public void setCcTLD(String cctld) {
        this.cctld = cctld;
    }

    /**
	 * Obtêm o dominio, tal qual sua definição presente em Sitios.
	 * Junto ao ID do Sítio e da localidade do link, forma uma chave única para o registro.
	 * @see Sitios
	 */
    public String getDominio() {
        return dominio;
    }

    /**
	 * Define o dominio, tal qual sua definição presente em Sitios.
	 * Junto ao ID do Sítio e da localidade do link, forma uma chave única para o registro.
	 * @see Sitios
	 */
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    /**
	 * Define se os links contados no registro são links locais do site ou se são links 
	 * externos. Link local é definido como aquele cuja referência não inicia-se com "*://", como
	 * em "http://" ou "ftp://"
	 * Junto ao ID do Host e o subdomínio referente ao Link, forma uma chave única para o registro.
	 */
    public Boolean getLocal() {
        return local;
    }

    /**
	 * Define se os links contados no registro são links locais do site ou se são links 
	 * externos. Link local é definido como aquele cuja referência não inicia-se com "*://", como
	 * em "http://" ou "ftp://"
	 * Junto ao ID do Host e o subdomínio referente ao Link, forma uma chave única para o registro.
	 */
    public void setLocal(Boolean local) {
        this.local = local;
    }
}
