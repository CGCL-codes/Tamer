package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the tbquevedo_classe database table.
 * 
 */
@Entity
@Table(name = "tbquevedo_classe")
public class TbquevedoClasse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "contab_historico")
    private int contabHistorico;

    @Column(length = 100)
    private String des;

    private int idclasse;

    @Column(columnDefinition = "enum('Y','N')")
    private String tipo;

    public TbquevedoClasse() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContabHistorico() {
        return this.contabHistorico;
    }

    public void setContabHistorico(int contabHistorico) {
        this.contabHistorico = contabHistorico;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getIdclasse() {
        return this.idclasse;
    }

    public void setIdclasse(int idclasse) {
        this.idclasse = idclasse;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
