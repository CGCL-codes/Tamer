package utn.frc.dlc.web.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utn.frc.dlc.base.Fotos;
import utn.frc.dlc.db.SqlManager;

/**
 *
 * @author dlcusr
 */
public class DBFoto {

    public static List loadFotos(SqlManager sql, String query) throws Exception {
        if (sql == null) throw new Exception("DBUFoto.loadFotos ERROR: Sql inválido.");
        if (query == null) throw new Exception("DBFoto.loadFotos ERROR: Query inválida.");
        List fotos = null;
        Fotos fot = null;
        sql.prepare(query);
        ResultSet rs = sql.executeQuery();
        if (rs.first()) {
            fotos = new ArrayList();
            do {
                fot = new Fotos();
                int id = rs.getInt("idfoto");
                String path = rs.getString("pathfoto");
                String fe = rs.getString(5);
                int idAm2 = rs.getInt("idAmigo2");
                int idAm1 = rs.getInt("idAmigo1");
                fot.setIdfoto(id);
                fot.setFecha(fe);
                fot.setIdAmigo1(idAm1);
                fot.setIdAmigo2(idAm2);
                fot.setPathfoto(path);
                fotos.add(fot);
            } while (rs.next());
        }
        rs.close();
        return fotos;
    }

    public static List loadFotos(SqlManager sql) throws Exception {
        String query = "SELECT * FROM foto f  ";
        return loadFotos(sql, query);
    }
}
