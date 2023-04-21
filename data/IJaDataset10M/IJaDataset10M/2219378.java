package edu.ar.modulo2.valentinis.modelo;

public class Empleado extends Persona {

    private static final long serialVersionUID = -761082931733981333L;

    private String legajo;

    private String nombre;

    private String apellido;

    private Categoria categoria;

    public Empleado() {
    }

    @Override
    public String getId() {
        return this.getLegajo();
    }

    @Override
    public String getDenominacion() {
        return this.getApellido() + ", " + this.getNombre();
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
