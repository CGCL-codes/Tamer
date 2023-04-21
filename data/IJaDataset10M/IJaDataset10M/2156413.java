package de.uni_leipzig.lots.webfrontend.formbeans.property;

import java.util.ArrayList;

/**
 * @author Alexander Kiel
 * @version $Id: TelProperty.java,v 1.7 2007/10/23 06:29:46 mai99bxd Exp $
 */
public class TelProperty extends AbstractDefaultProperty<TelProperty> {

    private static final ArrayList<PropertyValidator<? super TelProperty>> VALIDATORS;

    static {
        VALIDATORS = new ArrayList<PropertyValidator<? super TelProperty>>(1);
        VALIDATORS.add(new MaxLengthValidator(255));
    }

    public TelProperty() {
        super(VALIDATORS);
    }
}
