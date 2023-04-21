package net.sf.opennemesis.motor.estructuras.utilerias;

import net.sf.opennemesis.motor.estructuras.graficos.Sprite;

/**
 *
 * @author fictog
 */
public class Tesoro extends ObjetoInventario {

    private final int precio = -1;

    private int valor;

    private final float peso = 0.1F;

    public Tesoro(String nombre, Sprite sprite, int posX, int posY, int valor) throws IllegalArgumentException, NullPointerException {
        if (nombre == null || sprite == null) throw new NullPointerException("Tesoro.java");
        if (nombre.equals("") || posX < 0 || posY < 0 || valor <= 0) throw new IllegalArgumentException("Tesoro.java");
        this.nombre = nombre;
        this.sprite = sprite;
        this.posX = posX;
        this.posY = posY;
        this.valor = valor;
    }

    public final int getValor() {
        return valor;
    }

    protected void setDescripcion() {
        this.descripcion += "\nEste " + this.nombre + " tiene un valor de aproximadamente unas " + this.valor + " monedas.";
        this.descripcion += "\nY pesa menos de medio kilo.";
    }
}
