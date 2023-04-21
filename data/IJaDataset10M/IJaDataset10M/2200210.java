package provincia.dao;

import java.util.*;
import provincia.*;

/**
 *
 * @author Sergio
 */
public interface ProvinciaDAO {

    public int insertar(ProvinciaVO provincia);

    public int eliminar(int id);

    public int actualizar(ProvinciaVO provincia);

    public ArrayList<ProvinciaVO> listadoProvincias(ProvinciaParametros parametros);
}
