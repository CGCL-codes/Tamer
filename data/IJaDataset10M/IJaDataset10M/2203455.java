package br.com.pleno.gp.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Classe de entidade AreaOrganizacional
 * 
 * @author Lourival
 */
@Entity
@Table(name = "AREA_ORGANIZACIONAL")
public class AreaOrganizacional implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SUPERIOR")
    private Integer superior;

    @Column(name = "SIGLA")
    private String sigla;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    /** Creates a new instance of AreaOrganizacional */
    public AreaOrganizacional() {
    }

    /**
     * Cria uma nova inst�ncia de AreaOrganizacional com os valores especificados.
     * @param id o id do AreaOrganizacional
     */
    public AreaOrganizacional(Integer id) {
        this.id = id;
    }

    /**
     * Cria uma nova inst�ncia de AreaOrganizacional com os valores especificados.
     * @param id o id do AreaOrganizacional
     * @param titulo o titulo do AreaOrganizacional
     */
    public AreaOrganizacional(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    /**
     * Define o id deste AreaOrganizacional.
     * @return o id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Define o id deste AreaOrganizacional para o valor especificado.
     * @param id o novo id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Define o superior deste AreaOrganizacional.
     * @return o superior
     */
    public Integer getSuperior() {
        return this.superior;
    }

    /**
     * Define o superior deste AreaOrganizacional para o valor especificado.
     * @param superior o novo superior
     */
    public void setSuperior(Integer superior) {
        this.superior = superior;
    }

    /**
     * Define o sigla deste AreaOrganizacional.
     * @return o sigla
     */
    public String getSigla() {
        return this.sigla;
    }

    /**
     * Define o sigla deste AreaOrganizacional para o valor especificado.
     * @param sigla o novo sigla
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * Define o titulo deste AreaOrganizacional.
     * @return o titulo
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * Define o titulo deste AreaOrganizacional para o valor especificado.
     * @param titulo o novo titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Retorna um valor de c�digo hash para o objeto.  Esta implementa��o computa
     * um valor de c�digo hash baseado nos campos id deste objeto.
     * @return um valor de c�digo hash para este objeto.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Determina se outro objeto � igual a este AreaOrganizacional.  O resultado �
     * <code>true</code> se e somente se o argumento n�o for nulo e for um objeto AreaOrganizacional o qual
     * tem o mesmo valor para o campo id como este objeto.
     * @param object o objeto de refer�ncia com o qual comparar
     * @return <code>true</code> se este objeto � o mesmo como o argumento;
     * <code>false</code> caso contr�rio.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AreaOrganizacional)) {
            return false;
        }
        AreaOrganizacional other = (AreaOrganizacional) object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) return false;
        return true;
    }

    /**
     * Retorna uma representa��o literal deste objeto.  Esta implementa��o cria
     * uma representa��o baseada nos campos id.
     * @return uma representa��o literal deste objeto.
     */
    @Override
    public String toString() {
        return "br.com.pleno.gp.domain.AreaOrganizacional[id=" + id + "]";
    }
}
