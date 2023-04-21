package model.admin;

import java.util.ArrayList;
import model.TypeRequest;
import model.data.Degree;
import model.data.Subject;

/**
 * Clase responsable de administrar las materias.
 * Permite dar las altas, bajas y modificaciones de las mismas asi como realizar consultas.
 * @author mariano
 *
 */
public class AdminSubject extends Admin {

    private static AdminSubject instance;

    private AdminSubject() {
    }

    public static AdminSubject getInstance() {
        if (instance == null) {
            instance = new AdminSubject();
        }
        return instance;
    }

    /**
	 * Obtiene las materias de la carrera degree y del anio year desde el servidor. 
	 * @param degree
	 * @param year
	 * @return Un ArrayList con las materias.
	 */
    public ArrayList<Subject> getSubjectForYear(Degree degree, Integer year) {
        return (ArrayList<Subject>) this.requestWhitWhit("degree", degree, "year", year, null, "subjectServlet");
    }

    /**
	 * Obtiene las materias correlativas de la materia subject.
	 * @param subject
	 * @return Un ArrayList de las materias.
	 */
    public ArrayList<Subject> getPreviousForSubject(Subject subject) {
        return (ArrayList<Subject>) this.requestWhit("subject", subject, TypeRequest.C, "subjectServlet");
    }

    /**
	 * Da el alta de la nueva materia subject. 
	 * @param subject
	 * @return El identificador en el servidor de la materia.
	 */
    public Integer newSubject(Subject subject) {
        return (Integer) this.requestWhit("subject", subject, TypeRequest.A, "subjectServlet");
    }

    /**
	 * Actualiza en el servidor la materia subject y sus correlativas de acuerdo al parametro updatePrevious.
	 * @param subject
	 * @param updatePrevious
	 * @return Resultado de la operacion, true si exito.
	 */
    public Boolean updateSubject(Subject subject, Boolean updatePrevious) {
        return (Boolean) this.requestWhitWhit("subject", subject, "updatePrevious", updatePrevious, TypeRequest.M, "subjectServlet");
    }

    /**
	 * Elimina del servidor la materia subject.
	 * @param subject
	 * @return Resultado de la operacion, true si exito.
	 */
    public Boolean deleteSubject(Subject subject) {
        return (Boolean) this.requestWhit("subject", subject, TypeRequest.B, "subjectServlet");
    }
}
