package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the tbos_tipo database table.
 * 
 */
@Entity
@Table(name = "tbos_tipo")
public class TbosTipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    @Column(nullable = false, length = 80)
    private String des;

    @Column(length = 100)
    private String flag;

    private int idgenero;

    @Column(columnDefinition = "enum('Y','N')")
    private String padrao;

    @Lob()
    @Column(name = "ult_atu", nullable = false)
    private String ultAtu;

    public TbosTipo() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getIdgenero() {
        return this.idgenero;
    }

    public void setIdgenero(int idgenero) {
        this.idgenero = idgenero;
    }

    public String getPadrao() {
        return this.padrao;
    }

    public void setPadrao(String padrao) {
        this.padrao = padrao;
    }

    public String getUltAtu() {
        return this.ultAtu;
    }

    public void setUltAtu(String ultAtu) {
        this.ultAtu = ultAtu;
    }
}
