package org.ugc.cnel.manabi.bakenbeans;

import com.icesoft.faces.context.FileResource;
import java.io.File;

/**
 *
 * @author Admin
 */
public class Respaldos {

    public Respaldos(String nombre, String ruta, String size) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.size = size;
        this.fr = new FileResource(new File(ruta));
    }

    private String nombre;

    private String ruta;

    private String size;

    private FileResource fr;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public FileResource getFr() {
        return fr;
    }

    public void setFr(FileResource fr) {
        this.fr = fr;
    }
}
