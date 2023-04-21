package criaturas.utilidades;

import com.jme.math.Vector3f;
import criaturas.CriaturaFisica;

/**
 *
 * @author Administrador
 */
public class RayCollision {

    private CriaturaFisica cf;

    private String nombreParte;

    private Vector3f puntoLocal, puntoGlobal;

    public RayCollision(CriaturaFisica cf, String nombreParte, Vector3f puntoLocal, Vector3f puntoGlobal) {
        this.cf = cf;
        this.nombreParte = nombreParte;
        this.puntoLocal = puntoLocal;
        this.puntoGlobal = puntoGlobal;
    }

    public CriaturaFisica getCriaturaFisica() {
        return cf;
    }

    public String getNombreParte() {
        return nombreParte;
    }

    public Vector3f getPuntoGlobal() {
        return puntoGlobal;
    }

    public Vector3f getPuntoLocal() {
        return puntoLocal;
    }
}
