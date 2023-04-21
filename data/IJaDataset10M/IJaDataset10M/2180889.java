package subfamilia2.dao;

import java.util.*;
import subfamilia2.*;

/**
 *
 * @author Sergio
 */
public interface Subfamilia2DAO {

    public int insertar(Subfamilia2VO subfamilia2);

    public int eliminar(int id);

    public int actualizar(Subfamilia2VO subfamilia2);

    public ArrayList<Subfamilia2VO> listadoSubfamilias2(Subfamilia2Parametros parametros);
}
