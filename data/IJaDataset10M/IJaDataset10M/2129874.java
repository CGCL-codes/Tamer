package ar.edu.unlp.info.poo.rules.tests.model;

import ar.edu.unlp.info.poo.rules.property.Properties;
import ar.edu.unlp.info.poo.rules.wrapper.ObjectRuleWrapper;

public class BuisnessObjectExample extends ObjectRuleWrapper {

    private Long id;

    private String name;

    /**
	 * Crea una nueva instancia de la clase BuisnessObjectExample.
	 */
    public BuisnessObjectExample() {
        super();
    }

    public String getData() {
        Properties props = new Properties();
        props.put(Constants.AttributeName, 11);
        this.applyRule(Constants.OperationName, props);
        return this.getName() + props.get(Constants.ResultName).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
