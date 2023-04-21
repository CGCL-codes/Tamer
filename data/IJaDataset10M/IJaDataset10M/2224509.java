package br.home.entitys;

import java.sql.Date;
import java.sql.Time;
import java.util.Currency;
import java.util.List;

/**
 * @author : Efraim Gentil - Fortaleza/CE
 * @project: app-barraca
 * @packege: br.home.entitys
 * @file   : Mesa.java
 * @date   : 18/10/2011 at 23:04:05
 */
public class Mesa {

    private Integer idmesa;

    private int numeromesa;

    private Date dataatendimento;

    private Time horainicio;

    private Time horafim;

    private double valorpago;

    private double valor10porcento;

    private boolean finalizada;

    private int idgarcom;

    private List<Produtomesa> produtos;

    private Garcom garcom;

    public Integer getIdmesa() {
        return idmesa;
    }

    public void setIdmesa(Integer idmesa) {
        this.idmesa = idmesa;
    }

    public int getNumeromesa() {
        return numeromesa;
    }

    public void setNumeromesa(int numeromesa) {
        this.numeromesa = numeromesa;
    }

    public Date getDataatendimento() {
        return dataatendimento;
    }

    public void setDataatendimento(Date dataatendimento) {
        this.dataatendimento = dataatendimento;
    }

    public Time getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(Time horainicio) {
        this.horainicio = horainicio;
    }

    public Time getHorafim() {
        return horafim;
    }

    public void setHorafim(Time horafim) {
        this.horafim = horafim;
    }

    public double getValorpago() {
        return valorpago;
    }

    public void setValorpago(double valorpago) {
        this.valorpago = valorpago;
    }

    public double getValor10porcento() {
        return valor10porcento;
    }

    public void setValor10porcento(double valor10porcento) {
        this.valor10porcento = valor10porcento;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public int getIdgarcom() {
        return idgarcom;
    }

    public void setIdgarcom(int idgarcom) {
        this.idgarcom = idgarcom;
    }

    public List<Produtomesa> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produtomesa> produtos) {
        this.produtos = produtos;
    }

    public void setGarcom(Garcom garcom) {
        this.garcom = garcom;
    }

    public Garcom getGarcom() {
        return garcom;
    }
}
