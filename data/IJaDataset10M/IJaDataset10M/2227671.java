package krowdix.modelo.objetos;

import java.awt.Color;
import krowdix.modelo.objetos.agentes.Nodo;

/**
 * @author Daniel Alonso Fernández
 */
public class Relacion extends ObjetoRed {

    public static final String tipo = "RELACION";

    private int afinidad;

    private Entidad destino;

    private boolean naN;

    private Nodo origen;

    public Relacion(int id, Nodo origen, Entidad destino, int afinidad) {
        super(id);
        this.origen = origen;
        this.destino = destino;
        this.afinidad = afinidad;
        naN = destino.dameTipo().equals(Nodo.tipo);
    }

    public int dameAfinidad() {
        return afinidad;
    }

    public Color dameColor() {
        float porcentaje = afinidad / 100.0f;
        return new Color(1.0f - porcentaje, porcentaje, 0);
    }

    public Entidad dameDestino() {
        return destino;
    }

    public boolean dameNaN() {
        return naN;
    }

    public Nodo dameOrigen() {
        return origen;
    }

    @Override
    public String dameTipo() {
        return Relacion.tipo;
    }

    public void ponAfinidad(int nuevaAfinidad) {
        afinidad = nuevaAfinidad;
    }

    public void ponDestino(Entidad objeto) {
        destino = objeto;
        naN = destino.dameTipo().equals(Nodo.tipo);
    }

    public void ponOrigen(Nodo nodo) {
        origen = nodo;
    }
}
