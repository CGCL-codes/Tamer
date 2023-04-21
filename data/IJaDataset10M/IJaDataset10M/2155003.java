package org.keel.models.tests;

import org.keel.services.model.ModelException;
import org.keel.services.model.ModelRequest;
import org.keel.services.model.ModelResponse;
import org.keel.services.model.StandardLogEnabledModel;

/**
 * Model that throws a ModelException - for testing client error handling 
 *
 * @avalon.component
 * @avalon.service type=org.keel.services.model.Model
 * @x-avalon.info name=test-exception
 * @x-avalon.lifestyle type=singleton
 * 
 * @version		$Revision: 1.1 $  $Date: 2005/11/29 23:16:17 $
 * @author		Michael Nash
 */
public class TestException extends StandardLogEnabledModel {

    public ModelResponse execute(ModelRequest req) throws ModelException {
        throw new ModelException("TestException Model throws Exception");
    }
}
