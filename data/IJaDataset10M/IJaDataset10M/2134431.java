package pe.edu.upc.dsd.clienteweb.model;

import java.util.List;

public class Embarque {

    private String codigo;

    private String embarcador;

    private String consignatario;

    private String tipoCarga;

    private List<Producto> productos;

    private List<Puerto> puertos;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmbarcador() {
        return embarcador;
    }

    public void setEmbarcador(String embarcador) {
        this.embarcador = embarcador;
    }

    public String getConsignatario() {
        return consignatario;
    }

    public void setConsignatario(String consignatario) {
        this.consignatario = consignatario;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga(String tipoCarga) {
        this.tipoCarga = tipoCarga;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Puerto> getPuertos() {
        return puertos;
    }

    public void setPuertos(List<Puerto> puertos) {
        this.puertos = puertos;
    }
}
