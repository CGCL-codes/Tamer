package queriesBBDD;

import usuarios.URS;
import grupos.Foro;
import ddbb.GestorDB;

/**
 * @author Fernando
 *
 */
public class QueryParticipacionForo implements Query {

    private Foro foro;

    private URS usuario;

    private int instante;

    public QueryParticipacionForo(Foro f, URS u, int i) {
        foro = f;
        usuario = u;
        instante = i;
    }

    @Override
    public void ejecutaQuery() {
        GestorDB db = new GestorDB();
        db.agregaParticipante(foro.getIdGrupo(), usuario.getID(), instante);
    }
}
