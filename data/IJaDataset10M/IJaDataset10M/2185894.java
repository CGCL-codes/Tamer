package model.subject;

import model.data.Degree;
import model.data.Subject;

public class Validator {

    /**
	 * Verifica si la seleccion es posible, o sea si no produce bucles en el grafo.
	 * @param row
	 * @return True si la seleccion es posible.
	 */
    Boolean validateSelected(int row, TableModelSelectSubject tmss) {
        Subject subjectSel = this.mapSelected(tmss.getDegree(), tmss.getSelectedSubject(row));
        return !tmss.subjectEditing.isPrevious(subjectSel);
    }

    /**
	 * Retorna el objeto materia seleccionado que se corresponde con el grafo completo.
	 * @param degree
	 * @param selectedSubject
	 * @return Un Subject.
	 */
    private Subject mapSelected(Degree degree, Subject selectedSubject) {
        Subject ret = null;
        for (Subject subject : degree.getSubjects()) {
            if (subject.equals(selectedSubject)) {
                ret = subject;
                break;
            }
        }
        return ret;
    }
}
