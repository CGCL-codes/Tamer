package org.gecko.jee.community.mobidick.persistencevalidator;

import org.gecko.jee.community.mobidick.bean.State;
import org.gecko.jee.community.mobidick.persistence.DataAccessObject;
import org.gecko.jee.community.mobidick.validator.Rule;
import org.gecko.jee.community.mobidick.validator.Validable;

/**
 * <b> Description: Validation marking contract for a technical component
 * DataAccessObject.</b>
 * <p>
 * May be used to apply rules validation on the DataAccessObject itself.
 * </p>
 * <hr>
 * 
 * @author GECKO SOFTWARE
 * @param <STATE>
 * @param <PERSISTENCE>
 */
public interface DataAccessObjectValidator<STATE extends State, PERSISTENCE extends Object & DataAccessObject<STATE> & Validable<PERSISTENCE>> extends Rule<PERSISTENCE> {
}
