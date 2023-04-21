package tarea.dao;

import java.util.*;
import tarea.*;

/**
 *
 * @author Sergio
 */
public interface TareaDAO {

    public int insertar(TareaVO tarea);

    public int eliminar(int id);

    public int actualizar(TareaVO tarea);

    public ArrayList<TareaVO> listadoTareas(TareaParametros parametros);
}
