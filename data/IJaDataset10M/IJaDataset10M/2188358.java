package ar.edu.unlp.info.poo.rules.assessor;

import ar.edu.unlp.info.poo.rules.util.IAssessorVisitor;
import ar.edu.unlp.info.poo.rules.util.RuleContext;

/**
 * La clase CompositeNOTAssessor implementa el comportamiento de una condición 
 * compuesta de una regla donde el resultado de la evaluzación del mismo es igual
 * a la disyunción de la evaluacion de los miembros.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class CompositeNOTAssessor extends CompositeAbstractAssessor {

    /**
	 * Crea una nueva instancia de la clase CompositeNOTAssessor.
	 */
    public CompositeNOTAssessor() {
        super();
    }

    /**
	 * Crea una nueva instancia de la clase CompositeNOTAssessor.
	 * 
	 * @param assessor Condición que forma parte de la condición compuesta.
	 */
    public CompositeNOTAssessor(IAssessor assessor) {
        super();
        add(assessor);
    }

    /**
	 * Realiza la evaluación de la condicion que representa, realizando la negación
	 * del resultado de la evaluación del miembro. 
	 * 
	 * @param ctx Contexto sobre el que se evaluación la condición de la regla.
	 * @return Retorna <code>true</code> si la evaluación indico que se cumplen todas
	 * las restricciones o <code>false</code> en caso contrario.
	 */
    public boolean evaluate(RuleContext ctx) {
        boolean evaluateValue;
        evaluateValue = !this.getChildren().get(0).evaluate(ctx);
        this.setResult(evaluateValue, ctx, this);
        return evaluateValue;
    }

    /**
	 * Procesa la visita de un componente que recorre por la estructura de la condición
	 * realizando sus operaciones.
	 * 
	 * @param visitor Componente que navega la condición.
	 */
    public void accept(IAssessorVisitor visitor) {
        visitor.visitNOTAssessor(this);
    }
}
