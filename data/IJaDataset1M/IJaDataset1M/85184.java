package ws;

import java.util.ArrayList;
import java.util.Collection;
import org.orm.PersistentException;
import org.orm.PersistentTransaction;
import SOAPVO.AlumnoSOAPVO;
import SOAPVO.AnotacionSOAPVO;
import com.google.gson.Gson;

public class AlumnoSOA {

    /**
	 * Busca un alumno por id y lo retorna como json.
	 * Si el id es nulo retorna 0, si no encuentra el alumno retorna 1
	 * y si ocurre una excepcion retorna 2
	 * 
	 * @param id
	 * @return
	 */
    public static String getById(int id) {
        String json = null;
        if (id == 0) {
            json = "0";
        } else {
            try {
                orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
                Collection<AlumnoSOAPVO> colectionAlumnoSOAPVO = new ArrayList<AlumnoSOAPVO>();
                orm.Tan_alumno ormAlumnos;
                ormAlumnos = lDAOFactory.getTan_alumnoDAO().loadTan_alumnoByQuery("al_id='" + id + "'", null);
                if (ormAlumnos == null) {
                    json = "1";
                } else {
                    AlumnoSOAPVO alumno = AlumnoSOAPVO.crearAlumnoSOAPVO(ormAlumnos);
                    colectionAlumnoSOAPVO.add(alumno);
                    Gson gson = new Gson();
                    json = gson.toJson(colectionAlumnoSOAPVO);
                }
            } catch (PersistentException e) {
                e.printStackTrace();
                json = "2";
            }
        }
        return json;
    }

    /**
	 * Busca un alumno por rut y la retorna como json.
	 * Si el rut es nulo retorna 0, si no encuentra el alumno retorna 1
	 * y si ocurre una excepcion retorna 2
	 * 
	 * @param rut
	 * @return
	 */
    public static String getByRut(String rut) {
        String json = null;
        if ((rut == null) || (rut.equals(""))) {
            json = "0";
        } else {
            try {
                orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
                Collection<AlumnoSOAPVO> colectionAlumnoSOAPVO = new ArrayList<AlumnoSOAPVO>();
                orm.Tan_alumno ormAlumno;
                ormAlumno = lDAOFactory.getTan_alumnoDAO().loadTan_alumnoByQuery("al_rut='" + rut + "'", null);
                if (ormAlumno == null) {
                    json = "1";
                } else {
                    AlumnoSOAPVO alumno = AlumnoSOAPVO.crearAlumnoSOAPVO(ormAlumno);
                    colectionAlumnoSOAPVO.add(alumno);
                    Gson gson = new Gson();
                    json = gson.toJson(colectionAlumnoSOAPVO);
                }
            } catch (PersistentException e) {
                e.printStackTrace();
                json = "2";
            }
        }
        return json;
    }

    /**
	 * Busca los alumnos por nombre y los retorna como json.
	 * Si el nombre es nulo retorna 0, si no encuentra alumnos retorna 1
	 * y si ocurre una excepcion retorna 2
	 * 
	 * @param nombre
	 * @return
	 */
    public static String getByNombre(String nombre) {
        String json = null;
        if ((nombre == null) || (nombre.equals(""))) {
            json = "0";
        } else {
            try {
                orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
                Collection<AlumnoSOAPVO> colectionAlumnoSOAPVO = new ArrayList<AlumnoSOAPVO>();
                orm.Tan_alumno[] ormAlumnos;
                ormAlumnos = lDAOFactory.getTan_alumnoDAO().listTan_alumnoByQuery("al_nombre='" + nombre + "'", null);
                if (ormAlumnos.length == 0) {
                    json = "1";
                } else {
                    for (int i = 0; i < ormAlumnos.length; i++) {
                        AlumnoSOAPVO alumno = AlumnoSOAPVO.crearAlumnoSOAPVO(ormAlumnos[i]);
                        colectionAlumnoSOAPVO.add(alumno);
                    }
                    Gson gson = new Gson();
                    json = gson.toJson(colectionAlumnoSOAPVO);
                }
            } catch (PersistentException e) {
                e.printStackTrace();
                json = "2";
            }
        }
        return json;
    }

    /**
	 * Busca los alumnos por curso y los retorna como json.
	 * Si el id del curso es nulo retorna 0, si no encuentra alumnos retorna 1
	 * y si ocurre una excepcion retorna 2
	 * 
	 * @param cursoId
	 * @return
	 */
    public static String getByCurso(int cursoId) {
        String json = null;
        if (cursoId == 0) {
            json = "0";
        } else {
            try {
                orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
                Collection<AlumnoSOAPVO> colectionAlumnoSOAPVO = new ArrayList<AlumnoSOAPVO>();
                orm.Tan_alumno[] ormAlumnos;
                ormAlumnos = lDAOFactory.getTan_alumnoDAO().listTan_alumnoByQuery("tan_cursocur_id='" + cursoId + "'", null);
                if (ormAlumnos.length == 0) {
                    json = "1";
                } else {
                    for (int i = 0; i < ormAlumnos.length; i++) {
                        AlumnoSOAPVO alumno = AlumnoSOAPVO.crearAlumnoSOAPVO(ormAlumnos[i]);
                        colectionAlumnoSOAPVO.add(alumno);
                    }
                    Gson gson = new Gson();
                    json = gson.toJson(colectionAlumnoSOAPVO);
                }
            } catch (PersistentException e) {
                e.printStackTrace();
                json = "2";
            }
        }
        return json;
    }

    /**
	 * Busca todos los alumnos y los retorna como json.
	 * Si no encuentra alumnos retorna 1
	 * y si ocurre una excepcion retorna 2
	 * 
	 * @return
	 */
    public static String getAll() {
        String json = null;
        orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
        Collection<AlumnoSOAPVO> coleccionAlumnoSOAPVO = new ArrayList<AlumnoSOAPVO>();
        orm.Tan_alumno[] ormAlumnos;
        try {
            ormAlumnos = lDAOFactory.getTan_alumnoDAO().listTan_alumnoByQuery(null, null);
            if (ormAlumnos.length == 0) {
                json = "1";
            } else {
                for (int i = 0; i < ormAlumnos.length; i++) {
                    AlumnoSOAPVO objeto = AlumnoSOAPVO.crearAlumnoSOAPVO(ormAlumnos[i]);
                    coleccionAlumnoSOAPVO.add(objeto);
                }
                Gson gson = new Gson();
                json = gson.toJson(coleccionAlumnoSOAPVO);
            }
        } catch (PersistentException e) {
            e.printStackTrace();
            json = "2";
        }
        return json;
    }
}
