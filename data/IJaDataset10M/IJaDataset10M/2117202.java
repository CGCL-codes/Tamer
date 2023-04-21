package com.geproman.model;

import com.geproman.model.Calendario;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "data")
public class Data {

    @Id
    @Column(name = "cod_data")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod_data;

    @Temporal(TemporalType.DATE)
    @Column(name = "data")
    private Date data;

    @Column(name = "descr")
    private String descr;

    @ManyToOne
    @JoinColumn(name = "cod_cal", nullable = false)
    private Calendario calendario;

    public Integer getCod_data() {
        return cod_data;
    }

    public void setCod_data(Integer cod_data) {
        this.cod_data = cod_data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }
}
