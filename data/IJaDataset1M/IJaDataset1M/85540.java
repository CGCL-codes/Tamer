package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the tbclasse_itens database table.
 * 
 */
@Entity
@Table(name = "tbclasse_itens")
public class TbclasseIten implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    @Column(length = 50)
    private String flag;

    private int idcarac;

    private int idclasse;

    private int ordem;

    public TbclasseIten() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getIdcarac() {
        return this.idcarac;
    }

    public void setIdcarac(int idcarac) {
        this.idcarac = idcarac;
    }

    public int getIdclasse() {
        return this.idclasse;
    }

    public void setIdclasse(int idclasse) {
        this.idclasse = idclasse;
    }

    public int getOrdem() {
        return this.ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
}
