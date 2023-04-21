package com.proyecto.tropero.core.domain;

import java.math.BigDecimal;
import java.util.Collection;
import com.proyecto.tropero.core.domain.arquitectura.ObjetoPersistente;

public class Alimento extends ObjetoPersistente {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String descripcion;

    private TipoAlimento tipo;

    private BigDecimal materiaSeca;

    private BigDecimal calcio;

    private BigDecimal fosforo;

    private BigDecimal proteinaBruta;

    private BigDecimal digestibilidadaMateriaSeca;

    private BigDecimal fibra;

    private BigDecimal almidon;

    private BigDecimal carboHidratos;

    private BigDecimal carboHidratosSolubles;

    private BigDecimal proteinaBrutaSoluble;

    private BigDecimal grasa;

    private String descripcionCaracteristicas;

    private String estadoCultivo;

    private Long espacioMinimoSiembra;

    private Long cantMinReposicion;

    private UnidadMedida unidadDeMedidaSiembra;

    private Collection<Siembra> siembras;

    private Long stock;

    private Proveedor proveedor;

    private BigDecimal monto;

    private UnidadMedida unidadDeMedida;

    private Long minimoGranoSiembra;

    private Long espacioMinAlmacen;

    private Almacen almacen;

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Integer getId() {
        return (Integer) super.getId();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoAlimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlimento tipo) {
        this.tipo = tipo;
    }

    public Long getEspacioMinimoSiembra() {
        return espacioMinimoSiembra;
    }

    public void setEspacioMinimoSiembra(Long espacioMinimoSiembra) {
        this.espacioMinimoSiembra = espacioMinimoSiembra;
    }

    public String getDescripcionCaracteristicas() {
        return descripcionCaracteristicas;
    }

    public void setDescripcionCaracteristicas(String descripcionCaracteristicas) {
        this.descripcionCaracteristicas = descripcionCaracteristicas;
    }

    public String getEstadoCultivo() {
        return estadoCultivo;
    }

    public void setEstadoCultivo(String estadoCultivo) {
        this.estadoCultivo = estadoCultivo;
    }

    public Long getCantMinReposicion() {
        return cantMinReposicion;
    }

    public void setCantMinReposicion(Long cantMinReposicion) {
        this.cantMinReposicion = cantMinReposicion;
    }

    public UnidadMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public Collection<Siembra> getSiembras() {
        return siembras;
    }

    public void setSiembras(Collection<Siembra> siembras) {
        this.siembras = siembras;
    }

    public void setUnidadDeMedida(UnidadMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getEspacioMinAlmacen() {
        return espacioMinAlmacen;
    }

    public void setEspacioMinAlmacen(Long espacioMinAlmacen) {
        this.espacioMinAlmacen = espacioMinAlmacen;
    }

    public BigDecimal getMateriaSeca() {
        return materiaSeca;
    }

    public void setMateriaSeca(BigDecimal materiaSeca) {
        this.materiaSeca = materiaSeca;
    }

    public BigDecimal getCalcio() {
        return calcio;
    }

    public void setCalcio(BigDecimal calcio) {
        this.calcio = calcio;
    }

    public BigDecimal getFosforo() {
        return fosforo;
    }

    public void setFosforo(BigDecimal fosforo) {
        this.fosforo = fosforo;
    }

    public BigDecimal getProteinaBruta() {
        return proteinaBruta;
    }

    public void setProteinaBruta(BigDecimal proteinaBruta) {
        this.proteinaBruta = proteinaBruta;
    }

    public BigDecimal getDigestibilidadaMateriaSeca() {
        return digestibilidadaMateriaSeca;
    }

    public void setDigestibilidadaMateriaSeca(BigDecimal digestibilidadaMateriaSeca) {
        this.digestibilidadaMateriaSeca = digestibilidadaMateriaSeca;
    }

    public BigDecimal getFibra() {
        return fibra;
    }

    public void setFibra(BigDecimal fibra) {
        this.fibra = fibra;
    }

    public BigDecimal getAlmidon() {
        return almidon;
    }

    public void setAlmidon(BigDecimal almidon) {
        this.almidon = almidon;
    }

    public BigDecimal getCarboHidratos() {
        return carboHidratos;
    }

    public void setCarboHidratos(BigDecimal carboHidratos) {
        this.carboHidratos = carboHidratos;
    }

    public BigDecimal getCarboHidratosSolubles() {
        return carboHidratosSolubles;
    }

    public void setCarboHidratosSolubles(BigDecimal carboHidratosSolubles) {
        this.carboHidratosSolubles = carboHidratosSolubles;
    }

    public BigDecimal getProteinaBrutaSoluble() {
        return proteinaBrutaSoluble;
    }

    public void setProteinaBrutaSoluble(BigDecimal proteinaBrutaSoluble) {
        this.proteinaBrutaSoluble = proteinaBrutaSoluble;
    }

    public BigDecimal getGrasa() {
        return grasa;
    }

    public void setGrasa(BigDecimal grasa) {
        this.grasa = grasa;
    }

    public UnidadMedida getUnidadDeMedidaSiembra() {
        return unidadDeMedidaSiembra;
    }

    public void setUnidadDeMedidaSiembra(UnidadMedida unidadDeMedidaSiembra) {
        this.unidadDeMedidaSiembra = unidadDeMedidaSiembra;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getMinimoGranoSiembra() {
        return minimoGranoSiembra;
    }

    public void setMinimoGranoSiembra(Long minimoGranoSiembra) {
        this.minimoGranoSiembra = minimoGranoSiembra;
    }
}
