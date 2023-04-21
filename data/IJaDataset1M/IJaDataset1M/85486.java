package org.databene.benerator.util;

import org.databene.benerator.Generator;

/**
 * Parent class for {@link Generator} implementations that 
 * create method parameters as {@link Object} arrays.<br/><br/>
 * Created: 08.07.2011 18:20:24
 * @since 0.7.0
 * @author Volker Bergmann
 */
public abstract class UnsafeMethodParamsGenerator extends UnsafeGenerator<Object[]> {

    public Class<Object[]> getGeneratedType() {
        return Object[].class;
    }
}
