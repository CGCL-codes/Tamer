package org.vramework.vow.model.impl;

import java.lang.reflect.Field;
import org.vramework.vow.exceptions.VOWErrors;
import org.vramework.vow.exceptions.VOWException;
import org.vramework.vow.model.IAssociation;
import org.vramework.vow.model.IModel;

/**
 * {@link IModel} based on {@link PlainAssocation}s and other "Plain" classes.
 * 
 * @author thomas.mahringer
 */
public class PlainModel extends AbstractModel {

    public IAssociation createAssociation(Field f) {
        throw new VOWException(VOWErrors.Model_DoesNotSupportCreationOfAssociations, this);
    }
}
