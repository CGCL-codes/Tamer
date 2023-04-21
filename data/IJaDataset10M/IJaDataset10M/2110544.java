package com.hotelmanager.vo;

import java.util.Date;
import java.util.List;

public class Reserva {

    private Integer codigo;

    private Date dtreserva;

    private Cliente cliente;

    private Date dtprevent;

    private Date dtprevsai;

    private String flag;

    private String nome;

    private List<Quarto> quartos;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDtreserva() {
        return dtreserva;
    }

    public void setDtreserva(Date dtreserva) {
        this.dtreserva = dtreserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getDtprevent() {
        return dtprevent;
    }

    public void setDtprevent(Date dtprevent) {
        this.dtprevent = dtprevent;
    }

    public Date getDtprevsai() {
        return dtprevsai;
    }

    public void setDtprevsai(Date dtprevsai) {
        this.dtprevsai = dtprevsai;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Quarto> getQuartos() {
        return quartos;
    }

    public void setQuartos(List<Quarto> quartos) {
        this.quartos = quartos;
    }
}
