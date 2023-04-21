package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the tbauditor database table.
 * 
 */
@Entity
@Table(name = "tbauditor")
public class Tbauditor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date data;

    @Column(length = 200)
    private String descricao;

    @Column(nullable = false)
    private Time hora;

    private int idusuario;

    @Column(nullable = false)
    private int operacao;

    private int tag;

    public Tbauditor() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Time getHora() {
        return this.hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public int getIdusuario() {
        return this.idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getOperacao() {
        return this.operacao;
    }

    public void setOperacao(int operacao) {
        this.operacao = operacao;
    }

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
