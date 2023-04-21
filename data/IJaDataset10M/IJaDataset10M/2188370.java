package edu.ufasta.webproxy;

import java.util.Date;
import edu.ufasta.webproxy.persistencia.ObjectID;

public class Equipo {

    private IP ip;

    private ObjectID oid;

    private String descripcion;

    private String nombre;

    private Date ultimaConexion;

    public Equipo() {
        ip = new IP();
    }

    public String getNumeroIP() {
        return ip.getNumeroIP();
    }

    public IP getIp() {
        return ip;
    }

    public void setNumeroIP(String ip) {
        this.ip.setNumeroIP(ip);
    }

    public String getValor() {
        return ip.getNumeroIP();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(Date ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public Integer getId() {
        return oid.getId();
    }

    public ObjectID getOid() {
        return oid;
    }

    public void setOid(ObjectID oid) {
        this.oid = oid;
    }

    public void setId(int valor) {
        this.oid.setId(valor);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
