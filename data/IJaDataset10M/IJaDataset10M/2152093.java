package ar.edu.unlp.info.poo.rules.rule;

import ar.edu.unlp.info.poo.rules.property.Properties;
import ar.edu.unlp.info.poo.rules.result.Result;
import ar.edu.unlp.info.poo.rules.util.RuleContext;

/**
 * La Clase RuleObject  implementa el comportamiento general que tiene una reagla.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class RuleObject {

    private RuleObjectType type;

    private RuleContext context;

    /**
	 * Crea una nueva instancia de la clase RuleObject.
	 */
    public RuleObject() {
        super();
        this.setType(null);
        this.setContext(null);
    }

    /**
	 * Crea una nueva instancia de la clase RuleObject.
	 * 
	 * @param type Definición de la regla. 
	 */
    public RuleObject(RuleObjectType type) {
        super();
        this.setType(type);
        this.setContext(null);
    }

    /**
	 * Realiza la ejecución de la regla.
	 * 
	 * @param properties Propiedades que conforman el contexto para la evaluación de la regla.
	 */
    public void applyRule(Properties properties) {
        RuleContext ctx = new RuleContext();
        ctx.setAttributes(properties);
        ctx.setRule(this);
        this.clearResult();
        this.getType().applyRule(ctx);
        this.setContext(ctx);
    }

    /**
	 * Indica si la regla es aplicable para las properties dadas.
	 * Una regla es aplicable si se cumple la condición de la misma.
	 * 
	 * @param properties Propiedades que conforman el contexto para la evaluación de la regla.
	 * @return Retorna <code>true</code> si la regla es aplicable o <code>false</code> en
	 * caso contrario.
	 */
    public boolean isApplicable(Properties properties) {
        RuleContext ctx = new RuleContext();
        ctx.setAttributes(Properties.copy(properties));
        ctx.setRule(this);
        boolean applicable;
        applicable = this.getType().isApplicable(ctx);
        return applicable;
    }

    /**
	 * Retorna el resultado de la ejecución de la regla.
	 * 
	 * @return Instancia de una clase perteneciente a la jerarquia de Result 
	 * que representa el resultado de la ejecución de la regla. 
	 */
    public Result getResult() {
        return this.getContext().getResult();
    }

    /**
	 * Limpia el resultado de la ejecución de la regla.
	 */
    public void clearResult() {
        this.setContext(null);
    }

    /**
	 * Obtiene el nombre de la regla.
	 * 
	 * @return Nombre de la regla.
	 */
    public String getName() {
        return this.getType().getName();
    }

    public RuleObjectType getType() {
        return type;
    }

    public void setType(RuleObjectType type) {
        this.type = type;
    }

    public RuleContext getContext() {
        return context;
    }

    public void setContext(RuleContext context) {
        this.context = context;
    }
}
