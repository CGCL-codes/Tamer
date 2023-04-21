package datos.coneccion;

import java.sql.SQLException;
import java.util.List;
import objetos.constantes.Turno;
import objetos.entidades.Alumno;
import objetos.entidades.Materia;
import objetos.entidades.Profesor;

/**
 *
 * @author luciano
 */
public interface ProfesorConsultas {

    public List<Profesor> getAll() throws SQLException, ClassNotFoundException;

    public Profesor getProfesorPorId(Long id) throws SQLException, ClassNotFoundException;

    public Profesor getProfesorPorLegajo(String legajo) throws SQLException, ClassNotFoundException;

    public List<Profesor> getProfesoresPorMateria(Materia materia) throws SQLException, ClassNotFoundException;

    public List<Profesor> getProfesoresPorMateriaYAnio(Materia materia, Integer year) throws SQLException, ClassNotFoundException;

    public List<Profesor> getProfesoresPorAlumno(Alumno alumno) throws SQLException, ClassNotFoundException;

    public List<Profesor> getProfesoresPorAlumnoYAnio(Alumno alumno, Integer year) throws SQLException, ClassNotFoundException;

    public boolean editar(Profesor profesor) throws SQLException, ClassNotFoundException;

    public boolean crear(Profesor profesor) throws SQLException, ClassNotFoundException;
}
