package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the tbprodutos_relacionados_copy database table.
 * 
 */
@Entity
@Table(name = "tbprodutos_relacionados_copy")
public class TbprodutosRelacionadosCopy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    private int idproduto;

    @Column(name = "idproduto_rel")
    private int idprodutoRel;

    public TbprodutosRelacionadosCopy() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public int getIdproduto() {
        return this.idproduto;
    }

    public void setIdproduto(int idproduto) {
        this.idproduto = idproduto;
    }

    public int getIdprodutoRel() {
        return this.idprodutoRel;
    }

    public void setIdprodutoRel(int idprodutoRel) {
        this.idprodutoRel = idprodutoRel;
    }
}
