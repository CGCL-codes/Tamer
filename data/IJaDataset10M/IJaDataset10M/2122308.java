package br.gov.serpro.bolaum.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import br.gov.frameworkdemoiselle.annotation.ComboBox;
import br.gov.frameworkdemoiselle.annotation.DateField;
import br.gov.frameworkdemoiselle.annotation.Field;

@Entity
public class Partida {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Field(label = "{partida.label.visitante}", prompt = "{partida.prompt.visitante}")
    @ComboBox(fieldLabel = "nome")
    private Clube visitante;

    @ManyToOne
    @Field(label = "{partida.label.mandante}", prompt = "{partida.prompt.mandante}")
    @ComboBox(fieldLabel = "nome")
    private Clube mandante;

    @Column
    @Field(label = "{partida.label.data}", prompt = "{partida.prompt.data}")
    @DateField
    private Date data;

    @Column
    @Field(label = "{partida.label.local}", prompt = "{partida.prompt.local}")
    private String local;

    @ManyToOne
    private Rodada rodada;

    @Column
    private Integer golsMandante;

    @Column
    private Integer golsVisitante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getVisitante() {
        return visitante;
    }

    public void setVisitante(Clube visitante) {
        this.visitante = visitante;
    }

    public Clube getMandante() {
        return mandante;
    }

    public void setMandante(Clube mandante) {
        this.mandante = mandante;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setRodada(Rodada rodada) {
        this.rodada = rodada;
    }

    public Rodada getRodada() {
        return rodada;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }
}
