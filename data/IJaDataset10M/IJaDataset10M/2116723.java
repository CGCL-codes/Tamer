package acciones;

import queriesBBDD.GestorQueries;
import ddbb.GestorDB;
import general.ExclusionMutua;
import grupos.Foro;
import usuarios.URS;

/**
 * Clase para la acci�n de pedir participaci�n en un Foro.
 * 
 * @author Alberto
 */
public class AccionPedirParticipacionForo extends AccionGenerica {

    /**
	 * Foro en el que se pide participaci�n.
	 */
    private Foro foro;

    /**
	 * Informaci�n gen�rica de la acci�n.
	 */
    public static TipoAccion accion;

    public static AccionPedirParticipacionForo proto = new AccionPedirParticipacionForo();

    private AccionPedirParticipacionForo() {
    }

    ;

    /**
	 * Constructora que recibe por par�metros el usuario que ejecuta la acci�n, los puntos y el instante de la misma.
	 * @param u URS del usuario que ejecuta la acci�n.
	 * @param p Puntos de coste de la acci�n.
	 * @param inst Instante de tiempo en el que se ejecuta la acci�n.
	 */
    public AccionPedirParticipacionForo(URS u, TipoAccion p) {
        super(u);
        nombre = p.getNombre();
    }

    @Override
    public void ejecutaAccion(int i) {
        instante = i;
        GestorDB db = new GestorDB();
        int numForos = db.getMaxNumForo();
        if (numForos <= 0) return;
        int idForo = ExclusionMutua.getRandomInt(numForos);
        Foro elegido = db.getForo(idForo);
        while (elegido == null || db.esParticipanteDe(usuario.getID(), elegido.getIdGrupo())) {
            if (idForo == numForos + 1) break;
            idForo++;
            elegido = db.getForo(idForo);
        }
        if (idForo == numForos + 1 && (elegido == null || elegido.esParticipante(usuario))) {
            foro = null;
            return;
        }
        foro = elegido;
        GestorQueries.ParticipacionForo(foro, usuario, instante);
        foro.agregaParticipante(usuario);
    }

    /**
	 * M�todo para obtener el foro al que se pidi� participaci�n.
	 * @return Foro al que se pidi� la participaci�n.
	 */
    public Foro getForo() {
        return foro;
    }

    @Override
    public String toString() {
        return "Acci�n pedir participaci�n en el foro " + (foro == null ? "" : foro.toString()) + " en el instante " + instante;
    }

    public void setAccion(TipoAccion t) {
        accion = t;
    }

    public TipoAccion getAccion() {
        return accion;
    }

    @Override
    public boolean esFactible() {
        GestorDB db = new GestorDB();
        return (db.getMaxNumForo() > 0);
    }
}
