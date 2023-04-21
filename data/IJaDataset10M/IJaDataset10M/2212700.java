package net.sf.jrpglib.estructuras.utileria.deInventario.hechizo;

import net.sf.jrpglib.estructuras.utileria.deInventario.disciplina.DisciplinaMagica;
import net.sf.jrpglib.estructuras.graficos.Sprite;

/**
 *
 * @author fictog
 */
public class HechizoBenigno extends Hechizo {

    public HechizoBenigno(String nombre, Sprite sprite, int posX, int posY, int costo, String disciplina, int duracion, int rango, String descripcion) throws IllegalArgumentException, NullPointerException {
        if (nombre == null || sprite == null || disciplina == null) throw new NullPointerException();
        if (nombre.equals("") || disciplina.equals("")) throw new java.lang.IllegalArgumentException("Cadena vacia.");
        if (rango < 0 || posX < 0 || posY < 0 || costo < 0 || duracion <= 0) throw new java.lang.IllegalArgumentException("Algun atributo en el hechizo maligno: " + nombre + " es negativo");
        this.nombre = nombre;
        this.sprite = sprite;
        this.posX = posX;
        this.posY = posY;
        this.costo = costo;
        this.disciplina = new DisciplinaMagica(disciplina);
        this.duracion = duracion;
        this.rango = rango;
        this.descripcion = descripcion;
        setDescripcion();
    }

    protected void setDescripcion() {
        this.descripcion += "\nEste hechizo pertenece a la disciplina de las " + this.disciplina.getNombre() + ".";
        if (rango > 0) this.descripcion += "\nTiene un rango de unos " + rango + " metros."; else this.descripcion += "\nSe aplica su efecto sobre quien lo invoca";
        this.descripcion += "\nY pesa menos de medio kilo.";
    }
}
