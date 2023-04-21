package es.unizar.cps.tecnodiscap.data;

/**
 * @author ciru
 * 
 */
public class Usuario {

    private String nombre = "Defecto";

    private float umbralDerecha = 151;

    private float umbralIzquierda = 96;

    private float histeresisDerecha = 124;

    private float histeresisIzquierda = 114;

    private int izquierda = 1;

    private int derecha = 1;

    private int centro = 0;

    /**
	 * @param nombre
	 * @param umbralDerecha
	 * @param umbralIzquierda
	 * @param histeresisIzquierda
	 * @param histeresisDerecha
	 * @param derecha
	 * @param centro
	 * @param izquierda
	 */
    public Usuario(String nombre, float umbralDerecha, float umbralIzquierda, float histeresisIzquierda, float histeresisDerecha, int derecha, int centro, int izquierda) {
        this.nombre = nombre;
        this.umbralDerecha = umbralDerecha;
        this.umbralIzquierda = umbralIzquierda;
        this.histeresisDerecha = histeresisDerecha;
        this.histeresisIzquierda = histeresisIzquierda;
        this.derecha = derecha;
        this.centro = centro;
        this.izquierda = izquierda;
    }

    public Usuario() {
    }

    /**
	 * @return the histeresisDerecha
	 */
    public float getHisteresisDerecha() {
        return histeresisDerecha;
    }

    /**
	 * @param histeresisDerecha
	 *            the histeresisDerecha to set
	 */
    public void setHisteresisDerecha(float histeresisDerecha) {
        this.histeresisDerecha = histeresisDerecha;
    }

    /**
	 * @return the histeresisIzquierda
	 */
    public float getHisteresisIzquierda() {
        return histeresisIzquierda;
    }

    /**
	 * @param histeresisIzquierda
	 *            the histeresisIzquierda to set
	 */
    public void setHisteresisIzquierda(float histeresisIzquierda) {
        this.histeresisIzquierda = histeresisIzquierda;
    }

    /**
	 * @return the nombre
	 */
    public String getNombre() {
        return nombre;
    }

    /**
	 * @param nombre
	 *            the nombre to set
	 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
	 * @return the umbralDerecha
	 */
    public float getUmbralDerecha() {
        return umbralDerecha;
    }

    /**
	 * @param umbralDerecha
	 *            the umbralDerecha to set
	 */
    public void setUmbralDerecha(float umbralDerecha) {
        this.umbralDerecha = umbralDerecha;
    }

    /**
	 * @return the umbralIzquierda
	 */
    public float getUmbralIzquierda() {
        return umbralIzquierda;
    }

    /**
	 * @param umbralIzquierda
	 *            the umbralIzquierda to set
	 */
    public void setUmbralIzquierda(float umbralIzquierda) {
        this.umbralIzquierda = umbralIzquierda;
    }

    public int getCentro() {
        return centro;
    }

    public void setCentro(int centro) {
        this.centro = centro;
    }

    public int getDerecha() {
        return derecha;
    }

    public void setDerecha(int derecha) {
        this.derecha = derecha;
    }

    public int getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(int izquierda) {
        this.izquierda = izquierda;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public void setValues(Usuario userBackup) {
        this.nombre = userBackup.getNombre();
        this.umbralDerecha = userBackup.getUmbralDerecha();
        this.umbralIzquierda = userBackup.getUmbralIzquierda();
        this.histeresisDerecha = userBackup.getHisteresisDerecha();
        this.histeresisIzquierda = userBackup.getHisteresisIzquierda();
        this.derecha = userBackup.getDerecha();
        this.centro = userBackup.getCentro();
        this.izquierda = userBackup.getIzquierda();
    }
}
