package net.sourceforge.pmakbsc.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table
public class Organizacao extends EntidadePersistencia {

    private String nome;

    private String missao;

    private String visao;

    @OneToMany(mappedBy = "organizacao")
    private List<MapaEstrategico> mapasEstrategicos;

    @OneToMany(mappedBy = "organizacao")
    private List<Departamento> departamentos;

    @ManyToOne(optional = false)
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public List<MapaEstrategico> getMapasEstrategicos() {
        return mapasEstrategicos;
    }

    public void setMapasEstrategicos(List<MapaEstrategico> mapasEstrategicos) {
        this.mapasEstrategicos = mapasEstrategicos;
    }

    public String getMissao() {
        return missao;
    }

    public void setMissao(String missao) {
        this.missao = missao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVisao() {
        return visao;
    }

    public void setVisao(String visao) {
        this.visao = visao;
    }
}
