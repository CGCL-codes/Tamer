package ar.edu.unlp.info.poo.rules.util;

import java.util.ArrayList;
import java.util.List;
import ar.edu.unlp.info.poo.rules.property.Properties;
import ar.edu.unlp.info.poo.rules.result.Result;
import ar.edu.unlp.info.poo.rules.rule.RuleObject;

/**
 * La Clase RuleContext representa el contexto sobre el que se ejecuta una regla.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class RuleContext {

    private RuleObject rule;

    private Result result;

    private Properties attributes;

    private List<Result> assessorResults;

    private List<Result> actionResults;

    /**
	 * Crea una nueva clase de RuleContext.
	 */
    public RuleContext() {
        super();
        this.setRule(null);
        this.setResult(null);
        this.setAttributes(new Properties());
        this.setAssessorResults(new ArrayList<Result>());
        this.setActionResults(new ArrayList<Result>());
    }

    /**
	 * Obtiene los atributos establecidos para la regla.
	 * 
	 * @return Nombres de los atributos.
	 */
    public List<String> getAttributeNames() {
        return getAttributes().getNames();
    }

    /**
	 * Retorna el valor asociado al atributo.
	 * 
	 * @param name Nombre del atributo consultado.
	 * @return Valor del atributo.
	 */
    public Object getAttribute(String name) {
        return getAttributes().get(name);
    }

    /**
	 * Establece un valor a un atributo.
	 * 
	 * @param name Nombre del atributo.
	 * @param value Valor a establecer para el atributo.
	 */
    public void setAttribute(String name, Object value) {
        getAttributes().put(name, value);
    }

    /**
	 * Retorna el resutlado de la condición de la regla.
	 * 
	 * @return Instancia de la jerarquía de clases de Result que representa el 
	 * resultado de la condición de la regla.
	 */
    public Result getAssessorResult() {
        return this.getAssessorResults().get(0);
    }

    /**
	 * Establece un resultado de la evaluación de una condición de la regla.
	 * 
	 * @param result Resultado a registrar.
	 */
    public void setAssessorResult(Result result) {
        this.getAssessorResults().add(result);
    }

    /**
	 * Retorna el resutlado de la acción de la regla.
	 * 
	 * @return Instancia de la jerarquía de clases de Result que representa el 
	 * resultado de la acción de la regla.
	 */
    public Result getActionResult() {
        return this.getActionResults().get(0);
    }

    /**
	 * Establece un resultado de la ejecución de una acción de la regla.
	 * 
	 * @param result Resultado a registrar.
	 */
    public void setActionResult(Result result) {
        this.getActionResults().add(result);
    }

    /**
	 * Limpia los resultados registrados en el contexto.
	 */
    public void clearResult() {
        this.getAssessorResults().clear();
        this.getActionResults().clear();
    }

    public RuleObject getRule() {
        return rule;
    }

    public void setRule(RuleObject rule) {
        this.rule = rule;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Properties getAttributes() {
        return attributes;
    }

    public void setAttributes(Properties attributes) {
        this.attributes = attributes;
    }

    private List<Result> getAssessorResults() {
        return assessorResults;
    }

    private void setAssessorResults(List<Result> assessorResults) {
        this.assessorResults = assessorResults;
    }

    private List<Result> getActionResults() {
        return actionResults;
    }

    private void setActionResults(List<Result> actionResults) {
        this.actionResults = actionResults;
    }
}
