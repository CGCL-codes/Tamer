package resolucion_nombres;

public class Ambito {

    private String nombre;

    private String tipo;

    private Tabla_Simbolos declaraciones_contenidas;

    private Ambito contenedor;

    boolean debug = true;

    public Ambito(String nombre, String tipo, Tabla_Simbolos dec, Ambito contenedor) {
        this.nombre = nombre;
        this.tipo = tipo;
        if (dec != null) this.declaraciones_contenidas = dec; else {
            this.declaraciones_contenidas = new Tabla_Simbolos();
        }
        this.contenedor = contenedor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public Ambito getContenedor() {
        return contenedor;
    }

    public Simbolo getDeclaracion(String nombre) {
        return declaraciones_contenidas.getSimbolo(nombre);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setContenedor(Ambito contenedor) {
        this.contenedor = contenedor;
    }

    public void setDeclaracion(Simbolo s) {
        declaraciones_contenidas.setSimbolo(s);
    }

    public boolean equals(Ambito a) {
        return (a.getNombre() == nombre) && (a.getTipo() == tipo);
    }
}
