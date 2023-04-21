package modelo;

import dominio.Movimento;

public class MovimentoDAO extends Persistencia<Movimento> {

    private static MovimentoDAO dao = null;

    public static MovimentoDAO getInstance() {
        if (dao == null) {
            dao = new MovimentoDAO();
        }
        return dao;
    }

    public MovimentoDAO() {
        super(Movimento.class, "SCC");
    }
}
