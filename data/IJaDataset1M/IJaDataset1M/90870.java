package com.proyecto.tropero.core.bd;

public class DetalleSiembraDTO {

    private String descripcionUbicacion;

    private Long cantEspacioSembrar;

    private Long cantEspacioLibre;

    private Long cantEspacioTotal;

    private Integer idUbicacion;

    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getDescripcionUbicacion() {
        return descripcionUbicacion;
    }

    public void setDescripcionUbicacion(String descripcionUbicacion) {
        this.descripcionUbicacion = descripcionUbicacion;
    }

    public Long getCantEspacioSembrar() {
        return cantEspacioSembrar;
    }

    public void setCantEspacioSembrar(Long cantEspacioSembrar) {
        this.cantEspacioSembrar = cantEspacioSembrar;
    }

    public Long getCantEspacioLibre() {
        return cantEspacioLibre;
    }

    public void setCantEspacioLibre(Long cantEspacioLibre) {
        this.cantEspacioLibre = cantEspacioLibre;
    }

    public Long getCantEspacioTotal() {
        return cantEspacioTotal;
    }

    public void setCantEspacioTotal(Long cantEspacioTotal) {
        this.cantEspacioTotal = cantEspacioTotal;
    }
}
