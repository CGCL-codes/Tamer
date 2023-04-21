package com.blogspot.dmottab.limagtug.lab2.ws.bo;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author David Motta B.
 * dmottab.blogspot.com || www.android-peru.com
 */
@Root(name = "androidPhone")
public class AndroidPhone {

    @Element
    private String nombre;

    @Element
    private String marca;

    @Element
    private String versionSO;

    public AndroidPhone() {
    }

    public AndroidPhone(String nombre, String marca, String versionSO) {
        this.nombre = nombre;
        this.marca = marca;
        this.versionSO = versionSO;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getVersionSO() {
        return versionSO;
    }

    public void setVersionSO(String versionSO) {
        this.versionSO = versionSO;
    }
}
