package ebadat.dao;

import domain.Evaluacion;
import java.util.List;

/**
 *
 * @author Sonia
 */
public interface EvaluacionesDAO {

    int insert(Evaluacion e);

    int update(Evaluacion e);

    int delete(Evaluacion e);

    int delete_detalle(int codevaluacion);

    void close();

    List getAll();

    List getByCodigo(int string);

    int identity();

    int insertDetalle(Evaluacion e);

    int codEvaluacion(int seccion, int profesor, int area);

    List getEstudianteXLogro(int seccion, int profesor, int area);
}
