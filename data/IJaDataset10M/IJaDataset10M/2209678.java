package es.wtestgen.bean.alumno;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts.validator.ValidatorForm;
import es.wtestgen.domain.Examen;

public class TareaForm extends ValidatorForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1339869901585521465L;

    List<Examen> examenesPendientes = new ArrayList<Examen>();

    public List<Examen> getExamenesPendientes() {
        return examenesPendientes;
    }

    public void setExamenesPendientes(List<Examen> examenesPendientes) {
        this.examenesPendientes = examenesPendientes;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
