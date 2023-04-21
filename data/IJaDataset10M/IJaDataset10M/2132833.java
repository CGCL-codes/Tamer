package queriesBBDD;

import ddbb.GestorDB;
import grupos.Foro;

/**
 * @author Fernando
 *
 */
public class QueryCrearForo implements Query {

    private Foro foro;

    public QueryCrearForo(Foro foroCreado) {
        foro = foroCreado;
    }

    @Override
    public void ejecutaQuery() {
        GestorDB db = new GestorDB();
        db.crearForo(foro);
        db.agregaParticipante(foro.getIdGrupo(), foro.getCreador().getID(), foro.getInstanteCreacion());
    }
}
