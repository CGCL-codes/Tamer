package modelo;

import java.util.LinkedList;
import java.util.Iterator;
import javax.swing.*;

/**
 * @author Usuario
 *
 */
public class EditorDePista {

    private static final DatoApariencia CIELO_DIA_SEMI_NUBLADO = new DatoApariencia("src//vista//imagenTramo//cieloSemiNublado", "Dia Semi Nublado");

    private static final DatoApariencia CIELO_NOCTURNO = new DatoApariencia("src//vista//imagenTramo//CieloNocturno", "Noche Estrellada");

    private static final DatoApariencia CAMINO_DE_ASFALTO = new DatoApariencia("src//vista//imagenTramo//carretera2", "Camino De Asfalto");

    private static final DatoApariencia CAMINO_TIERRA_NEGRA = new DatoApariencia("src//vista//imagenTramo//TierraNegra", "Camino Tierra Negra");

    private static final DatoApariencia CAMINO_TIERRA_COLORADA = new DatoApariencia("src//vista//imagenTramo//Tierra-Clara-1", "Camino Tierra Colorada");

    private static final DatoApariencia ENTORNO_PASTISAL = new DatoApariencia("src//vista//imagenTramo//pasto", "Pastisal");

    private static final DatoApariencia ENTORNO_TIERRA_NEGRA = new DatoApariencia("src//vista//imagenTramo//TierraNegra", "Tierra Negra");

    private static final DatoApariencia ENTORNO_TIERRA_COLORADA = new DatoApariencia("src//vista//imagenTramo//Tierra-Clara-1", "Tierra Colorada");

    private static final DatoApariencia ENTORNO_RIO = new DatoApariencia("src//vista//imagenTramo//rio", "Rio");

    private LinkedList<DatoApariencia> listaAparienciaCielo = null;

    private LinkedList<DatoApariencia> listaAparienciaCamino = null;

    private LinkedList<DatoApariencia> listaAparienciaEntorno = null;

    public EditorDePista() {
        this.listaAparienciaCielo = new LinkedList<DatoApariencia>();
        this.listaAparienciaCamino = new LinkedList<DatoApariencia>();
        this.listaAparienciaEntorno = new LinkedList<DatoApariencia>();
        this.listaAparienciaCielo.add(this.getCIELO_DIA_SEMI_NUBLADO());
        this.listaAparienciaCielo.add(this.getCIELO_NOCTURNO());
        this.listaAparienciaCamino.add(this.getCAMINO_DE_ASFALTO());
        this.listaAparienciaCamino.add(this.getCAMINO_TIERRA_COLORADA());
        this.listaAparienciaCamino.add(this.getCAMINO_TIERRA_NEGRA());
        this.listaAparienciaEntorno.add(this.getENTORNO_PASTISAL());
        this.listaAparienciaEntorno.add(this.getENTORNO_RIO());
        this.listaAparienciaEntorno.add(this.getENTORNO_TIERRA_COLORADA());
        this.listaAparienciaEntorno.add(this.ENTORNO_TIERRA_NEGRA);
    }

    public Pista createPistaAutodromo(Auto auto1, Auto auto2) {
        return new Pista(auto1, auto2, 3600.0, this.getCIELO_DIA_SEMI_NUBLADO().getRuta(), this.getCAMINO_DE_ASFALTO().getRuta(), this.getENTORNO_PASTISAL().getRuta());
    }

    public Pista createPistaCaminoDesertico(Auto auto1, Auto auto2) {
        return new Pista(auto1, auto2, 4000.0, this.getCIELO_DIA_SEMI_NUBLADO().getRuta(), this.getCAMINO_TIERRA_COLORADA().getRuta(), this.getENTORNO_TIERRA_NEGRA().getRuta());
    }

    public Pista createPistaCaminoRural(Auto auto1, Auto auto2) {
        return new Pista(auto1, auto2, 5000.0, this.getCIELO_NOCTURNO().getRuta(), this.getCAMINO_TIERRA_COLORADA().getRuta(), this.getENTORNO_TIERRA_NEGRA().getRuta());
    }

    public Pista createPuenteRio(Auto auto1, Auto auto2) {
        return new Pista(auto1, auto2, 4535.0, this.getCIELO_NOCTURNO().getRuta(), this.getCAMINO_DE_ASFALTO().getRuta(), this.getENTORNO_RIO().getRuta());
    }

    public Pista createPistaTerraplen(Auto auto1, Auto auto2) {
        return new Pista(auto1, auto2, 4535.0, this.getCIELO_DIA_SEMI_NUBLADO().getRuta(), this.getCAMINO_TIERRA_COLORADA().getRuta(), this.getENTORNO_RIO().getRuta());
    }

    public void setearCielo(Pista pista, DatoApariencia dato) {
        pista.setRutaAparienciaCielo(dato.getRuta());
    }

    public void setearCamino(Pista pista, DatoApariencia dato) {
        pista.setRutaAparienciaCamino(dato.getRuta());
    }

    public void setearEntorno(Pista pista, DatoApariencia dato) {
        pista.setRutaAparienciaEntorno(dato.getRuta());
    }

    public Pista obtenerPistaDeLongitudDiferente(Pista pista, double longitud) {
        return (new Pista(pista.getAuto(0), pista.getAuto(1), longitud, pista.getRutaAparienciaCielo(), pista.getRutaAparienciaCamino(), pista.getRutaAparienciaEntorno()));
    }

    /**
	 * @return the listaAparienciaCielo
	 */
    public LinkedList<DatoApariencia> getListaAparienciaCielo() {
        return listaAparienciaCielo;
    }

    /**
	 * @param listaAparienciaCielo the listaAparienciaCielo to set
	 */
    public void setListaAparienciaCielo(LinkedList<DatoApariencia> listaAparienciaCielo) {
        this.listaAparienciaCielo = listaAparienciaCielo;
    }

    /**
	 * @return the cIELO_DIA_SEMI_NUBLADO
	 */
    private static DatoApariencia getCIELO_DIA_SEMI_NUBLADO() {
        return CIELO_DIA_SEMI_NUBLADO;
    }

    /**
	 * @return the cAMINO_DE_ASFALTO
	 */
    private static DatoApariencia getCAMINO_DE_ASFALTO() {
        return CAMINO_DE_ASFALTO;
    }

    /**
	 * @return the cAMINO_TIERRA_NEGRA
	 */
    private static DatoApariencia getCAMINO_TIERRA_NEGRA() {
        return CAMINO_TIERRA_NEGRA;
    }

    /**
	 * @return the cAMINO_TIERRA_COLORADA
	 */
    private static DatoApariencia getCAMINO_TIERRA_COLORADA() {
        return CAMINO_TIERRA_COLORADA;
    }

    /**
	 * @return the eNTORNO_PASTISAL
	 */
    private static DatoApariencia getENTORNO_PASTISAL() {
        return ENTORNO_PASTISAL;
    }

    /**
	 * @return the eNTORNO_TIERRA_NEGRA
	 */
    private static DatoApariencia getENTORNO_TIERRA_NEGRA() {
        return ENTORNO_TIERRA_NEGRA;
    }

    /**
	 * @return the eNTORNO_TIERRA_COLORADA
	 */
    private static DatoApariencia getENTORNO_TIERRA_COLORADA() {
        return ENTORNO_TIERRA_COLORADA;
    }

    /**
	 * @return the eNTORNO_RIO
	 */
    private static DatoApariencia getENTORNO_RIO() {
        return ENTORNO_RIO;
    }

    /**
	 * @return the listaAparienciaCamino
	 */
    public LinkedList<DatoApariencia> getListaAparienciaCamino() {
        return listaAparienciaCamino;
    }

    /**
	 * @param listaAparienciaCamino the listaAparienciaCamino to set
	 */
    public void setListaAparienciaCamino(LinkedList<DatoApariencia> listaAparienciaCamino) {
        this.listaAparienciaCamino = listaAparienciaCamino;
    }

    /**
	 * @return the listaAparienciaEntorno
	 */
    public LinkedList<DatoApariencia> getListaAparienciaEntorno() {
        return listaAparienciaEntorno;
    }

    /**
	 * @param listaAparienciaEntorno the listaAparienciaEntorno to set
	 */
    public void setListaAparienciaEntorno(LinkedList<DatoApariencia> listaAparienciaEntorno) {
        this.listaAparienciaEntorno = listaAparienciaEntorno;
    }

    /**
	 * @return the cIELO_NOCTURNO
	 */
    public static DatoApariencia getCIELO_NOCTURNO() {
        return CIELO_NOCTURNO;
    }
}
